package com.texasjake95.core.tile;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface ISeedProvider {
	
	ItemIntPair getSeed(Block block, int meta);
	
	boolean isSeed(Item item, int meta);
}
