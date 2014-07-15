package com.texasjake95.core.config;

import net.minecraftforge.common.config.Property;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.lib.config.BaseConfig;

public class CoreConfig extends BaseConfig {

	private static CoreConfig instance;

	public static CoreConfig getInstance()
	{
		if (instance == null)
			instance = new CoreConfig();
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
		Property autoSwitchProp = this.forgeConfig.get("auto_switch", "Auto Switch Tool", true);
		autoSwitchProp = setComment(autoSwitchProp, "Set this to true for the equipped tool to be switched to the most effective tool on the hot bar");
		this.autoSwitch = autoSwitchProp.getBoolean(true);
		Property forceToolProp = this.forgeConfig.get("auto_switch", "Force Tool", true);
		forceToolProp = setComment(forceToolProp, "Force tool usage if it is able to mine block");
		this.forceTool = forceToolProp.getBoolean(true);
		Property useBestToolProp = this.forgeConfig.get("auto_switch", "Use Best Tool", true);
		useBestToolProp = setComment(useBestToolProp, "Use the best tool possible");
		this.useBestTool = useBestToolProp.getBoolean(true);
		this.save();
	}

	@Override
	public String modName()
	{
		return CoreInfo.modName;
	}
}
