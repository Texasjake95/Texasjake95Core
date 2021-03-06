package com.texasjake95.core.addon;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.texasjake95.core.api.CoreInfo;

public class NEIConfig implements IConfigureNEI {
	
	@Override
	public void loadConfig()
	{
		API.registerRecipeHandler(new ShapelessDamageRecipeHandler());
		API.registerUsageHandler(new ShapelessDamageRecipeHandler());
	}
	
	@Override
	public String getName()
	{
		return "Texasjake95-Core NEI Config";
	}
	
	@Override
	public String getVersion()
	{
		return CoreInfo.modVersion;
	}
}
