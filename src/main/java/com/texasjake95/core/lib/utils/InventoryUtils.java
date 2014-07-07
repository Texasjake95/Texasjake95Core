package com.texasjake95.core.lib.utils;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.texasjake95.core.proxy.world.WorldProxy;

public class InventoryUtils {

	public static void addToInventory(IInventory inv, ItemStack stack)
	{
		if (stack == null)
			return;
		for (int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			if (stack.stackSize == 0)
			{
				break;
			}
			ItemStack invStack = inv.getStackInSlot(slot);
			if (invStack == null)
			{
				if (inv.isItemValidForSlot(slot, stack))
				{
					inv.setInventorySlotContents(slot, stack.copy());
					stack.stackSize = 0;
				}
				break;
			}
			if (stack.getItem() == invStack.getItem() && stack.getItemDamage() == invStack.getItemDamage())
				if (inv.isItemValidForSlot(slot, stack))
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
					{
						dropSize = itemstack.stackSize;
					}
					itemstack.stackSize -= dropSize;
					entityitem = new EntityItem(world, x + xChange, y + yChange, z + zChange, new ItemStack(itemstack.getItem(), dropSize, itemstack.getItemDamage()));
					entityitem.motionX = (float) rand.nextGaussian() * 0.05F;
					entityitem.motionY = (float) rand.nextGaussian() * 0.05F + 0.2F;
					entityitem.motionZ = (float) rand.nextGaussian() * 0.05F;
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
				}
			}
		}
	}
}
