package com.texasjake95.core.data.thread;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.handler.DefaultValueHandler;

public class ValueDumpThread implements Runnable {

	private static ValueDumpThread instance = new ValueDumpThread();
	private static boolean init = false;
	private static boolean trueInit = false;

	private static boolean canPrint()
	{
		if (!FluidDiscoveryThread.isFinished())
			return false;
		if (!ItemDiscoveryThread.isFinished())
			return false;
		if (!OreDiscoveryThread.isWaiting())
			return false;
		if (!RecipeDiscoveryThread.isWaiting())
			return false;
		if (!PotionDiscoveryThread.isFinished())
			return false;
		return true;
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static void startValueDump()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Value Dump Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		if (DataMapWrapper.shouldDump())
		{
			while (!canPrint())
				try
				{
					Thread.sleep(5);
				}
				catch (InterruptedException e)
				{
				}
			DefaultValueHandler.init();
			DataMapWrapper.dumpCritNodes();
		}
		synchronized (DataMapWrapper.lock)
		{
			trueInit = true;
			DataMapWrapper.lock.notifyAll();
		}
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			Texasjake95Core.txLogger.info(String.format("Values Dumpped after %s ms", duration));
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
