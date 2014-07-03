package com.texasjake95.core.config;

import com.texasjake95.core.lib.config.ConfigWriter;

public class CoreConfigWriter extends ConfigWriter {
	
	public CoreConfigWriter()
	{
		super();
	}
	
	@Override
	public String modID()
	{
		return "Texasjake95Core";
	}
	
	@Override
	public String modName()
	{
		return "Texasjake95 - Core";
	}
}
