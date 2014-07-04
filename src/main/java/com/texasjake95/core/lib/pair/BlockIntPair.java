package com.texasjake95.core.lib.pair;

import net.minecraft.block.Block;

import com.texasjake95.commons.util.pair.ObjectPair;

public class BlockIntPair extends ObjectPair<Block, Integer> {

	private static final long serialVersionUID = -1848907480192995086L;

	public BlockIntPair(Block e, Integer t)
	{
		super(e, t);
	}

	public Block getBlock()
	{
		return this.getObject1();
	}

	public int getMeta()
	{
		return this.getObject2();
	}
}
