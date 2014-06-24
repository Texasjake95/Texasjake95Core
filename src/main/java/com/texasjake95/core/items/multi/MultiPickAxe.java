package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickAxe extends ItemToolBase {
	
	private static final Set<Block> blocksEffectiveAgainst = createSet(true, false, true);
	
	public MultiPickAxe(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(axeDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "axe");
	}
}
