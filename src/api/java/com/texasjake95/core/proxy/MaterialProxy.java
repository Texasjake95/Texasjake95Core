package com.texasjake95.core.proxy;

import net.minecraft.block.material.Material;

public class MaterialProxy {

	public static boolean isToolNotRequired(Material material)
	{
		return material.isToolNotRequired();
	}
}
