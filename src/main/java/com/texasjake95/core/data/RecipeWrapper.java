package com.texasjake95.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import test.DataMap.IValueHandler;

import com.texasjake95.core.OreStack;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.WrappedStack.WrappedType;
import com.texasjake95.core.recipe.ShapelessDamageRecipe;

public class RecipeWrapper implements IValueHandler<WrappedStack, RecipeWrapper, FloatValue> {

	private ArrayList<WrappedStack> inputs = Lists.newArrayList();
	public boolean isValid = true;
	private WrappedStack output;

	public RecipeWrapper(IRecipe recipe)
	{
		if (recipe instanceof ShapedRecipes)
		{
			ShapedRecipes shaped = (ShapedRecipes) recipe;
			ItemStack[] stacks = shaped.recipeItems;
			for (ItemStack stack : stacks)
				if (stack != null)
					this.inputs.add(new WrappedStack(stack));
		}
		else if (recipe instanceof ShapelessRecipes)
		{
			ShapelessRecipes shapless = (ShapelessRecipes) recipe;
			List<?> inputs = shapless.recipeItems;
			for (Object obj : inputs)
				if (WrappedStack.canWrap(obj))
					this.inputs.add(new WrappedStack(obj));
		}
		else if (recipe instanceof ShapelessOreRecipe)
		{
			ShapelessOreRecipe shaplessOre = (ShapelessOreRecipe) recipe;
			ArrayList<Object> inputs = ReflectionHelper.getPrivateValue(ShapelessOreRecipe.class, shaplessOre, "input");
			for (Object input : inputs)
				this.addObject(input);
		}
		else if (recipe instanceof ShapedOreRecipe)
		{
			ShapedOreRecipe shapedOre = (ShapedOreRecipe) recipe;
			Object[] inputs = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOre, "input");
			for (Object input : inputs)
				this.addObject(input);
		}
		else if (recipe instanceof ShapelessDamageRecipe)
		{
			ShapelessDamageRecipe damageRecipe = (ShapelessDamageRecipe) recipe;
			for (Object input : damageRecipe.recipeItems)
				this.addObject(input);
		}
		else
			this.isValid = false;
		for (WrappedStack stack : this.inputs)
			if (!stack.isValid)
				this.isValid = false;
		if (this.isValid)
			this.output = new WrappedStack(recipe.getRecipeOutput(), true);
		Collections.sort(this.inputs);
	}

	public RecipeWrapper(WrappedStack out, List<WrappedStack> in)
	{
		this.output = out;
		if (!this.output.isValid)
			this.isValid = false;
		for (WrappedStack stack : in)
		{
			if (!stack.isValid)
				this.isValid = false;
			this.inputs.add(stack);
		}
		Collections.sort(this.inputs);
	}

	public RecipeWrapper(WrappedStack out, WrappedStack... in)
	{
		this.output = out;
		if (!this.output.isValid)
			this.isValid = false;
		for (WrappedStack stack : in)
		{
			if (!stack.isValid)
				this.isValid = false;
			this.inputs.add(stack);
		}
		Collections.sort(this.inputs);
	}

	private void addObject(Object input)
	{
		if (input instanceof List)
		{
			String name = OreStack.getCommonName((List) input);
			if (name != null && !name.equals("Unknown"))
				this.inputs.add(new WrappedStack(new OreStack(name)));
			else
			{
				this.isValid = false;
				return;
			}
		}
		else if (input instanceof ItemStack)
			this.inputs.add(new WrappedStack(input));
	}

	@Override
	public int compareTo(RecipeWrapper o)
	{
		int compare = this.output.compareTo(o.output);
		if (compare != 0)
			return compare;
		compare = Integer.compare(this.inputs.size(), o.inputs.size());
		if (compare != 0)
			return compare;
		for (int i = 0; i < this.inputs.size(); i++)
		{
			compare = this.inputs.get(i).compareTo(o.inputs.get(i));
			if (compare != 0)
				return compare;
		}
		return compare;
	}

	@Override
	public FloatValue determineValue()
	{
		FloatValue value = new FloatValue(0);
		for (WrappedStack input : this.inputs)
		{
			FloatValue temp = DataMapWrapper.getValue(input);
			if (temp == null)
				return null;
			FloatValue container = null;
			if (input.type == WrappedType.ITEMSTACK)
			{
				ItemStack stack = ((ItemStack) input.getEffectiveStack()).getItem().getContainerItem((ItemStack) input.getEffectiveStack());
				if (stack != null)
					container = DataMapWrapper.getValue(stack);
			}
			if (container != null && temp.value > container.value)
				temp = new FloatValue(temp.value - container.value);
			FloatValue result = new FloatValue(temp.value * input.getStackSize());
			value = value.combineValue(result);
		}
		value.value = value.value / this.output.getStackSize();
		return value;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof RecipeWrapper))
			return false;
		RecipeWrapper wrapper = (RecipeWrapper) object;
		if (!this.isValid || !wrapper.isValid)
			return false;
		Collections.sort(this.inputs);
		Collections.sort(wrapper.inputs);
		if (!this.output.equals(wrapper.output))
			return false;
		if (this.inputs.size() != wrapper.inputs.size())
			return false;
		for (int i = 0; i < this.inputs.size(); i++)
			if (!this.inputs.get(i).equals(wrapper.inputs.get(i)))
				return false;
		return true;
	}

	@Override
	public List<WrappedStack> getConnectedNodes()
	{
		return this.inputs;
	}

	@Override
	public int hashCode()
	{
		int hashCode = this.output.hashCode();
		hashCode = 7 * hashCode + this.inputs.size();
		for (WrappedStack stack : this.inputs)
			hashCode = 7 * hashCode + stack.hashCode();
		return hashCode;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		sb.append("{");
		for (WrappedStack input : this.inputs)
			if (isFirst)
			{
				sb.append(String.format("%s", input));
				isFirst = false;
			}
			else
				sb.append(String.format(", %s", input));
		sb.append("}");
		return sb.toString();
	}
}
