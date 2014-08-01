package com.texasjake95.core.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value = "com.texasjake95.core.asm")
public class Texasjake95CorePlugin implements IFMLCallHook, IFMLLoadingPlugin {

	@Override
	public Void call() throws Exception
	{
		return null;
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { "com.texasjake95.core.asm.transformer.FMPTransformer" };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
	}
}
