package com.texasjake95.core.tile;

import net.minecraft.block.Block;

public interface IBlockChecker {
	
	public boolean isValidBlock(Block block, int meta);
}
