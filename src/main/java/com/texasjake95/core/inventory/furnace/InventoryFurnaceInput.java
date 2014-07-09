package com.texasjake95.core.inventory.furnace;

import net.minecraft.item.ItemStack;

import com.texasjake95.core.inventory.InventoryBase;
import com.texasjake95.core.recipe.IRecipeProvider;

public class InventoryFurnaceInput extends InventoryBase {

	IRecipeProvider recipes;

	public InventoryFurnaceInput(int size, IRecipeProvider recipes)
	{
		super(size);
		this.recipes = recipes;
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
		return recipes.getResult(stack) != null;
	}
}
