package com.texasjake95.core;

import java.lang.ref.WeakReference;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.oredict.ShapedOreRecipe;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.items.CoreItems;
import com.texasjake95.core.tile.TileEntityFarm;

public class TxCoreCommonProxy {
	
	public static Block farm;
	protected static WeakReference<EntityPlayer> player = new WeakReference<EntityPlayer>(null);
	public static final GameProfile gameProfile = new GameProfile("txPlayer", "[TXMOD]");
	
	/**
	 * Register All Event Handlers
	 */
	public void registerEventHandlers()
	{
	}
	
	public void initItemsAndBlocks()
	{
		GameRegistry.registerBlock((farm = new BlockFarm()), "FarmBlock");
		GameRegistry.registerTileEntity(TileEntityFarm.class, "TXFARM");
		CoreItems.initItems();
	}
	
	@SuppressWarnings("unchecked")
	public void registerRecipes()
	{
		IRecipe recipe = new ShapedOreRecipe(new ItemStack(farm), new Object[] { "SLS", "FHF", "SLS", 'S', "cobblestone", 'F', Blocks.fence, 'L', "logWood", 'H', Blocks.hopper });
		CraftingManager.getInstance().getRecipeList().add(recipe);
	}
	
	private WeakReference<EntityPlayer> createNewPlayer(WorldServer world)
	{
		EntityPlayer player = FakePlayerFactory.get(world, gameProfile);
		return new WeakReference<EntityPlayer>(player);
	}
	
	private WeakReference<EntityPlayer> createNewPlayer(WorldServer world, int x, int y, int z)
	{
		EntityPlayer player = FakePlayerFactory.get(world, gameProfile);
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		return new WeakReference<EntityPlayer>(player);
	}
	
	public final WeakReference<EntityPlayer> getTXPlayer(WorldServer world)
	{
		if (player.get() == null)
		{
			player = createNewPlayer(world);
		}
		else
		{
			player.get().worldObj = world;
		}
		return player;
	}
	
	public final WeakReference<EntityPlayer> getTXPlayer(WorldServer world, int x, int y, int z)
	{
		if (player.get() == null)
		{
			player = createNewPlayer(world, x, y, z);
		}
		else
		{
			player.get().worldObj = world;
			player.get().posX = x;
			player.get().posY = y;
			player.get().posZ = z;
		}
		return player;
	}
}
