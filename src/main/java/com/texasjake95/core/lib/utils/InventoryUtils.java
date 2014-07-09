package com.texasjake95.core.lib.utils;

import java.util.Random;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.texasjake95.commons.util.Range;
import com.texasjake95.core.proxy.world.WorldProxy;

public class InventoryUtils {

	public static void addToInventory(IInventory inv, ItemStack stack, ForgeDirection side)
	{
		if (stack == null)
			return;
		for (int slot : getSlots(inv, side))
		{
			if (stack.stackSize == 0)
				break;
			ItemStack invStack = inv.getStackInSlot(slot);
			if (invStack == null)
			{
				if (isItemValidForSlot(inv, slot, stack, side))
				{
					inv.setInventorySlotContents(slot, stack.copy());
					stack.stackSize = 0;
				}
				break;
			}
			if (stack.getItem() == invStack.getItem() && stack.getItemDamage() == invStack.getItemDamage())
				if (isItemValidForSlot(inv, slot, stack, side))
				{
					int maxSize = Math.min(inv.getInventoryStackLimit(), invStack.getMaxStackSize());
					while (invStack.stackSize < maxSize && stack.stackSize > 0)
					{
						invStack.stackSize += 1;
						stack.stackSize -= 1;
					}
					inv.setInventorySlotContents(slot, invStack);
				}
		}
	}

	public static boolean isItemValidForSlot(IInventory inv, int slot, ItemStack stack, ForgeDirection side)
	{
		if (inv instanceof ISidedInventory)
		{
			ISidedInventory sided = (ISidedInventory) inv;
			return sided.canInsertItem(slot, stack, side.getOpposite().ordinal());
		}
		return inv.isItemValidForSlot(slot, stack);
	}

	public static int[] getSlots(IInventory inv, ForgeDirection side)
	{
		if (inv instanceof ISidedInventory)
		{
			ISidedInventory sided = (ISidedInventory) inv;
			return sided.getAccessibleSlotsFromSide(side.getOpposite().ordinal());
		}
		int[] slots = new int[inv.getSizeInventory()];
		for (int i : Range.range(inv.getSizeInventory()))
		{
			slots[i] = i;
		}
		return slots;
	}

	public static void explodeInventory(IInventory inv, Random rand, World world, int x, int y, int z)
	{
		for (int slot = 0; slot < inv.getSizeInventory(); ++slot)
		{
			ItemStack itemstack = inv.getStackInSlot(slot);
			if (itemstack != null)
			{
				float xChange = rand.nextFloat() * 0.8F + 0.1F;
				float yChange = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;
				for (float zChange = rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; WorldProxy.spawnEntityInWorld(world, entityitem))
				{
					int dropSize = rand.nextInt(21) + 10;
					if (dropSize > itemstack.stackSize)
						dropSize = itemstack.stackSize;
					itemstack.stackSize -= dropSize;
					entityitem = new EntityItem(world, x + xChange, y + yChange, z + zChange, new ItemStack(itemstack.getItem(), dropSize, itemstack.getItemDamage()));
					entityitem.motionX = (float) rand.nextGaussian() * 0.05F;
					entityitem.motionY = (float) rand.nextGaussian() * 0.05F + 0.2F;
					entityitem.motionZ = (float) rand.nextGaussian() * 0.05F;
					if (itemstack.hasTagCompound())
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
				}
			}
		}
	}
}
