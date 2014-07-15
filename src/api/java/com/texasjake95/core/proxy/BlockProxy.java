package com.texasjake95.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockProxy {

	public static Material getMaterial(Block block)
	{
		return block.getMaterial();
	}

	private BlockProxy()
	{
	}
}
