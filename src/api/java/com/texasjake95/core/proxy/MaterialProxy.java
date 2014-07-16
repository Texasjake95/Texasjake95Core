package com.texasjake95.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * This class is used as a proxy to call code for Minecraft's {@link Material} due to the
 * nature of the changing names of methods and fields.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
public class MaterialProxy {

	/**
	 * Is a tool not required to break this {@link Material}.
	 *
	 * @param material
	 *            the {@link Material} of the {@link Block} being broken
	 * @return true if the {@link Material} does not require a tool to break it
	 */
	public static boolean isToolNotRequired(Material material)
	{
		return material.isToolNotRequired();
	}
}
