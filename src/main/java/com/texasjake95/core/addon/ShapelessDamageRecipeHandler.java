package com.texasjake95.core.addon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

import com.texasjake95.core.recipe.ShapelessDamageRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class ShapelessDamageRecipeHandler extends ShapedRecipeHandler {
	
	public int[][] stackorder = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };
	
	public class CachedShapelessDamageRecipe extends CachedRecipe {
		
		public CachedShapelessDamageRecipe()
		{
			ingredients = new ArrayList<PositionedStack>();
		}
		
		public CachedShapelessDamageRecipe(ItemStack output)
		{
			this();
			setResult(output);
		}
		
		public CachedShapelessDamageRecipe(ShapelessDamageRecipe recipe)
		{
			this(recipe.getRecipeOutput());
			setIngredients(recipe);
		}
		
		public CachedShapelessDamageRecipe(Object[] input, ItemStack output)
		{
			this(Arrays.asList(input), output);
		}
		
		public CachedShapelessDamageRecipe(List<?> input, ItemStack output)
		{
			this(output);
			setIngredients(input);
		}
		
		public void setIngredients(List<?> items)
		{
			ingredients.clear();
			for (int ingred = 0; ingred < items.size(); ingred++)
			{
				PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
				stack.setMaxSize(1);
				ingredients.add(stack);
			}
		}
		
		public void setIngredients(ShapelessDamageRecipe recipe)
		{
			ArrayList<ItemStack> items = recipe.recipeItems;
			setIngredients(items);
		}
		
		public void setResult(ItemStack output)
		{
			result = new PositionedStack(output, 119, 24);
		}
		
		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 20, ingredients);
		}
		
		@Override
		public PositionedStack getResult()
		{
			return result;
		}
		
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;
	}
	
	public String getRecipeName()
	{
		return "Shapeless Dmg Recipe";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("crafting") && getClass() == ShapelessDamageRecipeHandler.class)
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
				arecipes.add(recipe);
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		@SuppressWarnings("unchecked")
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe irecipe : allrecipes)
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
			{
				CachedShapelessDamageRecipe recipe = null;
				if (irecipe instanceof ShapelessDamageRecipe)
					recipe = new CachedShapelessDamageRecipe((ShapelessDamageRecipe) irecipe);
				if (recipe == null)
					continue;
				arecipes.add(recipe);
			}
		}
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
				arecipes.add(recipe);
			}
		}
	}
	
	@Override
	public boolean isRecipe2x2(int recipe)
	{
		return getIngredientStacks(recipe).size() <= 4;
	}
}
