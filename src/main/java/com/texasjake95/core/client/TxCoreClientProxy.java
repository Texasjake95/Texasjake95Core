package com.texasjake95.core.client;

import com.texasjake95.core.TxCoreCommonProxy;
import com.texasjake95.core.client.handler.AutoSwitchHandler;
import com.texasjake95.core.config.client.CoreConfigEventHandler;
import com.texasjake95.core.lib.handler.EventRegister;

public class TxCoreClientProxy extends TxCoreCommonProxy {

	@Override
	public void registerEventHandlers()
	{
		EventRegister.registerFMLEventHandler(new AutoSwitchHandler());
		EventRegister.registerFMLEventHandler(new CoreConfigEventHandler());
		super.registerEventHandlers();
	}
}
