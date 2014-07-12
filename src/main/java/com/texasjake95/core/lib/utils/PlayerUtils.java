package com.texasjake95.core.lib.utils;

import cpw.mods.fml.common.eventhandler.Event.Result;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.texasjake95.core.inventory.InvRange;

public class PlayerUtils {

	public static class Teleporto extends Teleporter {

		public Teleporto(WorldServer world)
		{
			super(world);
		}

		@Override
		public void placeInPortal(Entity entity, double x, double y, double z, float p5)
		{
			int i = MathHelper.floor_double(entity.posX);
			int j = MathHelper.floor_double(entity.posY);
			int k = MathHelper.floor_double(entity.posZ);
			// int height = this.worldServerInstance.getHeightValue(i, k)+1;
			entity.setPosition(i, j, k);
		}
	}

	public static boolean canAddToInv(EntityPlayer player, EntityItem item)
	{
		EntityItemPickupEvent event = new EntityItemPickupEvent(player, item);
		if (MinecraftForge.EVENT_BUS.post(event) || (event.hasResult() && event.getResult() == Result.DENY))
			return false;
		if (InventoryUtils.canAddToInv(new InvRange(player.inventory, 36), item.getEntityItem()))
			return true;
		return false;
	}

	public static void teleportPlayerTo(EntityPlayer entityPlayer, double x, double y, double z)
	{
		teleportPlayerTo(entityPlayer, x, y, z, entityPlayer.worldObj.provider.dimensionId);
	}

	public static void teleportPlayerTo(EntityPlayer entityPlayer, double x, double y, double z, int dimension)
	{
		World world = entityPlayer.worldObj;
		if (world.isRemote)
			return;
		if (entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
			if (dimension != world.provider.dimensionId)
			{
				WorldServer ws = player.mcServer.worldServerForDimension(dimension);
				Teleporter telep = new Teleporto(ws);
				ws.getBlock((int) x, (int) y, (int) z).getUnlocalizedName();
				player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimension, telep);
			}
			player.setPositionAndUpdate(x, y, z);
		}
	}

	public static void teleportPlayerTo(EntityPlayer entityPlayer, int dimension)
	{
		if (entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
			if (dimension != player.worldObj.provider.dimensionId)
			{
				WorldServer ws = player.mcServer.worldServerForDimension(dimension);
				ChunkCoordinates coords = ws.getSpawnPoint();
				System.out.printf("Teleporting to %d, %d, %d -- %d\n", coords.posX, coords.posY, coords.posZ, dimension);
				teleportPlayerTo(entityPlayer, coords.posX, coords.posY, coords.posZ, dimension);
			}
		}
	}
}
