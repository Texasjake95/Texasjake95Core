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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.api.event.AutoSwitchEvent;
import com.texasjake95.core.api.handler.IToolRegistry;
import com.texasjake95.core.api.impl.CoreAPI;
import com.texasjake95.core.config.CoreConfig;
import com.texasjake95.core.lib.handler.event.TickHandler;

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

	private void addSlots(int min, int max, EntityPlayer player, IToolRegistry tools, ArrayList<Integer> bestSlots)
	{
		for (int i = min; i < this.data.hotBarSize; i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (tools.canAutoSwtichTo(stack))
			{
				player.inventory.currentItem = i;
				stack = player.inventory.getCurrentItem();
				int currentHarvest = this.data.getHarvestLevel(stack);
				float strVsBlock = this.data.getBreakSpeed(player);
				this.data.addSlots(i, bestSlots, tools, currentHarvest, strVsBlock, stack);
			}
		}
	}

	private void checkItems(int min, int max, EntityPlayer player, IToolRegistry tools)
	{
		for (int i = min; i < max; i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (tools.canAutoSwtichTo(stack))
				if (this.data.canToolHarvest(tools, stack))
				{
					player.inventory.currentItem = i;
					stack = player.inventory.getCurrentItem();
					int currentHarvest = this.data.getHarvestLevel(stack);
					float strVsBlock = this.data.getBreakSpeed(player);
					if (stack != null)
						this.data.checkAndUpdate(i, currentHarvest, strVsBlock);
				}
		}
	}

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
			if (player.capabilities.isCreativeMode)
				return;
			// if (CoreConfig.getInstance().autoSwitch) // Config switch
			if (Minecraft.getMinecraft().inGameHasFocus)
				if (checkIfAutoSwitchIsPossible(player))
					if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindAttack))
					{
						this.shouldSwitchBack = false;
						IToolRegistry tools = CoreAPI.toolRegistry;
						this.tickCount = 0;
						MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
						if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK)
						{
							World world = player.getEntityWorld();
							if (world.isAirBlock(mop.blockX, mop.blockY, mop.blockZ))
								return;
							this.data.reset(world, mop);
							int hotBarSize = InventoryPlayer.getHotbarSize();
							this.currentItem = this.currentItem != -1 ? this.currentItem : player.inventory.currentItem;
							ArrayList<Integer> bestSlots = new ArrayList<Integer>();
							// Temp storage of Item location
							int fakeCurrentItem = player.inventory.currentItem;
							// Cycle through out all the items
							this.checkItems(fakeCurrentItem, hotBarSize, player, tools);
							// Cycle through out all the items
							this.checkItems(0, fakeCurrentItem, player, tools);
							// Collect all slots that match the highest strVsBlock
							this.addSlots(fakeCurrentItem, hotBarSize, player, tools, bestSlots);
							// Collect all slots that match the highest strVsBlock
							this.addSlots(0, fakeCurrentItem, player, tools, bestSlots);
							// This checks to see if the players hand is enough to break the
							// block and if so switch to it or a non damageable item
							if (!CoreConfig.getInstance().forceTool)
							{
								if (this.data.bestFloat <= 1.0F)
									this.findBestItem(fakeCurrentItem, hotBarSize, player, tools);
								if (this.data.bestFloat != -1.0F)
									this.findBestItem(0, fakeCurrentItem, player, tools);
							}
							double meta = 1.0F;
							// Test all damages of items that match the best
							// strVsBlock and grab the lowest
							if (this.data.bestFloat != -1.0F)
								for (int slot : bestSlots)
								{
									ItemStack stack = player.inventory.getStackInSlot(slot);
									if (stack != null)
									{
										double damage = tools.getDurability(stack);
										meta = meta > damage ? damage : meta;
										if (Texasjake95Core.isTesting)
											Texasjake95Core.txLogger.debug(meta);
										if (meta == damage)
										{
											this.data.bestSlot = slot;
											if (Texasjake95Core.isTesting)
												Texasjake95Core.txLogger.debug(this.data.bestSlot + ":" + meta);
										}
									}
								}
							player.inventory.currentItem = this.data.bestSlot;
							if (Texasjake95Core.isTesting)
							{
								Texasjake95Core.txLogger.debug(this.data.block + ":" + this.data.blockMeta);
								Texasjake95Core.txLogger.debug("Setting slot to " + this.data.bestSlot);
							}
							player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.data.bestSlot));
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
			if (player.capabilities.isCreativeMode)
				return;
			if (!GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindAttack) && this.currentItem != -1 && this.shouldSwitchBack)
			{
				this.tickCount = 0;
				player.inventory.currentItem = this.currentItem;
				player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.currentItem));
				this.currentItem = -1;
				this.shouldSwitchBack = false;
			}
		}
	}

	private void findBestItem(int min, int max, EntityPlayer player, IToolRegistry tools)
	{
		for (int i = min; i < max; i++)
		{
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (tools.canAutoSwtichTo(stack))
			{
				player.inventory.currentItem = i;
				stack = player.inventory.getCurrentItem();
				if (stack == null || !tools.isDamageable(stack))
				{
					this.data.bestFloat = -1.0F;
					this.data.bestSlot = i;
					break;
				}
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
