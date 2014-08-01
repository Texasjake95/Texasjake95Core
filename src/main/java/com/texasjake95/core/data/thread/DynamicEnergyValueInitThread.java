package com.texasjake95.core.data.thread;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.data.DataMapWrapper;

public class DynamicEnergyValueInitThread implements Runnable {

	private static DynamicEnergyValueInitThread instance = new DynamicEnergyValueInitThread();
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
			Thread thread = new Thread(instance, "Data Value Resolver Thread");
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
		DataMapWrapper.resolveValues();
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			Texasjake95Core.txLogger.info(String.format("Data system initialized after %s ms", duration));
		trueInit = true;
		ConfigRemapThread.resovleValues();
	}
}
