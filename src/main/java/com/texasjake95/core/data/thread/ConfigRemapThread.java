package com.texasjake95.core.data.thread;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.handler.DefaultValueHandler;

public class ConfigRemapThread implements Runnable {

	private static ConfigRemapThread instance = new ConfigRemapThread();
	private static boolean init = false;
	private static boolean trueInit = false;

	public static boolean canInit()
	{
		if (!ValueDumpThread.isFinished())
			return false;
		if (!FluidDiscoveryThread.isFinished())
			return false;
		if (!ItemDiscoveryThread.isFinished())
			return false;
		if (!OreDiscoveryThread.isFinished())
			return false;
		if (!RecipeDiscoveryThread.isFinished())
			return false;
		if (!PotionDiscoveryThread.isFinished())
			return false;
		if (!DynamicEnergyValueInitThread.isFinished())
			return false;
		return true;
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static void resovleValues()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Config Remap Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		while (!canInit())
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		DefaultValueHandler.initConfig();
		DataMapWrapper.transplantValues();
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			Texasjake95Core.txLogger.info(String.format("Config Remap finished after %s ms", duration));
		trueInit = true;
	}
}
