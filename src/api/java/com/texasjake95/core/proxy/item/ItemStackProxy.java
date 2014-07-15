package com.texasjake95.core.proxy.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemStackProxy {

	public static Item getItem(ItemStack stack)
	{
		return stack.getItem();
	}

	public static double getMaxDamage(ItemStack stack)
	{
		return stack.getMaxDamage();
	}

	public static int getMetadata(ItemStack stack)
	{
		return stack.getItemDamage();
	}

	public static int getStackSize(ItemStack stack)
	{
		return stack.stackSize;
	}

	public static boolean isDamageable(ItemStack stack)
	{
		return stack.isItemStackDamageable();
	}

	public static ItemStack splitStack(ItemStack stack, int decr)
	{
		return stack.splitStack(decr);
	}

	public static void tryPlaceItemIntoWorld(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face, float idk, float idk2, float idk3)
	{
		stack.tryPlaceItemIntoWorld(player, world, x, y, z, face, idk, idk2, idk3);
	}
}
