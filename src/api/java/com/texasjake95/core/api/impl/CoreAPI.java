package com.texasjake95.core.api.impl;

import com.texasjake95.core.api.handler.IToolRegistry;

public final class CoreAPI {

	public static final IToolRegistry toolRegistry = ToolHandlerRegistry.getInstance();

	private CoreAPI()
	{
	}
}
