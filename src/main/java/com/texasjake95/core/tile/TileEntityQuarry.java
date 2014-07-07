package com.texasjake95.core.tile;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.network.Packet;
import net.minecraft.world.ChunkCoordIntPair;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.chunkloading.IChunkLoader;
import com.texasjake95.core.chunkloading.TicketHelper;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageTileQuarry;
import com.texasjake95.core.tile.quarry.QuadrantQuarry;

public class TileEntityQuarry extends TileEntityQuad<TileEntityQuarry, QuadrantQuarry> implements IChunkLoader {

	private Ticket chunkTicket;

	public TileEntityQuarry()
	{
		super(10);
		this.addQuad(new QuadrantQuarry(ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.NORTH));
	}

	@Override
	public List<ChunkCoordIntPair> getChunks()
	{
		ArrayList<ChunkCoordIntPair> chunks = Lists.newArrayList();
		ChunkCoordIntPair chunk = new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4);
		chunks.add(chunk);
		for (QuadrantQuarry quad : this.getQuadCount())
			for (ChunkCoordIntPair workingChunk : quad.getWorkingChunkCoordIntPairs(this.xCoord, this.zCoord))
				if (!chunk.equals(workingChunk))
					chunks.add(workingChunk);
		return chunks;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return CorePacketHandler.INSTANCE.getPacketFrom(new MessageTileQuarry(this));
	}

	@Override
	public String getInventoryName()
	{
		return "tileEntity.txQuarry.name";
	}

	@Override
	public String getModID()
	{
		return CoreInfo.modId;
	}

	@Override
	public Object getModObject()
	{
		return Texasjake95Core.INSTANCE;
	}

	@Override
	public Ticket getTicket()
	{
		return this.chunkTicket;
	}

	@Override
	public void invalidate()
	{
		TicketHelper.releaseTicket(this);
		super.invalidate();
	}

	@Override
	public void setTicket(Ticket ticket)
	{
		this.chunkTicket = ticket;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (this.chunkTicket == null && this.quadAreValid())
				TicketHelper.updateChunks(this);
			if (++this.syncTicks % 200 == 0)
			{
				CorePacketHandler.INSTANCE.sendToAllAround(new MessageTileQuarry(this), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 30);
				this.syncTicks = 0;
				if (this.quadAreValid())
					TicketHelper.updateChunks(this);
			}
			this.validateAndRunQuads(this.empty());
			this.pushToChest();
			if (!this.quadAreValid())
			{
				TicketHelper.releaseTicket(this);
				this.chunkTicket = null;
			}
		}
	}
}
