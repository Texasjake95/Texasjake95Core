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

	private FurnaceBase furnace;

	public TileEntityFurnaceBase(int slots, int fuelSlots, int ticksToCook)
	{
		furnace = new FurnaceBase(slots, fuelSlots, ticksToCook);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			this.furnace.updateEntity(this.worldObj);
		}
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		furnace.readFromPacket(data, byteBuf, clazz);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		furnace.writeToPacket(dos, byteBuf, clazz);
	}

	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
		furnace.readFromNBT(nbtTagCompound);
	}

	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
		furnace.writeToNBT(nbtTagCompound);
	}

	@Override
	public int getSizeInventory()
	{
		return furnace.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return furnace.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return furnace.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return furnace.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		furnace.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName()
	{
		return "tile.txFurnace.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return furnace.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
		furnace.openInventory();
	}

	@Override
	public void closeInventory()
	{
		furnace.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return furnace.isItemValidForSlot(slot, stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? furnace.getOutputSlots() : (side == 1 ? furnace.getInputSlots() : furnace.getFuelSlots());
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(slot, stack);
	}

	public void printInv()
	{
		for (int slot = 0; slot < this.getSizeInventory(); slot++)
		{
			System.out.println(this.getStackInSlot(slot));
		}
		System.out.println();
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		for (int input : furnace.getInputSlots())
			if (slot == input)
				return false;
		for (int fuel : furnace.getFuelSlots())
			if (slot == fuel)
				return stack.getItem() == Items.bucket;
		return side == 0;
	}
}
