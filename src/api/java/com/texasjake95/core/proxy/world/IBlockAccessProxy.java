package com.texasjake95.core.proxy.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * This class is used as a proxy to call code for Minecraft's {@link IBlockAccess} due to the
 * nature of the changing names of methods.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
public class IBlockAccessProxy {

	/**
	 * Retrieves the {@link BiomeGenBase} for a given set of x/z coordinates.
	 *
	 * @param access
	 * @param x
	 * @param z
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static BiomeGenBase getBiomeGenForCoords(IBlockAccess access, int x, int z)
	{
		MinecraftServer.getServer().initiateShutdown();
		return access.getBiomeGenForCoords(x, z);
	}

	/**
	 * Get the {@link Block} located at the requested location.
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the {@link Block} located at the requested location
	 */
	public static Block getBlock(IBlockAccess access, int x, int y, int z)
	{
		return access.getBlock(x, y, z);
	}

	/**
	 * Get the metadata of the {@link Block} located at the requested location.
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the metadata of the {@link Block} located at the requested location
	 */
	public static int getBlockMetadata(IBlockAccess access, int x, int y, int z)
	{
		return access.getBlockMetadata(x, y, z);
	}

	/**
	 * .
	 *
	 * @param access
	 * @param x
	 * @param y
	 * @param z
	 * @param lightValue
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static int getLightBrightnessForSkyBlocks(IBlockAccess access, int x, int y, int z, int lightValue)
	{
		return access.getLightBrightnessForSkyBlocks(x, y, z, lightValue);
	}

	/**
	 * Get the {@link TileEntity} located at the requested location.
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return the {@link TileEntity} located at the requested location
	 */
	public static TileEntity getTileEntity(IBlockAccess access, int x, int y, int z)
	{
		return access.getTileEntity(x, y, z);
	}

	/**
	 * Returns true if the block at the requested location is empty or is considered an air.
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @return true if the {@link Block} located at the requested location is empty or is
	 *         considered an air block
	 */
	public static boolean isAirBlock(IBlockAccess access, int x, int y, int z)
	{
		return access.isAirBlock(x, y, z);
	}

	/**
	 * Is this block powering in the specified direction.
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param direction
	 *            the direction of the block
	 * @return the amount of power from 0 to 15
	 */
	public static int isBlockProvidingPowerTo(IBlockAccess access, int x, int y, int z, int direction)
	{
		return access.isBlockProvidingPowerTo(x, y, z, direction);
	}

	/**
	 * Returns current world height.
	 */
	/*
	 * @SideOnly(Side.CLIENT) public static int getHeight(){}
	 */
	/**
	 * set by !chunk.getAreLevelsEmpty
	 */
	/*
	 * @SideOnly(Side.CLIENT) public static boolean extendedLevelsInChunkCache(){}
	 */
	/**
	 * Is the {@link Block} located at the specified location solid on the passed side. <br>
	 * FORGE PATCH
	 *
	 *
	 * @param access
	 *            the instance of the {@link IBlockAccess}
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param side
	 *            the side to check
	 * @param defaultValue
	 *            default return value
	 * @return true if the {@link Block} is solid on the side
	 */
	public static boolean isSideSolid(IBlockAccess access, int x, int y, int z, ForgeDirection side, boolean defaultValue)
	{
		return access.isSideSolid(x, y, z, side, defaultValue);
	}
}
