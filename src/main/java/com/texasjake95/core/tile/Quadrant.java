package com.texasjake95.core.tile;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.lib.helper.InventoryHelper;
import com.texasjake95.core.proxy.item.ItemStackProxy;
import com.texasjake95.core.proxy.world.WorldProxy;

public class Quadrant {
	
	private final ForgeDirection direction1;
	private final ForgeDirection direction2;
	private final IBlockChecker checker;
	private boolean valid;
	private int check = 0;
	private int row = 1, column = 1;
	private HashMap<Integer, HashMap<Integer, BlockIntPair>> seedMap = Maps.newHashMap();
	
	public Quadrant(ForgeDirection direction1, ForgeDirection direction2, IBlockChecker checker)
	{
		this.checker = checker;
		if (direction1 == direction2.getOpposite())
			throw new IllegalArgumentException(direction1 + " and " + direction2 + " can NOT go together");
		this.direction1 = direction1;
		this.direction2 = direction2;
	}
	
	private boolean checkSide(World world, int x, int y, int z, ForgeDirection d)
	{
		int offsetX = d.offsetX;
		int offsetY = d.offsetY;
		int offsetZ = d.offsetZ;
		for (int i = 0; i < 10; i++)
		{
			Block block = WorldProxy.getBlock(world, x + offsetX, y + offsetY, z + offsetZ);
			int meta = WorldProxy.getBlockMetadata(world, x + offsetX, y + offsetY, z + offsetZ);
			if (this.checker.isValidBlock(block, meta))
			{
				offsetX += d.offsetX;
				offsetY += d.offsetY;
				offsetZ += d.offsetZ;
				continue;
			}
			else
				return false;
		}
		return true;
	}
	
	public ForgeDirection getDirectionX()
	{
		if (this.direction1.offsetX != 0)
			return this.direction1;
		return this.direction2;
	}
	
	public ForgeDirection getDirectionZ()
	{
		if (this.direction1.offsetZ != 0)
			return this.direction1;
		return this.direction2;
	}
	
	private int getOffsetX(ForgeDirection d)
	{
		int x = d.offsetX * 10;
		return x;
	}
	
	private int getOffsetY(ForgeDirection d)
	{
		int y = d.offsetY * 10;
		return y;
	}
	
	private int getOffsetZ(ForgeDirection d)
	{
		int z = d.offsetZ * 10;
		return z;
	}
	
	public boolean isValid()
	{
		return this.valid;
	}
	
	public void load(NBTTagCompound compoundTag)
	{
		this.row = compoundTag.getInteger("row");
		if (this.row < 1 || 10 > this.row)
		{
			this.row = 1;
		}
		this.column = compoundTag.getInteger("column");
		if (this.column < 1 || 10 > this.column)
		{
			this.column = 1;
		}
		this.check = compoundTag.getInteger("check");
		this.valid = compoundTag.getBoolean("valid");
	}
	
	public void run(World world, int x, int y, int z, TileEntityFarm inv)
	{
		if (this.valid)
		{
			int offsetX = this.row * this.getDirectionX().offsetX;
			int offsetZ = this.column * this.getDirectionZ().offsetZ;
			int trueX = x + offsetX, trueZ = z + offsetZ;
			Block block = WorldProxy.getBlock(world, trueX, y, trueZ);
			int meta = WorldProxy.getBlockMetadata(world, trueX, y, trueZ);
			if (WorldProxy.isAirBlock(world, trueX, y, trueZ))
			{
				HashMap<Integer, BlockIntPair> columnMap = this.seedMap.get(this.row);
				if (columnMap != null)
				{
					BlockIntPair pair = columnMap.get(this.column);
					if (pair != null)
					{
						EntityPlayer player = Texasjake95Core.proxy.getTXPlayer((WorldServer) world, trueX, y, trueZ).get();
						ItemIntPair item = TileEntityFarm.getSeed(pair.getBlock(), pair.getMeta());
						ItemStack stack = inv.getSeedInv().getStack(item);
						if (stack != null)
						{
							ItemStackProxy.tryPlaceItemIntoWorld(stack, player, world, trueX, y - 1, trueZ, 1, 0, 0, 0);
							columnMap.remove(this.column);
							if (columnMap.isEmpty())
							{
								this.seedMap.remove(this.row);
							}
							else
							{
								this.seedMap.put(this.row, columnMap);
							}
						}
						else
						{
						}
					}
				}
			}
			else if (TileEntityFarm.isFullGrown(block, meta, world, trueX, y, trueZ))
			{
				EntityPlayer player = Texasjake95Core.proxy.getTXPlayer((WorldServer) world, trueX, y, trueZ).get();
				ArrayList<ItemStack> returnList = TileEntityFarm.getHarvests(player, world, trueX, y, trueZ, block, meta);
				for (ItemStack stack : returnList)
				{
					if (TileEntityFarm.isSeed(stack))
					{
						inv.getSeedInv().addItemStack(stack);
					}
					InventoryHelper.addToInventory(inv, stack);
				}
				ItemIntPair pair = TileEntityFarm.getSeed(block, meta);
				if (pair != null)
				{
					ItemStack stack = inv.getSeedInv().getStack(pair);
					if (stack != null)
					{
						ItemStackProxy.tryPlaceItemIntoWorld(stack, player, world, trueX, y - 1, trueZ, 1, 0, 0, 0);
					}
					else
					{
						HashMap<Integer, BlockIntPair> columnMap = this.seedMap.get(this.row);
						if (columnMap == null)
						{
							columnMap = Maps.newHashMap();
						}
						columnMap.put(this.column, new BlockIntPair(block, meta));
					}
				}
			}
			if (this.row >= 9 && this.column >= 9)
			{
				this.row = 1;
				this.column = 1;
				return;
			}
			if (this.row >= 9)
			{
				this.row = 1;
				this.column += 1;
			}
			else
			{
				this.row += 1;
				return;
			}
		}
	}
	
	public void save(NBTTagCompound compoundTag)
	{
		compoundTag.setInteger("row", this.row);
		compoundTag.setInteger("column", this.column);
		compoundTag.setInteger("check", this.check);
		compoundTag.setBoolean("valid", this.valid);
	}
	
	public void validate(World world, int x, int y, int z)
	{
		this.check += 1;
		if (this.check >= 20)
		{
			this.valid = false;
			this.check = 0;
			if (!this.checkSide(world, x, y, z, this.direction1))
				return;
			if (!this.checkSide(world, x, y, z, this.direction2))
				return;
			int offsetX = this.getOffsetX(this.direction1);
			int offsetY = this.getOffsetY(this.direction1);
			int offsetZ = this.getOffsetZ(this.direction1);
			if (!this.checkSide(world, x + offsetX, y + offsetY, z + offsetZ, this.direction2))
				return;
			offsetX = this.getOffsetX(this.direction2);
			offsetY = this.getOffsetY(this.direction2);
			offsetZ = this.getOffsetZ(this.direction2);
			if (!this.checkSide(world, x + offsetX, y + offsetY, z + offsetZ, this.direction1))
				return;
			this.valid = true;
		}
	}
}
