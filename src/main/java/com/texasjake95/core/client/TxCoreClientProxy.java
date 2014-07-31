package com.texasjake95.core.client;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.TxCoreCommonProxy;
import com.texasjake95.core.client.handler.AutoSwitchHandler;
import com.texasjake95.core.client.handler.OverlayHandler;
import com.texasjake95.core.config.client.CoreConfigEventHandler;
import com.texasjake95.core.lib.handler.EventRegister;

/**
 * The client proxy used by {@link Texasjake95Core}.<br>
 * This proxy is used for the client side
 *
 * @author Texasjake95
 *
 */
public class TxCoreClientProxy extends TxCoreCommonProxy {

	@Override
	public void registerEventHandlers()
	{
		EventRegister.registerFMLEventHandler(new AutoSwitchHandler());
		EventRegister.registerFMLEventHandler(new CoreConfigEventHandler());
		EventRegister.registerForgeEventHandler(new OverlayHandler());
		super.registerEventHandlers();
	}
}
