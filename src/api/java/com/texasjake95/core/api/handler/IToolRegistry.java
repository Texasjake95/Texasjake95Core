package com.texasjake95.core.api.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IToolRegistry extends IToolHandler {

	IToolHandler getHandler(Item item, int itemMeta);

	IToolHandler getHandler(ItemStack stack);

	void registerToolHandler(IToolHandler handler, Item item, int itemMeta, boolean damageMatters);

	void registerToolHandler(IToolHandler handler, ItemStack stack, boolean damageMatters);
}
