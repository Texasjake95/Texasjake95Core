package com.texasjake95.core.api.impl;

import com.texasjake95.core.api.handler.IToolRegistry;

/**
 * This class contains the implementations of various registries and other Interfaces that the
 * mod uses.
 *
 * @author Texasjake95
 *
 */
public final class CoreAPI {

	/**
	 * The {@link IToolRegistry} instance that is used to auto switch to items along the
	 * player's hotbar.
	 */
	public static final IToolRegistry toolRegistry = ToolHandlerRegistry.getInstance();
}
