package com.texasjake95.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IRecipeProvider {

	void addRecipe(Block input, ItemStack output, float exp);

	void addRecipe(Item input, ItemStack output, float exp);

	void addRecipe(ItemStack input, ItemStack output, float exp);

	void addRecipe(String input, ItemStack output, float exp);

	float getEXP(ItemStack stack);

	ItemStack getResult(ItemStack stack);
}
