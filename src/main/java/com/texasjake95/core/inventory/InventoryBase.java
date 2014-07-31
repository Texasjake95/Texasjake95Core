package com.texasjake95.core.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;

import net.minecraftforge.common.util.Constants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBase implements IInventory {

	private ItemStack[] inv;
	private int size;
	private int stackLimit;

	public InventoryBase(int size)
	{
		this(size, 64);
	}

	public InventoryBase(int size, int stackLimit)
	{
		this.inv = new ItemStack[size];
		this.size = size;
		this.stackLimit = stackLimit;
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public ItemStack decrStackSize(int slot, int decr)
	{
		if (this.inv[slot] != null)
		{
			ItemStack itemstack;
			if (this.inv[slot].stackSize <= decr)
			{
				itemstack = this.inv[slot];
				this.inv[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inv[slot].splitStack(decr);
				if (this.inv[slot].stackSize == 0)
					this.inv[slot] = null;
				return itemstack;
			}
		}
		else
			return null;
	}

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return this.stackLimit;
	}

	@Override
	public int getSizeInventory()
	{
		return this.size;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot < this.size)
			return this.inv[slot];
		return null;
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
	public boolean isItemValidForSlot(int var1, ItemStack var2)
	{
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public void openInventory()
	{
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagList nbttaglist = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte slot = nbttagcompound1.getByte("Slot");
			if (slot >= 0 && slot < this.inv.length)
				this.inv[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public void readFromPacket(ByteBufInputStream dis, ByteBuf byteBuf) throws IOException
	{
		this.size = dis.readInt();
		this.stackLimit = dis.readInt();
		this.inv = new ItemStack[this.size];
		for (int i = 0; i < this.size; i++)
			if (dis.readBoolean())
				this.inv[i] = ByteBufUtils.readItemStack(byteBuf);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if (slot < this.size)
			this.inv[slot] = stack;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.inv.length; ++i)
			if (this.inv[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inv[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		compound.setTag("Items", nbttaglist);
	}

	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException
	{
		dos.writeInt(this.size);
		dos.writeInt(this.stackLimit);
		for (int i = 0; i < this.size; i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if (stack == null)
				dos.writeBoolean(false);
			else
			{
				dos.writeBoolean(true);
				ByteBufUtils.writeItemStack(byteBuf, stack);
			}
		}
	}
}
