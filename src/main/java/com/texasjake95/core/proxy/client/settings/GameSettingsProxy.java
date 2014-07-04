package com.texasjake95.core.proxy.client.settings;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GameSettingsProxy {
	
	public static boolean isKeyDown(KeyBinding keyBinding)
	{
		return GameSettings.isKeyDown(keyBinding);
	}
	
	public static KeyBinding getKeyBindAttack(GameSettings gameSettings)
	{
		return gameSettings.keyBindAttack;
	}
}
