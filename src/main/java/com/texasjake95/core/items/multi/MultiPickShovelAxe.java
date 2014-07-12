package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.texasjake95.core.items.ItemToolBase;

public class MultiPickShovelAxe extends ItemToolBase {

	private static final Set<Block> blocksEffectiveAgainst = createSet(true, true, true);
	private String material;
	private String tool;

	public MultiPickShovelAxe(ToolMaterial par3EnumToolMaterial, String texturePrefix)
	{
		super(axeDamage, par3EnumToolMaterial, blocksEffectiveAgainst, texturePrefix, "pickaxe", "shovel", "axe");
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

	public MultiPickShovelAxe setMaterialName(String material)
	{
		this.material = "material." + material + ".name";
		return this;
	}

	public MultiPickShovelAxe setToolName(String tool)
	{
		this.tool = "tool." + tool + ".name";
		return this;
	}
}
