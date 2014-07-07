package com.texasjake95.core.inventory.furnace;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import com.texasjake95.core.inventory.InventoryBase;

public class InventoryFurnaceFuel extends InventoryBase {

	public InventoryFurnaceFuel(int size)
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
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return TileEntityFurnace.isItemFuel(stack);
	}
}
