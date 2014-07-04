package com.texasjake95.core.config.client;

import cpw.mods.fml.client.config.GuiConfig;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import net.minecraft.client.gui.GuiScreen;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.config.CoreConfig;

public class CoreGuiConfig extends GuiConfig {

	public CoreGuiConfig(GuiScreen parent)
	{
		super(parent, new ConfigElement<ConfigCategory>(CoreConfig.getInstance().forgeConfig.getCategory("auto_switch")).getChildElements(), CoreInfo.modId, false, false, GuiConfig.getAbridgedConfigPath(CoreConfig.getInstance().forgeConfig.toString()));
	}
}
