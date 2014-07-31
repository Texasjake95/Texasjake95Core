package com.texasjake95.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.texasjake95.core.tile.TilePipe;

public class SuctionMap {

	Map<WrappedStack, Integer> suction = Maps.newHashMap();

	public void addSuction(ItemStack stack, int suction)
	{
		this.addSuction(new WrappedStack(stack), suction);
	}

	public void addSuction(OreStack stack, int suction)
	{
		this.addSuction(new WrappedStack(stack), suction);
	}

	public void addSuction(String oreName, int suction)
	{
		this.addSuction(new OreStack(oreName), suction);
	}

	private void addSuction(WrappedStack stack, int suction)
	{
		WrappedStack key = this.getKey(stack);
		if (key == null)
			key = stack;
		this.suction.put(key, suction);
	}

	public ForgeDirection getBestDirection(ItemStack stack, World world, int x, int y, int z)
	{
		return this.getBestDirection(new WrappedStack(stack), world, x, y, z);
	}

	public ForgeDirection getBestDirection(WrappedStack stack, World world, int x, int y, int z)
	{
		ForgeDirection direction = ForgeDirection.UNKNOWN;
		int suction = 0;
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
			if (tile instanceof TilePipe)
			{
				TilePipe pipe = (TilePipe) tile;
				int pipeSuction = pipe.getSuction(stack);
				if (suction < pipeSuction)
				{
					suction = pipeSuction;
					direction = d;
				}
			}
		}
		if (this.getSuction(stack) > suction)
			direction = ForgeDirection.UNKNOWN;
		return direction;
	}

	public WrappedStack getKey(WrappedStack stack)
	{
		Iterator<Entry<WrappedStack, Integer>> iterator = this.suction.entrySet().iterator();
		Entry<WrappedStack, Integer> entry;
		do
		{
			if (!iterator.hasNext())
				return stack;
			entry = iterator.next();
		}
		while (!this.objectMatches(stack, entry.getKey()));
		return entry.getKey();
	}

	public Map<WrappedStack, Integer> getMap()
	{
		return this.suction;
	}

	public int getSuction(ItemStack stack)
	{
		return this.getSuction(new WrappedStack(stack));
	}

	public int getSuction(WrappedStack stack)
	{
		Iterator<Entry<WrappedStack, Integer>> iterator = this.suction.entrySet().iterator();
		Entry<WrappedStack, Integer> entry;
		do
		{
			if (!iterator.hasNext())
				return 0;
			entry = iterator.next();
		}
		while (!this.objectMatches(stack, entry.getKey()));
		return entry.getValue();
	}

	private boolean objectMatches(WrappedStack stack, WrappedStack object)
	{
		if (stack == null || object == null)
			return false;
		return stack.equals(object);
	}

	public void updateMap(WrappedStack key, Integer value)
	{
		int current = this.getSuction(key);
		current = Math.max(current, value);
		if (current == value)
			this.addSuction(key, value);
	}
}
