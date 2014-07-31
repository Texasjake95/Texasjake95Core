package com.texasjake95.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMachine extends ItemBlock {

	public ItemBlockMachine(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
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
			case 2:
				return "tile.txFurnace";
			case 5:
				return "tile.pipe";
			default:
				return "tile.txfarm";
		}
	}
}
