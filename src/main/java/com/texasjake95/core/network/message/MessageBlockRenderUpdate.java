package com.texasjake95.core.network.message;

import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class MessageBlockRenderUpdate implements IMessage, IMessageHandler<MessageBlockRenderUpdate, IMessage> {

	private int blockID, x, y, z, progress;;

	public MessageBlockRenderUpdate()
	{
	}

	public MessageBlockRenderUpdate(Block block, int x, int y, int z, int progress)
	{
		this.blockID = Block.getIdFromBlock(block);
		this.x = x;
		this.y = y;
		this.z = z;
		this.progress = progress;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.blockID = buf.readInt();
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.progress = buf.readInt();
	}

	@Override
	public IMessage onMessage(MessageBlockRenderUpdate message, MessageContext ctx)
	{
		Minecraft.getMinecraft().theWorld.destroyBlockInWorldPartially(message.blockID, message.x, message.y, message.z, message.progress);
		return null;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.blockID);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.progress);
	}
}
