package com.texasjake95.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public interface IPacketHandler {
	
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException;
	
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf) throws IOException;
}
