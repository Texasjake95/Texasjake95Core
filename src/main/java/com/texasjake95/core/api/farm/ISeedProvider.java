package com.texasjake95.core.api.farm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.texasjake95.core.lib.pair.ItemIntPair;

public interface ISeedProvider {
	
	ItemIntPair getSeed(Block block, int meta);
	
	boolean isSeed(Item item, int meta);
}
