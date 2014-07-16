package com.texasjake95.core.api.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.impl.ToolHandlerRegistry;

/**
 * Implement this to create a registry to handle {@link IToolHandler}s.
 *
 * @see ToolHandlerRegistry
 * @author Texasjake95
 *
 */
public interface IToolRegistry extends IToolHandler {

	/**
	 * Get the {@link IToolHandler} for the {@link Item} and its metadata.
	 *
	 * @param item
	 *            the {@link Item} in question
	 * @param itemMeta
	 *            the metadata of the {@link Item} in question
	 * @return the {@link IToolHandler} for the {@link Item} and metadata
	 */
	IToolHandler getHandler(Item item, int itemMeta);

	/**
	 * Get the {@link IToolHandler} for the {@link ItemStack}.<br>
	 * Generally boils down to getHandler(Item item, int itemMeta)
	 *
	 * @param stack
	 *            the {@link ItemStack} in question the metadata of the {@link Item} in
	 *            question
	 * @return the {@link IToolHandler} for the {@link ItemStack}
	 */
	IToolHandler getHandler(ItemStack stack);

	/**
	 * Register an {@link IToolHandler} via {@link Item} and metadata.<br>
	 * The last parameter (damageMatters) is used to state if the {@link Item}'s damage is
	 * needed to find the handler<br>
	 * NOTE: Only one handler can be registered per {@link Item}
	 *
	 * @param handler
	 *            the {@link IToolHandler} to register
	 * @param item
	 *            the {@link Item} to associate the {@link IToolHandler} with
	 * @param itemMeta
	 *            the metadata to associate the {@link IToolHandler} with if the damage is
	 *            important
	 * @param damageMatters
	 *            is the damage important?
	 */
	void registerToolHandler(IToolHandler handler, Item item, int itemMeta, boolean damageMatters);

	/**
	 * Register an {@link IToolHandler} via {@link ItemStack}. This generally boils down to
	 * registerToolHandler(IToolHandler handler, Item item, int itemMeta, boolean
	 * damageMatters)<br>
	 * The last parameter (damageMatters) is used to state if the {@link ItemStack}'s damage is
	 * needed to find the handler<br>
	 * NOTE: Only one handler can be registered per {@link Item}
	 *
	 * @param handler
	 *            the {@link IToolHandler} to register
	 * @param stack
	 *            the stack to pull the {@link Item} and metadata from
	 * @param damageMatters
	 *            is the damage important?
	 */
	void registerToolHandler(IToolHandler handler, ItemStack stack, boolean damageMatters);
}
