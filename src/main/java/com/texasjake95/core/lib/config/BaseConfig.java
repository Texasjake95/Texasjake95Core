package com.texasjake95.core.lib.config;

import com.texasjake95.commons.file.config.ConfigFile;

public abstract class BaseConfig {
	
	public ConfigFile config;
	public ConfigWriter configw;
	private boolean hasWorldGen = false;
	public boolean retroGen = false;
	
	public BaseConfig(ConfigWriter configw, boolean hasWorldGen)
	{
		this.configw = configw;
		this.config = configw.config();
		this.hasWorldGen = hasWorldGen;
	}
	
	public void initProps()
	{
		this.config.load();
		if (this.hasWorldGen)
		{
			this.retroGen = Boolean.parseBoolean(this.config.getCatagory("WorldGen").addPropertyWithComment("Retro Gen", false, "Set this to true for the world to retroGen on World load").value);
		}
		this.config.save();
	}
	
	public abstract String modName();

	public void endProps()
	{
		
	}
}
