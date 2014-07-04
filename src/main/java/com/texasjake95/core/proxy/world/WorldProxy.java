package com.texasjake95.core.proxy.world;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldProxy {

	public static Block getBlock(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z);
	}

	public static int getBlockMetadata(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	public static TileEntity getTileEntity(World world, int x, int y, int z)
	{
		return world.getTileEntity(x, y, z);
	}

	public static boolean isAirBlock(World world, int x, int y, int z)
	{
		return world.isAirBlock(x, y, z);
	}

	/**
	 * Sets the block and metadata at a given location. Args: X, Y, Z, new
	 * block, new metadata, flags. Flag 1 will cause a block update. Flag 2 will
	 * send the change to clients (you almost always want this). Flag 4 prevents
	 * the block from being re-rendered, if this is a client world. Flags can be
	 * added together.
	 */
	public static void placeBlock(World world, int x, int y, int z, Block block, int meta, int flag)
	{
		world.setBlock(x, y, z, block, meta, flag);
	}

	public static MovingObjectPosition rayTraceBlocks(World world, Vec3 vec1, Vec3 vec2)
	{
		return world.rayTraceBlocks(vec1, vec2);
	}

	public static boolean spawnEntityInWorld(World world, Entity entity)
	{
		return world.spawnEntityInWorld(entity);
	}
}
