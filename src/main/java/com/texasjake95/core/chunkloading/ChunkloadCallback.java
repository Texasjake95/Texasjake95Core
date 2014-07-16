package com.texasjake95.core.chunkloading;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.texasjake95.core.proxy.world.IBlockAccessProxy;

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
			int loaderX = ticket.getModData().getInteger("loaderX");
			int loaderY = ticket.getModData().getInteger("loaderY");
			int loaderZ = ticket.getModData().getInteger("loaderZ");
			TileEntity tile = IBlockAccessProxy.getTileEntity(world, loaderX, loaderY, loaderZ);
			if (tile instanceof IChunkLoader)
			{
				IChunkLoader loader = (IChunkLoader) tile;
				if (this.modID.equals(loader.getModID()))
					loader.setTicket(ticket);
			}
		}
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
	{
		List<Ticket> validTickets = Lists.newArrayList();
		for (Ticket ticket : tickets)
		{
			int loaderX = ticket.getModData().getInteger("loaderX");
			int loaderY = ticket.getModData().getInteger("loaderY");
			int loaderZ = ticket.getModData().getInteger("loaderZ");
			TileEntity tile = IBlockAccessProxy.getTileEntity(world, loaderX, loaderY, loaderZ);
			if (tile instanceof IChunkLoader)
			{
				IChunkLoader loader = (IChunkLoader) tile;
				if (maxTicketCount > validTickets.size() && this.modID.equals(loader.getModID()))
					validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}
