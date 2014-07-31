package com.texasjake95.core.data.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.item.ItemStack;

import com.texasjake95.commons.util.pair.ObjectPair;
import com.texasjake95.core.OreStack;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.RecipeWrapper;

public class OreDiscoveryThread implements Runnable {

	static class ReverseHolder extends ObjectPair<WrappedStack, RecipeWrapper> {

		private static final long serialVersionUID = 5691501880368657499L;

		public ReverseHolder(WrappedStack output, RecipeWrapper wrapper)
		{
			super(output, wrapper);
		}

		public WrappedStack getOutput()
		{
			return this.getObject1();
		}

		public RecipeWrapper getRecipe()
		{
			return this.getObject2();
		}
	}

	private static boolean trueInit = false;
	static OreDiscoveryThread instance = new OreDiscoveryThread();
	private static boolean init = false;
	private static List<ReverseHolder> reverseList = Collections.synchronizedList(new ArrayList<ReverseHolder>());
	private static boolean isWaiting;

	private static synchronized void addToReverse(ReverseHolder reverseHolder)
	{
		reverseList.add(reverseHolder);
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static boolean isWaiting()
	{
		return isWaiting;
	}

	public static void startOreDiscovery()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Ore Discovery Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		for (String name : OreDictionary.getOreNames())
		{
			OreStack stack = new OreStack(name);
			for (ItemStack itemStack : stack.getAllItems())
			{
				if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				{
					List<ItemStack> realStacks = DataMapWrapper.getValidItems(itemStack);
					for (ItemStack realStack : realStacks)
						DataMapWrapper.addOreDictionary(stack, realStack);
				}
				DataMapWrapper.addOreDictionary(stack, itemStack);
			}
		}
		isWaiting = true;
		synchronized (DataMapWrapper.lock)
		{
			while (DataMapWrapper.shouldDump() && !ValueDumpThread.isFinished())
				try
				{
					DataMapWrapper.lock.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
		}
		isWaiting = false;
		synchronized (reverseList)
		{
			Iterator<ReverseHolder> inerator = reverseList.iterator();
			while (inerator.hasNext())
			{
				ReverseHolder reverse = inerator.next();
				DataMapWrapper.addWrapper(reverse.getOutput(), reverse.getRecipe());
			}
		}
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			System.out.println(String.format("Ore Discovery initialized after %s ms", duration));
		trueInit = true;
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
