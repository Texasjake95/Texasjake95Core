package com.texasjake95.core.network;

import cpw.mods.fml.relauncher.Side;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.lib.network.PacketHandler;
import com.texasjake95.core.network.message.MessageBlockRenderUpdate;
import com.texasjake95.core.network.message.MessageTileFarm;
import com.texasjake95.core.network.message.MessageTileQuarry;

public class CorePacketHandler extends PacketHandler {
	
	public static final CorePacketHandler INSTANCE = new CorePacketHandler();
	
	private CorePacketHandler()
	{
		super(CoreInfo.modId.toLowerCase());
	}
	
	@Override
	protected void registerMessages()
	{
		this.registerMessage(MessageTileFarm.class, MessageTileFarm.class, 0, Side.CLIENT);
		this.registerMessage(MessageTileQuarry.class, MessageTileQuarry.class, 1, Side.CLIENT);
		this.registerMessage(MessageBlockRenderUpdate.class, MessageBlockRenderUpdate.class, 2, Side.CLIENT);
	}
}
