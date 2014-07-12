package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.texasjake95.core.items.ItemToolBase;

public class MultiWeapon extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(true, true, true, Blocks.web);
	private String material;
	private String tool;

	public MultiWeapon(ToolMaterial par2EnumToolMaterial, String texturePrefix)
	{
		super(swordDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "axe", "shovel");
		this.isSword = true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (this.tool == null || this.material == null)
			return super.getItemStackDisplayName(stack);
		String materail = StatCollector.translateToLocal(this.material).trim();
		String tool = StatCollector.translateToLocal(this.tool).trim();
		String format = StatCollector.translateToLocal("tool.format").trim();
		return format.replaceAll("%%materail", materail).replaceAll("%%tool", tool);
	}

	public MultiWeapon setMaterialName(String material)
	{
		this.material = "material." + material + ".name";
		return this;
	}

	public MultiWeapon setToolName(String tool)
	{
		this.tool = "tool." + tool + ".name";
		return this;
	}
}
