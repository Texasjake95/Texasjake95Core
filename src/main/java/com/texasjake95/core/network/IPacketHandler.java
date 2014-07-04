package com.texasjake95.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public interface IPacketHandler {

	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException;

	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException;
}
