package com.texasjake95.core.proxy;

import net.minecraft.block.material.Material;


public class MaterailProxy {

	public static boolean isToolNotRequired(Material material)
	{
		return material.isToolNotRequired();
	}
}
