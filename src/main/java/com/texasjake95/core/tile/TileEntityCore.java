package com.texasjake95.core.tile;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.texasjake95.core.network.IPacketHandler;

public abstract class TileEntityCore extends TileEntity implements IPacketHandler {
	
	protected ForgeDirection facing;
	protected String customName;
	protected String owner;
	
	public TileEntityCore()
	{
		this.facing = ForgeDirection.SOUTH;
		this.customName = "";
		this.owner = "";
	}
	
	public Block getBlock()
	{
		return this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
	}
	
	public int getBlockMeta()
	{
		return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
	}
	
	public String getCustomName()
	{
		return this.customName;
	}
	
	public ForgeDirection getFacing()
	{
		return this.facing;
	}
	
	private ForgeDirection getNextRotation()
	{
		switch (this.getFacing())
		{
			case UP:
				return ForgeDirection.DOWN;
			case DOWN:
				return ForgeDirection.NORTH;
			case NORTH:
				return ForgeDirection.SOUTH;
			case SOUTH:
				return ForgeDirection.EAST;
			case EAST:
				return ForgeDirection.WEST;
			case WEST:
				return ForgeDirection.UP;
			default:
				return ForgeDirection.UNKNOWN;
		}
	}
	
	public String getOwnerName()
	{
		return this.owner;
	}
	
	public boolean hasCustomName()
	{
		return this.customName != null && this.customName.length() > 0;
	}
	
	public boolean isSameBlock(Block block)
	{
		return this.isSameBlock(block, 0);
	}
	
	public boolean isSameBlock(Block block, int meta)
	{
		return this.getBlock() == block && this.getBlockMeta() == meta;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		if (nbtTagCompound.hasKey("direction"))
		{
			this.facing = ForgeDirection.getOrientation(nbtTagCompound.getByte("direction"));
		}
		if (nbtTagCompound.hasKey("customName"))
		{
			this.customName = nbtTagCompound.getString("customName");
		}
		if (nbtTagCompound.hasKey("owner"))
		{
			this.owner = nbtTagCompound.getString("owner");
		}
		load(nbtTagCompound);
	}
	
	
	public boolean rotateBlock(ForgeDirection axis)
	{
		this.setFacing(this.getNextRotation());
		return true;
	}
	
	public void setCustomName(String name)
	{
		this.customName = name;
	}
	
	public void setFacing(ForgeDirection direction)
	{
		if (direction != ForgeDirection.UNKNOWN)
		{
			this.facing = direction;
		}
	}
	
	public void setOwnerName(String name)
	{
		this.owner = name;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setByte("direction", (byte) this.facing.ordinal());
		nbtTagCompound.setString("owner", this.owner);
		if (this.hasCustomName())
		{
			nbtTagCompound.setString("customName", this.customName);
		}
		save(nbtTagCompound);
	}
	
	protected abstract void save(NBTTagCompound nbtTagCompound);
	
	protected abstract void load(NBTTagCompound nbtTagCompound);
	
}
