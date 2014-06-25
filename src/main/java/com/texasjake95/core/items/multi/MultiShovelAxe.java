package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;

import com.texasjake95.core.items.ItemToolBase;

public class MultiShovelAxe extends ItemToolBase {
	
	public static final Set<Block> blocksEffectiveAgainst = createSet(false, true, true);
	
	public MultiShovelAxe(ToolMaterial par2EnumToolMaterial, String texurePrefix)
	{
		super(axeDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texurePrefix, "shovel", "axe");
	}
}
