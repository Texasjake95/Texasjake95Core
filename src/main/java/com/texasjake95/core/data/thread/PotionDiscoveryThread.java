package com.texasjake95.core.data.thread;

import static net.minecraft.init.Items.potionitem;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.RecipeWrapper;

public class PotionDiscoveryThread implements Runnable {

	private static final PotionDiscoveryThread instance = new PotionDiscoveryThread();
	private static boolean init = false;
	private static boolean trueInit = false;

	private static synchronized void addPotion(ItemStack ingred, int basePotion, int result, TreeSet<Integer> allPotions, HashSet<Integer> newPotions)
	{
		if (allPotions.add(result))// it's new
			newPotions.add(result);
		WrappedStack out = new WrappedStack(new ItemStack(Items.potionitem, 1, result));
		WrappedStack in = new WrappedStack(new ItemStack(Items.potionitem, 1, basePotion));
		WrappedStack secondary = new WrappedStack(ingred);
		RecipeWrapper wrapper = new RecipeWrapper(out, in, secondary);
		DataMapWrapper.addWrapper(out, wrapper);
	}

	private static void addPotions(Collection<ItemStack> ingeriants)
	{
		synchronized (ingeriants)
		{
			TreeSet<Integer> allPotions = new TreeSet<Integer>();
			HashSet<Integer> searchPotions = new HashSet<Integer>();
			searchPotions.add(0);
			allPotions.add(0);
			do
			{
				HashSet<Integer> newPotions = new HashSet<Integer>();
				for (Integer basePotion : searchPotions)
				{
					if (ItemPotion.isSplash(basePotion))
						continue;
					for (ItemStack ingred : ingeriants)
					{
						int result = PotionHelper.applyIngredient(basePotion, ingred.getItem().getPotionEffect(ingred));
						if (ItemPotion.isSplash(result))
						{// splash potions qualify
							addPotion(ingred, basePotion, result, allPotions, newPotions);
							continue;
						}
						List<?> baseMods = potionitem.getEffects(basePotion);
						List<?> newMods = potionitem.getEffects(result);// compare ID's
						if (basePotion > 0 && baseMods == newMods || // same modifers and not
																		// water->empty
								baseMods != null && (baseMods.equals(newMods) || newMods == null) || // modifiers
																										// different
																										// and
																										// doesn't
																										// lose
																										// modifiers
								basePotion == result || // same potion
								levelModifierChanged(basePotion, result))// redstone/glowstone
																			// cycle
							continue;
						addPotion(ingred, basePotion, result, allPotions, newPotions);
					}
				}
				searchPotions = newPotions;
			}
			while (!searchPotions.isEmpty());
		}
	}

	public static void init()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Potion Discovery Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	private static synchronized boolean levelModifierChanged(int basePotionID, int result)
	{
		int basemod = basePotionID & 0xE0;
		int resultmod = result & 0xE0;
		return basemod != 0 && basemod != resultmod;
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		addPotions(ItemDiscoveryThread.getIngredents());
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			Texasjake95Core.txLogger.info(String.format("Potion Discovery initialized after %s ms", duration));
		trueInit = true;
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
