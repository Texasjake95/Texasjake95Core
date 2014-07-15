package com.texasjake95.core.lib.utils;

import java.util.ArrayList;

import net.minecraftforge.event.ForgeEventFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockUtils {

	public static ArrayList<ItemStack> getDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
	{
		ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();
		ArrayList<ItemStack> dropsList = block.getDrops(world, x, y, z, meta, 0);
		float dropChance = ForgeEventFactory.fireBlockHarvesting(dropsList, world, block, x, y, z, meta, 0, 1.0F, false, player);
		for (ItemStack s : dropsList)
			if (world.rand.nextFloat() <= dropChance)
				returnList.add(s);
		world.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
		world.setBlockToAir(x, y, z);
		return returnList;
	}

	private BlockUtils()
	{
	}
}
