package com.texasjake95.core;

import java.lang.ref.WeakReference;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.oredict.ShapedOreRecipe;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.blocks.BlockMachine;
import com.texasjake95.core.items.CoreItems;
import com.texasjake95.core.items.ItemBlockMachine;
import com.texasjake95.core.tile.FurnaceTest;
import com.texasjake95.core.tile.TileEntityFarm;
import com.texasjake95.core.tile.TileEntityQuarry;

/**
 * The proxy used by {@link Texasjake95Core}.<br>
 * This proxy is used both for server side and client side
 *
 * @author Texasjake95
 *
 */
public class TxCoreCommonProxy {

	public static Block machine;
	/**
	 * A {@link WeakReference} to a {@link FakePlayer}.
	 */
	protected static WeakReference<EntityPlayer> player = new WeakReference<EntityPlayer>(null);
	/**
	 * The {@link GameProfile} used by the {@link FakePlayer} above.
	 */
	public static final GameProfile gameProfile = new GameProfile(new UUID(156L, 783L), "[TXMOD]");

	/**
	 * Create a {@link FakePlayer} using the world provided and
	 * {@link TxCoreCommonProxy#gameProfile}.
	 *
	 * @param world
	 *            the instance of {@link WorldServer} the {@link FakePlayer} will be created in
	 * @return a {@link WeakReference} of a {@link FakePlayer}
	 */
	private WeakReference<EntityPlayer> createNewPlayer(WorldServer world)
	{
		EntityPlayer player = FakePlayerFactory.get(world, gameProfile);
		return new WeakReference<EntityPlayer>(player);
	}

	/**
	 * Create a {@link FakePlayer} using the world provided and
	 * {@link TxCoreCommonProxy#gameProfile} and set the location of the player to the location
	 * provided.
	 *
	 * @param world
	 *            the instance of {@link WorldServer} the {@link FakePlayer} will be created in
	 * @param x
	 *            the x coordinate the player will be located
	 * @param y
	 *            the y coordinate the player will be located
	 * @param z
	 *            the x coordinate the player will be located
	 * @return a {@link WeakReference} of a {@link FakePlayer}
	 */
	private WeakReference<EntityPlayer> createNewPlayer(WorldServer world, int x, int y, int z)
	{
		EntityPlayer player = FakePlayerFactory.get(world, gameProfile);
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		return new WeakReference<EntityPlayer>(player);
	}

	/**
	 * Create or get a {@link FakePlayer} in the provided {@link WorldServer} and the location
	 * provided.
	 *
	 * @param world
	 *            the instance of {@link WorldServer} the {@link FakePlayer} will be located in
	 * @return a {@link WeakReference} of a {@link FakePlayer}
	 */
	public final WeakReference<EntityPlayer> getTXPlayer(WorldServer world)
	{
		if (player.get() == null)
			player = this.createNewPlayer(world);
		else
			player.get().worldObj = world;
		return player;
	}

	/**
	 * Create or get a {@link FakePlayer} in the provided {@link WorldServer} and the location
	 * provided.
	 *
	 * @param world
	 *            the instance of {@link WorldServer} the {@link FakePlayer} will be created in
	 * @param x
	 *            the x coordinate the player will be located
	 * @param y
	 *            the y coordinate the player will be located
	 * @param z
	 *            the x coordinate the player will be located
	 * @return a {@link WeakReference} of a {@link FakePlayer}
	 */
	public final WeakReference<EntityPlayer> getTXPlayer(WorldServer world, int x, int y, int z)
	{
		if (player.get() == null)
			player = this.createNewPlayer(world, x, y, z);
		else
		{
			player.get().worldObj = world;
			player.get().posX = x;
			player.get().posY = y;
			player.get().posZ = z;
		}
		return player;
	}

	/**
	 * Add all items and blocks to Minecraft.
	 */
	public void initItemsAndBlocks()
	{
		machine = new BlockMachine();
		GameRegistry.registerBlock(machine, ItemBlockMachine.class, "Machines");
		GameRegistry.registerTileEntity(TileEntityFarm.class, "TXFARM");
		GameRegistry.registerTileEntity(TileEntityQuarry.class, "TXQUARRY");
		GameRegistry.registerTileEntity(FurnaceTest.class, "TXTESTFURNACE");
		CoreItems.initItems();
	}

	/**
	 * Registers All Event Handlers.
	 */
	public void registerEventHandlers()
	{
	}

	/**
	 * Registers All Recipes.
	 */
	@SuppressWarnings("unchecked")
	public void registerRecipes()
	{
		IRecipe recipe = new ShapedOreRecipe(new ItemStack(machine), new Object[] { "SLS", "FHF", "SLS", 'S', "cobblestone", 'F', Blocks.fence, 'L', "logWood", 'H', Blocks.hopper });
		CraftingManager.getInstance().getRecipeList().add(recipe);
		// RecipeProviders.furnace.addRecipe(new ItemStack(CoreItems.misc, 1,
		// 0), new ItemStack(Items.iron_ingot, 1, 0), .7f);
		// RecipeProviders.furnace.addRecipe(new ItemStack(CoreItems.misc, 1,
		// 1), new ItemStack(Items.gold_ingot, 1, 0), .7f);
	}
}
