package com.texasjake95.core.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapelessDamageRecipe implements IRecipe {
	
	/** Is a List of ItemStack that composes the recipe. */
	public final ArrayList<ItemStack> recipeItems = new ArrayList<ItemStack>();
	/** Is the ItemStack that you get when craft the recipe. */
	private final ItemStack recipeOutput;
	
	public ShapelessDamageRecipe(ItemStack par1ItemStack, Object... recipe)
	{
		ArrayList<ItemStack> inputFinal = new ArrayList<ItemStack>();
		Object[] inputs = recipe;
		int inputLength = recipe.length;
		for (int i = 0; i < inputLength; ++i)
		{
			Object input = inputs[i];
			if (input instanceof ItemStack)
			{
				inputFinal.add(((ItemStack) input).copy());
			}
			else if (input instanceof Item)
			{
				inputFinal.add(new ItemStack((Item) input));
			}
			else
			{
				if (!(input instanceof Block))
					throw new RuntimeException("Invalid shapeless damage recipe!");
				inputFinal.add(new ItemStack((Block) input));
			}
		}
		this.recipeItems.addAll(inputFinal);
		this.recipeOutput = par1ItemStack;
	}
	
	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
	{
		ArrayList<ItemStack> remainingIngredients = new ArrayList<ItemStack>(this.recipeItems);
		int damage = 0;
		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				ItemStack stack = par1InventoryCrafting.getStackInRowAndColumn(y, x);
				if (stack != null)
				{
					boolean validIngredient = false;
					Iterator<ItemStack> iterator = remainingIngredients.iterator();
					while (iterator.hasNext())
					{
						ItemStack ingredient = iterator.next();
						if (stack.getItem() == ingredient.getItem())
						{
							validIngredient = true;
							if (stack.getItem() instanceof ICraftDamage)
							{
								if (((ICraftDamage) stack.getItem()).useDamage(stack.getItemDamage()))
								{
									damage = damage + stack.getItemDamage();
									remainingIngredients.remove(ingredient);
									break;
								}
								else
								{
									remainingIngredients.remove(ingredient);
									break;
								}
							}
							else
							{
								damage = damage + stack.getItemDamage();
								remainingIngredients.remove(ingredient);
							}
							break;
						}
					}
					if (!validIngredient)
						return null;
				}
			}
		}
		ItemStack output = new ItemStack(this.recipeOutput.copy().getItem(), this.recipeOutput.copy().stackSize, damage);
		return output.copy();
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.recipeOutput;
	}
	
	/**
	 * Returns the size of the recipe area
	 */
	@Override
	public int getRecipeSize()
	{
		return this.recipeItems.size();
	}
	
	@Override
	public boolean matches(InventoryCrafting var1, World var2)
	{
		ArrayList<ItemStack> remainingIngredients = new ArrayList<ItemStack>(this.recipeItems);
		for (int x = 0; x < 3; ++x)
		{
			for (int y = 0; y < 3; ++y)
			{
				ItemStack stack = var1.getStackInRowAndColumn(y, x);
				if (stack != null)
				{
					boolean validIngredient = false;
					Iterator<ItemStack> iterator = remainingIngredients.iterator();
					while (iterator.hasNext())
					{
						ItemStack ingredient = iterator.next();
						if (stack.getItem() == ingredient.getItem())
						{
							validIngredient = true;
							remainingIngredients.remove(ingredient);
							break;
						}
					}
					if (!validIngredient)
						return false;
				}
			}
		}
		return remainingIngredients.isEmpty();
	}
}
