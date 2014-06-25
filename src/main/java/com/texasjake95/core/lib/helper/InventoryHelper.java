package com.texasjake95.core.lib.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryHelper {
	
	public static void addToInventory(IInventory inv, ItemStack stack)
	{
		if (stack == null)
			return;
		for (int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			if (stack.stackSize == 0)
			{
				break;
			}
			ItemStack invStack = inv.getStackInSlot(slot);
			if (invStack == null)
			{
				if (inv.isItemValidForSlot(slot, stack))
				{
					inv.setInventorySlotContents(slot, stack.copy());
					stack.stackSize = 0;
				}
				break;
			}
			if (stack.getItem() == invStack.getItem() && stack.getItemDamage() == invStack.getItemDamage())
				if (inv.isItemValidForSlot(slot, stack))
				{
					int maxSize = Math.min(inv.getInventoryStackLimit(), invStack.getMaxStackSize());
					while (invStack.stackSize < maxSize && stack.stackSize > 0)
					{
						invStack.stackSize += 1;
						stack.stackSize -= 1;
					}
					inv.setInventorySlotContents(slot, invStack);
				}
		}
	}
}
