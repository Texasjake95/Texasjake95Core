package com.texasjake95.core.network.message;

import com.texasjake95.core.tile.TileEntityQuarry;

public class MessageTileQuarry extends MessageTileCore<MessageTileQuarry> {
	
	public MessageTileQuarry()
	{
		super();
	}
	
	public MessageTileQuarry(TileEntityQuarry quarry)
	{
		super(quarry);
	}
}
