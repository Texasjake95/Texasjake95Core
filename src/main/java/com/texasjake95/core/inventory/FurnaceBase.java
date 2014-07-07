package com.texasjake95.core.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

import com.texasjake95.core.inventory.furnace.InventoryFurnace;
import com.texasjake95.core.network.IPacketHandler;

public class FurnaceBase implements IInventory, IPacketHandler {

	public InventoryFurnace furnace;
	private int furnaceBurnTime;
	private int furnaceCookTime;
	private int ticksToCook;
	private int currentItemBurnTime;

	public FurnaceBase(int slots, int fuelSlots, int ticksToCook)
	{
		this.furnace = new InventoryFurnace(slots, fuelSlots);
		this.ticksToCook = ticksToCook;
	}

	private boolean canSmelt()
	{
		for (int slot = 0; slot < this.furnace.getInputs().getSizeInventory(); slot++)
			if (this.canSmelt(slot))
				return true;
		return false;
	}

	private boolean canSmelt(int slot)
	{
		if (this.furnace.getInputs().getStackInSlot(slot) == null)
			return false;
		else
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnace.getInputs().getStackInSlot(slot));
			if (itemstack == null)
				return false;
			if (this.furnace.getOutputs().getStackInSlot(slot) == null)
				return true;
			if (!this.furnace.getOutputs().getStackInSlot(slot).isItemEqual(itemstack))
				return false;
			int result = this.furnace.getOutputs().getStackInSlot(slot).stackSize + itemstack.stackSize;
			return result <= this.getInventoryStackLimit() && result <= this.furnace.getOutputs().getStackInSlot(slot).getMaxStackSize(); // Forge
		}
	}

	@Override
	public void closeInventory()
	{
		this.furnace.closeInventory();
	}

	private int[] createSlotArray(int size, int offset)
	{
		int[] slots = new int[size];
		for (int slot = 0; slot < size; slot++)
			slots[slot] = slot + offset;
		return slots;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return this.furnace.decrStackSize(slot, amount);
	}

	public int[] getFuelSlots()
	{
		int[] fuel = this.createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		return fuel;
	}

	public int[] getInputSlots()
	{
		int[] input = this.createSlotArray(this.furnace.getInputs().getSizeInventory(), 0);
		return input;
	}

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return this.furnace.getInventoryStackLimit();
	}

	public int[] getOutputSlots()
	{
		int[] output = this.createSlotArray(this.furnace.getOutputs().getSizeInventory(), this.furnace.getInputs().getSizeInventory());
		int[] fuel = this.createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		int[] realOutput = new int[output.length + fuel.length];
		for (int slot = 0; slot < output.length; slot++)
			realOutput[slot] = output[slot];
		for (int slot = 0; slot < fuel.length; slot++)
			realOutput[slot + output.length] = fuel[slot];
		return realOutput;
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
		return this.getStackInSlot(slot);
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
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
	public void markDirty()
	{
		this.furnace.markDirty();
	}

	@Override
	public void openInventory()
	{
		this.furnace.openInventory();
	}

	private void print(int[] g)
	{
		for (int i : g)
			System.out.println(i);
		System.out.println();
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.furnace.readFromNBT(compound.getCompoundTag("furnace"));
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.readFromPacket(data, byteBuf);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.furnace.setInventorySlotContents(slot, stack);
	}

	public void smeltItem(int slot)
	{
		if (this.canSmelt(slot))
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnace.getInputs().getStackInSlot(slot));
			if (this.furnace.getOutputs().getStackInSlot(slot) == null)
				this.furnace.getOutputs().setInventorySlotContents(slot, itemstack.copy());
			else if (this.furnace.getOutputs().getStackInSlot(slot).isItemEqual(itemstack))
				this.furnace.getOutputs().getStackInSlot(slot).stackSize += itemstack.stackSize;
			--this.furnace.getInputs().getStackInSlot(slot).stackSize;
			if (this.furnace.getInputs().getStackInSlot(slot).stackSize <= 0)
				this.furnace.getInputs().setInventorySlotContents(slot, null);
		}
	}

	public void updateEntity(World world)
	{
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		if (this.furnaceBurnTime > 0)
			--this.furnaceBurnTime;
		if (!world.isRemote)
		{
			if (this.furnaceBurnTime != 0 || !this.furnace.getFuel().isEmpty() && !this.furnace.getInputs().isEmpty())
			{
				if (this.furnaceBurnTime == 0 && this.canSmelt())
					for (int slot = 0; slot < this.furnace.getFuel().getSizeInventory(); slot++)
						if (this.furnaceBurnTime <= 0)
						{
							this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnace.getFuel().getStackInSlot(slot));
							if (this.furnaceBurnTime > 0)
							{
								flag1 = true;
								if (this.furnace.getFuel().getStackInSlot(slot) != null)
								{
									--this.furnace.getFuel().getStackInSlot(slot).stackSize;
									if (this.furnace.getFuel().getStackInSlot(slot).stackSize == 0)
										this.furnace.getFuel().setInventorySlotContents(slot, this.furnace.getFuel().getStackInSlot(slot).getItem().getContainerItem(this.furnace.getFuel().getStackInSlot(slot)));
								}
							}
						}
						else
							break;
				if (this.isBurning() && this.canSmelt())
				{
					++this.furnaceCookTime;
					if (this.furnaceCookTime >= this.ticksToCook)
						for (int slot = 0; slot < this.furnace.getInputs().getSizeInventory(); slot++)
						{
							this.furnaceCookTime = 0;
							this.smeltItem(slot);
							flag1 = true;
						}
				}
				else
					this.furnaceCookTime = 0;
			}
			if (flag != this.furnaceBurnTime > 0)
				flag1 = true;
		}
		if (flag1)
			this.markDirty();
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound data = new NBTTagCompound();
		this.furnace.writeToNBT(data);
		compound.setTag("furnace", data);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.writeToPacket(dos, byteBuf);
	}
}
