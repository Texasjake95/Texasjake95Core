package com.texasjake95.core.data;

import java.text.DecimalFormat;

import test.DataMap.IValue;

import com.texasjake95.core.WrappedStack;

public class FloatValue implements IValue<WrappedStack, FloatValue> {

	public static final DecimalFormat formatter = new DecimalFormat("#,###.##");
	public float value = 0;

	public FloatValue(float value)
	{
		this.value = value;
	}

	@Override
	public FloatValue combineValue(FloatValue value)
	{
		if (value != null)
		{
			float newValue = value.value + this.value;
			FloatValue result = new FloatValue(newValue);
			return result;
		}
		return this.copy();
	}

	@Override
	public int compareTo(FloatValue o)
	{
		return Float.compare(this.value, o.value);
	}

	@Override
	public FloatValue copy()
	{
		return new FloatValue(this.value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof FloatValue))
			return false;
		FloatValue other = (FloatValue) obj;
		return this.value == other.value;
	}

	@Override
	public FloatValue getMostAproprateValue(WrappedStack node, FloatValue value)
	{
		if (value == null)
			return this.copy();
		if (this.value > value.value && value.value != 0 || this.value == 0)
			return value.copy();
		return this.copy();
	}

	@Override
	public int hashCode()
	{
		return Float.valueOf(this.value).hashCode();
	}

	@Override
	public boolean isValid()
	{
		return this.value > 0;
	}

	@Override
	public String toString()
	{
		return String.format("%s", formatter.format(this.value));
	}
}
