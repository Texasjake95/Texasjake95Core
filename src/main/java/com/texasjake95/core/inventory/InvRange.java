package com.texasjake95.core.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.texasjake95.commons.util.Range;

public class InvRange {

	private IInventory inv;
	private Range range;

	public InvRange(IInventory inv, int max)
	{
		this(inv, 0, max);
	}

	public InvRange(IInventory inv, int min, int max)
	{
		this.inv = new InventoryBase(max, inv.getInventoryStackLimit());
		this.range = Range.range(min, max);
		for (int slot : this.range)
			if (inv.getStackInSlot(slot) != null)
				this.inv.setInventorySlotContents(slot, inv.getStackInSlot(slot).copy());
	}

	public int getInvSize()
	{
		return this.inv.getInventoryStackLimit();
	}

	public Range getSlots()
	{
		return this.range;
	}

	public ItemStack getStack(int slot)
	{
		return this.inv.getStackInSlot(slot);
	}

	public void setItem(int slot, ItemStack stack)
	{
		this.inv.setInventorySlotContents(slot, stack);
	}
}
