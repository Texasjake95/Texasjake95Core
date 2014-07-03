package com.texasjake95.core.chunkloading;

import com.google.common.collect.ImmutableSetMultimap;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;

public class TicketHelper {
	
	public static void forceChunk(Ticket ticket, ChunkCoordIntPair chunk)
	{
		ImmutableSetMultimap<ChunkCoordIntPair, Ticket> chunks = ForgeChunkManager.getPersistentChunksFor(ticket.world);
		if (chunks.containsKey(chunk))
			return;
		ForgeChunkManager.forceChunk(ticket, chunk);
	}
	
	public static void populateTicket(Ticket ticket, TileEntity tile)
	{
		ticket.getModData().setInteger("loaderX", tile.xCoord);
		ticket.getModData().setInteger("loaderY", tile.yCoord);
		ticket.getModData().setInteger("loaderZ", tile.zCoord);
	}
	
	public static void registerChunkLoading(Object modInstance, String modID)
	{
		ForgeChunkManager.setForcedChunkLoadingCallback(modInstance, new ChunkloadCallback(modID));
	}
	
	public static void releaseTicket(IChunkLoader loader)
	{
		ForgeChunkManager.releaseTicket(loader.getTicket());
		loader.setTicket(null);
	}
	
	public static void updateChunks(TileEntity tile)
	{
		if (tile instanceof IChunkLoader)
		{
			IChunkLoader loader = (IChunkLoader) tile;
			Ticket ticket = loader.getTicket();
			if (ticket == null)
			{
				ticket = ForgeChunkManager.requestTicket(loader.getModObject(), tile.getWorldObj(), Type.NORMAL);
				loader.setTicket(ticket);
			}
			if (ticket != null)
			{
				TicketHelper.populateTicket(ticket, tile);
				for (ChunkCoordIntPair chunk : ticket.getChunkList())
				{
					ForgeChunkManager.unforceChunk(ticket, chunk);
				}
				for (ChunkCoordIntPair chunk : loader.getChunks())
				{
					forceChunk(ticket, chunk);
				}
			}
		}
	}
}
