package com.texasjake95.core.inventory.furnace;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryFurnace implements IInventory {

	private InventoryFurnaceInput inputSlots;
	private InventoryFurnaceOutput outputSlots;
	private InventoryFurnaceFuel fuelSlots;

	public InventoryFurnaceInput getInputs()
	{
		return this.inputSlots;
	}

	public InventoryFurnaceOutput getOutputs()
	{
		return this.outputSlots;
	}

	public InventoryFurnaceFuel getFuel()
	{
		return this.fuelSlots;
	}

	public InventoryFurnace(int slots, int fuelSlots)
	{
		inputSlots = new InventoryFurnaceInput(slots);
		outputSlots = new InventoryFurnaceOutput(slots);
		this.fuelSlots = new InventoryFurnaceFuel(fuelSlots);
	}

	public IInventory getInventory(int slot)
	{
		IInventory inv = this.inputSlots;
		if (slot >= inv.getSizeInventory())
		{
			slot -= inv.getSizeInventory();
			inv = this.outputSlots;
		}
		if (slot >= inv.getSizeInventory())
		{
			slot -= inv.getSizeInventory();
			inv = this.fuelSlots;
		}
		return inv;
	}

	public int shiftSlot(int slot)
	{
		IInventory inv = this.inputSlots;
		if (slot >= inv.getSizeInventory())
		{
			slot -= inv.getSizeInventory();
			inv = this.outputSlots;
		}
		if (slot >= inv.getSizeInventory())
		{
			slot -= inv.getSizeInventory();
			inv = this.fuelSlots;
		}
		return slot;
	}

	@Override
	public int getSizeInventory()
	{
		return inputSlots.getSizeInventory() + outputSlots.getSizeInventory() + fuelSlots.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return getInventory(slot).getStackInSlot(shiftSlot(slot));
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return getInventory(slot).decrStackSize(shiftSlot(slot), amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.getInventory(slot).setInventorySlotContents(shiftSlot(slot), stack);
	}

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		inputSlots.markDirty();
		outputSlots.markDirty();
		fuelSlots.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
		inputSlots.openInventory();
		outputSlots.openInventory();
		fuelSlots.openInventory();
	}

	@Override
	public void closeInventory()
	{
		inputSlots.closeInventory();
		outputSlots.closeInventory();
		fuelSlots.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return this.getInventory(slot).isItemValidForSlot(shiftSlot(slot), stack);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.inputSlots.readFromNBT(compound.getCompoundTag("input"));
		this.outputSlots.readFromNBT(compound.getCompoundTag("output"));
		this.fuelSlots.readFromNBT(compound.getCompoundTag("fuel"));
	}

	public void readFromPacket(ByteBufInputStream dis, ByteBuf byteBuf) throws IOException
	{
		this.inputSlots.readFromPacket(dis, byteBuf);
		this.outputSlots.readFromPacket(dis, byteBuf);
		this.fuelSlots.readFromPacket(dis, byteBuf);
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound data = new NBTTagCompound();
		this.inputSlots.writeToNBT(data);
		compound.setTag("input", data);
		data = new NBTTagCompound();
		this.outputSlots.writeToNBT(data);
		compound.setTag("output", data);
		data = new NBTTagCompound();
		this.fuelSlots.writeToNBT(data);
		compound.setTag("fuel", data);
	}

	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException
	{
		this.inputSlots.writeToPacket(dos, byteBuf);
		this.outputSlots.writeToPacket(dos, byteBuf);
		this.fuelSlots.writeToPacket(dos, byteBuf);
	}
}
