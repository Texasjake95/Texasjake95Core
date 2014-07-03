package com.texasjake95.core.chunkloading;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import net.minecraft.tileentity.TileEntity;

public class TicketHelper {
	
	public static void registerChunkLoading(Object modInstance, String modID)
	{
		ForgeChunkManager.setForcedChunkLoadingCallback(modInstance, new ChunkloadCallback(modID));
	}
	
	public static void populateTicket(Ticket ticket, TileEntity tile)
	{
		ticket.getModData().setInteger("loaderX", tile.xCoord);
		ticket.getModData().setInteger("loaderY", tile.yCoord);
		ticket.getModData().setInteger("loaderZ", tile.zCoord);
	}
}
