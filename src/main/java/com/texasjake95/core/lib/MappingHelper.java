package com.texasjake95.core.lib;

import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.registry.GameRegistry.Type;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class MappingHelper {

	public static void remapBlock(MissingMapping mapping, Block block)
	{
		if (mapping.type == Type.BLOCK)
			mapping.remap(block);
		else
			mapping.remap(Item.getItemFromBlock(block));
	}
}
