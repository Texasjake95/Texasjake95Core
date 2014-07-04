package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.texasjake95.core.items.ItemToolBase;

public class MultiSwordAxe extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(false, false, false, Blocks.web);

	public MultiSwordAxe(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "axe");
		this.isSword = true;
	}
}
