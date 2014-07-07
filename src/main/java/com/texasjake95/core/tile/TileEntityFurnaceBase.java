package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.texasjake95.core.inventory.FurnaceBase;

public class TileEntityFurnaceBase extends TileEntityCore implements ISidedInventory {

	public FurnaceBase furnace;

	public TileEntityFurnaceBase(int slots, int fuelSlots, int ticksToCook)
	{
		this.furnace = new FurnaceBase(slots, fuelSlots, ticksToCook, this);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		for (int input : this.furnace.getInputSlots())
			if (slot == input)
				return false;
		for (int fuel : this.furnace.getFuelSlots())
			if (slot == fuel)
				return stack.getItem() == Items.bucket;
		return side == 0;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(slot, stack);
	}

	@Override
	public void closeInventory()
	{
		this.furnace.closeInventory();
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return this.furnace.decrStackSize(slot, amount);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? this.furnace.getOutputSlots() : (side == 1 ? this.furnace.getInputSlots() : this.furnace.getFuelSlots());
	}

	@Override
	public String getInventoryName()
	{
		return "tile.txFurnace.name";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return this.furnace.getInventoryStackLimit();
	}

	@Override
	public int getSizeInventory()
	{
		return this.furnace.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.furnace.getStackInSlot(slot);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.furnace.getStackInSlotOnClosing(slot);
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return this.furnace.isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
		this.furnace.load(nbtTagCompound);
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
	}

	@Override
	public void openInventory()
	{
		this.furnace.openInventory();
	}

	public void printInv()
	{
		for (int slot = 0; slot < this.getSizeInventory(); slot++)
			System.out.println(this.getStackInSlot(slot));
		System.out.println();
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.readFromPacket(data, byteBuf, clazz);
	}

	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
		this.furnace.save(nbtTagCompound);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.furnace.setInventorySlotContents(slot, stack);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
			this.furnace.updateEntity(this.worldObj);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.writeToPacket(dos, byteBuf, clazz);
	}
}
