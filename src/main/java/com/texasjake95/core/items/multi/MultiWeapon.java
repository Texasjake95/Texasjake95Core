package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.texasjake95.core.items.ItemToolBase;

public class MultiWeapon extends ItemToolBase {
	
	private static final Set<Block> blocksEffectiveAgainst = createSet(true, true, true, Blocks.web);
	
	public MultiWeapon(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "axe", "shovel");
		this.isSword = true;
	}
}
