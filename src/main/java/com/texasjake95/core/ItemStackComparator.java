package com.texasjake95.core;

import java.util.Comparator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackComparator implements Comparator<ItemStack> {

	@Override
	public int compare(ItemStack o1, ItemStack o2)
	{
		int compare = Integer.compare(Item.getIdFromItem(o1.getItem()), Item.getIdFromItem(o2.getItem()));
		if (compare != 0)
			return compare;
		compare = Integer.compare(o1.getItemDamage(), o2.getItemDamage());
		if (compare != 0)
			return compare;
		compare = Integer.compare(o1.stackSize, o2.stackSize);
		if (compare != 0)
			return compare;
		return compare;
	}
}
