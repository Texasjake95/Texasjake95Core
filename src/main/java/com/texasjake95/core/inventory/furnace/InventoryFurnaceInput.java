package com.texasjake95.core.inventory.furnace;

import net.minecraft.item.ItemStack;

import com.texasjake95.core.inventory.InventoryBase;

public class InventoryFurnaceInput extends InventoryBase {

	public InventoryFurnaceInput(int size)
	{
		super(size);
	}

	public boolean isEmpty()
	{
		for (int slot = 0; slot < this.getSizeInventory(); slot++)
			if (this.getStackInSlot(slot) != null)
				return false;
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack stack)
	{
		return true;
	}
}
