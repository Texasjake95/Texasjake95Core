package com.texasjake95.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FurnaceMaceratorRecipeProvider implements IRecipeProvider {

	@Override
	public ItemStack getResult(ItemStack stack)
	{
		ItemStack mac = RecipeProviders.macerator.getResult(stack);
		ItemStack furnace = null;
		if (mac != null)
		{
			mac = mac.copy();
			for (int i = 0; i < mac.stackSize; i++)
			{
				if (furnace == null)
					furnace = RecipeProviders.furnace.getResult(mac).copy();
				else
					furnace.stackSize += furnace.stackSize;
			}
		}
		else
		{
			ItemStack temp = RecipeProviders.furnace.getResult(stack);
			if (temp != null)
				furnace = temp.copy();
		}
		return furnace;
	}

	@Override
	public void addRecipe(Block input, ItemStack output, float exp)
	{
	}

	@Override
	public void addRecipe(Item input, ItemStack output, float exp)
	{
	}

	@Override
	public void addRecipe(ItemStack input, ItemStack output, float exp)
	{
	}

	@Override
	public void addRecipe(String input, ItemStack output, float exp)
	{
	}

	@Override
	public float getEXP(ItemStack stack)
	{
		return 0;
	}
}
