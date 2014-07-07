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
		return this.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		furnace.setInventorySlotContents(slot, stack);
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
		return furnace.getInventoryStackLimit();
	}

	@Override
	public void markDirty()
	{
		furnace.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
		this.furnace.openInventory();
	}

	@Override
	public void closeInventory()
	{
		this.furnace.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return this.furnace.isItemValidForSlot(slot, stack);
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.readFromPacket(data, byteBuf);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.writeToPacket(dos, byteBuf);
	}

	public void updateEntity(World world)
	{
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		if (this.furnaceBurnTime > 0)
		{
			--this.furnaceBurnTime;
		}
		if (!world.isRemote)
		{
			if (this.furnaceBurnTime != 0 || !this.furnace.getFuel().isEmpty() && !this.furnace.getInputs().isEmpty())
			{
				if (this.furnaceBurnTime == 0 && this.canSmelt())
				{
					for (int slot = 0; slot < this.furnace.getFuel().getSizeInventory(); slot++)
					{
						if (furnaceBurnTime <= 0)
						{
							this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnace.getFuel().getStackInSlot(slot));
							if (this.furnaceBurnTime > 0)
							{
								flag1 = true;
								if (this.furnace.getFuel().getStackInSlot(slot) != null)
								{
									--this.furnace.getFuel().getStackInSlot(slot).stackSize;
									if (this.furnace.getFuel().getStackInSlot(slot).stackSize == 0)
									{
										this.furnace.getFuel().setInventorySlotContents(slot, this.furnace.getFuel().getStackInSlot(slot).getItem().getContainerItem(this.furnace.getFuel().getStackInSlot(slot)));
									}
								}
							}
						}
						else
							break;
					}
				}
				if (this.isBurning() && this.canSmelt())
				{
					++this.furnaceCookTime;
					if (this.furnaceCookTime >= ticksToCook)
					{
						for (int slot = 0; slot < this.furnace.getInputs().getSizeInventory(); slot++)
						{
							this.furnaceCookTime = 0;
							this.smeltItem(slot);
							flag1 = true;
						}
					}
				}
				else
				{
					this.furnaceCookTime = 0;
				}
			}
			if (flag != this.furnaceBurnTime > 0)
			{
				flag1 = true;
			}
		}
		if (flag1)
		{
			this.markDirty();
		}
	}

	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
	}

	public void smeltItem(int slot)
	{
		if (this.canSmelt(slot))
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnace.getInputs().getStackInSlot(slot));
			if (this.furnace.getOutputs().getStackInSlot(slot) == null)
			{
				this.furnace.getOutputs().setInventorySlotContents(slot, itemstack.copy());
			}
			else if (this.furnace.getOutputs().getStackInSlot(slot).isItemEqual(itemstack))
			{
				this.furnace.getOutputs().getStackInSlot(slot).stackSize += itemstack.stackSize;
			}
			--this.furnace.getInputs().getStackInSlot(slot).stackSize;
			if (this.furnace.getInputs().getStackInSlot(slot).stackSize <= 0)
			{
				this.furnace.getInputs().setInventorySlotContents(slot, null);
			}
		}
	}

	private boolean canSmelt()
	{
		for (int slot = 0; slot < this.furnace.getInputs().getSizeInventory(); slot++)
			if (canSmelt(slot))
				return true;
		return false;
	}

	private boolean canSmelt(int slot)
	{
		if (this.furnace.getInputs().getStackInSlot(slot) == null)
		{
			return false;
		}
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
			return result <= getInventoryStackLimit() && result <= this.furnace.getOutputs().getStackInSlot(slot).getMaxStackSize(); // Forge
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.furnace.readFromNBT(compound.getCompoundTag("furnace"));
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound data = new NBTTagCompound();
		this.furnace.writeToNBT(data);
		compound.setTag("furnace", data);
	}

	public int[] getOutputSlots()
	{
		int[] output = createSlotArray(this.furnace.getOutputs().getSizeInventory(), this.furnace.getInputs().getSizeInventory());
		int[] fuel = createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		int[] realOutput = new int[output.length + fuel.length];
		for (int slot = 0; slot < output.length; slot++)
			realOutput[slot] = output[slot];
		for (int slot = 0; slot < fuel.length; slot++)
			realOutput[slot + output.length] = fuel[slot];
		return realOutput;
	}

	private int[] createSlotArray(int size, int offset)
	{
		int[] slots = new int[size];
		for (int slot = 0; slot < size; slot++)
			slots[slot] = slot + offset;
		return slots;
	}

	public int[] getInputSlots()
	{
		int[] input = createSlotArray(this.furnace.getInputs().getSizeInventory(), 0);
		return input;
	}

	private void print(int[] g)
	{
		for (int i : g)
		{
			System.out.println(i);
		}
		System.out.println();
	}

	public int[] getFuelSlots()
	{
		int[] fuel = createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		return fuel;
	}
}
