package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCore extends TileEntity {
	
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
	}
	
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf) throws IOException
	{
		if (data != null)
		{
			this.facing = ForgeDirection.getOrientation(data.readByte());
			this.owner = data.readUTF();
		}
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
	}
	
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException
	{
		if (dos != null)
		{
			dos.writeInt(this.xCoord);
			dos.writeInt(this.yCoord);
			dos.writeInt(this.zCoord);
			dos.writeByte((byte) this.facing.ordinal());
			dos.writeUTF(this.owner);
		}
	}
}
