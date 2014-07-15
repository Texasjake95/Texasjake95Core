package com.texasjake95.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

public interface IPacketHandler {

	/**
	 * Used to read data from a packet's data via {@link ByteBufInputStream} and
	 * {@link ByteBuf}.
	 *
	 * @param data
	 *            the {@link ByteBufInputStream} containing the data to read
	 * @param byteBuf
	 *            the {@link ByteBuf} the {@link ByteBufInputStream} is reading from
	 * @param clazz
	 *            the class this is being read from
	 * @throws IOException
	 */
	void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException;

	/**
	 * Used to write data to a packet via {@link ByteBufOutputStream} and {@link ByteBuf}.
	 *
	 * @param data
	 *            the {@link ByteBufOutputStream} the data is being wrote to
	 * @param byteBuf
	 *            the {@link ByteBuf} the {@link ByteBufInputStream} is writing to
	 * @param clazz
	 *            the class this is being wrote to
	 * @throws IOException
	 */
	void writeToPacket(ByteBufOutputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException;
}
