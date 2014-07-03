package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.inventory.InventoryBase;
import com.texasjake95.core.lib.helper.InventoryHelper;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageTileQuarry;
import com.texasjake95.core.proxy.inventory.IInventoryProxy;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.quarry.QuadrantQuarry;

public class TileEntityQuarry extends TileEntityCore implements IInventory {
	
	private InventoryBase inv = new InventoryBase(10);
	private int syncTicks;
	private QuadrantQuarry quad = new QuadrantQuarry(ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.NORTH);
	private Ticket chunkTicket;
	
	@Override
	public void closeInventory()
	{
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int decr)
	{
		return this.inv.decrStackSize(slot, decr);
	}
	
	private boolean empty()
	{
		for (int invSlot = 0; invSlot < IInventoryProxy.getSizeInventory(this); invSlot++)
		{
			ItemStack stack = IInventoryProxy.getStackInSlot(this, invSlot);
			if (stack != null)
				return false;
		}
		return true;
	}
	
	public void forceChunks(Ticket ticket)
	{
		if (this.chunkTicket == null)
		{
			this.chunkTicket = ticket;
		}
		if (this.chunkTicket != null)
		{
			this.chunkTicket.getModData().setInteger("quarryX", this.xCoord);
			this.chunkTicket.getModData().setInteger("quarryY", this.yCoord);
			this.chunkTicket.getModData().setInteger("quarryZ", this.zCoord);
			for (ChunkCoordIntPair chunk : this.chunkTicket.getChunkList())
			{
				ForgeChunkManager.unforceChunk(this.chunkTicket, chunk);
			}
			ChunkCoordIntPair chunk = new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4);
			ForgeChunkManager.forceChunk(this.chunkTicket, chunk);
			ChunkCoordIntPair workingChunk = this.quad.getCurrentChunkCoordIntPair(this.xCoord, this.zCoord);
			if (!chunk.equals(workingChunk))
			{
				ForgeChunkManager.forceChunk(this.chunkTicket, workingChunk);
			}
		}
	}
	
	private IInventory getChestInv(World world, int x, int y, int z, Block block)
	{
		IInventory inv = (IInventory) WorldProxy.getTileEntity(world, x, y, z);
		if (WorldProxy.getBlock(world, x - 1, y, z) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x - 1, y, z), inv);
		}
		if (WorldProxy.getBlock(world, x + 1, y, z) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x + 1, y, z), inv);
		}
		if (WorldProxy.getBlock(world, x, y, z - 1) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x, y, z - 1), inv);
		}
		if (WorldProxy.getBlock(world, x, y, z + 1) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x, y, z + 1), inv);
		}
		return inv;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CorePacketHandler.INSTANCE.getPacketFrom(new MessageTileQuarry(this));
	}
	
	@Override
	public String getInventoryName()
	{
		return "tileEntity.farm.name";
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return this.inv.getInventoryStackLimit();
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inv.getSizeInventory();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inv.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.inv.getStackInSlotOnClosing(slot);
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public void invalidate()
	{
		ForgeChunkManager.releaseTicket(this.chunkTicket);
		super.invalidate();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}
	
	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
		this.inv.readFromNBT(nbtTagCompound.getCompoundTag("inv"));
		this.quad.load(nbtTagCompound.getCompoundTag("quad"));
	}
	
	@Override
	public void openInventory()
	{
	}
	
	private void pushToChest()
	{
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = WorldProxy.getTileEntity(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (tile instanceof TileEntityChest)
			{
				Block chest = WorldProxy.getBlock(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
				IInventory temp = this.getChestInv(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ, chest);
				this.pushToInv(temp);
			}
			else if (tile instanceof IInventory)
			{
				IInventory temp = (IInventory) tile;
				this.pushToInv(temp);
			}
		}
	}
	
	private void pushToInv(IInventory inv)
	{
		for (int invSlot = 0; invSlot < IInventoryProxy.getSizeInventory(this); invSlot++)
		{
			ItemStack stack = IInventoryProxy.getStackInSlot(this, invSlot);
			if (stack == null)
			{
				continue;
			}
			if (stack.stackSize == 0)
			{
				IInventoryProxy.setInventorySlotContents(this, invSlot, null);
				continue;
			}
			InventoryHelper.addToInventory(inv, stack);
		}
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.inv.readFromPacket(data, byteBuf);
		this.quad.readFromPacket(data, byteBuf, clazz);
	}
	
	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
		NBTTagCompound compound = new NBTTagCompound();
		this.inv.writeToNBT(compound);
		nbtTagCompound.setTag("inv", compound);
		compound = new NBTTagCompound();
		this.quad.save(compound);
		nbtTagCompound.setTag("quad", compound);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inv.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (this.chunkTicket == null && this.quad.isValid())
			{
				this.chunkTicket = ForgeChunkManager.requestTicket(Texasjake95Core.INSTANCE, this.worldObj, Type.NORMAL);
				if (this.chunkTicket != null)
				{
					this.forceChunks(this.chunkTicket);
				}
			}
			if (this.syncTicks++ % 200 == 0)
			{
				CorePacketHandler.INSTANCE.sendToAllAround(new MessageTileQuarry(this), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 30);
				this.syncTicks = 1;
				if (this.chunkTicket != null && this.quad.isValid())
				{
					this.forceChunks(this.chunkTicket);
				}
			}
			if (this.quad != null)
			{
				this.quad.validate(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				if (this.empty())
				{
					this.quad.run(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this);
				}
			}
			this.pushToChest();
			if (!this.quad.isValid())
			{
				ForgeChunkManager.releaseTicket(this.chunkTicket);
				this.chunkTicket = null;
			}
		}
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.inv.writeToPacket(dos, byteBuf);
		this.quad.writeToPacket(dos, byteBuf, clazz);
	}
}
