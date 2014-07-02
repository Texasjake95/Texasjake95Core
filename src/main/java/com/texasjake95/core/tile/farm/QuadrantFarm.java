package com.texasjake95.core.tile.farm;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.lib.helper.InventoryHelper;
import com.texasjake95.core.lib.pair.BlockIntPair;
import com.texasjake95.core.lib.pair.ItemIntPair;
import com.texasjake95.core.proxy.item.ItemStackProxy;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.Quadrant;
import com.texasjake95.core.tile.TileEntityFarm;

public class QuadrantFarm extends Quadrant<TileEntityFarm> {
	
	public QuadrantFarm(ForgeDirection eastWest, ForgeDirection upDown, ForgeDirection northSouth)
	{
		super(eastWest, upDown, northSouth);
	}
	
	private HashMap<Byte, HashMap<Byte, BlockIntPair>> seedMap = Maps.newHashMap();
	
	@Override
	protected void handleBlock(World world, int x, int y, int z, TileEntityFarm tile)
	{
		Block block = WorldProxy.getBlock(world, x, y, z);
		int meta = WorldProxy.getBlockMetadata(world, x, y, z);
		if (WorldProxy.isAirBlock(world, x, y, z))
		{
			HashMap<Byte, BlockIntPair> columnMap = this.seedMap.get(this.row);
			if (columnMap != null)
			{
				BlockIntPair pair = columnMap.get(this.column);
				if (pair != null)
				{
					EntityPlayer player = Texasjake95Core.proxy.getTXPlayer((WorldServer) world, x, y, z).get();
					ItemIntPair item = TileEntityFarm.getSeed(pair.getBlock(), pair.getMeta());
					ItemStack stack = tile.getSeedInv().getStack(item);
					if (stack != null)
					{
						ItemStackProxy.tryPlaceItemIntoWorld(stack, player, world, x, y - 1, z, 1, 0, 0, 0);
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
		else if (TileEntityFarm.isFullGrown(block, meta, world, x, y, z))
		{
			EntityPlayer player = Texasjake95Core.proxy.getTXPlayer((WorldServer) world, x, y, z).get();
			ArrayList<ItemStack> returnList = TileEntityFarm.getHarvests(player, world, x, y, z, block, meta);
			for (ItemStack stack : returnList)
			{
				if (TileEntityFarm.isSeed(stack))
				{
					tile.getSeedInv().addItemStack(stack);
				}
				InventoryHelper.addToInventory(tile, stack);
			}
			ItemIntPair pair = TileEntityFarm.getSeed(block, meta);
			if (pair != null)
			{
				ItemStack stack = tile.getSeedInv().getStack(pair);
				if (stack != null)
				{
					ItemStackProxy.tryPlaceItemIntoWorld(stack, player, world, x, y - 1, z, 1, 0, 0, 0);
				}
				else
				{
					HashMap<Byte, BlockIntPair> columnMap = this.seedMap.get(this.row);
					if (columnMap == null)
					{
						columnMap = Maps.newHashMap();
					}
					columnMap.put(this.column, new BlockIntPair(block, meta));
				}
			}
		}
	}
	
	@Override
	protected void incrementLoc()
	{
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
	
	@Override
	protected boolean _validate(World world, int x, int y, int z)
	{
		if (!this.checkSide(world, x, y, z, this.northSouth))
			return false;
		if (!this.checkSide(world, x, y, z, this.eastWest))
			return false;
		int offsetX = this.getOffsetX(this.northSouth);
		int offsetY = this.getOffsetY(this.northSouth);
		int offsetZ = this.getOffsetZ(this.northSouth);
		if (!this.checkSide(world, x + offsetX, y + offsetY, z + offsetZ, this.eastWest))
			return false;
		offsetX = this.getOffsetX(this.eastWest);
		offsetY = this.getOffsetY(this.eastWest);
		offsetZ = this.getOffsetZ(this.eastWest);
		if (!this.checkSide(world, x + offsetX, y + offsetY, z + offsetZ, this.northSouth))
			return false;
		return true;
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
	
	private boolean checkSide(World world, int x, int y, int z, ForgeDirection d)
	{
		int offsetX = d.offsetX;
		int offsetY = d.offsetY;
		int offsetZ = d.offsetZ;
		for (int i = 0; i < 10; i++)
		{
			Block block = WorldProxy.getBlock(world, x + offsetX, y + offsetY, z + offsetZ);
			int meta = WorldProxy.getBlockMetadata(world, x + offsetX, y + offsetY, z + offsetZ);
			if (isValidBlock(block, meta))
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
	
	private boolean isValidBlock(Block block, int meta)
	{
		if (block == Blocks.fence || block == Blocks.fence_gate || Blocks.nether_brick_fence == block)
			return true;
		return false;
	}

	@Override
	protected void loadExtra(NBTTagCompound compoundTag)
	{
		this.row = compoundTag.getByte("row");
		if (this.row < 1 || 10 > this.row)
		{
			this.row = 1;
		}
		this.column = compoundTag.getByte("column");
		if (this.column < 1 || 10 > this.column)
		{
			this.column = 1;
		}
		this.height = compoundTag.getByte("height");		
	}

	@Override
	protected void saveExtra(NBTTagCompound compoundTag)
	{
		// TODO Auto-generated method stub
		
	}
}
