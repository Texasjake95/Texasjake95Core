package com.texasjake95.core;

import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.texasjake95.core.client.gui.GuiFurnaceBase;
import com.texasjake95.core.inventory.furnace.ContainerFurnaceBase;
import com.texasjake95.core.tile.TileEntityFurnaceBase;

/**
 * {@link Texasjake95Core}'s implementation of {@link IGuiHandler}.
 *
 * @author Texasjake95
 *
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		switch (ID)
		{
			case 0:
			{
				if (tile instanceof TileEntityFurnaceBase)
				{
					TileEntityFurnaceBase furnace = (TileEntityFurnaceBase) tile;
					return new GuiFurnaceBase(player.inventory, furnace.furnace);
				}
			}
			default:
				break;
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		switch (ID)
		{
			case 0:
			{
				if (tile instanceof TileEntityFurnaceBase)
				{
					TileEntityFurnaceBase furnace = (TileEntityFurnaceBase) tile;
					return new ContainerFurnaceBase(player.inventory, furnace.furnace);
				}
			}
			default:
				break;
		}
		return null;
	}
}
