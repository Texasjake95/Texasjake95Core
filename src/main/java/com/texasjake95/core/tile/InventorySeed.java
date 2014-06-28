package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.Constants;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.texasjake95.core.network.IPacketHandler;
import com.texasjake95.core.proxy.item.ItemStackProxy;

public class InventorySeed implements IPacketHandler {
	
	private int limit;
	private HashMap<String, ItemStack> map = Maps.newHashMap();
	
	public InventorySeed(int limit)
	{
		this.limit = limit;
	}
	
	public void addItemStack(ItemStack stack)
	{
		if (stack == null)
			return;
		String key = this.key(stack);
		ItemStack tempStack = this.map.get(key);
		if (tempStack == null)
		{
			tempStack = stack.copy();
			stack.stackSize = 0;
		}
		int maxSize = Math.min(stack.getMaxStackSize(), this.limit);
		while (tempStack.stackSize < maxSize && stack.stackSize > 0)
		{
			tempStack.stackSize += 1;
			stack.stackSize -= 1;
		}
		this.map.put(key, tempStack);
	}
	
	public ItemStack getStack(Item seed, int meta)
	{
		return this.map.get(this.key(seed, meta));
	}
	
	public ItemStack getStack(ItemIntPair pair)
	{
		if (pair == null)
			return null;
		return this.getStack(pair.getItem(), pair.getMeta());
	}
	
	private String key(Item item, int meta)
	{
		return GameRegistry.findUniqueIdentifierFor(item).toString() + meta;
	}
	
	private String key(ItemStack stack)
	{
		return this.key(ItemStackProxy.getItem(stack), ItemStackProxy.getMetadata(stack));
	}
	
	public void load(NBTTagCompound data)
	{
		NBTTagList items = data.getTagList("items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); i++)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
			ItemStack stack = ItemStack.loadItemStackFromNBT(item);
			this.addItemStack(stack);
		}
	}
	
	public void printItems()
	{
		for (Entry<String, ItemStack> entry : this.map.entrySet())
		{
			ItemStack stack = entry.getValue();
			if (stack == null)
			{
				System.out.println("null");
			}
			else
			{
				System.out.println(stack.getItem().getUnlocalizedName() + ":" + stack.getItemDamage() + ":" + stack.stackSize);
			}
		}
	}
	
	public void save(NBTTagCompound data)
	{
		NBTTagList items = new NBTTagList();
		for (Entry<String, ItemStack> entry : this.map.entrySet())
		{
			NBTTagCompound item = new NBTTagCompound();
			entry.getValue().writeToNBT(item);
			items.appendTag(item);
		}
		data.setTag("items", items);
	}
	
	public void dropItemStacks(World world, int x, int y, int z, Random rand)
	{
		for (Entry<String, ItemStack> entry : this.map.entrySet())
		{
			ItemStack itemstack = entry.getValue();
			if (itemstack != null)
			{
				float xChange = rand.nextFloat() * 0.8F + 0.1F;
				float yChange = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;
				for (float zChange = rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
				{
					int dropSize = rand.nextInt(21) + 10;
					if (dropSize > itemstack.stackSize)
					{
						dropSize = itemstack.stackSize;
					}
					itemstack.stackSize -= dropSize;
					entityitem = new EntityItem(world, (double) ((float) x + xChange), (double) ((float) y + yChange), (double) ((float) z + zChange), new ItemStack(itemstack.getItem(), dropSize, itemstack.getItemDamage()));
					entityitem.motionX = (double) ((float) rand.nextGaussian() * 0.05F);
					entityitem.motionY = (double) ((float) rand.nextGaussian() * 0.05F + 0.2F);
					entityitem.motionZ = (double) ((float) rand.nextGaussian() * 0.05F);
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
				}
			}
		}
	}
	
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf) throws IOException
	{
		dos.writeInt(this.limit);
		dos.writeInt(this.map.size());
		for (Entry<String, ItemStack> entry : this.map.entrySet())
		{
			dos.writeUTF(entry.getKey());
			ItemStack stack = entry.getValue();
			ByteBufUtils.writeItemStack(byteBuf, stack);
		}
	}
	
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf) throws IOException
	{
		this.limit = data.readInt();
		int size = data.readInt();
		for (int i = 0; i < size; i++)
		{
			String key = data.readUTF();
			map.put(key, ByteBufUtils.readItemStack(byteBuf));
		}
	}
}
