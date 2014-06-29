package com.texasjake95.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.network.message.MessageTileFarm;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CoreInfo.modId.toLowerCase());
	private static boolean init = false;
	
	public static SimpleNetworkWrapper getInstance()
	{
		if (!init)
			init();
		return INSTANCE;
	}
	
	private static void init()
	{
		init = true;
		INSTANCE.registerMessage(MessageTileFarm.class, MessageTileFarm.class, 0, Side.CLIENT);
	}
	
	public static void sendTo(IMessage message, EntityPlayerMP player)
	{
		getInstance().sendTo(message, player);
	}
	
	public static void sendToAll(IMessage message)
	{
		getInstance().sendToAll(message);
	}
	
	public static void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
	{
		sendToAllAround(message, new TargetPoint(dimension, x, y, z, range));
	}
	
	public static void sendToAllAround(IMessage message, TargetPoint point)
	{
		getInstance().sendToAllAround(message, point);
	}
	
	public static void sendToDimension(IMessage message, int dimensionId)
	{
		getInstance().sendToDimension(message, dimensionId);
	}
	
	public static void sendToServer(IMessage message)
	{
		getInstance().sendToServer(message);
	}
	
	public static Packet getPacketFrom(IMessage message)
	{
		return getInstance().getPacketFrom(message);
	}
}