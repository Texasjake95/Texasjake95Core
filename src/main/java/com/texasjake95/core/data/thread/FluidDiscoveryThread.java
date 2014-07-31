package com.texasjake95.core.data.thread;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.RecipeWrapper;

public class FluidDiscoveryThread implements Runnable {

	private static FluidDiscoveryThread instance = new FluidDiscoveryThread();
	private static boolean init = false;
	private static boolean trueInit = false;

	private static void addMilk()
	{
		Fluid milk;
		if (!FluidRegistry.isFluidRegistered("milk"))
		{
			milk = new Fluid("milk").setUnlocalizedName(Items.milk_bucket.getUnlocalizedName());
			FluidRegistry.registerFluid(milk);
		}
		else
			milk = FluidRegistry.getFluid("milk");
		if (!FluidContainerRegistry.isContainer(new ItemStack(Items.milk_bucket)))
			FluidContainerRegistry.registerFluidContainer(new FluidStack(milk, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Items.milk_bucket), FluidContainerRegistry.EMPTY_BUCKET);
	}

	public static boolean isFinished()
	{
		return trueInit;
	}

	public static void startFluidDiscovery()
	{
		if (!init)
		{
			Thread thread = new Thread(instance, "Fluid Discovery Thread");
			thread.setDaemon(true);
			thread.start();
			init = true;
		}
	}

	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		addMilk();
		FluidContainerData[] dataArray = FluidContainerRegistry.getRegisteredFluidContainerData();
		for (FluidContainerData data : dataArray)
		{
			WrappedStack out = new WrappedStack(data.filledContainer);
			FluidStack stack = data.fluid;
			WrappedStack fluid = new WrappedStack(stack.copy(), true);
			WrappedStack in = new WrappedStack(data.emptyContainer);
			RecipeWrapper wrapper = new RecipeWrapper(out, in, fluid);
			RecipeWrapper fluidWrapper = new RecipeWrapper(out, fluid);
			if (data.emptyContainer != null)
				DataMapWrapper.addWrapper(out, wrapper);
			else if (data.filledContainer.getItem().hasContainerItem(data.filledContainer))
				DataMapWrapper.addWrapper(out, fluidWrapper);
		}
		DataMapWrapper.addWrapper(new WrappedStack(Blocks.water), new WrappedStack(Items.water_bucket));
		DataMapWrapper.addWrapper(new WrappedStack(Blocks.lava), new WrappedStack(Items.lava_bucket));
		long duration = System.currentTimeMillis() - startTime;
		if (duration > 10)
			System.out.println(String.format("Fluid Discovery initialized after %s ms", duration));
		trueInit = true;
		if (DynamicEnergyValueInitThread.canInit())
			DataMapWrapper.finishSetup();
	}
}
