package com.texasjake95.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class FurnaceRecipeProvider implements IRecipeProvider {

	private static final FurnaceRecipes instance = FurnaceRecipes.smelting();

	@Override
	public void addRecipe(Block block, ItemStack stack, float exp)
	{
		instance.func_151393_a(block, stack, exp);
	}

	@Override
	public void addRecipe(Item item, ItemStack stack, float exp)
	{
		instance.func_151396_a(item, stack, exp);
	}

	@Override
	public void addRecipe(ItemStack input, ItemStack output, float exp)
	{
		instance.func_151394_a(input, output, exp);
	}

	@Override
	public void addRecipe(String input, ItemStack output, float exp)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public float getEXP(ItemStack stack)
	{
		return instance.func_151398_b(stack);
	}

	@Override
	public ItemStack getResult(ItemStack stack)
	{
		return instance.getSmeltingResult(stack);
	}
}
