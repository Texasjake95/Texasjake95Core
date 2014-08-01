package com.texasjake95.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WrappedNBTStack extends WrappedStack {

	public WrappedNBTStack(Object object)
	{
		super(object);
	}

	public WrappedNBTStack(Object object, boolean stackSizeMatters)
	{
		super(object, stackSizeMatters);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (super.equals(obj))
		{
			if (!(obj instanceof WrappedNBTStack))
				return false;
			if (this.type == WrappedType.ITEMSTACK)
			{
				WrappedNBTStack wrappedStack = (WrappedNBTStack) obj;
				ItemStack otherStack = (ItemStack) wrappedStack.getEffectiveStack();
				ItemStack thisStack = (ItemStack) this.getEffectiveStack();
				if (!otherStack.hasTagCompound() && !thisStack.hasTagCompound())
					return false;
				if (otherStack.hasTagCompound() && !thisStack.hasTagCompound())
					return false;
				if (!otherStack.hasTagCompound() && thisStack.hasTagCompound())
					return false;
				NBTTagCompound otherCompound = otherStack.getTagCompound();
				NBTTagCompound thisCompound = thisStack.getTagCompound();
				return thisCompound.equals(otherCompound);
			}
			else
				return true;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int hashCode = super.hashCode();
		switch (this.type)
		{
			case FLUIDSTACK:
				break;
			case ITEMSTACK:
				ItemStack stack = (ItemStack) this.getEffectiveStack();
				if (stack.hasTagCompound())
					hashCode = hashCode * 7 + ((ItemStack) this.getEffectiveStack()).getTagCompound().hashCode();
				break;
			case NONE:
				break;
			case ORESTACK:
				break;
			default:
				break;
		}
		return hashCode;
	}
}
