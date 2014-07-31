package com.texasjake95.core.api.farm;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.commons.util.pair.ObjectPair;

/**
 * This class is basically an {@link ItemStack} without the stackSize.
 *
 * @author Texasjake95
 *
 */
public class ItemIntPair extends ObjectPair<Item, Integer> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6309327972344177571L;

	/**
	 * Create an ItemIntPair using an {@link Item} and the meta data of the item.
	 *
	 * @param item
	 *            the Item
	 * @param meta
	 *            the Meta
	 */
	public ItemIntPair(Item item, Integer meta)
	{
		super(item, meta);
	}

	/**
	 * Create an ItemIntPair using an {@link ItemStack}.<br>
	 * Boils down to ItemIntPair(Item item, Integer meta)
	 *
	 * @param stack
	 *            the {@link ItemStack} to convert in to an ItemIntPair
	 */
	public ItemIntPair(ItemStack stack)
	{
		this(stack.getItem(), stack.getItemDamage());
	}

	/**
	 * Retrieves the {@link Item} of the pair.
	 *
	 * @return the {@link Item} associated with this pair
	 */
	public Item getItem()
	{
		return this.getObject1();
	}

	/**
	 * Retrieves the metadata of the pair.
	 *
	 * @return the metadata associated with this pair
	 */
	public int getMeta()
	{
		return this.getObject2();
	}

	@Override
	public String toString()
	{
		return String.format("%s:%d", this.getItem().getUnlocalizedName(), this.getMeta());
	}
}
