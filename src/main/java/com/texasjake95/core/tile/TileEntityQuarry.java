package com.texasjake95.core.tile;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
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
	
	public void forceChunks(Ticket ticket)
	{
		if (this.chunkTicket == null)
		{
			this.chunkTicket = ticket;
		}
		if (this.chunkTicket != null)
		{
			TicketHelper.populateTicket(chunkTicket, this);
			for (ChunkCoordIntPair chunk : this.chunkTicket.getChunkList())
			{
				ForgeChunkManager.unforceChunk(this.chunkTicket, chunk);
			}
			ChunkCoordIntPair chunk = new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4);
			ForgeChunkManager.forceChunk(this.chunkTicket, chunk);
			for (QuadrantQuarry quad : this.getQuadCount())
			{
				ChunkCoordIntPair workingChunk = quad.getCurrentChunkCoordIntPair(this.xCoord, this.zCoord);
				if (!chunk.equals(workingChunk))
				{
					ForgeChunkManager.forceChunk(this.chunkTicket, workingChunk);
				}
			}
		}
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
	public void invalidate()
	{
		ForgeChunkManager.releaseTicket(this.chunkTicket);
		super.invalidate();
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (this.chunkTicket == null && this.quadAreValid())
			{
				this.chunkTicket = ForgeChunkManager.requestTicket(Texasjake95Core.INSTANCE, this.worldObj, Type.NORMAL);
				if (this.chunkTicket != null)
				{
					this.forceChunks(this.chunkTicket);
				}
			}
			if (this.syncTicks++ % 200 == 0)
			{
				CorePacketHandler.INSTANCE.sendToAllAround(new MessageTileQuarry(this), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 30);
				this.syncTicks = 1;
				if (this.chunkTicket != null && this.quadAreValid())
				{
					this.forceChunks(this.chunkTicket);
				}
			}
			this.validateAndRunQuads(this.empty());
			this.pushToChest();
			if (!this.quadAreValid())
			{
				ForgeChunkManager.releaseTicket(this.chunkTicket);
				this.chunkTicket = null;
			}
		}
	}
	
	@Override
	public String getMod()
	{
		return CoreInfo.modId;
	}
}
