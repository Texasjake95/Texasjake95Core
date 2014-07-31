package com.texasjake95.core.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import test.DataMap.RemovalType;

import com.texasjake95.core.OreStack;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.handler.DefaultValueHandler;
import com.texasjake95.core.data.thread.ConfigRemapThread;
import com.texasjake95.core.data.thread.DynamicEnergyValueInitThread;

public class DataMapWrapper {

	public static final Object lock = new Object();
	private static final ValueMap testMap = new ValueMap();
	private static final boolean dump = false;
	private static boolean init = false;
	private static final boolean minecraftOnly = false;
	/**
	 * .
	 */
	private static Set<WrappedStack> wildStacks = Collections.synchronizedSet(new HashSet<WrappedStack>());
	private static boolean resolved = false;

	public static synchronized void addNode(Object object)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				List<ItemStack> realStacks = getValidItems(stack);
				for (ItemStack realStack : realStacks)
					addWildCard(stack, realStack);
			}
			addNode(new WrappedStack(object));
			if (OreStack.canBeWrapped(stack))
			{
				OreStack oreStack = new OreStack(stack);
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					addNode(realStack);
				}
			}
		}
		else if (object instanceof OreStack)
		{
			OreStack oreStack = (OreStack) object;
			if (oreStack.getNames().size() > 1)
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					addNode(realStack);
				}
			else if (!oreStack.getNames().isEmpty())
				addNode(new WrappedStack(object));
		}
		else if (object instanceof FluidStack)
			addNode(new WrappedStack(object));
		else if (object instanceof Block)
		{
			ItemStack stack = new ItemStack((Block) object);
			addNode(stack);
		}
		else if (object instanceof Item)
		{
			ItemStack stack = new ItemStack((Item) object);
			addNode(stack);
		}
		else if (object instanceof WrappedStack)
		{
			if (isValid((WrappedStack) object))
				testMap.addNode((WrappedStack) object);
		}
		else if (!WrappedStack.canWrap(object))
			return;
	}

	public static synchronized void addOreDictionary(OreStack oreStack, ItemStack itemStack)
	{
		RecipeWrapper wrapper = new RecipeWrapper(new WrappedStack(oreStack), new WrappedStack(itemStack));
		testMap.addWrapper(new WrappedStack(oreStack), wrapper);
		RecipeWrapper reverse = new RecipeWrapper(new WrappedStack(itemStack), new WrappedStack(oreStack));
		testMap.addOreWrapper(new WrappedStack(itemStack), reverse);
	}

	public static void addProvider(Item item, IValueProvider valueProvider)
	{
		testMap.addValueProvider(item, valueProvider);
	}

	public static synchronized void addValue(Object object, float value, ValueMap.Type type)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				List<ItemStack> realStacks = getValidItems(stack);
				for (ItemStack realStack : realStacks)
					addValue(realStack, value, type);
			}
			addValue(new WrappedStack(object), value, type);
		}
		else if (object instanceof OreStack)
		{
			OreStack oreStack = (OreStack) object;
			if (oreStack.getNames().size() > 1)
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					addValue(realStack, value, type);
				}
			else
				addValue(new WrappedStack(object), value, type);
		}
		else if (object instanceof String)
			addValue(new OreStack((String) object), value, type);
		else if (object instanceof List<?>)
			addValue(new OreStack(OreStack.getCommonName((List<?>) object)), value, type);
		else if (object instanceof FluidStack)
			addValue(new WrappedStack(object), value, type);
		else if (object instanceof Block)
		{
			ItemStack stack = new ItemStack((Block) object);
			addValue(stack, value, type);
		}
		else if (object instanceof Item)
		{
			ItemStack stack = new ItemStack((Item) object);
			addValue(stack, value, type);
		}
		else if (object instanceof WrappedStack)
		{
			if (isValid((WrappedStack) object))
				testMap.setValue((WrappedStack) object, new FloatValue(value), type);
		}
		else if (object instanceof String)
		{
			OreStack oreStack = new OreStack((String) object);
			if (oreStack.isValid())
				addValue(oreStack, value, type);
		}
	}

	public static synchronized void addWildCard(ItemStack wildStack, ItemStack itemStack)
	{
		RecipeWrapper wrapper = new RecipeWrapper(new WrappedStack(wildStack), new WrappedStack(itemStack));
		testMap.addWrapper(new WrappedStack(wildStack), wrapper);
		RecipeWrapper reverse = new RecipeWrapper(new WrappedStack(itemStack), new WrappedStack(wildStack));
		testMap.addWildCardWrapper(new WrappedStack(itemStack), reverse);
	}

	public static synchronized void addWrapper(Object output, Object... inputs)
	{
		if (!(WrappedStack.canWrap(output) || output instanceof WrappedStack))
			return;
		addNode(output);
		WrappedStack outputStack = createWrappedStack(output);
		List<WrappedStack> inputStacks = Lists.newArrayList();
		for (Object input : inputs)
			if (!WrappedStack.canWrap(input))
				return;
			else
			{
				addNode(input);
				inputStacks.add(new WrappedStack(input));
			}
		addWrapper(outputStack, new RecipeWrapper(outputStack, inputStacks));
	}

	public static synchronized void addWrapper(WrappedStack stack, RecipeWrapper wrapper)
	{
		if (wrapper.isValid && isValid(stack))
		{
			for (WrappedStack input : wrapper.getConnectedNodes())
			{
				if (!isValid(input))
					return;
				addNode(input.getEffectiveStack());
			}
			testMap.addWrapper(stack, wrapper);
		}
	}

	private static synchronized WrappedStack createWrappedStack(Object object)
	{
		if (object instanceof WrappedStack)
			return (WrappedStack) object;
		if (!WrappedStack.canWrap(object))
			return null;
		return new WrappedStack(object);
	}

	public static void dump()
	{
		testMap.dump();
	}

	public static synchronized void dumpCritNodes()
	{
		List<WrappedStack> critNodes = testMap.getCritNodes();
		Collections.sort(critNodes);
		try
		{
			File file = new File("CritNodes.txt");
			if (!file.exists())
			{
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
			}
			PrintStream stream = new PrintStream(new FileOutputStream(file));
			for (WrappedStack stack : critNodes)
				stream.println(stack);
			stream.close();
		}
		catch (Exception e)
		{
		}
	}

	public static void finishSetup()
	{
		DynamicEnergyValueInitThread.resovleValues();
	}

	public static List<WrappedStack> getAllNodes()
	{
		return testMap.getAllNodes();
	}

	private static synchronized float getFactor(Object object)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			if (stack.getItem().isDamageable())
			{
				float percent = (float) stack.getItemDamage() / (float) stack.getMaxDamage();
				return 1 - percent;
			}
		}
		return 1;
	}

	public static Set<WrappedStack> getNodes(Object object)
	{
		if (object instanceof ItemStack)
			return getNodes(new WrappedStack(object));
		else if (object instanceof OreStack)
		{
			OreStack oreStack = (OreStack) object;
			if (oreStack.getNames().size() > 1)
			{
				Set<WrappedStack> stacks = Sets.newTreeSet();
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					for (WrappedStack stack : getNodes(realStack))
						stacks.add(stack);
				}
				return stacks;
			}
			else if (!oreStack.getNames().isEmpty())
				return getNodes(new WrappedStack(object));
		}
		else if (object instanceof FluidStack)
			return getNodes(new WrappedStack(object));
		else if (object instanceof Block)
		{
			ItemStack stack = new ItemStack((Block) object);
			return getNodes(stack);
		}
		else if (object instanceof Item)
		{
			ItemStack stack = new ItemStack((Item) object);
			return getNodes(stack);
		}
		else if (object instanceof WrappedStack)
		{
			if (isValid((WrappedStack) object))
				return getNodesFromValue(getValue(object));
		}
		else if (!WrappedStack.canWrap(object))
			return new TreeSet<WrappedStack>();
		return new TreeSet<WrappedStack>();
	}

	public static Set<WrappedStack> getNodesFromValue(FloatValue value)
	{
		if (value != null)
			return getNodesInRange(value, value);
		return new TreeSet<WrappedStack>();
	}

	public static Set<WrappedStack> getNodesInRange(float min, float max)
	{
		return getNodesInRange(new FloatValue(min), new FloatValue(max));
	}

	public static Set<WrappedStack> getNodesInRange(float value, float neg, float pos)
	{
		return getNodesInRange(value - neg, value + pos);
	}

	public static Set<WrappedStack> getNodesInRange(FloatValue value, float neg, float pos)
	{
		if (value == null)
			return new TreeSet<WrappedStack>();
		return getNodesInRange(value.value - neg, value.value + pos);
	}

	public static Set<WrappedStack> getNodesInRange(FloatValue min, FloatValue max)
	{
		if (!ConfigRemapThread.isFinished())
			return new TreeSet<WrappedStack>();
		if (min != null && max != null)
			return testMap.getNodesInRange(min, max);
		return new TreeSet<WrappedStack>();
	}

	public static synchronized List<ItemStack> getValidItems(ItemStack itemStack)
	{
		ArrayList<ItemStack> list = Lists.newArrayList();
		Item item = itemStack.getItem();
		CreativeTabs[] tabs = item.getCreativeTabs();
		for (CreativeTabs tab : tabs)
			item.getSubItems(item, tab, list);
		return list;
	}

	public static synchronized FloatValue getValue(Object object)
	{
		FloatValue value = getValueNoScale(object);
		if (value != null)
			value = new FloatValue(value.value * getFactor(object));
		return value;
	}

	public static synchronized FloatValue getValueNoScale(Object object)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			if (stack.getItem() instanceof Item)
			{
			}
		}
		if (object instanceof WrappedStack)
			return getValue(((WrappedStack) object).getEffectiveStack());
		if (WrappedStack.canWrap(object))
		{
			FloatValue value = testMap.getValue(new WrappedStack(object));
			return value;
		}
		return null;
	}

	public static synchronized Collection<WrappedStack> getWildStacks()
	{
		return wildStacks;
	}

	public static synchronized boolean hasValue(Object object)
	{
		if (WrappedStack.canWrap(object))
			return testMap.hasValue(new WrappedStack(object));
		return false;
	}

	public static synchronized void init()
	{
		if (!init)
		{
			DefaultValueHandler.init();
			init = true;
		}
	}

	private static synchronized boolean isValid(WrappedStack stack)
	{
		if (minecraftOnly)
			if (stack.getEffectiveStack() instanceof ItemStack)
			{
				ItemStack itemStack = (ItemStack) stack.getEffectiveStack();
				return GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).modId.equals("minecraft");
			}
		return stack.isValid;
	}

	public static void removeValue(Object object, boolean trueRemove, RemovalType removal)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				List<ItemStack> realStacks = getValidItems(stack);
				for (ItemStack realStack : realStacks)
					removeValue(realStack, trueRemove, removal);
			}
			removeValue(new WrappedStack(object), trueRemove, removal);
			if (OreStack.canBeWrapped(stack))
			{
				OreStack oreStack = new OreStack(stack);
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					removeValue(realStack, trueRemove, removal);
				}
			}
		}
		else if (object instanceof OreStack)
		{
			OreStack oreStack = (OreStack) object;
			if (oreStack.getNames().size() > 1)
				for (String name : oreStack.getNames())
				{
					OreStack realStack = new OreStack(name);
					removeValue(realStack, trueRemove, removal);
				}
			else if (!oreStack.getNames().isEmpty())
				removeValue(new WrappedStack(object), trueRemove, removal);
		}
		else if (object instanceof FluidStack)
			removeValue(new WrappedStack(object), trueRemove, removal);
		else if (object instanceof Block)
		{
			ItemStack stack = new ItemStack((Block) object);
			removeValue(stack, trueRemove, removal);
		}
		else if (object instanceof Item)
		{
			ItemStack stack = new ItemStack((Item) object);
			removeValue(stack, trueRemove, removal);
		}
		else if (object instanceof WrappedStack)
		{
			if (isValid((WrappedStack) object))
				testMap.removeValue((WrappedStack) object, trueRemove, removal);
		}
		else if (!WrappedStack.canWrap(object))
			return;
	}

	public static synchronized void resolveValues()
	{
		init();
		if (!resolved)
		{
			for (int pass = 0; pass < 3; pass++)
				testMap.resolveValues(true);
			// dump();
			resolved = true;
		}
	}

	public static synchronized void resolveValues(boolean forceRemap)
	{
		testMap.resolveValues(forceRemap);
	}

	public static boolean shouldDump()
	{
		return dump;
	}

	public static void toggleDebug()
	{
		testMap.toggleDebugMode();
	}

	public static void transplantValues()
	{
		testMap.transplantValues();
	}
}
