package com.texasjake95.core.data.thread;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.RecipeWrapper;
import com.texasjake95.core.recipe.RecipeProviders;

public class RecipeDiscoveryThread implements Runnable {

	static RecipeDiscoveryThread instance = new RecipeDiscoveryThread();
	private static boolean init = false;
	private static boolean trueInit = false;
	private static boolean isWaiting = false;

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static boolean isWaiting()
	{
		return isWaiting;
	}

	public static void startRecipeDiscovery()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Recipe Discovery Resolver Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		for (Object object : CraftingManager.getInstance().getRecipeList())
			if (object instanceof IRecipe)
			{
				IRecipe recipe = (IRecipe) object;
				RecipeWrapper wrapper = new RecipeWrapper(recipe);
				WrappedStack output = new WrappedStack(recipe.getRecipeOutput());
				DataMapWrapper.addWrapper(output, wrapper);
			}
		Map<?, ?> furnace = FurnaceRecipes.smelting().getSmeltingList();
		for (Entry<?, ?> entry : furnace.entrySet())
		{
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (key instanceof ItemStack && val instanceof ItemStack)
			{
				WrappedStack in = new WrappedStack(key);
				WrappedStack out = new WrappedStack(val, true);
				RecipeWrapper wrapper = new RecipeWrapper(out, in);
				DataMapWrapper.addWrapper(out, wrapper);
			}
		}
		Object o = RecipeProviders.macerator;
		RecipeDiscoveryThread.isWaiting = true;
		synchronized (DataMapWrapper.lock)
		{
			while (DataMapWrapper.shouldDump() && !ValueDumpThread.isFinished())
				try
				{
					DataMapWrapper.lock.wait();
				}
				catch (InterruptedException e)
				{
				}
		}
		RecipeDiscoveryThread.isWaiting = false;
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			System.out.println(String.format("Recipe Discovery finished after %s ms", duration));
		trueInit = true;
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
