package com.texasjake95.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MaceratorRecipeProvider implements IRecipeProvider {

	private HashMap<Object, ItemStack> recipes = Maps.newHashMap();
	private HashMap<Object, Float> expOutput = Maps.newHashMap();

	public MaceratorRecipeProvider()
	{
		// this.addRecipe("oreIron", new ItemStack(CoreItems.misc, 2, 0), 0);
		// this.addRecipe("oreGold", new ItemStack(CoreItems.misc, 2, 1), 0);
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
		this.recipes.put(input, output);
		this.expOutput.put(input, exp);
	}

	@Override
	public void addRecipe(String input, ItemStack output, float exp)
	{
		ArrayList<ItemStack> stacks = OreDictionary.getOres(input);
		System.out.println(input + ": " + stacks.size());
		this.addRecipe(stacks, output, exp);
	}

	@Override
	public float getEXP(ItemStack stack)
	{
		return 0;
	}

	@Override
	public ItemStack getResult(ItemStack stack)
	{
		Iterator<Entry<Object, ItemStack>> iterator = this.recipes.entrySet().iterator();
		Entry<Object, ItemStack> entry;
		do
		{
			if (!iterator.hasNext())
				return null;
			entry = iterator.next();
		}
		while (!this.objectMatches(stack, entry.getKey()));
		return entry.getValue();
	}

	@SuppressWarnings("rawtypes")
	private boolean objectMatches(ItemStack stack, Object object)
	{
		if (object instanceof List)
		{
			List list = (List) object;
			Iterator iterator = list.iterator();
			ItemStack temp;
			do
			{
				if (!iterator.hasNext())
					return false;
				temp = (ItemStack) iterator.next();
			}
			while (!this.objectMatches(stack, temp));
			return true;
		}
		else if (object instanceof ItemStack)
		{
			ItemStack stack2 = (ItemStack) object;
			return stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack.getItemDamage());
		}
		return false;
	}
}
