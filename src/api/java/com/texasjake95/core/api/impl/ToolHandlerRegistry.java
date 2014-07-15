package com.texasjake95.core.api.impl;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.handler.IToolHandler;
import com.texasjake95.core.api.handler.IToolRegistry;
import com.texasjake95.core.api.impl.vanilla.VanillaToolHandler;
import com.texasjake95.core.proxy.BlockProxy;
import com.texasjake95.core.proxy.MaterailProxy;
import com.texasjake95.core.proxy.item.ItemStackProxy;

final class ToolHandlerRegistry implements IToolRegistry {

	private static final IToolHandler DEFAULT = new VanillaToolHandler();
	private static ToolHandlerRegistry instance = null;

	static ToolHandlerRegistry getInstance()
	{
		if (instance == null)
			instance = new ToolHandlerRegistry();
		return instance;
	}

	private HashMap<String, IToolHandler> damageableHandler = new HashMap<String, IToolHandler>();
	private HashMap<String, Boolean> damageMatters = new HashMap<String, Boolean>();
	private HashMap<String, HashMap<Integer, IToolHandler>> nonDamageableHandler = new HashMap<String, HashMap<Integer, IToolHandler>>();

	private ToolHandlerRegistry()
	{
		this.registerToolHandler(DEFAULT, new ItemStack(Items.diamond_pickaxe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.golden_pickaxe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.iron_pickaxe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.stone_pickaxe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.wooden_pickaxe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.diamond_shovel), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.golden_shovel), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.iron_shovel), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.stone_shovel), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.wooden_shovel), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.diamond_axe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.golden_axe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.iron_axe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.stone_axe), false);
		this.registerToolHandler(DEFAULT, new ItemStack(Items.wooden_axe), false);
	}

	@Override
	public boolean canAutoSwtichTo(ItemStack stack)
	{
		if (stack == null)
			return true;
		return this.getHandler(stack).canAutoSwtichTo(stack);
	}

	@Override
	public boolean canHarvest(Block block, int blockMeta, ItemStack stack)
	{
		if (stack == null)
			return MaterailProxy.isToolNotRequired(BlockProxy.getMaterial(block));
		return this.getHandler(stack).canHarvest(block, blockMeta, stack);
	}

	@Override
	public double getDurability(ItemStack stack)
	{
		if (stack == null)
			return 1.0F;
		return this.getHandler(stack).getDurability(stack);
	}

	@Override
	public IToolHandler getHandler(Item item, int itemMeta)
	{
		if (this.damageMatters.containsKey(item))
		{
			if (this.damageMatters.get(item))
			{
				if (this.nonDamageableHandler.containsKey(item) && this.nonDamageableHandler.get(item) != null)
				{
					HashMap<Integer, IToolHandler> metaHandlers = this.nonDamageableHandler.get(item);
					if (metaHandlers.containsKey(itemMeta))
						return metaHandlers.get(itemMeta);
					else
						return DEFAULT;
				}
				else
					return DEFAULT;
			}
			else if (this.damageableHandler.containsKey(item) && this.damageableHandler.get(item) != null)
				return this.damageableHandler.get(item);
			else
				return DEFAULT;
		}
		else
			return DEFAULT;
	}

	@Override
	public IToolHandler getHandler(ItemStack stack)
	{
		return this.getHandler(ItemStackProxy.getItem(stack), ItemStackProxy.getMetadata(stack));
	}

	@Override
	public boolean isDamageable(ItemStack stack)
	{
		if (stack == null)
			return false;
		return this.getHandler(stack).isDamageable(stack);
	}

	@Override
	public void registerToolHandler(IToolHandler handler, Item item, int itemMeta, boolean doesDamageMatters)
	{
		this.damageMatters.put(Item.itemRegistry.getNameForObject(item), doesDamageMatters);
		if (doesDamageMatters)
		{
			HashMap<Integer, IToolHandler> metaHandlers = this.nonDamageableHandler.get(item);
			if (metaHandlers == null)
				metaHandlers = new HashMap<Integer, IToolHandler>();
			metaHandlers.put(itemMeta, handler);
			this.nonDamageableHandler.put(Item.itemRegistry.getNameForObject(item), metaHandlers);
		}
		else
			this.damageableHandler.put(Item.itemRegistry.getNameForObject(item), handler);
	}

	@Override
	public void registerToolHandler(IToolHandler handler, ItemStack stack, boolean doesDamageMatters)
	{
		this.registerToolHandler(handler, stack.getItem(), stack.getItemDamage(), doesDamageMatters);
	}
}
