package com.texasjake95.core.api.farm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Implement this inorder to have control on what is a seed and what seed belongs with what
 * block/meta combo.
 *
 * @author Texasjake95
 *
 */
public interface ISeedProvider {

	/**
	 * Retrieve the seed associated with the specified Block meta combo.
	 *
	 * @param block
	 *            the {@link Block} instance that the seed is being requested for
	 * @param meta
	 *            the meta data of the {@link Block} that the seed is being requested for
	 * @return an {@link ItemIntPair} that was created with {@link Item} and Metadata of the
	 *         item that is the seed for the {@link Block} and it's Metadata
	 */
	ItemIntPair getSeed(Block block, int meta);

	/**
	 * Is the {@link Item} and metadata passed a seed?
	 *
	 * @param item
	 *            the {@link Item} in question
	 * @param meta
	 *            the metadata of the {@link Item} in question
	 * @return true if the {@link Item} and metadata passed is a seed
	 */
	boolean isSeed(Item item, int meta);
}
