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
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.inventory.furnace.FurnaceBase;
import com.texasjake95.core.lib.utils.InventoryUtils;
import com.texasjake95.core.recipe.IRecipeProvider;
import com.texasjake95.core.recipe.RecipeProviders;

public class TileEntityFurnaceBase extends TileEntityCore implements ISidedInventory {

	public FurnaceBase furnace;
	private ForgeDirection[] validChest = new ForgeDirection[] { ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH };

	public TileEntityFurnaceBase(int slots, int fuelSlots, int ticksToCook)
	{
		this(slots, fuelSlots, ticksToCook, RecipeProviders.macerator);
	}

	public TileEntityFurnaceBase(int slots, int fuelSlots, int ticksToCook, IRecipeProvider recipes)
	{
		this.furnace = new FurnaceBase(slots, fuelSlots, ticksToCook, this, recipes);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		for (int input : this.furnace.getInputSlots())
			if (slot == input)
				return false;
		for (int fuel : this.furnace.getFuelSlots())
			if (slot == fuel)
				return !this.furnace.isItemFuel(stack);
		return side == 0;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		if (this.hasRedStone)
			return this.isItemValidForSlot(slot, stack);
		int validSlot = side == 0 ? -1 : this.furnace.isItemFuel(stack) ? this.furnace.getFuelSlots(stack) : this.furnace.getInputSlots(stack);
		if (slot == validSlot)
			return this.isItemValidForSlot(slot, stack);
		return false;
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
		return side == 0 ? this.furnace.getOutputSlots() : this.furnace.getInputSlots();
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
	public boolean isUseableByPlayer(EntityPlayer player)
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
			Texasjake95Core.txLogger.debug(this.getStackInSlot(slot));
		Texasjake95Core.txLogger.debug("");
	}

	private void pushToInv(IInventory inv, ForgeDirection side)
	{
		for (int invSlot : this.getAccessibleSlotsFromSide(side.getOpposite().getOpposite().ordinal()))
		{
			ItemStack stack = this.getStackInSlot(invSlot);
			if (this.canExtractItem(invSlot, stack, side.getOpposite().getOpposite().ordinal()))
			{
				if (stack == null)
					continue;
				if (stack.stackSize == 0)
				{
					this.setInventorySlotContents(invSlot, null);
					continue;
				}
				InventoryUtils.addToInventory(inv, stack, side);
			}
		}
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
		{
			this.updateRedstone();
			this.furnace.updateEntity(this.worldObj);
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
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.writeToPacket(dos, byteBuf, clazz);
	}
}
