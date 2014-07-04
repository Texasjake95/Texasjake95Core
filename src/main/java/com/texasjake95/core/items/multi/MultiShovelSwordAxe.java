package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.texasjake95.core.items.ItemToolBase;

public class MultiShovelSwordAxe extends ItemToolBase {

	public static final Set<Block> blocksEffectiveAgainst = createSet(false, true, true, Blocks.web);

	public MultiShovelSwordAxe(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "shovel", "axe");
		this.isSword = true;
	}
}
