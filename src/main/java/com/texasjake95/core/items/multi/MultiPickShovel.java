package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickShovel extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(true, true, false);

	public MultiPickShovel(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(pickaxeDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "shovel");
	}
}