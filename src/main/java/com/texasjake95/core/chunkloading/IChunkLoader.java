package com.texasjake95.core.chunkloading;

import net.minecraftforge.common.ForgeChunkManager.Ticket;


public interface IChunkLoader {

	void forceChunks(Ticket ticket);
	
	String getMod();
}
