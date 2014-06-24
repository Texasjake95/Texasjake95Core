package com.texasjake95.core.proxy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.proxy.entity.PlayerProxy;

public class PlayerInventoryProxy {
	
	public static ItemStack getCurrentItem(EntityPlayer player)
	{
		return PlayerProxy.getPlayerInventory(player).getCurrentItem();
	}
	
	public static int getCurrentItemSlot(EntityPlayer player)
	{
		return PlayerProxy.getPlayerInventory(player).currentItem;
	}
	
	public static int getHotBarSize()
	{
		return InventoryPlayer.getHotbarSize();
	}
	
	public static ItemStack getStackInSlot(EntityPlayer player, int slot)
	{
		return IInventoryProxy.getStackInSlot(PlayerProxy.getPlayerInventory(player), slot);
	}
	
	public static void setCurrentItemSlot(EntityPlayer player, int slot)
	{
		PlayerProxy.getPlayerInventory(player).currentItem = slot;
	}
}
