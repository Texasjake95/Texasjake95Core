package com.texasjake95.core.proxy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovingObjectPosition;

public class MinecraftProxy {

	public static GameSettings getGameSettings()
	{
		return getMinecraft().gameSettings;
	}

	public static Minecraft getMinecraft()
	{
		return Minecraft.getMinecraft();
	}

	public static MovingObjectPosition getObjectMouseOver()
	{
		return getMinecraft().objectMouseOver;
	}

	public static WorldClient getWorld()
	{
		return getMinecraft().theWorld;
	}

	public static boolean inGameHasFocus()
	{
		return getMinecraft().inGameHasFocus;
	}
}
