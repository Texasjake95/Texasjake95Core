package com.texasjake95.core.data;

import gnu.trove.map.hash.TCustomHashMap;
import codechicken.microblock.ItemMicroPart;

import net.minecraft.item.ItemStack;

import test.DefaultHashingStrategy;

import com.texasjake95.core.WrappedNBTStack;
import com.texasjake95.core.WrappedStack;

public class FMPValueProvider implements IValueProvider {

	TCustomHashMap<WrappedStack, FloatValue> valueMap = new TCustomHashMap<WrappedStack, FloatValue>(new DefaultHashingStrategy<WrappedStack>());

	public float getDivide(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
			case 1:
				return 8;
			case 2:
				return 4;
			case 4:
				return 2;
			case 257:
				return 8;
			case 258:
				return 4;
			case 260:
				return 2;
			case 513:
				return 32;
			case 514:
				return 16;
			case 516:
				return 8;
			case 769:
				return 16;
			case 770:
				return 8;
			case 772:
				return 4;
			default:
				return 1;
		}
	}

	@Override
	public float getValue(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemMicroPart)
		{
			WrappedNBTStack wrappedStack = new WrappedNBTStack(stack);
			if (!this.valueMap.contains(wrappedStack) || !this.valueMap.get(wrappedStack).isValid())
			{
				System.out.println("Value does not exist for " + wrappedStack);
				ItemStack part = ItemMicroPart.getMaterial(stack).getItem();
				FloatValue value = DataMapWrapper.getValueNoScale(part);
				if (value != null && value.isValid())
					value = new FloatValue(value.value / this.getDivide(stack));
				if (value != null && value.isValid())
					this.valueMap.put(wrappedStack, value);
			}
			else
				return this.valueMap.get(wrappedStack).value;
		}
		return 0;
	}
}
