package com.texasjake95.core.inventory.furnace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.proxy.inventory.PlayerInventoryProxy;

public class ContainerFurnaceBase extends Container {

	private FurnaceBase furnace;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerFurnaceBase(InventoryPlayer invPlayer, FurnaceBase furnaceBase)
	{
		this.furnace = furnaceBase;
		int offset = 0;
		for (int slot = 0; slot < this.furnace.furnace.getInputs().getSizeInventory(); slot++)
			this.addSlotToContainer(new Slot(this.furnace, slot + offset, 56 + slot * 18, 17));
		offset += this.furnace.furnace.getInputs().getSizeInventory();
		for (int slot = 0; slot < this.furnace.furnace.getOutputs().getSizeInventory(); slot++)
			this.addSlotToContainer(new SlotFurnace(invPlayer.player, this.furnace, slot + offset, 116 + slot * 18, 35));
		offset += this.furnace.furnace.getOutputs().getSizeInventory();
		for (int slot = 0; slot < this.furnace.furnace.getFuel().getSizeInventory(); slot++)
			this.addSlotToContainer(new Slot(this.furnace, slot + offset, 56 + slot * 18, 53));
		int i;
		for (i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		for (i = 0; i < 9; ++i)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
		crafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
		crafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.furnace.isUseableByPlayer(player);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			if (this.lastCookTime != this.furnace.furnaceCookTime)
				icrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
			if (this.lastBurnTime != this.furnace.furnaceBurnTime)
				icrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
			if (this.lastItemBurnTime != this.furnace.currentItemBurnTime)
				icrafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
		}
		this.lastCookTime = this.furnace.furnaceCookTime;
		this.lastBurnTime = this.furnace.furnaceBurnTime;
		this.lastItemBurnTime = this.furnace.currentItemBurnTime;
	}

	private boolean isInRange(int number, int start, int end)
	{
		return number >= start && number < end;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);
		int inputStart = 0;
		int inputEnd = this.furnace.furnace.getInputs().getSizeInventory();
		int outputStart = inputEnd;
		int outputEnd = inputEnd + this.furnace.furnace.getOutputs().getSizeInventory();
		int fuelStart = outputEnd;
		int fuelEnd = fuelStart + this.furnace.furnace.getFuel().getSizeInventory();
		int playerInvStart = fuelEnd;
		int playerInvEnd = playerInvStart + player.inventory.mainInventory.length;
		int playerHotbarStart = playerInvEnd - PlayerInventoryProxy.getHotBarSize();
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (this.isInRange(slotNumber, outputStart, outputEnd))
			{
				if (!this.mergeItemStack(itemstack1, playerInvStart, playerInvEnd, true))
					return null;
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (!this.isInRange(slotNumber, inputStart, inputEnd) && !this.isInRange(slotNumber, fuelStart, fuelEnd))
			{
				if (this.furnace.recipeProvider.getResult(itemstack1) != null)
				{
					if (!this.mergeItemStack(itemstack1, inputStart, inputEnd, false))
						return null;
				}
				else if (this.furnace.isItemFuel(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, fuelStart, fuelEnd, false))
						return null;
				}
				else if (slotNumber >= playerInvStart && slotNumber < playerHotbarStart)
				{
					if (!this.mergeItemStack(itemstack1, playerHotbarStart, playerInvEnd, false))
						return null;
				}
				else if (slotNumber >= playerHotbarStart && slotNumber < playerInvEnd && !this.mergeItemStack(itemstack1, playerInvStart, playerHotbarStart, false))
					return null;
			}
			else if (!this.mergeItemStack(itemstack1, playerInvStart, playerInvEnd, false))
				return null;
			if (itemstack1.stackSize == 0)
				slot.putStack((ItemStack) null);
			else
				slot.onSlotChanged();
			if (itemstack1.stackSize == itemstack.stackSize)
				return null;
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int type, int time)
	{
		if (type == 0)
			this.furnace.furnaceCookTime = time;
		if (type == 1)
			this.furnace.furnaceBurnTime = time;
		if (type == 2)
			this.furnace.currentItemBurnTime = time;
	}
}
