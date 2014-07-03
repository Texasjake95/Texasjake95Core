package com.texasjake95.core.tile.farm;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHarvester {
	
	public static enum MachineType
	{
		FARM,
		QUARRY;
	}
	
	ArrayList<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta, MachineType type);
}
