package com.texasjake95.core.proxy.entity.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class PlayerInventoryProxy {

	public static ItemStack getCurrentItem(EntityPlayer player)
	{
		return EntityPlayerProxy.getPlayerInventory(player).getCurrentItem();
	}

	public static int getCurrentItemSlot(EntityPlayer player)
	{
		return EntityPlayerProxy.getPlayerInventory(player).currentItem;
	}

	public static int getHotBarSize()
	{
		return InventoryPlayer.getHotbarSize();
	}

	public static void setCurrentItemSlot(EntityPlayer player, int slot)
	{
		EntityPlayerProxy.getPlayerInventory(player).currentItem = slot;
	}
}
