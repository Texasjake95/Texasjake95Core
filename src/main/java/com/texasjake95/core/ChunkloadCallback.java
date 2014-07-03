package com.texasjake95.core;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.texasjake95.core.tile.TileEntityQuarry;

public class ChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback {
	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket ticket : tickets)
		{
			int quarryX = ticket.getModData().getInteger("quarryX");
			int quarryY = ticket.getModData().getInteger("quarryY");
			int quarryZ = ticket.getModData().getInteger("quarryZ");
			TileEntityQuarry tq = (TileEntityQuarry) world.getTileEntity(quarryX, quarryY, quarryZ);
			tq.forceChunks(ticket);
		}
	}
	
	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
	{
		List<Ticket> validTickets = Lists.newArrayList();
		for (Ticket ticket : tickets)
		{
			int quarryX = ticket.getModData().getInteger("quarryX");
			int quarryY = ticket.getModData().getInteger("quarryY");
			int quarryZ = ticket.getModData().getInteger("quarryZ");
			Block block = world.getBlock(quarryX, quarryY, quarryZ);
			int meta = world.getBlockMetadata(quarryX, quarryY, quarryZ);
			if (block == TxCoreCommonProxy.farm && meta == 1)
			{
				validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}
