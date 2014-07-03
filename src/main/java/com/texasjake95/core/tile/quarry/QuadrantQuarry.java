package com.texasjake95.core.tile.quarry;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.lib.helper.InventoryHelper;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageBlockRenderUpdate;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.Quadrant;
import com.texasjake95.core.tile.TileEntityFarm;
import com.texasjake95.core.tile.TileEntityQuarry;

public class QuadrantQuarry extends Quadrant<TileEntityQuarry> {
	
	boolean resetHeight = false;
	boolean increase = false;
	private float breakIndex = 0;
	private boolean isDone = false;
	
	public QuadrantQuarry(ForgeDirection eastWest, ForgeDirection upDown, ForgeDirection northSouth)
	{
		super(eastWest, upDown, northSouth);
	}
	
	@Override
	protected boolean _validate(World world, int x, int y, int z)
	{
		return !this.isDone;
	}
	
	public ChunkCoordIntPair getCurrentChunkCoordIntPair(int x, int z)
	{
		return new ChunkCoordIntPair((x + this.row * this.eastWest.offsetX) >> 4, (z + this.column * this.northSouth.offsetZ) >> 4);
	}
	
	@Override
	protected void handleBlock(World world, int x, int y, int z, TileEntityQuarry tile)
	{
		if (this.height >= tile.yCoord)
		{
			this.resetHeight = true;
		}
		Block block = WorldProxy.getBlock(world, x, y, z);
		if (block instanceof BlockLiquid || block instanceof IFluidBlock || block == Blocks.air)
		{
			this.increase = true;
			this.breakIndex = 0;
			block = WorldProxy.getBlock(world, x, y - 1, z);
			if (block == Blocks.bedrock)
			{
				this.resetHeight = true;
			}
			return;
		}
		float hardness = block.getBlockHardness(world, x, y, z);
		if (this.breakIndex >= hardness)
		{
			this.breakIndex = 0;
			CorePacketHandler.INSTANCE.sendToAllAround(new MessageBlockRenderUpdate(block, x, y, z, 10), world.provider.dimensionId, x, y, z, 10D);
			this.increase = true;
			int meta = WorldProxy.getBlockMetadata(world, x, y, z);
			EntityPlayer player = Texasjake95Core.proxy.getTXPlayer((WorldServer) world, x, y, z).get();
			ArrayList<ItemStack> returnList = TileEntityFarm.getHarvests(player, world, x, y, z, block, meta);
			double d = World.MAX_ENTITY_RADIUS;
			@SuppressWarnings("unchecked")
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - d, y - d, z - d, x + d, y + d, z + d));
			for (EntityItem item : items)
			{
				returnList.add(item.getEntityItem());
				item.setDead();
			}
			for (ItemStack stack : returnList)
			{
				InventoryHelper.addToInventory(tile, stack);
			}
			block = WorldProxy.getBlock(world, x, y - 1, z);
			if (block == Blocks.bedrock)
			{
				this.resetHeight = true;
			}
		}
		else
		{
			this.breakIndex += .25F;
			CorePacketHandler.INSTANCE.sendToAllAround(new MessageBlockRenderUpdate(block, x, y, z, MathHelper.clamp_int(Math.round((this.breakIndex / hardness) * 10), 0, 10)), world.provider.dimensionId, x, y, z, 10D);
		}
	}
	
	@Override
	protected void incrementLoc()
	{
		if (this.resetHeight)
		{
			this.height = 0;
			this.resetHeight = false;
			this.increase = false;
			if (this.row >= 9 && this.column >= 9)
			{
				this.isDone = true;
				return;
			}
			if (this.row >= 9)
			{
				this.row = 1;
				this.column += 1;
			}
			else
			{
				this.row += 1;
				return;
			}
		}
		else
		{
			if (this.increase)
			{
				this.height += 1;
				this.increase = false;
			}
		}
	}
	
	@Override
	protected void loadExtra(NBTTagCompound compoundTag)
	{
		this.row = compoundTag.getByte("row");
		if (this.row < 1 || 10 < this.row)
		{
			this.row = 1;
		}
		this.column = compoundTag.getByte("column");
		if (this.column < 1 || 10 < this.column)
		{
			this.column = 1;
		}
		this.height = compoundTag.getByte("height");
		this.breakIndex = compoundTag.getFloat("breakIndex");
		this.resetHeight = compoundTag.getBoolean("resetHeight");
		this.increase = compoundTag.getBoolean("increase");
		this.isDone = compoundTag.getBoolean("isDone");
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		super.readFromPacket(data, byteBuf, clazz);
		this.increase = data.readBoolean();
		this.resetHeight = data.readBoolean();
		this.breakIndex = data.readFloat();
		this.isDone = data.readBoolean();
	}
	
	@Override
	protected void saveExtra(NBTTagCompound compoundTag)
	{
		compoundTag.setBoolean("resetHeight", this.resetHeight);
		compoundTag.setBoolean("increase", this.increase);
		compoundTag.setFloat("breakIndex", this.breakIndex);
		compoundTag.setBoolean("isDone", this.isDone);
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		super.writeToPacket(dos, byteBuf, clazz);
		dos.writeBoolean(this.increase);
		dos.writeBoolean(this.resetHeight);
		dos.writeFloat(this.breakIndex);
		dos.writeBoolean(this.isDone);
	}
}
