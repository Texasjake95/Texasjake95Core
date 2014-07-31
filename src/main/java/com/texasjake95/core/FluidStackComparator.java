package com.texasjake95.core;

import java.util.Comparator;

import net.minecraftforge.fluids.FluidStack;

public class FluidStackComparator implements Comparator<FluidStack> {

	@Override
	public int compare(FluidStack o1, FluidStack o2)
	{
		int compare = Integer.compare(o1.fluidID, o2.fluidID);
		if (compare != 0)
			return compare;
		compare = Integer.compare(o1.amount, o2.amount);
		if (compare != 0)
			return compare;
		return compare;
	}
}
