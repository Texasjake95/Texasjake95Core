package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickShovelAxe extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(true, true, true);

	public MultiPickShovelAxe(ToolMaterial par3EnumToolMaterial, String texturePrefix)
	{
		super(axeDamage, par3EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "shovel", "axe");
	}
}
