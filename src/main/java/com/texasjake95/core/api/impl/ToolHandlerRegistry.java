package com.texasjake95.core.api.impl;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.handler.IToolHandler;
import com.texasjake95.core.api.handler.IToolRegistry;
import com.texasjake95.core.api.impl.vanilla.VanillaToolHandler;

/**
 * The Default implementation of {@link IToolRegistry}.
 *
 * @author Texasjake95
 *
 */
public final class ToolHandlerRegistry implements IToolRegistry {

	/**
	 * the default implementation of {@link IToolHandler}.
	 */
	private static final IToolHandler DEFAULT = new VanillaToolHandler();
	/**
	 * The active instance of ToolHandlerRegistry.
	 */
	private static ToolHandlerRegistry instance = null;

	/**
	 * Create or retrieve the active instance of ToolHandlerRegistry.
	 *
	 * @return the active instance of ToolHandlerRegistry
	 */
	static ToolHandlerRegistry getInstance()
	{
		if (instance == null)
			instance = new ToolHandlerRegistry();
		return instance;
	}

	/**
	 * The handlers that are damageable.
	 */
	private HashMap<Item, IToolHandler> damageableHandler = new HashMap<Item, IToolHandler>();
	/**
	 * Does the damage matter for the Item.
	 */
	private HashMap<Item, Boolean> damageMatters = new HashMap<Item, Boolean>();
	/**
	 * The handlers that are not damageable.
	 */
	private HashMap<Item, HashMap<Integer, IToolHandler>> nonDamageableHandler = new HashMap<Item, HashMap<Integer, IToolHandler>>();

	/**
	 * So no one initializes this again.
	 */
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
			return block.getMaterial().isToolNotRequired();
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
		return this.getHandler(stack.getItem(), stack.getItemDamage());
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
		this.damageMatters.put(item, doesDamageMatters);
		if (doesDamageMatters)
		{
			HashMap<Integer, IToolHandler> metaHandlers = this.nonDamageableHandler.get(item);
			if (metaHandlers == null)
				metaHandlers = new HashMap<Integer, IToolHandler>();
			metaHandlers.put(itemMeta, handler);
			this.nonDamageableHandler.put(item, metaHandlers);
		}
		else
			this.damageableHandler.put(item, handler);
	}

	@Override
	public void registerToolHandler(IToolHandler handler, ItemStack stack, boolean doesDamageMatters)
	{
		this.registerToolHandler(handler, stack.getItem(), stack.getItemDamage(), doesDamageMatters);
	}
}