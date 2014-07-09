package com.texasjake95.core.inventory;

import net.minecraft.item.ItemStack;

public class SlotData implements Comparable<SlotData> {

	public final int slot;
	public final ItemStack stack;

	public SlotData(int slot, ItemStack stack)
	{
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public String toString()
	{
		return "Slot: " + slot + " => Stack: " + stack;
	}

	@Override
	public int compareTo(SlotData data)
	{
		int compare = Integer.compare(stack.stackSize, data.stack.stackSize);
		if (compare != 0)
			return compare;
		compare = Integer.compare(this.slot, data.slot);
		return compare;
	}
}
