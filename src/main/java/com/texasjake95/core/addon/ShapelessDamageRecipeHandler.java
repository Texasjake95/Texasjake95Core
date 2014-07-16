package com.texasjake95.core.addon;

import java.util.ArrayList;
import java.util.List;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import com.texasjake95.core.recipe.ShapelessDamageRecipe;

public class ShapelessDamageRecipeHandler extends ShapelessRecipeHandler {

	public class CachedShapelessDamageRecipe extends CachedShapelessRecipe {

		public CachedShapelessDamageRecipe(ShapelessDamageRecipe recipe)
		{
			super(recipe.getRecipeOutput());
			this.setIngredients(recipe);
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return this.getCycledIngredients(ShapelessDamageRecipeHandler.this.cycleticks / 20, this.ingredients);
		}

		public void setIngredients(ShapelessDamageRecipe recipe)
		{
			ArrayList<ItemStack> items = recipe.recipeItems;
			this.setIngredients(items);
		}
	}

	@Override
	public String getRecipeName()
	{
		return "Shapeless Dmg Recipe";
	}

	@Override
	public boolean isRecipe2x2(int recipe)
	{
		return this.getIngredientStacks(recipe).size() <= 4;
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		@SuppressWarnings("unchecked")
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe irecipe : allrecipes)
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
			{
				CachedShapelessDamageRecipe recipe = null;
				if (irecipe instanceof ShapelessDamageRecipe)
					recipe = new CachedShapelessDamageRecipe((ShapelessDamageRecipe) irecipe);
				if (recipe == null)
					continue;
				this.arecipes.add(recipe);
			}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("crafting") && this.getClass() == ShapelessDamageRecipeHandler.class)
		{
			@SuppressWarnings("unchecked")
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for (IRecipe irecipe : allrecipes)
			{
				CachedShapelessDamageRecipe recipe = null;
				if (irecipe instanceof ShapelessDamageRecipe)
					recipe = new CachedShapelessDamageRecipe((ShapelessDamageRecipe) irecipe);
				if (recipe == null)
					continue;
				this.arecipes.add(recipe);
			}
		}
		else
			super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		@SuppressWarnings("unchecked")
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe irecipe : allrecipes)
		{
			CachedShapelessDamageRecipe recipe = null;
			if (irecipe instanceof ShapelessDamageRecipe)
				recipe = new CachedShapelessDamageRecipe((ShapelessDamageRecipe) irecipe);
			if (recipe == null)
				continue;
			if (recipe.contains(recipe.ingredients, ingredient))
			{
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				this.arecipes.add(recipe);
			}
		}
	}
}
