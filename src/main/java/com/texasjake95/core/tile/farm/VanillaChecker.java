package com.texasjake95.core.tile.farm;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.texasjake95.core.lib.pair.ItemIntPair;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.TileEntityFarm;

public class VanillaChecker implements IGrowthChecker, IHarvester, ISeedProvider {
	
	@Override
	public ArrayList<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta, MachineType type)
	{
		ArrayList<ItemStack> returnList = Lists.newArrayList();
		if (block == Blocks.cactus || block == Blocks.reeds)
		{
			for (int i = 2; i >= (type == MachineType.FARM ? 1 : 0); i--)
			{
				if (WorldProxy.isAirBlock(world, x, y + i, z))
				{
					continue;
				}
				for (ItemStack stack : TileEntityFarm.getNormalDrops(player, world, x, y + i, z, block, meta))
				{
					returnList.add(stack);
				}
			}
		}
		if (block == Blocks.pumpkin || block == Blocks.melon_block)
		{
			for (ItemStack stack : TileEntityFarm.getNormalDrops(player, world, x, y, z, block, meta))
			{
				returnList.add(stack);
			}
		}
		return returnList;
	}
	
	@Override
	public ItemIntPair getSeed(Block block, int meta)
	{
		return null;
	}
	
	@Override
	public boolean isGrown(Block block, int meta, World world, int x, int y, int z)
	{
		if (block == Blocks.cactus || block == Blocks.reeds)
			return !WorldProxy.isAirBlock(world, x, y + 1, z);
		if (block == Blocks.pumpkin || block == Blocks.melon_block)
			return true;
		return false;
	}
	
	@Override
	public boolean isSeed(Item item, int meta)
	{
		return false;
	}
}
