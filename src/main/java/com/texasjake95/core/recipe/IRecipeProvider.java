package com.texasjake95.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IRecipeProvider {

	public void addRecipe(Block input, ItemStack output, float exp);

	public void addRecipe(Item input, ItemStack output, float exp);

	public void addRecipe(ItemStack input, ItemStack output, float exp);

	public void addRecipe(String input, ItemStack output, float exp);

	public float getEXP(ItemStack stack);

	public ItemStack getResult(ItemStack stack);
}
