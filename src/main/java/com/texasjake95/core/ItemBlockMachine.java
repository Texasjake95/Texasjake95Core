package com.texasjake95.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.proxy.item.ItemStackProxy;

public class ItemBlockMachine extends ItemBlock {

	public ItemBlockMachine(Block p_i45326_1_)
	{
		super(p_i45326_1_);
	}

	@Override
	public int getMetadata(int i)
	{
		return i;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		switch (ItemStackProxy.getMetadata(itemstack))
		{
			case 0:
				return "tile.txfarm";
			case 1:
				return "tile.txquarry";
			case 2:
				return "tile.txFurnace";
			default:
				return "tile.txfarm";
		}
	}
}
