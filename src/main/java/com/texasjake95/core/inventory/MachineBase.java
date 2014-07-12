package com.texasjake95.core.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.texasjake95.core.inventory.furnace.InventoryFurnace;
import com.texasjake95.core.network.IPacketHandler;
import com.texasjake95.core.recipe.IRecipeProvider;

public abstract class MachineBase implements IInventory, IPacketHandler {

	public InventoryFurnace furnace;
	public int furnaceBurnTime;
	public int furnaceCookTime;
	public int currentItemBurnTime;
	private final int ticksToCook;
	private IInventory tile;
	public final IRecipeProvider recipeProvider;

	public MachineBase(int slots, int fuelSlots, int ticksToCook, IInventory tile, IRecipeProvider recipes)
	{
		this.furnace = new InventoryFurnace(slots, fuelSlots, recipes);
		this.recipeProvider = recipes;
		this.ticksToCook = ticksToCook;
		this.tile = tile;
	}

	protected boolean canSmelt()
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
			ItemStack itemstack = this.recipeProvider.getResult(this.furnace.getInputs().getStackInSlot(slot));
			if (itemstack == null)
				return false;
			if (this.furnace.getOutputs().getStackInSlot(slot) == null)
				return true;
			if (!this.furnace.getOutputs().getStackInSlot(slot).isItemEqual(itemstack))
				return false;
			int result = this.furnace.getOutputs().getStackInSlot(slot).stackSize + itemstack.stackSize;
			return result <= this.getInventoryStackLimit() && result <= this.furnace.getOutputs().getStackInSlot(slot).getMaxStackSize();
		}
	}

	@Override
	public void closeInventory()
	{
		this.furnace.closeInventory();
	}

	private Integer[] createSlotArray(IInventory inv, int offset, ItemStack stack)
	{
		ArrayList<Integer> slots = Lists.newArrayList();
		for (int i = 0; i < inv.getSizeInventory(); i++)
			if (inv.getStackInSlot(i) == null)
				slots.add(i + offset);
		if (!slots.isEmpty())
			return slots.toArray(new Integer[slots.size()]);
		ArrayList<SlotData> slotData = Lists.newArrayList();
		for (int i = 0; i < inv.getSizeInventory(); i++)
			if (inv.getStackInSlot(i) != null && stack.isItemEqual(inv.getStackInSlot(i)))
				slotData.add(new SlotData(i, inv.getStackInSlot(i)));
		Collections.sort(slotData);
		for (SlotData data : slotData)
			slots.add(data.slot + offset);
		return slots.toArray(new Integer[slots.size()]);
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

	/**
	 * Returns an integer between 0 and the passed value representing how much
	 * burn time is left on the current fuel item, where 0 means that the item
	 * is exhausted and the passed value means that the item is fresh
	 */
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int scale)
	{
		if (this.currentItemBurnTime == 0)
			this.currentItemBurnTime = 200;
		return this.furnaceBurnTime * scale / this.currentItemBurnTime;
	}

	/**
	 * Returns an integer between 0 and the passed value representing how close
	 * the current item is to being completely cooked
	 */
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int scale)
	{
		return this.furnaceCookTime * scale / this.ticksToCook;
	}

	public int[] getFuelSlots()
	{
		int[] fuel = this.createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		return fuel;
	}

	public int getFuelSlots(ItemStack stack)
	{
		Integer[] fuel = this.createSlotArray(this.furnace.getFuel(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory(), stack);
		if (fuel.length >= 1)
			return fuel[0];
		return -1;
	}

	public int[] getInputSlots()
	{
		int[] input = this.createSlotArray(this.furnace.getInputs().getSizeInventory(), 0);
		int[] fuel = this.createSlotArray(this.furnace.getFuel().getSizeInventory(), this.furnace.getOutputs().getSizeInventory() + this.furnace.getInputs().getSizeInventory());
		int[] realInput = new int[input.length + fuel.length];
		for (int slot = 0; slot < input.length; slot++)
			realInput[slot] = input[slot];
		for (int slot = 0; slot < fuel.length; slot++)
			realInput[slot + input.length] = fuel[slot];
		return realInput;
	}

	public int getInputSlots(ItemStack stack)
	{
		Integer[] input = this.createSlotArray(this.furnace.getInputs(), 0, stack);
		if (input.length >= 1)
			return input[0];
		return -1;
	}

	@Override
	public String getInventoryName()
	{
		return this.tile.getInventoryName();
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

	protected abstract boolean handleFuel(int slot);

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
	}

	protected abstract boolean isItemFuel(ItemStack stack);

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

	public void load(NBTTagCompound compound)
	{
		this.furnace.load(compound);
		this.furnaceBurnTime = compound.getInteger("furnaceBurnTime");
		this.furnaceCookTime = compound.getInteger("furnaceCookTime");
		this.currentItemBurnTime = compound.getInteger("currentItemBurnTime");
	}

	@Override
	public void markDirty()
	{
		this.tile.markDirty();
		this.furnace.markDirty();
	}

	protected abstract boolean needsFuel();

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

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.readFromPacket(data, byteBuf);
	}

	public void save(NBTTagCompound compound)
	{
		this.furnace.save(compound);
		compound.setInteger("furnaceBurnTime", this.furnaceBurnTime);
		compound.setInteger("furnaceCookTime", this.furnaceCookTime);
		compound.setInteger("currentItemBurnTime", this.currentItemBurnTime);
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
			ItemStack itemstack = this.recipeProvider.getResult(this.furnace.getInputs().getStackInSlot(slot));
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
				if (this.needsFuel())
					for (int slot = 0; slot < this.furnace.getFuel().getSizeInventory(); slot++)
						if (this.handleFuel(slot))
							break;
				if (this.isBurning() && this.canSmelt())
				{
					++this.furnaceCookTime;
					if (this.furnaceCookTime == this.ticksToCook)
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

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.furnace.writeToPacket(dos, byteBuf);
	}
}
