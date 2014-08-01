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

/**
 * Used to unify OreStack, FluidStacks, and ItemStacks for comparison to one another.
 *
 * @author Texasjake95
 *
 */
public class WrappedStack implements Comparable<WrappedStack> {

	/**
	 * Used to state what type of kind of object is wrapped in a {@link WrappedStack}.
	 *
	 * @author Texasjake95
	 *
	 */
	public enum WrappedType
	{
		/**
		 * States that an {@link ItemStack} has been wrapped.
		 */
		ITEMSTACK,
		/**
		 * States that an {@link OreStack} has been wrapped.
		 */
		ORESTACK,
		/**
		 * States that an {@link FluidStack} has been wrapped.
		 */
		FLUIDSTACK,
		/**
		 * States that an invalid object has been wrapped.
		 */
		NONE;
	}

	/**
	 * Comparator used for ItemStacks.
	 */
	public static final Comparator<ItemStack> itemStackCompare = new ItemStackComparator();
	/**
	 * Comparator used for FluidStacks.
	 */
	private static final Comparator<FluidStack> fluidStackCompare = new FluidStackComparator();

	/**
	 * Can the provided object be wrapped.
	 *
	 * @param object
	 *            the object to wrap
	 * @return true, if the object can be wrapped
	 */
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

	/**
	 * Is the WrappedStack valid.
	 */
	public boolean isValid = true;
	/**
	 * The type of object wrapped.
	 */
	public WrappedType type = WrappedType.NONE;
	/**
	 * The wrapped object.
	 */
	private Object wrappedStack;
	/**
	 * Does the stack size of the wrapped object matter.
	 */
	private boolean stackSizeMatters = false;

	/**
	 * Wrap the passed object.
	 *
	 * @param object
	 *            the object to wrap
	 */
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

	/**
	 * Wrap the passed object and state if the size of the object matters.
	 *
	 * @param object
	 *            the object to wrap
	 * @param stackSizeMatters
	 *            does the stack size of the object matter
	 */
	public WrappedStack(Object object, boolean stackSizeMatters)
	{
		this(object);
		if (this.isValid)
			this.stackSizeMatters = stackSizeMatters;
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
					return fluidStackCompare.compare((FluidStack) this.wrappedStack, (FluidStack) o.wrappedStack);
				case ITEMSTACK:
					return itemStackCompare.compare((ItemStack) this.wrappedStack, (ItemStack) o.wrappedStack);
				case NONE:
					return 0;
				case ORESTACK:
					return ((OreStack) this.wrappedStack).compareTo((OreStack) o.wrappedStack);
				default:
					return 0;
			}
		return compare;
	}

	/**
	 * Is the wrapped object comparable to the passed object.
	 *
	 * @param object
	 *            the object to check for.
	 * @return true, if the object is comparable
	 */
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
				return fluidStack.getFluid().getID() == ((FluidStack) this.wrappedStack).getFluid().getID();
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
				return OreDictionary.itemMatches((ItemStack) this.wrappedStack, stack, false) || OreDictionary.itemMatches(stack, (ItemStack) this.wrappedStack, false);
			case NONE:
				break;
			case ORESTACK:
				OreStack oreStack = (OreStack) object;
				return oreStack.hasItemStack((ItemStack) this.wrappedStack);
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
				return ((OreStack) this.wrappedStack).hasItemStack(stack);
			case NONE:
				break;
			case ORESTACK:
				OreStack oreStack = (OreStack) object;
				return ((OreStack) this.wrappedStack).areEqual(oreStack);
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
		WrappedStack wrapped = (WrappedStack) object;
		if (this.type != wrapped.type || this.type == WrappedType.NONE || wrapped.type == WrappedType.NONE)
			return false;
		switch (this.type)
		{
			case FLUIDSTACK:
				FluidStack thisFluidStack = (FluidStack) this.wrappedStack;
				FluidStack wrappedFluidStack = (FluidStack) wrapped.wrappedStack;
				return thisFluidStack.getFluid().getID() == wrappedFluidStack.getFluid().getID();
			case ITEMSTACK:
				ItemStack thisItemStack = (ItemStack) this.wrappedStack;
				ItemStack wrappedItemStack = (ItemStack) wrapped.wrappedStack;
				if (thisItemStack.getItem() == wrappedItemStack.getItem())
					if (thisItemStack.getItem().isDamageable() && wrappedItemStack.getItem().isDamageable())
						return true;
				return OreDictionary.itemMatches(thisItemStack, wrappedItemStack, true);
			case NONE:
				break;
			case ORESTACK:
				OreStack thisOreStack = (OreStack) this.wrappedStack;
				OreStack wrappedOreStack = (OreStack) wrapped.wrappedStack;
				return thisOreStack.areEqual(wrappedOreStack) || wrappedOreStack.areEqual(thisOreStack);
			default:
				break;
		}
		return false;
	}

	public Object getEffectiveStack()
	{
		return this.wrappedStack;
	}

	public int getStackSize()
	{
		if (!this.stackSizeMatters)
			return 1;
		else
			switch (this.type)
			{
				case FLUIDSTACK:
					return ((FluidStack) this.wrappedStack).amount;
				case ITEMSTACK:
					return ((ItemStack) this.wrappedStack).stackSize;
				case NONE:
					break;
				case ORESTACK:
					return ((OreStack) this.wrappedStack).getStackSize();
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
					hashcode = 7 * hashcode + GameRegistry.findUniqueIdentifierFor(((ItemStack) this.wrappedStack).getItem()).hashCode();
					if (!((ItemStack) this.wrappedStack).getItem().isDamageable())
						hashcode = 7 * hashcode + ((ItemStack) this.wrappedStack).getItemDamage();
					break;
				case NONE:
					break;
				case ORESTACK:
				case FLUIDSTACK:
					hashcode = 7 * hashcode + this.wrappedStack.hashCode();
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
		this.wrappedStack = object.copy();
	}

	private void processItemStack(ItemStack stack)
	{
		if (stack.getItem() == null)
		{
			this.isValid = false;
			return;
		}
		this.wrappedStack = stack.copy();
	}

	private void processOreStack(OreStack object)
	{
		if (object.isValid())
			this.wrappedStack = object;
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
				sb.append(GameRegistry.findUniqueIdentifierFor(((ItemStack) this.wrappedStack).getItem()) + ":" + ((ItemStack) this.wrappedStack).getItemDamage());
				break;
			case NONE:
				break;
			case ORESTACK:
				sb.append(this.wrappedStack);
				break;
			case FLUIDSTACK:
				sb.append(this.wrappedStack);
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
				return ((FluidStack) this.wrappedStack).getFluid().getLocalizedName((FluidStack) this.wrappedStack);
			case ITEMSTACK:
				return ((ItemStack) this.wrappedStack).getDisplayName();
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
