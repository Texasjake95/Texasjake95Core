package com.texasjake95.core.proxy.client.settings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class GameSettingsProxy {

	@SideOnly(Side.CLIENT)
	public static KeyBinding getKeyBindAttack(GameSettings gameSettings)
	{
		return gameSettings.keyBindAttack;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isKeyDown(KeyBinding keyBinding)
	{
		return GameSettings.isKeyDown(keyBinding);
	}
}
