package com.texasjake95.core.api.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IToolRegistry {

	public IToolHandler getHandler(Item item, int itemMeta);

	public IToolHandler getHandler(ItemStack stack);

	public void registerToolHandler(IToolHandler handler, Item item, int itemMeta, boolean damageMatters);

	public void registerToolHandler(IToolHandler handler, ItemStack stack, boolean damageMatters);
}
