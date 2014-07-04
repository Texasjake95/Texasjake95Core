package com.texasjake95.core.lib.config;

import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.Loader;

import net.minecraftforge.common.config.Configuration;

import com.texasjake95.commons.file.config.ConfigFile;

public abstract class ConfigWriter {

	// private ConfigFile config;
	private Configuration forgeConfig;

	public ConfigWriter()
	{
		// CoreConfigHandler.getInstance().addConfig(this);
		// this.config =
		// this.createConfig(Loader.instance().getConfigDir().getCanonicalPath(),
		// this.modName());
		try
		{
			this.forgeConfig = new Configuration(new File(Loader.instance().getConfigDir().getCanonicalPath() + "/texasjake95/" + this.modID() + ".cfg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// this.config.load();
		this.forgeConfig.load();
	}

	// public ConfigFile config()
	// {
	// return this.config;
	// }
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

	public Configuration forgeConfig()
	{
		return this.forgeConfig;
	}

	public abstract String modID();

	public abstract String modName();
}
