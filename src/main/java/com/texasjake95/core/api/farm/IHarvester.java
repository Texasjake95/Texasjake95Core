package com.texasjake95.core.api.farm;

import java.util.ArrayList;

import net.minecraftforge.common.util.FakePlayer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implement this to handle the gathering of the drops a plant will drop when broken.
 *
 * @author Texasjake95
 *
 */
public interface IHarvester {

	/**
	 * This is used to break the block at the location specified.
	 *
	 * @param player
	 *            the {@link EntityPlayer} (will be a {@link FakePlayer}) breaking the
	 *            {@link Block}
	 * @param world
	 *            the instance of the {@link World} the {@link EntityPlayer} and {@link Block}
	 *            are in
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @param block
	 *            the {@link Block} being broke
	 * @param meta
	 *            the metadata of the {@link Block} being broke
	 */
	void breakBlock(EntityPlayer player, World world, int x, int y, int z, Block block, int meta);

	/**
	 * This is used to get the drops a {@link Block} will drop upon breaking.<br>
	 * NOTE: DO NOT BREAK BLOCK HERE USE {@link IHarvester#breakBlock}
	 *
	 * @param player
	 *            the {@link EntityPlayer} (will be a {@link FakePlayer}) breaking the
	 *            {@link Block}
	 * @param world
	 *            the instance of the {@link World} the {@link EntityPlayer} and {@link Block}
	 *            are in
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @param block
	 *            the {@link Block} being broke
	 * @param meta
	 *            the metadata of the {@link Block} being broke
	 * @return a list of ItemStacks being dropped
	 */
	ArrayList<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta);
}
