package com.texasjake95.core.tile;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;

import com.texasjake95.commons.helpers.Checker;
import com.texasjake95.core.SuctionMap;
import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.lib.utils.InventoryUtils;

public class TilePipe extends TileEntityInv implements IPipe {

	SuctionMap map = new SuctionMap();

	public TilePipe()
	{
		super(12, 64);
	}

	@Override
	public String getInventoryName()
	{
		return "Pipe";
	}

	public int getSuction(ItemStack stack)
	{
		return this.map.getSuction(stack);
	}

	public int getSuction(WrappedStack stack)
	{
		return this.map.getSuction(stack);
	}

	public SuctionMap getSuctionMap()
	{
		return this.map;
	}

	public void printSuction()
	{
		Iterator<Entry<WrappedStack, Integer>> iterator = this.map.getMap().entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<WrappedStack, Integer> entry = iterator.next();
			Texasjake95Core.txLogger.debug(String.format("%s : %s", entry.getKey(), entry.getValue()));
		}
		Texasjake95Core.txLogger.debug("Inventory");
		for (int i = 0; i < this.getSizeInventory(); i++)
			if (this.getStackInSlot(i) != null)
				Texasjake95Core.txLogger.debug(this.getStackInSlot(i));
	}

	@Override
	protected void pushToChest()
	{
		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if (stack != null)
			{
				ForgeDirection d = this.map.getBestDirection(stack, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
				if (tile instanceof TilePipe && d != ForgeDirection.UNKNOWN)
					InventoryUtils.addToInventory((TilePipe) tile, stack, d);
				if (stack.stackSize == 0)
					this.setInventorySlotContents(i, null);
			}
		}
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (tile instanceof TilePipe)
				continue;
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

	@Override
	public void updateEntity()
	{
		this.updateSuction();
		this.pushToChest();
	}

	private void updateSuction()
	{
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (tile instanceof TilePipe)
			{
				TilePipe pipe = (TilePipe) tile;
				Map<WrappedStack, Integer> pipeSuction = pipe.getSuctionMap().getMap();
				Iterator<Entry<WrappedStack, Integer>> iterator = pipeSuction.entrySet().iterator();
				while (iterator.hasNext())
				{
					Entry<WrappedStack, Integer> entry = iterator.next();
					this.map.updateMap(entry.getKey(), entry.getValue() - 1);
				}
			}
			if (tile instanceof TileEntityFurnace)
			{
				if (Checker.doAnyMatch(d, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST))
					this.map.addSuction(new ItemStack(Items.coal), Integer.MAX_VALUE);
				if (d == ForgeDirection.DOWN)
				{
					this.map.addSuction("cobblestone", Integer.MAX_VALUE);
					this.map.addSuction("oreIron", Integer.MAX_VALUE);
					this.map.addSuction("oreGold", Integer.MAX_VALUE);
				}
			}
		}
	}
}
