package com.texasjake95.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
