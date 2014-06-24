package com.texasjake95.core.api.handler;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * This interface is used to help the auto switcher determine the if the tool is
 * appropriate for the current block the player is pointing at<br>
 * 
 * This interface can be changed on request, if the request is reasonable
 * 
 * @author Texasjake95
 * 
 */
public interface IToolHandler {
	
	/**
	 * If false it prevents Items from being switched to
	 * 
	 * @param stack
	 * @return
	 */
	public boolean canAutoSwtichTo(ItemStack stack);
	
	/**
	 * Allows a mod to determine is the tool can harvest a given block
	 * 
	 * @param blockID
	 * @param blockMeta
	 * @param stack
	 * @return
	 */
	public boolean canHarvest(Block block, int blockMeta, ItemStack stack);
	
	/**
	 * Allow the custom gathering of a tools remaining durability in a percent
	 * form
	 * 
	 * @param stack
	 * @return a double less than 1.0F
	 */
	public double getDurability(ItemStack stack);
	
	/**
	 * Lets the mod mark an item as damageable in case the item is marked as not
	 * damageable
	 * 
	 * @param stack
	 * @return
	 */
	public boolean isDamageable(ItemStack stack);
}
