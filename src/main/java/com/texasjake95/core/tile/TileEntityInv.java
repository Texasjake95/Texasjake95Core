package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import com.texasjake95.core.inventory.InventoryBase;
import com.texasjake95.core.lib.utils.InventoryUtils;

public abstract class TileEntityInv extends TileEntityCore implements IInventory {

	private InventoryBase inv;
	private ForgeDirection[] validChest = new ForgeDirection[] { ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH };

	public TileEntityInv(int size)
	{
		this.inv = new InventoryBase(size);
	}

	public TileEntityInv(int size, int limit)
	{
		this.inv = new InventoryBase(size, limit);
	}

	public TileEntityInv(InventoryBase inv)
	{
		this.inv = inv;
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public ItemStack decrStackSize(int slot, int decr)
	{
		return this.inv.decrStackSize(slot, decr);
	}

	protected boolean empty()
	{
		for (int invSlot = 0; invSlot < this.getSizeInventory(); invSlot++)
		{
			ItemStack stack = this.getStackInSlot(invSlot);
			if (stack != null)
				return false;
		}
		return true;
	}

	protected IInventory getChestInv(World world, int x, int y, int z, Block block)
	{
		IInventory inv = (IInventory) world.getTileEntity(x, y, z);
		for (ForgeDirection d : this.validChest)
			if (world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == block)
				inv = new InventoryLargeChest("container.chestDouble", (IInventory) world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), inv);
		return inv;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return this.inv.getInventoryStackLimit();
	}

	@Override
	public int getSizeInventory()
	{
		return this.inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inv.getStackInSlot(slot);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.inv.getStackInSlotOnClosing(slot);
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}

	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
		this.inv.readFromNBT(nbtTagCompound.getCompoundTag("inv"));
	}

	@Override
	public void openInventory()
	{
	}

	protected void pushToChest()
	{
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (tile instanceof TileEntityChest)
			{
				Block chest = this.worldObj.getBlock(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
				IInventory temp = this.getChestInv(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ, chest);
				this.pushToInv(temp, d);
			}
			else if (tile instanceof IInventory)
			{
				IInventory temp = (IInventory) tile;
				this.pushToInv(temp, d);
			}
		}
	}

	protected void pushToInv(IInventory inv, ForgeDirection side)
	{
		for (int invSlot = 0; invSlot < this.getSizeInventory(); invSlot++)
		{
			ItemStack stack = this.getStackInSlot(invSlot);
			if (stack == null)
				continue;
			if (stack.stackSize == 0)
			{
				this.setInventorySlotContents(invSlot, null);
				continue;
			}
			InventoryUtils.addToInventory(inv, stack, side);
			if (stack.stackSize == 0)
			{
				this.setInventorySlotContents(invSlot, null);
				continue;
			}
		}
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.inv.readFromPacket(data, byteBuf);
	}

	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
		NBTTagCompound compound = new NBTTagCompound();
		this.inv.writeToNBT(compound);
		nbtTagCompound.setTag("inv", compound);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inv.setInventorySlotContents(slot, stack);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.inv.writeToPacket(dos, byteBuf);
	}
}
