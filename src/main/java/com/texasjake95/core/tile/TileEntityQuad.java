package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityQuad<E extends TileEntityCore, T extends Quadrant<E>> extends TileEntityInv {

	private final ArrayList<T> list = Lists.newArrayList();

	public TileEntityQuad(int size)
	{
		super(size);
	}

	public TileEntityQuad(int size, int limit)
	{
		super(size, limit);
	}

	protected final void addQuad(T quad)
	{
		if (!this.list.isEmpty())
			for (T inter : this.list)
				if (inter.northSouth == quad.northSouth)
					if (inter.eastWest == quad.eastWest)
						if (inter.upDown == quad.upDown)
							return;
		this.list.add(quad);
	}

	protected final ImmutableList<T> getQuadCount()
	{
		return ImmutableList.copyOf(this.list);
	}

	@Override
	protected void load(NBTTagCompound nbtTagCompound)
	{
		super.load(nbtTagCompound);
		for (int i = 0; i < this.list.size(); i++)
		{
			T quad = this.list.get(i);
			quad.load(nbtTagCompound.getCompoundTag("quad"));
		}
	}

	protected final boolean quadAreValid()
	{
		for (T quad : this.getQuadCount())
			if (quad.isValid())
				return true;
		return false;
	}

	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		super.readFromPacket(data, byteBuf, clazz);
		for (T quad : this.getQuadCount())
			quad.readFromPacket(data, byteBuf, clazz);
	}

	@SuppressWarnings("unchecked")
	protected final void runQuads()
	{
		for (T quad : this.getQuadCount())
			quad.run(this.worldObj, this.xCoord, this.yCoord, this.zCoord, (E) this);
	}

	@Override
	protected void save(NBTTagCompound nbtTagCompound)
	{
		super.save(nbtTagCompound);
		NBTTagCompound compound = new NBTTagCompound();
		for (int i = 0; i < this.list.size(); i++)
		{
			T quad = this.list.get(i);
			quad.save(compound);
			nbtTagCompound.setTag("quad", compound);
			compound = new NBTTagCompound();
		}
	}

	protected final void validateAndRunQuads(boolean run)
	{
		this.validateQuads();
		if (run)
			this.runQuads();
	}

	protected final void validateQuads()
	{
		for (T quad : this.getQuadCount())
			quad.validate(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		super.writeToPacket(dos, byteBuf, clazz);
		for (T quad : this.getQuadCount())
			quad.writeToPacket(dos, byteBuf, clazz);
	}
}
