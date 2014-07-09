package com.texasjake95.core.inventory.furnace;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import com.texasjake95.core.inventory.MachineBase;
import com.texasjake95.core.recipe.IRecipeProvider;
import com.texasjake95.core.recipe.RecipeProviders;

public class FurnaceBase extends MachineBase {

	public FurnaceBase(int slots, int fuelSlots, int ticksToCook, IInventory tile)
	{
		super(slots, fuelSlots, ticksToCook, tile, RecipeProviders.furnace);
	}

	public FurnaceBase(int slots, int fuelSlots, int ticksToCook, IInventory tile, IRecipeProvider recipes)
	{
		super(slots, fuelSlots, ticksToCook, tile, recipes);
	}

	@Override
	protected boolean needsFuel()
	{
		return this.furnaceBurnTime == 0 && this.canSmelt();
	}

	@Override
	protected boolean handleFuel(int slot)
	{
		this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnace.getFuel().getStackInSlot(slot));
		if (this.furnaceBurnTime > 0)
		{
			if (this.furnace.getFuel().getStackInSlot(slot) != null)
			{
				--this.furnace.getFuel().getStackInSlot(slot).stackSize;
				if (this.furnace.getFuel().getStackInSlot(slot).stackSize == 0)
					this.furnace.getFuel().setInventorySlotContents(slot, this.furnace.getFuel().getStackInSlot(slot).getItem().getContainerItem(this.furnace.getFuel().getStackInSlot(slot)));
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemFuel(ItemStack stack)
	{
		return TileEntityFurnace.isItemFuel(stack);
	}

}
