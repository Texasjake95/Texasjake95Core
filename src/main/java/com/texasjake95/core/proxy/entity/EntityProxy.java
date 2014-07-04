package com.texasjake95.core.proxy.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityProxy {

	public static World getWorld(Entity entity)
	{
		return entity.worldObj;
	}
}
