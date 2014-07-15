package com.texasjake95.core.api.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This interface is used to help the auto switcher determine the if the tool is appropriate
 * for the current block the player is pointing at. <br>
 *
 * This interface can be changed on request, if the request is reasonable and a idea of how the
 * call is fired is provided
 *
 * @author Texasjake95
 *
 */
public interface IToolHandler {

	/**
	 * If false it prevents the {@link ItemStack} from being switched to.
	 *
	 * @param stack
	 *            the {@link ItemStack} attempting to be switched to
	 * @return can stack be switched to
	 */
	boolean canAutoSwtichTo(ItemStack stack);

	/**
	 * Allows a mod to determine is the tool can harvest a given block.
	 *
	 * @param block
	 *            the {@link Block} currently being broken
	 * @param blockMeta
	 *            the meta of the {@link Block} being broken
	 * @param stack
	 *            the {@link ItemStack} attempting to harvest the {@link Block}
	 * @return can stack harvest the block
	 */
	boolean canHarvest(Block block, int blockMeta, ItemStack stack);

	/**
	 * Allow the custom gathering of a tools remaining durability in a percent form.
	 *
	 * @param stack
	 *            the tool that damage percentage is being requested
	 * @return a double less than 1.0F
	 */
	double getDurability(ItemStack stack);

	/**
	 * Lets the mod mark an {@link ItemStack} as damageable in case the {@link Item} is marked
	 * as not damageable.
	 *
	 * @param stack
	 *            the {@link ItemStack} in question
	 * @return is the item damageable
	 */
	boolean isDamageable(ItemStack stack);
}
