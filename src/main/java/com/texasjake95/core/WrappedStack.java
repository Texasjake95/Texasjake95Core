package com.texasjake95.core;

import java.util.Comparator;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WrappedStack implements Comparable<WrappedStack> {

	public enum WrappedType
	{
		ITEMSTACK,
		ORESTACK,
		FLUIDSTACK,
		NONE;
	}

	public static final Comparator<ItemStack> itemStackCompare = new ItemStackComparator();
	private static final Comparator<FluidStack> fluidStackCompare = new FluidStackComparator();

	public static boolean canWrap(Object object)
	{
		if (object instanceof Block)
			return true;
		if (object instanceof Item)
			return true;
		if (object instanceof ItemStack)
			return ((ItemStack) object).getItem() != null;
		if (object instanceof OreStack)
			return ((OreStack) object).isValid();
		if (object instanceof FluidStack)
			return ((FluidStack) object).getFluid() != null;
		if (object instanceof Fluid)
			return true;
		if (object instanceof String)
			return true;
		if (object instanceof List)
		{
			if (((List<?>) object).isEmpty())
				return false;
			return ((List<?>) object).get(0) instanceof ItemStack;
		}
		if (object instanceof WrappedStack)
			return ((WrappedStack) object).isValid;
		return false;
	}

	public boolean isValid = true;
	public WrappedType type = WrappedType.NONE;
	private ItemStack itemStack;
	private OreStack oreStack;
	private FluidStack fluidStack;
	private boolean stackSizeMatters = false;
	public static boolean equals = false;

	public WrappedStack(Object object)
	{
		if (!canWrap(object))
			this.isValid = false;
		else if (object instanceof ItemStack)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof Block)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof Item)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof OreStack)
		{
			this.processOreStack((OreStack) object);
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof String)
		{
			this.processOreStack(new OreStack((String) object));
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof List)
		{
			this.processOreStack(new OreStack(OreStack.getCommonName((List<?>) object)));
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof FluidStack)
		{
			this.processFluidStack((FluidStack) object);
			this.type = WrappedType.FLUIDSTACK;
		}
		else if (object instanceof Fluid)
		{
			this.processFluidStack(new FluidStack((Fluid) object, 1));
			this.type = WrappedType.FLUIDSTACK;
		}
		else if (object instanceof WrappedStack)
			this.processWrappedStack((WrappedStack) object);
		if (!this.isValid)
			this.type = WrappedType.NONE;
	}

	public WrappedStack(Object object, boolean stackSizeMatters)
	{
		this(object);
		if (this.isValid)
			this.stackSizeMatters = true;
	}

	@Override
	public int compareTo(WrappedStack o)
	{
		int compare = this.type.compareTo(o.type);
		if (compare != 0)
			return compare;
		if (this.isValid)
			switch (this.type)
			{
				case FLUIDSTACK:
					return fluidStackCompare.compare(this.fluidStack, o.fluidStack);
				case ITEMSTACK:
					return itemStackCompare.compare(this.itemStack, o.itemStack);
				case NONE:
					return 0;
				case ORESTACK:
					return this.oreStack.compareTo(o.oreStack);
				default:
					return 0;
			}
		return compare;
	}

	public boolean contains(Object object)
	{
		if (object instanceof WrappedStack)
			return this.contains(((WrappedStack) object).getEffectiveStack());
		switch (this.type)
		{
			case ITEMSTACK:
				return this.containsItemStack(object);
			case NONE:
				break;
			case ORESTACK:
				return this.containsOreStack(object);
			case FLUIDSTACK:
				return this.containsFluidStack(object);
			default:
				break;
		}
		return false;
	}

	private boolean containsFluidStack(Object object)
	{
		WrappedType stackType = object instanceof ItemStack ? WrappedType.ITEMSTACK : object instanceof OreStack ? WrappedType.ORESTACK : object instanceof FluidStack ? WrappedType.FLUIDSTACK : WrappedType.NONE;
		switch (stackType)
		{
			case FLUIDSTACK:
				FluidStack fluidStack = (FluidStack) object;
				return fluidStack.getFluid().getID() == this.fluidStack.getFluid().getID();
			case ITEMSTACK:
				break;
			// ItemStack stack = (ItemStack) object;
			// return FluidContainerRegistry.containsFluid(stack, this.fluidStack);
			case NONE:
				break;
			case ORESTACK:
				break;
			default:
				break;
		}
		return false;
	}

	private boolean containsItemStack(Object object)
	{
		WrappedType stackType = object instanceof ItemStack ? WrappedType.ITEMSTACK : object instanceof OreStack ? WrappedType.ORESTACK : object instanceof FluidStack ? WrappedType.FLUIDSTACK : WrappedType.NONE;
		switch (stackType)
		{
			case ITEMSTACK:
				ItemStack stack = (ItemStack) object;
				return OreDictionary.itemMatches(this.itemStack, stack, false) || OreDictionary.itemMatches(stack, this.itemStack, false);
			case NONE:
				break;
			case ORESTACK:
				OreStack oreStack = (OreStack) object;
				return oreStack.hasItemStack(this.itemStack);
			case FLUIDSTACK:
				break;
			// return FluidContainerRegistry.containsFluid((ItemStack)
			// this.getEffectiveStack(), (FluidStack) object);
			default:
				break;
		}
		return false;
	}

	private boolean containsOreStack(Object object)
	{
		WrappedType stackType = object instanceof ItemStack ? WrappedType.ITEMSTACK : object instanceof OreStack ? WrappedType.ORESTACK : object instanceof FluidStack ? WrappedType.FLUIDSTACK : WrappedType.NONE;
		switch (stackType)
		{
			case ITEMSTACK:
				ItemStack stack = (ItemStack) object;
				return this.oreStack.hasItemStack(stack);
			case NONE:
				break;
			case ORESTACK:
				OreStack oreStack = (OreStack) object;
				return this.oreStack.areEqual(oreStack);
			case FLUIDSTACK:
				break;
			default:
				break;
		}
		return false;
	}

	private ItemStack createItemStack(Object object)
	{
		if (object instanceof ItemStack)
		{
			ItemStack stack = (ItemStack) object;
			return stack;
		}
		else if (object instanceof Block)
			return new ItemStack((Block) object);
		else if (object instanceof Item)
			return new ItemStack((Item) object);
		return null;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof WrappedStack))
			return false;
		if (equals)
		{
			if (!(canWrap(object) || object instanceof WrappedStack) || object == null || !this.isValid)
				return false;
			ItemStack stack = this.createItemStack(object);
			if (stack != null)
				return this.contains(stack);
			OreStack oreStack = object instanceof OreStack ? (OreStack) object : null;
			if (oreStack != null)
				return this.contains(oreStack);
			FluidStack fluidStack = object instanceof FluidStack ? (FluidStack) object : object instanceof Fluid ? new FluidStack((Fluid) object, 1) : null;
			if (fluidStack != null)
				return this.contains(fluidStack);
			WrappedStack wrapped = (WrappedStack) object;
			Object wrappedStack = wrapped.getEffectiveStack();
			return wrappedStack != null && this.contains(wrappedStack);
		}
		WrappedStack wrapped = (WrappedStack) object;
		if (this.type != wrapped.type || this.type == WrappedType.NONE || wrapped.type == WrappedType.NONE)
			return false;
		switch (this.type)
		{
			case FLUIDSTACK:
				FluidStack thisFluidStack = this.fluidStack;
				FluidStack wrappedFluidStack = wrapped.fluidStack;
				return thisFluidStack.getFluid().getID() == wrappedFluidStack.getFluid().getID();
			case ITEMSTACK:
				ItemStack thisItemStack = this.itemStack;
				ItemStack wrappedItemStack = wrapped.itemStack;
				if (thisItemStack.getItem() == wrappedItemStack.getItem())
					if (thisItemStack.getItem().isDamageable() && wrappedItemStack.getItem().isDamageable())
						return true;
				return OreDictionary.itemMatches(thisItemStack, wrappedItemStack, true);
			case NONE:
				break;
			case ORESTACK:
				OreStack thisOreStack = this.oreStack;
				OreStack wrappedOreStack = wrapped.oreStack;
				return thisOreStack.areEqual(wrappedOreStack) || wrappedOreStack.areEqual(thisOreStack);
			default:
				break;
		}
		return false;
	}

	public Object getEffectiveStack()
	{
		switch (this.type)
		{
			case ITEMSTACK:
				return this.itemStack;
			case NONE:
				break;
			case ORESTACK:
				return this.oreStack;
			case FLUIDSTACK:
				return this.fluidStack;
			default:
				break;
		}
		return null;
	}

	public int getStackSize()
	{
		if (!this.stackSizeMatters)
			return 1;
		else
			switch (this.type)
			{
				case FLUIDSTACK:
					return this.fluidStack.amount;
				case ITEMSTACK:
					return this.itemStack.stackSize;
				case NONE:
					break;
				case ORESTACK:
					return this.oreStack.getStackSize();
				default:
					break;
			}
		return 1;
	}

	@Override
	public int hashCode()
	{
		int hashcode = this.type.hashCode();
		if (this.isValid)
			switch (this.type)
			{
				case ITEMSTACK:
					hashcode = 7 * hashcode + GameRegistry.findUniqueIdentifierFor(this.itemStack.getItem()).hashCode();
					if (!this.itemStack.getItem().isDamageable())
						hashcode = 7 * hashcode + this.itemStack.getItemDamage();
					break;
				case NONE:
					break;
				case ORESTACK:
					hashcode = 7 * hashcode + this.oreStack.hashCode();
					break;
				case FLUIDSTACK:
					hashcode = 7 * hashcode + this.fluidStack.hashCode();
					break;
				default:
					break;
			}
		return hashcode;
	}

	private void processFluidStack(FluidStack object)
	{
		if (object.getFluid() == null)
		{
			this.isValid = false;
			return;
		}
		this.fluidStack = object.copy();
	}

	private void processItemStack(ItemStack stack)
	{
		if (stack.getItem() == null)
		{
			this.isValid = false;
			return;
		}
		this.itemStack = stack.copy();
	}

	private void processOreStack(OreStack object)
	{
		if (object.isValid())
			this.oreStack = object;
		else
			this.isValid = false;
	}

	private void processWrappedStack(WrappedStack wrappedStack)
	{
		Object object = wrappedStack.getEffectiveStack();
		if (object instanceof ItemStack)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof Block)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof Item)
		{
			this.processItemStack(this.createItemStack(object));
			this.type = WrappedType.ITEMSTACK;
		}
		else if (object instanceof OreStack)
		{
			this.processOreStack((OreStack) object);
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof String)
		{
			this.processOreStack(new OreStack((String) object));
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof List)
		{
			this.processOreStack(new OreStack(OreStack.getCommonName((List<?>) object)));
			this.type = WrappedType.ORESTACK;
		}
		else if (object instanceof FluidStack)
		{
			this.processFluidStack((FluidStack) object);
			this.type = WrappedType.FLUIDSTACK;
		}
		else if (object instanceof Fluid)
		{
			this.processFluidStack(new FluidStack((Fluid) object, 1));
			this.type = WrappedType.FLUIDSTACK;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.type + ": ");
		sb.append(this.toStringNoType());
		return sb.toString();
	}

	public String toStringNoType()
	{
		StringBuilder sb = new StringBuilder();
		switch (this.type)
		{
			case ITEMSTACK:
				sb.append(GameRegistry.findUniqueIdentifierFor(this.itemStack.getItem()) + ":" + this.itemStack.getItemDamage());
				break;
			case NONE:
				break;
			case ORESTACK:
				sb.append(this.oreStack);
				break;
			case FLUIDSTACK:
				sb.append(this.fluidStack);
				break;
			default:
				break;
		}
		return sb.toString();
	}

	public String translate()
	{
		switch (this.type)
		{
			case FLUIDSTACK:
				return this.fluidStack.getFluid().getLocalizedName(this.fluidStack);
			case ITEMSTACK:
				return this.itemStack.getDisplayName();
			case NONE:
				break;
			case ORESTACK:
				break;
			default:
				break;
		}
		return this.toString();
	}
}
