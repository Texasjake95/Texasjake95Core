package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBorder extends TileEntityCore {
	
	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		// TODO Auto-generated method stub
	}
}
