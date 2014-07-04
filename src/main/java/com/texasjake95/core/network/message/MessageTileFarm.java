package com.texasjake95.core.network.message;

import com.texasjake95.core.tile.TileEntityFarm;

public class MessageTileFarm extends MessageTileCore<MessageTileFarm> {

	public MessageTileFarm()
	{
		super();
	}

	public MessageTileFarm(TileEntityFarm farm)
	{
		super(farm);
	}
}
