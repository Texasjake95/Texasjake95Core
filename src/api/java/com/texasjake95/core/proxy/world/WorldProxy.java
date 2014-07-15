package com.texasjake95.core.proxy.world;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * This class is used as a proxy to call code for Minecraft's {@link World} due to the nature
 * of the changing names of methods.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change
 *
 * @author Texasjake95
 *
 */
public class WorldProxy {

	/**
	 * Get the {@link Block} located at the requested location.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the {@link Block} located at the requested location
	 */
	public static Block getBlock(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z);
	}

	/**
	 * Get the metadata of the {@link Block} located at the requested location.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the metadata of the {@link Block} located at the requested location
	 */
	public static int getBlockMetadata(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	/**
	 * Get the {@link TileEntity} located at the requested location.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the {@link TileEntity} located at the requested location
	 */
	public static TileEntity getTileEntity(World world, int x, int y, int z)
	{
		return world.getTileEntity(x, y, z);
	}

	/**
	 * Returns true if the block at the requested location is empty or is considered an air.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return true if the {@link Block} located at the requested location is empty or is
	 *         considered an air block
	 */
	public static boolean isAirBlock(World world, int x, int y, int z)
	{
		return world.isAirBlock(x, y, z);
	}

	/**
	 * Used to see if one of the blocks next to you or your block is getting power from a
	 * neighboring block. Used by items like TNT or Doors so they don't have redstone going
	 * straight into them.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return true is the {@link Block} is receiving indirect power from any form of redstone
	 */
	public static boolean isBlockIndirectlyGettingPowered(World world, int x, int y, int z)
	{
		return world.isBlockIndirectlyGettingPowered(x, y, z);
	}

	/**
	 *
	 * Sets the block and metadata at a given location.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param block
	 *            the {@link Block} being placed
	 * @param meta
	 *            the meta of the {@link Block} being placed
	 * @param flag
	 *            1 will cause a block update<br>
	 *            2 will send the change to clients (you almost always want this)<br>
	 *            4 prevents the block from being re-rendered, if this is a client world<br>
	 *            Flags can be added together.
	 */
	public static void placeBlock(World world, int x, int y, int z, Block block, int meta, int flag)
	{
		world.setBlock(x, y, z, block, meta, flag);
	}

	/**
	 * Performs a raytrace against all blocks in the {@link World} except liquids.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param vec1
	 *            ''
	 * @param vec2
	 *            ''
	 * @return The {@link MovingObjectPosition} where the {@link Vec3}s collided
	 */
	public static MovingObjectPosition rayTraceBlocks(World world, Vec3 vec1, Vec3 vec2)
	{
		return world.rayTraceBlocks(vec1, vec2);
	}

	/**
	 * Attempt to spawn the {@link Entity} in to the {@link World}.
	 *
	 * @param world
	 *            the instance of the {@link World}
	 * @param entity
	 *            the {@link Entity} attempting to spawn
	 * @return true if the {@link Entity} was successfully spawned into the {@link World}
	 */
	public static boolean spawnEntityInWorld(World world, Entity entity)
	{
		return world.spawnEntityInWorld(entity);
	}
}
