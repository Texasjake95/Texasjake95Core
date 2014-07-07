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

	public InventoryFurnace(int slots, int fuelSlots)
	{
		this.inputSlots = new InventoryFurnaceInput(slots);
		this.outputSlots = new InventoryFurnaceOutput(slots);
		this.fuelSlots = new InventoryFurnaceFuel(fuelSlots);
	}

	@Override
	public void closeInventory()
	{
		this.inputSlots.closeInventory();
		this.outputSlots.closeInventory();
		this.fuelSlots.closeInventory();
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return this.getInventory(slot).decrStackSize(this.shiftSlot(slot), amount);
	}

	public InventoryFurnaceFuel getFuel()
	{
		return this.fuelSlots;
	}

	public InventoryFurnaceInput getInputs()
	{
		return this.inputSlots;
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

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public InventoryFurnaceOutput getOutputs()
	{
		return this.outputSlots;
	}

	@Override
	public int getSizeInventory()
	{
		return this.inputSlots.getSizeInventory() + this.outputSlots.getSizeInventory() + this.fuelSlots.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.getInventory(slot).getStackInSlot(this.shiftSlot(slot));
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.getStackInSlot(slot);
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return this.getInventory(slot).isItemValidForSlot(this.shiftSlot(slot), stack);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	public void markDirty()
	{
		this.inputSlots.markDirty();
		this.outputSlots.markDirty();
		this.fuelSlots.markDirty();
	}

	@Override
	public void openInventory()
	{
		this.inputSlots.openInventory();
		this.outputSlots.openInventory();
		this.fuelSlots.openInventory();
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

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.getInventory(slot).setInventorySlotContents(this.shiftSlot(slot), stack);
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
