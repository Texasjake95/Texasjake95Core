package com.texasjake95.core.proxy.client.entity;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec3;

public class ClientPlayerProxy {

	public static Vec3 getLook(EntityPlayer player, float idk)
	{
		return player.getLook(idk);
	}

	public static Vec3 getPostion(EntityPlayer player, float idk)
	{
		return player.getPosition(idk);
	}

	public static void sendPacket(EntityClientPlayerMP player, Packet packet)
	{
		player.sendQueue.addToSendQueue(packet);
	}
}
