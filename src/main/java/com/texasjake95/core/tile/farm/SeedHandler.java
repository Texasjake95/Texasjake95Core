package com.texasjake95.core.tile.farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.texasjake95.core.api.farm.IGrowthChecker;
import com.texasjake95.core.api.farm.IHarvester;
import com.texasjake95.core.api.farm.ISeedProvider;
import com.texasjake95.core.api.farm.ItemIntPair;
import com.texasjake95.core.lib.utils.BlockUtils;
import com.texasjake95.core.proxy.item.ItemStackProxy;

public class SeedHandler {

	private static HashMap<Block, HashMap<Integer, ItemIntPair>> seedRegistry = Maps.newHashMap();
	private static HashMap<Block, IGrowthChecker> growthRegistry = Maps.newHashMap();
	private static HashMap<Block, IHarvester> harvestRegistry = Maps.newHashMap();
	private static HashMap<Block, ISeedProvider> seedProRegistry = Maps.newHashMap();
	private static HashSet<ISeedProvider> seedSet = Sets.newHashSet();
	private static HashMap<Item, HashSet<Integer>> seeds = Maps.newHashMap();
	private static VanillaChecker vanillaChecker = new VanillaChecker();
	static
	{
		registerSeed(Blocks.wheat, 7, Items.wheat_seeds, 0);
		registerSeed(Blocks.potatoes, 7, Items.potato, 0);
		registerSeed(Blocks.carrots, 7, Items.carrot, 0);
		registerSeed(Blocks.cactus, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.reeds, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.pumpkin, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.melon_block, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.nether_wart, 3, Items.nether_wart, 0);
	}

	public static ArrayList<ItemStack> getHarvests(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
	{
		IHarvester harvest = harvestRegistry.get(block);
		if (harvest != null)
			return harvest.getDrops(player, world, x, y, z, block, meta);
		else
			return BlockUtils.getDrops(player, world, x, y, z, block, meta);
	}

	public static ItemIntPair getSeed(Block block, int meta)
	{
		ISeedProvider seed = seedProRegistry.get(block);
		if (seed != null)
		{
			ItemIntPair pair = seed.getSeed(block, meta);
			if (pair != null)
				return pair;
		}
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap == null)
			return null;
		return metaMap.get(meta);
	}

	public static boolean isFullGrown(Block block, int meta, World world, int x, int y, int z)
	{
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap != null && metaMap.containsKey(meta))
			return true;
		IGrowthChecker checker = growthRegistry.get(block);
		if (checker == null)
			return false;
		return checker.isGrown(block, meta, world, x, y, z);
	}

	public static boolean isSeed(ItemStack stack)
	{
		for (ISeedProvider seed : seedSet)
			if (seed != null && seed.isSeed(ItemStackProxy.getItem(stack), ItemStackProxy.getMetadata(stack)))
				return true;
		HashSet<Integer> meta = seeds.get(ItemStackProxy.getItem(stack));
		if (meta == null)
			return false;
		return meta.contains(ItemStackProxy.getMetadata(stack));
	}

	public static void registerSeed(Block block, IGrowthChecker growth, IHarvester harvester, ISeedProvider seed)
	{
		growthRegistry.put(block, growth);
		harvestRegistry.put(block, harvester);
		seedProRegistry.put(block, seed);
		seedSet.add(seed);
	}

	public static void registerSeed(Block block, int meta, Item seed, int seedMeta)
	{
		ItemIntPair pair = new ItemIntPair(seed, seedMeta);
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap == null)
			metaMap = Maps.newHashMap();
		metaMap.put(meta, pair);
		seedRegistry.put(block, metaMap);
		HashSet<Integer> seedList = seeds.get(seed);
		if (seedList == null)
			seedList = Sets.newHashSet();
		seedList.add(seedMeta);
		seeds.put(seed, seedList);
	}
}
