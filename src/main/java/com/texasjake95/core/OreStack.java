package com.texasjake95.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OreStack implements Comparable<OreStack> {

	/**
	 * A Map of names that should be preferred when looking up names for OreDictionary.
	 */
	private static final HashMap<WrappedStack, String> preferedName = Maps.newHashMap();
	static
	{
		// Register dyes
		String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
		for (int i = 0; i < 16; i++)
		{
			ItemStack dye = new ItemStack(Items.dye, 1, i);
			ItemStack block = new ItemStack(Blocks.stained_glass, 1, 15 - i);
			ItemStack pane = new ItemStack(Blocks.stained_glass_pane, 1, 15 - i);
			preferedName.put(new WrappedStack(dye), "dye" + dyes[i]);
			preferedName.put(new WrappedStack(block), "blockGlass" + dyes[i]);
			preferedName.put(new WrappedStack(pane), "paneGlass" + dyes[i]);
		}
	}

	public static boolean canBeWrapped(ItemStack stack)
	{
		return OreDictionary.getOreIDs(stack).length > 0;// || deepCheck(stack);
	}

	private static boolean deepCheck(ItemStack stack)
	{
		for (String name : OreDictionary.getOreNames())
			if (objectMatches(stack, OreDictionary.getOres(name)))
				return true;
		return false;
	}

	public static String getCommonName(List<?> inputs)
	{
		Map<Integer, Integer> counter = Maps.newHashMap();
		for (Object input : inputs)
			if (input instanceof ItemStack)
			{
				if (preferedName.containsKey(new WrappedStack(input)))
				{
					int id = OreDictionary.getOreID(preferedName.get(new WrappedStack(input)));
					if (counter.containsKey(id))
					{
						int count = counter.get(id);
						count += 1;
						counter.put(id, count);
					}
					else
						counter.put(id, 1);
				}
				int[] ids = OreDictionary.getOreIDs((ItemStack) input);
				for (int id : ids)
					if (counter.containsKey(id))
					{
						int count = counter.get(id);
						count += 1;
						counter.put(id, count);
					}
					else
						counter.put(id, 1);
			}
		int id = -1, count = 0;
		for (Entry<Integer, Integer> entry : counter.entrySet())
			if (count < entry.getValue())
			{
				if (OreDictionary.getOreName(entry.getKey()).contains(OreDictionary.getOreName(id)))
					continue;
				id = entry.getKey();
				count = entry.getValue();
			}
		return OreDictionary.getOreName(id);
	}

	private static List<String> getProperNames(ItemStack stack)
	{
		ArrayList<String> names = Lists.newArrayList();
		if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
		{
			for (String name : OreDictionary.getOreNames())
				if (objectMatches(stack, OreDictionary.getOres(name)))
					names.add(name);
		}
		else
			for (int id : OreDictionary.getOreIDs(stack))
				names.add(OreDictionary.getOreName(id));
		return names;
	}

	@SuppressWarnings("rawtypes")
	private static boolean objectMatches(ItemStack stack, Object object)
	{
		if (object instanceof List)
		{
			List list = (List) object;
			Iterator iterator = list.iterator();
			Object temp;
			do
			{
				if (!iterator.hasNext())
					return false;
				temp = iterator.next();
			}
			while (!objectMatches(stack, temp));
			return true;
		}
		else if (object instanceof ItemStack)
		{
			ItemStack stack2 = (ItemStack) object;
			if (stack == null && object != null)
				return false;
			if (object == null && stack != null)
				return false;
			return OreDictionary.itemMatches(stack2, stack, false) || OreDictionary.itemMatches(stack, stack2, false);
		}
		return false;
	}

	private ArrayList<ArrayList<ItemStack>> itemLists = Lists.newArrayList();
	private HashSet<String> nameSet = Sets.newHashSet();
	private int stackSize = 1;

	public OreStack(Block block)
	{
		this(block, OreDictionary.WILDCARD_VALUE);
	}

	public OreStack(Block block, int meta)
	{
		this(block, 1, meta);
	}

	public OreStack(Block block, int stackSize, int meta)
	{
		this(Item.getItemFromBlock(block), stackSize, meta);
	}

	public OreStack(Item item)
	{
		this(item, 0);
	}

	public OreStack(Item item, int meta)
	{
		this(item, 1, meta);
	}

	public OreStack(Item item, int stackSize, int meta)
	{
		this(new ItemStack(item, stackSize, meta));
	}

	public OreStack(ItemStack itemStack)
	{
		this(itemStack, itemStack.stackSize);
	}

	private OreStack(ItemStack itemStack, int stackSize)
	{
		this.stackSize = stackSize;
		for (String name : getProperNames(itemStack))
		{
			this.nameSet.add(name);
			this.itemLists.add(OreDictionary.getOres(name));
		}
	}

	public OreStack(String... oreNames)
	{
		for (String oreName : oreNames)
		{
			this.nameSet.add(oreName);
			this.itemLists.add(OreDictionary.getOres(oreName));
		}
	}

	public boolean areEqual(OreStack oreStack)
	{
		if (oreStack == null)
			return false;
		if (oreStack.nameSet.size() != this.nameSet.size())
			return false;
		if (!this.nameSet.containsAll(oreStack.nameSet))
			return false;
		return true;
	}

	@Override
	public int compareTo(OreStack o)
	{
		int compare = Integer.compare(this.nameSet.size(), o.nameSet.size());
		if (compare != 0)
			return compare;
		ArrayList<String> list1 = Lists.newArrayList(this.nameSet);
		ArrayList<String> list2 = Lists.newArrayList(o.nameSet);
		Collections.sort(list1);
		Collections.sort(list2);
		for (int i = 0; i < list1.size(); i++)
		{
			compare = list1.get(i).compareTo(list2.get(i));
			if (compare != 0)
				return compare;
		}
		return compare;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof OreStack))
			return false;
		OreStack other = (OreStack) object;
		if (other.nameSet.size() != this.nameSet.size())
			return false;
		if (!this.nameSet.containsAll(other.nameSet))
			return false;
		return true;
	}

	public List<ItemStack> getAllItems()
	{
		ArrayList<ItemStack> stacks = Lists.newArrayList();
		for (List<ItemStack> list : this.itemLists)
			stacks.addAll(list);
		return stacks;
	}

	public HashSet<String> getNames()
	{
		return this.nameSet;
	}

	public int getStackSize()
	{
		return this.stackSize;
	}

	@Override
	public int hashCode()
	{
		int hashcode = this.stackSize;
		for (String name : this.nameSet)
			hashcode = 7 * hashcode + name.hashCode();
		return hashcode;
	}

	public boolean hasItemStack(ItemStack stack)
	{
		return objectMatches(stack, this.itemLists);
	}

	public boolean isValid()
	{
		if (this.itemLists.isEmpty())
			return false;
		boolean isVaild = false;
		for (List<ItemStack> stacks : this.itemLists)
			if (!stacks.isEmpty())
				isVaild = true;
		return isVaild;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		sb.append("{");
		for (String string : this.nameSet)
			if (isFirst)
			{
				sb.append(String.format("%s", string));
				isFirst = false;
			}
			else
				sb.append(String.format(", %s", string));
		sb.append("}");
		return sb.toString();
	}

	public String toStringList()
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		sb.append("{");
		sb.append(this);
		for (List<ItemStack> list : this.itemLists)
			for (ItemStack itemStack : list)
			{
				WrappedStack stack = new WrappedStack(itemStack);
				if (isFirst)
				{
					sb.append(String.format("%s", stack));
					isFirst = false;
				}
				else
					sb.append(String.format(", %s", stack));
			}
		return sb.toString();
	}
}
