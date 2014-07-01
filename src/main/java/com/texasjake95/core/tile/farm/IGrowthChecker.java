package com.texasjake95.core.tile.farm;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IGrowthChecker {
	
	boolean isGrown(Block block, int meta, World world, int x, int y, int z);
}
