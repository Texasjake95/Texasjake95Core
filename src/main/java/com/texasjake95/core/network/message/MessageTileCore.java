package com.texasjake95.core.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.tileentity.TileEntity;

import com.texasjake95.core.tile.TileEntityCore;

public class MessageTileCore<T extends MessageTileCore<T>> implements IMessage, IMessageHandler<T, IMessage> {
	
	public int x, y, z;
	public byte orientation;
	public String customName, owner;
	protected ByteBuf byteBuf;
	protected TileEntityCore tile;
	
	public MessageTileCore()
	{
	}
	
	public MessageTileCore(TileEntityCore tile)
	{
		this.x = tile.xCoord;
		this.y = tile.yCoord;
		this.z = tile.zCoord;
		this.orientation = (byte) tile.getFacing().ordinal();
		this.customName = tile.getCustomName();
		this.owner = tile.getOwnerName();
		this.tile = tile;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.orientation = buf.readByte();
		int customNameLength = buf.readInt();
		this.customName = new String(buf.readBytes(customNameLength).array());
		int ownerLength = buf.readInt();
		this.owner = new String(buf.readBytes(ownerLength).array());
		this.byteBuf = buf;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeByte(orientation);
		buf.writeInt(customName.length());
		buf.writeBytes(customName.getBytes());
		buf.writeInt(owner.length());
		buf.writeBytes(owner.getBytes());
		try
		{
			this.tile.writeToPacket(new ByteBufOutputStream(buf), buf, this.getClass());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public IMessage onMessage(T message, MessageContext ctx)
	{
		TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		if (tileEntity instanceof TileEntityCore)
		{
			((TileEntityCore) tileEntity).setFacing(ForgeDirection.getOrientation(message.orientation));
			((TileEntityCore) tileEntity).setCustomName(message.customName);
			((TileEntityCore) tileEntity).setOwnerName(message.owner);
			try
			{
				((TileEntityCore) tileEntity).readFromPacket(new ByteBufInputStream(message.byteBuf), message.byteBuf, message.getClass());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}
