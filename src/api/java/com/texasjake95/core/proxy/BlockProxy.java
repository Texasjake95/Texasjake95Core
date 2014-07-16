package com.texasjake95.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * This class is used as a proxy to call code for Minecraft's {@link Block} due to the nature
 * of the changing names of methods and fields.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
public class BlockProxy {

	/**
	 * Get the {@link Material} from the {@link Block} specified.
	 *
	 * @param block
	 *            the {@link Block} in question
	 * @return the {@link Material} the block is made of
	 */
	public static Material getMaterial(Block block)
	{
		return block.getMaterial();
	}
}
