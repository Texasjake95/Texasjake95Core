package com.texasjake95.core.proxy.client.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.network.Packet;

/**
 * This class is used as a proxy to call code for Minecraft's {@link EntityClientPlayerMPProxy}
 * due to the nature of the changing names of methods and fields.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
@SideOnly(Side.CLIENT)
public class EntityClientPlayerMPProxy extends EntityPlayerSPProxy {

	/**
	 * Send the packet provided to the specified player.
	 *
	 * @param player
	 *            the player receiving the packet
	 * @param packet
	 *            the packet to send
	 */
	@SideOnly(Side.CLIENT)
	public static void sendPacket(EntityClientPlayerMP player, Packet packet)
	{
		player.sendQueue.addToSendQueue(packet);
	}
}
