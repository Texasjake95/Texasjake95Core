package com.texasjake95.core.config.client;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.config.CoreConfig;

public class CoreConfigEventHandler {

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
	{
		if (eventArgs.modID.equals(CoreInfo.modId))
		{
			CoreConfig.getInstance().initProps();
		}
	}
}
