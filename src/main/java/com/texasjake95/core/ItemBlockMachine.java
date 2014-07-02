package com.texasjake95.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
		switch (itemstack.getItemDamage())
		{
			case 0:
				return "tile.txfarm";
			case 1:
				return "tile.txquarry";
			default:
				return "tile.txfarm";
		}
	}
}
