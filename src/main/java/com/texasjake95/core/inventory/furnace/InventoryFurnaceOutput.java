package com.texasjake95.core.inventory.furnace;

import net.minecraft.item.ItemStack;

import com.texasjake95.core.inventory.InventoryBase;

public class InventoryFurnaceOutput extends InventoryBase {

	public InventoryFurnaceOutput(int size)
	{
		super(size);
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2)
	{
		return false;
	}
}
