package com.texasjake95.core.proxy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovingObjectPosition;

public class MinecraftProxy {
	
	public static Minecraft getMinecraft()
	{
		return Minecraft.getMinecraft();
	}
	
	public static boolean inGameHasFocus()
	{
		return getMinecraft().inGameHasFocus;
	}
	
	public static GameSettings getGameSettings()
	{
		return getMinecraft().gameSettings;
	}
	
	public static MovingObjectPosition getObjectMouseOver()
	{
		return getMinecraft().objectMouseOver;
	}
}
