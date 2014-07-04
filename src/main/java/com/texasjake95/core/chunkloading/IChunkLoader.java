package com.texasjake95.core.chunkloading;

import java.util.List;

import net.minecraftforge.common.ForgeChunkManager.Ticket;

import net.minecraft.world.ChunkCoordIntPair;

public interface IChunkLoader {

	public List<ChunkCoordIntPair> getChunks();

	String getModID();

	Object getModObject();

	Ticket getTicket();

	void setTicket(Ticket ticket);
}
