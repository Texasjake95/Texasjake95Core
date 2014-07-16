package com.texasjake95.core.proxy.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovingObjectPosition;

/**
 * This class is used as a proxy to call code for Minecraft's {@link Minecraft} due to the
 * nature of the changing names of methods and fields.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
@SideOnly(Side.CLIENT)
public class MinecraftProxy {

	/**
	 * Retrieve the {@link GameSettings} used by the client.
	 *
	 * @return the instance of {@link GameSettings} used by Minecraft
	 */
	@SideOnly(Side.CLIENT)
	public static GameSettings getGameSettings()
	{
		return getMinecraft().gameSettings;
	}

	/**
	 * Retrieve the active instance of {@link Minecraft} used by the client.
	 *
	 * @return the active instance of {@link Minecraft}
	 */
	@SideOnly(Side.CLIENT)
	public static Minecraft getMinecraft()
	{
		return Minecraft.getMinecraft();
	}

	/**
	 * Retrieve the current {@link MovingObjectPosition} that the mouse is over (i.e. block,
	 * entity, null, etc.).
	 *
	 * @return the current {@link MovingObjectPosition} (or ray trace) that the mouse is
	 *         currently over
	 */
	@SideOnly(Side.CLIENT)
	public static MovingObjectPosition getObjectMouseOver()
	{
		return getMinecraft().objectMouseOver;
	}

	/**
	 * Retrieve the current {@link WorldClient} (or the only known world) to the client.
	 *
	 * @return the current active {@link WorldClient} used by the client
	 */
	@SideOnly(Side.CLIENT)
	public static WorldClient getWorld()
	{
		return getMinecraft().theWorld;
	}

	/**
	 * See if any pressed keys or clicks effects the player.
	 *
	 * @return true if pressed keys or clicks effect the player
	 */
	@SideOnly(Side.CLIENT)
	public static boolean inGameHasFocus()
	{
		return getMinecraft().inGameHasFocus;
	}
}
