package com.texasjake95.core.lib.handler;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraftforge.common.MinecraftForge;

import com.texasjake95.commons.event.MainBus;

public class EventRegister {
	
	public static void registerCommonsEventHandler(Object object)
	{
		MainBus.BUS.register(object);
	}
	
	public static void registerFMLEventHandler(Object object)
	{
		FMLCommonHandler.instance().bus().register(object);
	}
	
	public static void registerForgeEventHandler(Object object)
	{
		MinecraftForge.EVENT_BUS.register(object);
	}
	
	public static void registerOreGenEventHandler(Object object)
	{
		MinecraftForge.ORE_GEN_BUS.register(object);
	}
	
	public static void registerTerrainEventHandler(Object object)
	{
		MinecraftForge.TERRAIN_GEN_BUS.register(object);
	}
}
