package com.texasjake95.core.data;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

import java.util.HashSet;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import test.DataMap;
import test.DefaultHashingStrategy;

import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.WrappedStack.WrappedType;

public class ValueMap extends DataMap<WrappedStack, RecipeWrapper, FloatValue> {

	public static enum Type
	{
		DEFAULT,
		MOD,
		USER;
	}

	private static final HashingStrategy<UniqueIdentifier> uniqueIdentifier = new DefaultHashingStrategy<UniqueIdentifier>();
	TCustomHashMap<WrappedStack, FloatValue> modMap;
	TCustomHashMap<WrappedStack, FloatValue> userMap;
	TCustomHashMap<WrappedStack, FloatValue> defaultMap;
	TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>> oreDict;
	TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>> wildCard;
	TCustomHashMap<UniqueIdentifier, IValueProvider> providers;

	public ValueMap()
	{
		super();
		this.modMap = new TCustomHashMap<WrappedStack, FloatValue>(this.nodeHash);
		this.userMap = new TCustomHashMap<WrappedStack, FloatValue>(this.nodeHash);
		this.defaultMap = new TCustomHashMap<WrappedStack, FloatValue>(this.nodeHash);
		this.wildCard = new TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>>(this.nodeHash);
		this.oreDict = new TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>>(this.nodeHash);
		this.providers = new TCustomHashMap<UniqueIdentifier, IValueProvider>(uniqueIdentifier);
	}

	public ValueMap(HashingStrategy<WrappedStack> node, HashingStrategy<FloatValue> value)
	{
		super(node, value);
		this.modMap = new TCustomHashMap<WrappedStack, FloatValue>(node);
		this.userMap = new TCustomHashMap<WrappedStack, FloatValue>(node);
		this.defaultMap = new TCustomHashMap<WrappedStack, FloatValue>(node);
		this.wildCard = new TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>>(node);
		this.oreDict = new TCustomHashMap<WrappedStack, HashSet<RecipeWrapper>>(node);
	}

	@Override
	protected FloatValue _getValue(WrappedStack node, boolean forceRemap)
	{
		FloatValue value = super._getValue(node, forceRemap);
		if (value == null || !value.isValid())
			value = this._getValue(this.oreDict, node, forceRemap);
		if (value == null || !value.isValid())
			value = this._getValue(this.wildCard, node, forceRemap);
		if (value == null || !value.isValid())
			if (node.type == WrappedType.ITEMSTACK)
			{
				float temp = 0;
				ItemStack stack = (ItemStack) node.getEffectiveStack();
				UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
				if (this.providers.contains(id))
					temp = this.providers.get(id).getValue(stack);
				value = new FloatValue(temp);
			}
		return value;
	}

	public void addOreNode(WrappedStack node)
	{
		this.addNode(this.oreDict, node);
	}

	public void addOreWrapper(WrappedStack node, RecipeWrapper wrapper)
	{
		this.addWrapper(this.oreDict, node, wrapper);
	}

	public void addValueProvider(Item item, IValueProvider valueProvider)
	{
		this.providers.put(GameRegistry.findUniqueIdentifierFor(item), valueProvider);
	}

	public void addWildCardNode(WrappedStack node)
	{
		this.addNode(this.wildCard, node);
	}

	public void addWildCardWrapper(WrappedStack node, RecipeWrapper wrapper)
	{
		this.addWrapper(this.wildCard, node, wrapper);
	}

	@Override
	protected boolean containsValueInMap(WrappedStack node)
	{
		return this.userMap.contains(node) || this.modMap.contains(node) || this.defaultMap.contains(node) || super.containsValueInMap(node);
	}

	@Override
	public FloatValue getValue(WrappedStack node, boolean forceRemap)
	{
		if (this.userMap.contains(node))
			return this.userMap.get(node);
		if (this.modMap.contains(node))
			return this.modMap.get(node);
		if (this.defaultMap.contains(node))
			return this.defaultMap.get(node);
		return super.getValue(node, forceRemap);
	}

	@Override
	protected FloatValue getValueFromMap(WrappedStack node)
	{
		if (this.userMap.contains(node))
			return this.userMap.get(node);
		if (this.modMap.contains(node))
			return this.modMap.get(node);
		if (this.defaultMap.contains(node))
			return this.defaultMap.get(node);
		return super.getValueFromMap(node);
	}

	@Override
	public void removeValue(WrappedStack node, boolean trueRemove, RemovalType removal)
	{
		if (this.defaultMap.contains(node))
			this.removeValue(this.defaultMap, node, trueRemove, removal);
		if (this.modMap.contains(node))
			this.removeValue(this.modMap, node, trueRemove, removal);
		if (this.userMap.contains(node))
			this.removeValue(this.userMap, node, trueRemove, removal);
		super.removeValue(node, trueRemove, removal);
		if (trueRemove)
		{
			if (this.oreDict.contains(node))
				this.oreDict.put(node, new HashSet<RecipeWrapper>());
			if (this.wildCard.contains(node))
				this.wildCard.put(node, new HashSet<RecipeWrapper>());
		}
	}

	@Override
	public void resolveValues(boolean forceRemap)
	{
		TCustomHashMap<WrappedStack, FloatValue> tempMap = new TCustomHashMap<WrappedStack, FloatValue>(this.nodeHash);
		tempMap.putAll(this.valueMap);
		tempMap.putAll(this.defaultMap);
		tempMap.putAll(this.modMap);
		tempMap.putAll(this.userMap);
		this.resolveValues(tempMap, forceRemap);
	}

	public void setValue(WrappedStack node, FloatValue value, Type type)
	{
		this.addNode(node);
		switch (type)
		{
			case DEFAULT:
				this.defaultMap.put(node, value);
				this.addReverseLookup(value, node);
				break;
			case MOD:
				this.modMap.put(node, value);
				this.addReverseLookup(value, node);
				break;
			case USER:
				this.userMap.put(node, value);
				this.addReverseLookup(value, node);
				break;
			default:
				break;
		}
	}

	public void transplantValues()
	{
		this.valueMap.putAll(this.defaultMap);
		this.valueMap.putAll(this.modMap);
		this.valueMap.putAll(this.userMap);
	}
}
