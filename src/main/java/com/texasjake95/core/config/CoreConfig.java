package com.texasjake95.core.config;

import net.minecraftforge.common.config.Property;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.lib.config.BaseConfig;

public class CoreConfig extends BaseConfig {
	
	private static CoreConfig instance;
	
	public static CoreConfig getInstance()
	{
		if (instance == null)
		{
			instance = new CoreConfig();
		}
		return instance;
	}
	
	public boolean autoSwitch = true;
	public boolean forceTool = true;
	public boolean useBestTool = true;
	
	public CoreConfig()
	{
		super(new CoreConfigWriter(), false);
	}
	
	@Override
	public void initProps()
	{
		super.initProps();
		// this.config.load();
		// this.autoSwitch =
		// this.config.getCatagory("Auto Switch").addPropertyWithComment("Auto Switch Tool",
		// true,
		// "Set this to true for the equipped tool to be switched to the most effective tool on the hot bar").getBoolean(true);
		Property autoSwitch = this.forgeConfig.get("auto_switch", "Auto Switch Tool", true);
		autoSwitch = setComment(autoSwitch, "Set this to true for the equipped tool to be switched to the most effective tool on the hot bar");
		this.autoSwitch = autoSwitch.getBoolean(true);
		// this.useBestTool =
		// this.config.getCatagory("Auto Switch").addPropertyWithComment("Use Best Tool",
		// true, "Use the best tool possible").getBoolean(true);
		// this.forceTool =
		// this.config.getCatagory("Auto Switch").addPropertyWithComment("Force Tool",
		// true,
		// "Force tool usage if it is able to mine block").getBoolean(true);
		// this.config.save();
		this.save();
	}
	
	@Override
	public String modName()
	{
		return CoreInfo.modName;
	}
}
