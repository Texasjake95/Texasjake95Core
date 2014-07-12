package com.texasjake95.core.items.multi;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.texasjake95.core.items.ItemToolBase;

public class MultiShovelAxe extends ItemToolBase {

	public static final Set<Block> blocksEffectiveAgainst = createSet(false, true, true);
	private String material;
	private String tool;

	public MultiShovelAxe(ToolMaterial par2EnumToolMaterial, String texurePrefix)
	{
		super(axeDamage, par2EnumToolMaterial, blocksEffectiveAgainst, texurePrefix, "shovel", "axe");
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

	public MultiShovelAxe setMaterialName(String material)
	{
		this.material = "material." + material + ".name";
		return this;
	}

	public MultiShovelAxe setToolName(String tool)
	{
		this.tool = "tool." + tool + ".name";
		return this;
	}
}
