package com.texasjake95.core.lib.pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.commons.util.pair.ObjectPair;

public class ItemIntPair extends ObjectPair<Item, Integer> {

	private static final long serialVersionUID = 6309327972344177571L;

	public ItemIntPair(Item item, Integer meta)
	{
		super(item, meta);
	}

	public ItemIntPair(ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage());
	}

	public Item getItem()
	{
		return this.getObject1();
	}

	public int getMeta()
	{
		return this.getObject2();
	}
}
