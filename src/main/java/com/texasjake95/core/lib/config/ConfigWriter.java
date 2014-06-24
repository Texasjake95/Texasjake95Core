package com.texasjake95.core.lib.config;

import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.Loader;

import com.texasjake95.commons.file.config.ConfigFile;

public abstract class ConfigWriter {
	
	private ConfigFile config;
	
	public ConfigWriter() throws IOException
	{
		// CoreConfigHandler.getInstance().addConfig(this);
		this.config = this.createConfig(Loader.instance().getConfigDir().getCanonicalPath(), this.modName());
		this.config.load();
	}
	
	public ConfigFile config()
	{
		return this.config;
	}
	
	public ConfigFile createConfig(String confiDir, String modID)
	{
		File file = new File(confiDir + "/texasjake95/" + modID + ".cfg");
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return new ConfigFile(file);
	}
	
	public void endProps(ConfigFile config)
	{
		config.save();
	}
	
	public abstract String modID();
	
	public abstract String modName();
}
