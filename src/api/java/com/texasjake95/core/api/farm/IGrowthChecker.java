package com.texasjake95.core.api.farm;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Implement this for custom handling the direction of plants being fully grown.
 *
 * @author Texasjake95
 *
 */
public interface IGrowthChecker {

	/**
	 * Allows custom checking if the {@link Block} at the specified location is grown or not.
	 *
	 * @param block
	 *            the {@link Block} (plant) being checked
	 * @param meta
	 *            the meta of the {@link Block} being checked
	 * @param world
	 *            the instance of the {@link World} the {@link Block} is in
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @return true if plant is fully grown
	 */
	boolean isGrown(Block block, int meta, World world, int x, int y, int z);
}
