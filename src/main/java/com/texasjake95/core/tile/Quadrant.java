package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import com.texasjake95.commons.helpers.Checker;
import com.texasjake95.core.network.IPacketHandler;

public abstract class Quadrant<T extends TileEntityCore> implements IPacketHandler {

	protected ForgeDirection northSouth;
	protected ForgeDirection eastWest;
	protected ForgeDirection upDown;
	protected boolean valid;
	protected byte check = 0;
	protected byte row = 1;
	protected byte height = 0;
	protected byte column = 1;

	public Quadrant(ForgeDirection eastWest, ForgeDirection upDown, ForgeDirection northSouth)
	{
		if (!Checker.doAnyMatch(northSouth, ForgeDirection.NORTH, ForgeDirection.SOUTH))
			throw new IllegalArgumentException("The pramater \"northSouth\" must be NORTH or SOUTH");
		this.northSouth = northSouth;
		if (!Checker.doAnyMatch(eastWest, ForgeDirection.EAST, ForgeDirection.WEST))
			throw new IllegalArgumentException("The pramater \"eastWest\" must be EAST or WEST");
		this.eastWest = eastWest;
		if (!Checker.doAnyMatch(upDown, ForgeDirection.UP, ForgeDirection.DOWN))
			throw new IllegalArgumentException("The pramater \"upDown\" must be UP or DOWN");
		this.upDown = upDown;
	}

	protected abstract boolean _validate(World world, int x, int y, int z);

	public abstract ArrayList<ChunkCoordIntPair> getWorkingChunkCoordIntPairs(int x, int z);

	protected abstract void handleBlock(World world, int x, int y, int z, T tile);

	protected abstract void incrementLoc();

	public boolean isValid()
	{
		return this.valid;
	}

	public void load(NBTTagCompound compoundTag)
	{
		this.loadExtra(compoundTag);
		this.check = compoundTag.getByte("check");
		this.valid = compoundTag.getBoolean("valid");
	}

	protected abstract void loadExtra(NBTTagCompound compoundTag);

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		this.valid = data.readBoolean();
		this.check = data.readByte();
		this.row = data.readByte();
		this.column = data.readByte();
		this.height = data.readByte();
	}

	public void run(World world, int x, int y, int z, T tile)
	{
		if (this.valid)
		{
			int offsetX = this.row * this.eastWest.offsetX;
			int offsetY = this.height * this.upDown.offsetY;
			int offsetZ = this.column * this.northSouth.offsetZ;
			int trueX = x + offsetX, trueY = y + offsetY, trueZ = z + offsetZ;
			this.handleBlock(world, trueX, trueY, trueZ, tile);
			this.incrementLoc();
		}
	}

	public void save(NBTTagCompound compoundTag)
	{
		compoundTag.setByte("row", this.row);
		compoundTag.setByte("column", this.column);
		compoundTag.setByte("height", this.height);
		compoundTag.setByte("check", this.check);
		compoundTag.setBoolean("valid", this.valid);
		this.saveExtra(compoundTag);
	}

	protected abstract void saveExtra(NBTTagCompound compoundTag);

	public void validate(World world, int x, int y, int z)
	{
		this.check += 1;
		if (this.check >= 20)
		{
			this.valid = false;
			this.check = 0;
			if (this._validate(world, x, y, z))
				this.valid = true;
		}
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		dos.writeBoolean(this.valid);
		dos.writeByte(this.check);
		dos.writeByte(this.row);
		dos.writeByte(this.column);
		dos.writeByte(this.height);
	}
}
