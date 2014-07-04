package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickShovelSword extends ItemToolBase {

	public static final Set<Block> blocksEffectiveAgainst = createSet(true, true, false, Blocks.web);

	public MultiPickShovelSword(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "shovel");
		this.isSword = true;
	}
}