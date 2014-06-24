package com.texasjake95.core.proxy.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemProxy {
	
	public static int getHarvestLevel(Item item, ItemStack stack, String toolClass)
	{
		return item.getHarvestLevel(stack, toolClass);
	}
	
	public static int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return getHarvestLevel(ItemStackProxy.getItem(stack), stack, toolClass);
	}
}
