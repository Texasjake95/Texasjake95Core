package com.texasjake95.core.data.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;

public class ItemDiscoveryThread implements Runnable {

	private static ItemDiscoveryThread instance = new ItemDiscoveryThread();
	private static SortedSet<ItemStack> ingredents = Collections.synchronizedSortedSet(Sets.newTreeSet(WrappedStack.itemStackCompare));
	private static SortedSet<ItemStack> allStacks = Collections.synchronizedSortedSet(Sets.newTreeSet(WrappedStack.itemStackCompare));
	private static boolean init = false;
	private static boolean trueInit = false;

	public static Collection<ItemStack> getAllStacks()
	{
		return allStacks;
	}

	public static Collection<ItemStack> getIngredents()
	{
		return ingredents;
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static void startItemDiscovery()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Item Discovery Resolver Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		for (Object obj : Item.itemRegistry)
			if (obj instanceof Item)
				try
				{
					Item item = (Item) obj;
					ArrayList<ItemStack> list = Lists.newArrayList();
					CreativeTabs[] tabs = item.getCreativeTabs();
					for (CreativeTabs tab : tabs)
						item.getSubItems(item, tab, list);
					for (ItemStack stack : list)
					{
						DataMapWrapper.addNode(stack);
						allStacks.add(stack);
						if (stack.getItem().isPotionIngredient(stack))
							ingredents.add(stack);
					}
				}
				catch (Exception e)
				{
					System.out.println(e.getClass() + ": When trying to get sub items for " + obj);
					e.printStackTrace(System.out);
					continue;
				}
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			System.out.println(String.format("Item Discovery finished after %s ms", duration));
		PotionDiscoveryThread.init();
		trueInit = true;
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
