package com.texasjake95.core.proxy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class IInventoryProxy {

	public static void closeInventory(IInventory inv)
	{
		inv.closeInventory();
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	public static ItemStack decrStackSize(IInventory inv, int slot, int decrease)
	{
		return inv.decrStackSize(slot, decrease);
	}

	public static String getInventoryName(IInventory inv)
	{
		return inv.getInventoryName();
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	public static int getInventoryStackLimit(IInventory inv)
	{
		return inv.getInventoryStackLimit();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public static int getSizeInventory(IInventory inv)
	{
		return inv.getSizeInventory();
	}

	/**
	 * Returns the stack in slot i
	 */
	public static ItemStack getStackInSlot(IInventory inv, int slot)
	{
		return inv.getStackInSlot(slot);
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	public static ItemStack getStackInSlotOnClosing(IInventory inv, int slot)
	{
		return inv.getStackInSlotOnClosing(slot);
	}

	/**
	 * Returns if the inventory is named
	 */
	public static boolean hasCustomInventoryName(IInventory inv)
	{
		return inv.hasCustomInventoryName();
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public static boolean isItemValidForSlot(IInventory inv, int slot, ItemStack stack)
	{
		return inv.isItemValidForSlot(slot, stack);
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	public static boolean isUseableByPlayer(IInventory inv, EntityPlayer player)
	{
		return inv.isUseableByPlayer(player);
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved
	 * to disk later - the game won't think it hasn't changed and skip it.
	 */
	public static void markDirty(IInventory inv)
	{
		inv.markDirty();
	}

	public static void openInventory(IInventory inv)
	{
		inv.openInventory();
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public static void setInventorySlotContents(IInventory inv, int slot, ItemStack stack)
	{
		inv.setInventorySlotContents(slot, stack);
	}
}
