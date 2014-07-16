package com.texasjake95.core.client.handler;

import java.util.ArrayList;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;

import net.minecraftforge.common.MinecraftForge;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.api.event.AutoSwitchEvent;
import com.texasjake95.core.api.handler.IToolRegistry;
import com.texasjake95.core.api.impl.CoreAPI;
import com.texasjake95.core.lib.handler.event.TickHandler;
import com.texasjake95.core.proxy.client.MinecraftProxy;
import com.texasjake95.core.proxy.client.entity.EntityClientPlayerMPProxy;
import com.texasjake95.core.proxy.client.settings.GameSettingsProxy;
import com.texasjake95.core.proxy.entity.EntityProxy;
import com.texasjake95.core.proxy.entity.player.EntityPlayerProxy;
import com.texasjake95.core.proxy.entity.player.PlayerInventoryProxy;
import com.texasjake95.core.proxy.inventory.IInventoryProxy;
import com.texasjake95.core.proxy.world.IBlockAccessProxy;

// TODO Create Item Handler for to help tools decide if they are appropriate
// i.e. determine durability % able to harvest block
// blackList etc
// Create Map for above!!!!!
/**
 * This class handles the auto switching of tools on the player's hot bar.
 *
 * @author Texasjake95
 *
 */
public class AutoSwitchHandler extends TickHandler {

	/**
	 * Check and see if anything is preventing the player from switching to any new tools.
	 *
	 * @param player
	 *            - the player in question
	 * @return if the tool can auto switch
	 */
	private static boolean checkIfAutoSwitchIsPossible(EntityPlayer player)
	{
		AutoSwitchEvent event = new AutoSwitchEvent(player);
		if (MinecraftForge.EVENT_BUS.post(event))
			return false;
		return true;
	}

	/*
	 * public static void teleport(World world, EntityPlayer player) { if (!world.isRemote) {
	 * Vec3 vec3 = ClientPlayerProxy.getPostion(player, 1.0F); vec3.yCoord++; Vec3 lookVec =
	 * ClientPlayerProxy.getLook(player, 1.0F); Vec3 aVector = vec3.addVector(lookVec.xCoord *
	 * 50.0D, lookVec.yCoord * 50.0D, lookVec.zCoord * 50.0D); MovingObjectPosition
	 * movingObjPos = WorldProxy.rayTraceBlocks(world, vec3, aVector); if (movingObjPos !=
	 * null) { PlayerProxy.setPostion(player, movingObjPos); } } }
	 */
	/**
	 * Current Item slot.
	 */
	private int currentItem = -1;
	/**
	 * The instance of {@link AutoHandlerData} the class uses to handle the data that it
	 * gathers.
	 */
	AutoHandlerData data = new AutoHandlerData();
	/**
	 * Used to flag the switching back to the original tool in hand.
	 */
	private boolean shouldSwitchBack = false;
	/**
	 * the current amount of ticks since the 'Attack' Keybinding was released.
	 */
	private int tickCount = 0;

	/**
	 * Handle Tool Auto Switching.
	 *
	 * @param tickStart
	 *            - did the tick start
	 * @param player
	 *            - the player in question
	 */
	private void doAutoSwitchStart(boolean tickStart, EntityClientPlayerMP player)
	{
		if (tickStart)
		{
			if (EntityPlayerProxy.isCreative(player))
				return;
			World world = EntityProxy.getWorld(player);
			// if (CoreConfig.getInstance().autoSwitch) // Config switch
			if (MinecraftProxy.inGameHasFocus())
				if (checkIfAutoSwitchIsPossible(player))
					if (GameSettingsProxy.isKeyDown(GameSettingsProxy.getKeyBindAttack(MinecraftProxy.getGameSettings())))
					{
						this.shouldSwitchBack = false;
						IToolRegistry tools = CoreAPI.toolRegistry;
						this.tickCount = 0;
						MovingObjectPosition mop = MinecraftProxy.getObjectMouseOver();
						if (mop != null)
						{
							if (IBlockAccessProxy.isAirBlock(world, mop.blockX, mop.blockY, mop.blockZ))
								return;
							this.data.reset(world, mop);
							int hotBarSize = PlayerInventoryProxy.getHotBarSize();
							this.currentItem = this.currentItem != -1 ? this.currentItem : PlayerInventoryProxy.getCurrentItemSlot(player);
							ArrayList<Integer> bestSlots = new ArrayList<Integer>();
							// Temp storage of Item location
							int fakeCurrentItem = PlayerInventoryProxy.getCurrentItemSlot(player);
							// Cycle through out all the items
							for (int i = fakeCurrentItem; i < hotBarSize; i++)
							{
								ItemStack stack = IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i);
								if (tools.canAutoSwtichTo(stack))
									if (this.data.canToolHarvest(tools, stack))
									{
										PlayerInventoryProxy.setCurrentItemSlot(player, i);
										stack = PlayerInventoryProxy.getCurrentItem(player);
										int currentHarvest = this.data.getHarvestLevel(stack);
										float strVsBlock = this.data.getBreakSpeed(player);
										if (stack != null)
											this.data.checkAndUpdate(i, currentHarvest, strVsBlock);
									}
							}
							// Cycle through out all the items
							for (int i = 0; i <= fakeCurrentItem; i++)
							{
								ItemStack stack = IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i);
								if (tools.canAutoSwtichTo(stack))
									if (this.data.canToolHarvest(tools, stack))
									{
										PlayerInventoryProxy.setCurrentItemSlot(player, i);
										stack = PlayerInventoryProxy.getCurrentItem(player);
										int currentHarvest = this.data.getHarvestLevel(stack);
										float strVsBlock = this.data.getBreakSpeed(player);
										if (stack != null)
											this.data.checkAndUpdate(i, currentHarvest, strVsBlock);
									}
							}
							// Collect all slots that match the highest
							// strVsBlock
							for (int i = fakeCurrentItem; i < this.data.hotBarSize; i++)
								if (tools.canAutoSwtichTo(IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i)))
								{
									PlayerInventoryProxy.setCurrentItemSlot(player, i);
									ItemStack stack = PlayerInventoryProxy.getCurrentItem(player);
									int currentHarvest = this.data.getHarvestLevel(stack);
									float strVsBlock = this.data.getBreakSpeed(player);
									this.data.addSlots(i, bestSlots, tools, currentHarvest, strVsBlock, stack);
								}
							// Collect all slots that match the highest
							// strVsBlock
							for (int i = 0; i <= fakeCurrentItem; i++)
								if (tools.canAutoSwtichTo(IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i)))
								{
									PlayerInventoryProxy.setCurrentItemSlot(player, i);
									ItemStack stack = PlayerInventoryProxy.getCurrentItem(player);
									int currentHarvest = this.data.getHarvestLevel(stack);
									float strVsBlock = this.data.getBreakSpeed(player);
									this.data.addSlots(i, bestSlots, tools, currentHarvest, strVsBlock, stack);
								}
							// This checks to see if the players hand is
							// enough to break the block and if so switch to
							// it or a non damageable item
							// if (!CoreConfig.getInstance().forceTool)
							if (this.data.bestFloat <= 1.0F)
							{
								for (int i = fakeCurrentItem; i < hotBarSize; i++)
									if (tools.canAutoSwtichTo(IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i)))
									{
										PlayerInventoryProxy.setCurrentItemSlot(player, i);
										ItemStack stack = PlayerInventoryProxy.getCurrentItem(player);
										if (stack == null || !tools.isDamageable(stack))
										{
											this.data.bestFloat = -1.0F;
											this.data.bestSlot = i;
											break;
										}
									}
								if (this.data.bestFloat != -1.0F)
									for (int i = 0; i <= fakeCurrentItem; i++)
										if (tools.canAutoSwtichTo(IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), i)))
										{
											PlayerInventoryProxy.setCurrentItemSlot(player, i);
											ItemStack stack = PlayerInventoryProxy.getCurrentItem(player);
											if (stack == null || !tools.isDamageable(stack))
											{
												this.data.bestSlot = i;
												break;
											}
										}
							}
							double meta = 1.0F;
							// Test all damages of items that match the best
							// strVsBlock and grab the lowest
							if (this.data.bestFloat != -1.0F)
								for (int slot : bestSlots)
								{
									ItemStack stack = IInventoryProxy.getStackInSlot(EntityPlayerProxy.getPlayerInventory(player), slot);
									if (stack != null)
									{
										double damage = tools.getDurability(stack);
										meta = meta > damage ? damage : meta;
										if (Texasjake95Core.isTesting)
											System.out.println(meta);
										if (meta == damage)
										{
											this.data.bestSlot = slot;
											if (Texasjake95Core.isTesting)
												System.out.println(this.data.bestSlot + ":" + meta);
										}
									}
								}
							PlayerInventoryProxy.setCurrentItemSlot(player, this.data.bestSlot);
							if (Texasjake95Core.isTesting)
							{
								System.out.println(this.data.block + ":" + this.data.blockMeta);
								System.out.println("Setting slot to " + this.data.bestSlot);
							}
							EntityClientPlayerMPProxy.sendPacket(player, new C09PacketHeldItemChange(this.data.bestSlot));
							this.data.clear();
						}
					}
					else
					{
						// Count ticks before switching back to prevent
						// constant switching
						this.tickCount++;
						if (this.tickCount >= 2)
							this.shouldSwitchBack = true;
					}
		}
		else
		{
			if (EntityPlayerProxy.isCreative(player))
				return;
			if (!GameSettingsProxy.isKeyDown(GameSettingsProxy.getKeyBindAttack(MinecraftProxy.getGameSettings())) && this.currentItem > -1 && this.shouldSwitchBack)
			{
				this.tickCount = 0;
				PlayerInventoryProxy.setCurrentItemSlot(player, this.currentItem);
				EntityClientPlayerMPProxy.sendPacket(player, new C09PacketHeldItemChange(this.data.bestSlot));
				this.currentItem = -1;
				this.shouldSwitchBack = false;
			}
		}
	}

	@Override
	protected void handleClientTick(ClientTickEvent event)
	{
	}

	@Override
	protected void handlePlayerTick(PlayerTickEvent event)
	{
		if (event.side == Side.CLIENT)
			this.doAutoSwitchStart(event.phase == Phase.START, (EntityClientPlayerMP) event.player);
		// if (event.player.getCurrentEquippedItem() != null)
		// this.teleport(event.player.worldObj, event.player);
	}

	@Override
	protected void handleRenderTick(RenderTickEvent event)
	{
	}

	@Override
	protected void handleServerTick(ServerTickEvent event)
	{
	}

	@Override
	protected void handleWorldTick(WorldTickEvent event)
	{
	}
}
