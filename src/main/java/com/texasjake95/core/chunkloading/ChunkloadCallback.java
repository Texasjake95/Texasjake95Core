package com.texasjake95.core.chunkloading;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.texasjake95.core.proxy.world.WorldProxy;

public class ChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {
	
	private final String modID;
	
	public ChunkloadCallback(String modID)
	{
		this.modID = modID;
	}
	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket ticket : tickets)
		{
			int quarryX = ticket.getModData().getInteger("loaderX");
			int quarryY = ticket.getModData().getInteger("loaderY");
			int quarryZ = ticket.getModData().getInteger("loaderyZ");
			IChunkLoader loader = (IChunkLoader) world.getTileEntity(quarryX, quarryY, quarryZ);
			loader.forceChunks(ticket);
		}
	}
	
	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
	{
		List<Ticket> validTickets = Lists.newArrayList();
		for (Ticket ticket : tickets)
		{
			int quarryX = ticket.getModData().getInteger("loaderX");
			int quarryY = ticket.getModData().getInteger("loaderY");
			int quarryZ = ticket.getModData().getInteger("loaderZ");
			TileEntity tile = WorldProxy.getTileEntity(world, quarryX, quarryY, quarryZ);
			if (tile instanceof IChunkLoader)
			{
				if (maxTicketCount > validTickets.size())
					validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}
