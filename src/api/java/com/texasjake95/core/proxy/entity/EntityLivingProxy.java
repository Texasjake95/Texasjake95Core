package com.texasjake95.core.proxy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class EntityLivingProxy extends EntityProxy {

	public static Vec3 getLook(EntityLivingBase entity, float idk)
	{
		return entity.getLook(idk);
	}

	@SideOnly(Side.CLIENT)
	public static Vec3 getPostion(EntityLivingBase entity, float idk)
	{
		return entity.getPosition(idk);
	}
}
