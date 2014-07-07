package com.texasjake95.core.proxy.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MovingObjectPosition;

public class PlayerProxy {

	public static float getBreakSpeed(EntityPlayer player, Block block, boolean idk, int blockMeta, int x, int y, int z)
	{
		return player.getBreakSpeed(block, idk, blockMeta, x, y, z);
	}

	public static InventoryPlayer getPlayerInventory(EntityPlayer player)
	{
		return player.inventory;
	}

	public static boolean isCreative(EntityPlayer player)
	{
		return player.capabilities.isCreativeMode;
	}

	public static void setPostion(EntityPlayer player, int x, int y, int z)
	{
		if (player instanceof EntityPlayerMP)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			if (playerMP.playerNetServerHandler.netManager.isChannelOpen())
			{
				playerMP.setPositionAndUpdate(x, y, z);
			}
		}
	}

	public static void setPostion(EntityPlayer player, MovingObjectPosition movingObjPos)
	{
		setPostion(player, movingObjPos.blockX, movingObjPos.blockY + 1, movingObjPos.blockZ);
	}
}
