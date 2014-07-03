package com.texasjake95.core.lib.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class BaseConfig {
	
	public static Property setComment(Property prop, String comment)
	{
		prop.comment = comment;
		return prop;
	}
	
	// public ConfigFile config;
	public Configuration forgeConfig;
	public ConfigWriter configw;
	private boolean hasWorldGen = false;
	public boolean retroGen = false;
	
	public BaseConfig(ConfigWriter configw, boolean hasWorldGen)
	{
		this.configw = configw;
		// this.config = configw.config();
		this.forgeConfig = configw.forgeConfig();
		this.hasWorldGen = hasWorldGen;
	}
	
	public void initProps()
	{
		// this.config.load();
		if (this.hasWorldGen)
		{
			// this.retroGen =
			// Boolean.parseBoolean(this.config.getCatagory("WorldGen").addPropertyWithComment("Retro Gen",
			// false,
			// "Set this to true for the world to retroGen on World load").value);
		}
		// this.config.save();
	}
	
	public abstract String modName();
	
	public final void save()
	{
		if (this.forgeConfig.hasChanged())
		{
			this.forgeConfig.save();
		}
	}
}
