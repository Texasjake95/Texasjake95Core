package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

import com.texasjake95.core.inventory.InventorySeed;
import com.texasjake95.core.lib.pair.ItemIntPair;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageTileFarm;
import com.texasjake95.core.tile.farm.QuadrantFarm;

public class TileEntityFarm extends TileEntityQuad<TileEntityFarm, QuadrantFarm> {
	
	private InventorySeed seedInv = new InventorySeed(10);
	
	public TileEntityFarm()
	{
		super(10);
		this.addQuad(new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.NORTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.NORTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.SOUTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH));
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CorePacketHandler.INSTANCE.getPacketFrom(new MessageTileFarm(this));
	}
	
	@Override
	public String getInventoryName()
	{
		return "tileEntity.txFarm.name";
	}
	
	public ItemStack getItemStack(Item seed, int meta)
	{
		return this.seedInv.getStack(seed, meta);
	}
	
	public ItemStack getItemStack(ItemIntPair seed)
	{
		return this.seedInv.getStack(seed.getItem(), seed.getMeta());
	}
	
	public InventorySeed getSeedInv()
	{
		return this.seedInv;
	}
	
	@Override
	public void load(NBTTagCompound nbtTagCompound)
	{
		super.load(nbtTagCompound);
		this.seedInv.load(nbtTagCompound.getCompoundTag("seedInv"));
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (data != null)
		{
			super.readFromPacket(data, byteBuf, clazz);
			this.seedInv.readFromPacket(data, byteBuf, clazz);
		}
	}
	
	@Override
	public void save(NBTTagCompound nbtTagCompound)
	{
		super.save(nbtTagCompound);
		NBTTagCompound data = new NBTTagCompound();
		this.seedInv.save(data);
		nbtTagCompound.setTag("seedInv", data);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (++this.syncTicks % 200 == 0)
			{
				CorePacketHandler.INSTANCE.sendToAllAround(new MessageTileFarm(this), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 30);
				this.syncTicks = 0;
			}
			this.validateAndRunQuads(this.empty());
			this.pushToChest();
		}
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (dos != null)
		{
			super.writeToPacket(dos, byteBuf, clazz);
			this.seedInv.writeToPacket(dos, byteBuf, clazz);
		}
	}
}
