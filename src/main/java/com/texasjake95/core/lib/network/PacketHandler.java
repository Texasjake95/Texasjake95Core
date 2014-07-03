package com.texasjake95.core.lib.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;

public abstract class PacketHandler {
	
	private final SimpleNetworkWrapper channel;
	
	protected PacketHandler(String channelName)
	{
		this.channel = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		this.registerMessages();
	}
	
	public Packet getPacketFrom(IMessage message)
	{
		return this.channel.getPacketFrom(message);
	}
	
	protected <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, int discriminator, Side side)
	{
		this.channel.registerMessage(messageHandler, requestMessageType, discriminator, side);
	}
	
	protected abstract void registerMessages();
	
	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		this.channel.sendTo(message, player);
	}
	
	public void sendToAll(IMessage message)
	{
		this.channel.sendToAll(message);
	}
	
	public void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
	{
		this.sendToAllAround(message, new TargetPoint(dimension, x, y, z, range));
	}
	
	public void sendToAllAround(IMessage message, TargetPoint point)
	{
		this.channel.sendToAllAround(message, point);
	}
	
	public void sendToDimension(IMessage message, int dimensionId)
	{
		this.channel.sendToDimension(message, dimensionId);
	}
	
	public void sendToServer(IMessage message)
	{
		this.channel.sendToServer(message);
	}
}