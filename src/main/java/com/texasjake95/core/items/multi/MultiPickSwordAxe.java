package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickSwordAxe extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(true, false, true, Blocks.web);

	public MultiPickSwordAxe(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "axe");
		this.isSword = true;
	}
}
