package com.texasjake95.core.handler;

import net.minecraftforge.common.ForgeHooks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.handler.IToolHandler;
import com.texasjake95.core.proxy.item.ItemStackProxy;

public class VanillaToolHandler implements IToolHandler {
	
	@Override
	public boolean canAutoSwtichTo(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean canHarvest(Block block, int blockMeta, ItemStack stack)
	{
		return ForgeHooks.canToolHarvestBlock(block, blockMeta, stack) || stack.getItem().canHarvestBlock(block, stack);
	}
	
	@Override
	public double getDurability(ItemStack stack)
	{
		double per = this.isDamageable(stack) ? ItemStackProxy.getMetadata(stack) / ItemStackProxy.getMaxDamage(stack) : 0.0F;
		return 1.0D - per;
	}
	
	@Override
	public boolean isDamageable(ItemStack stack)
	{
		return ItemStackProxy.isDamageable(stack);
	}
}
