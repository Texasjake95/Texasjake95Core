package com.texasjake95.core.inventory.furnace;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.texasjake95.core.recipe.IRecipeProvider;

public class InventoryFurnace implements IInventory {

	private InventoryFurnaceInput inputSlots;
	private InventoryFurnaceOutput outputSlots;
	private InventoryFurnaceFuel fuelSlots;
	static final String input = "input";
	static final String output = "output";
	static final String fuel = "fuel";

	public InventoryFurnace(int slots, int fuelSlots, IRecipeProvider recipes)
	{
		this.inputSlots = new InventoryFurnaceInput(slots, recipes);
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

	public void load(NBTTagCompound compound)
	{
		NBTTagCompound data = compound.getCompoundTag(input);
		this.inputSlots.readFromNBT(data);
		data = compound.getCompoundTag(output);
		this.outputSlots.readFromNBT(data);
		data = compound.getCompoundTag(fuel);
		this.fuelSlots.readFromNBT(data);
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

	public void readFromPacket(ByteBufInputStream dis, ByteBuf byteBuf) throws IOException
	{
		this.inputSlots.readFromPacket(dis, byteBuf);
		this.outputSlots.readFromPacket(dis, byteBuf);
		this.fuelSlots.readFromPacket(dis, byteBuf);
	}

	public void save(NBTTagCompound compound)
	{
		NBTTagCompound data = new NBTTagCompound();
		this.inputSlots.writeToNBT(data);
		compound.setTag(input, data);
		data = new NBTTagCompound();
		this.outputSlots.writeToNBT(data);
		compound.setTag(output, data);
		data = new NBTTagCompound();
		this.fuelSlots.writeToNBT(data);
		compound.setTag(fuel, data);
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

	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException
	{
		this.inputSlots.writeToPacket(dos, byteBuf);
		this.outputSlots.writeToPacket(dos, byteBuf);
		this.fuelSlots.writeToPacket(dos, byteBuf);
	}
}
