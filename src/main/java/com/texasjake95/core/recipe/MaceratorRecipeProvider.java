package com.texasjake95.core.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.items.CoreItems;
import com.texasjake95.core.items.ItemMisc;

public class MaceratorRecipeProvider implements IRecipeProvider {

	private HashMap<WrappedStack, ItemStack> recipes = Maps.newHashMap();
	private HashMap<WrappedStack, Float> expOutput = Maps.newHashMap();

	public MaceratorRecipeProvider()
	{
		this.addRecipe("oreIron", new ItemStack(CoreItems.misc, 2, ItemMisc.ironDust), 0);
		this.addRecipe("oreGold", new ItemStack(CoreItems.misc, 2, ItemMisc.goldDust), 0);
	}

	@Override
	public void addRecipe(Block input, ItemStack output, float exp)
	{
		this.addRecipe(new ItemStack(input), output, exp);
	}

	@Override
	public void addRecipe(Item input, ItemStack output, float exp)
	{
		this.addRecipe(new ItemStack(input), output, exp);
	}

	@Override
	public void addRecipe(ItemStack input, ItemStack output, float exp)
	{
		this.addRecipe((Object) input, output, exp);
	}

	private void addRecipe(Object input, ItemStack output, float exp)
	{
		if (WrappedStack.canWrap(input))
		{
			WrappedStack stack = new WrappedStack(input);
			DataMapWrapper.addWrapper(new WrappedStack(output, true), stack);
			this.recipes.put(stack, output);
			this.expOutput.put(stack, exp);
		}
	}

	@Override
	public void addRecipe(String input, ItemStack output, float exp)
	{
		this.addRecipe(new WrappedStack(input), output, exp);
	}

	@Override
	public float getEXP(ItemStack stack)
	{
		Iterator<Entry<WrappedStack, Float>> iterator = this.expOutput.entrySet().iterator();
		Entry<WrappedStack, Float> entry;
		do
		{
			if (!iterator.hasNext())
				return 0;
			entry = iterator.next();
		}
		while (!entry.getKey().contains(stack));
		return entry.getValue();
	}

	@Override
	public ItemStack getResult(ItemStack stack)
	{
		Iterator<Entry<WrappedStack, ItemStack>> iterator = this.recipes.entrySet().iterator();
		Entry<WrappedStack, ItemStack> entry;
		do
		{
			if (!iterator.hasNext())
				return null;
			entry = iterator.next();
		}
		while (!entry.getKey().contains(stack));
		return entry.getValue();
	}
}
