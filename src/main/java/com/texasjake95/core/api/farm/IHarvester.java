package com.texasjake95.core.api.farm;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHarvester {

	ArrayList<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta);
}
