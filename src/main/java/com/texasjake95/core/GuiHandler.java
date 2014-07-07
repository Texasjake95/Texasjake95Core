package com.texasjake95.core;

import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.texasjake95.core.client.gui.GuiFurnaceBase;
import com.texasjake95.core.inventory.furnace.ContainerFurnaceBase;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.TileEntityFurnaceBase;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = WorldProxy.getTileEntity(world, x, y, z);
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
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = WorldProxy.getTileEntity(world, x, y, z);
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
		}
		return null;
	}
}
