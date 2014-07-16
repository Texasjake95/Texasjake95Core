package com.texasjake95.core.client.handler;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.api.handler.IToolRegistry;
import com.texasjake95.core.config.CoreConfig;
import com.texasjake95.core.proxy.entity.player.EntityPlayerProxy;
import com.texasjake95.core.proxy.item.ItemProxy;
import com.texasjake95.core.proxy.world.WorldProxy;

/**
 * This class holds the data for {@link AutoSwitchHandler}. <br>
 * It only holds data relevant in discovering which slot holds the most efficient item against
 * the block the player is breaking
 *
 * @author Texasjake95
 *
 */
public class AutoHandlerData {

	public float bestFloat = 0;
	public int bestSlot = 0;
	public Block block;
	public int blockHardness;
	public int blockMeta;
	public int hotBarSize;
	public int lowestHarvestLevel;
	public String tool;
	public int x, y, z;

	public AutoHandlerData()
	{
	}

	public AutoHandlerData(World world, MovingObjectPosition mop)
	{
		this.x = mop.blockX;
		this.y = mop.blockY;
		this.z = mop.blockZ;
		this.block = WorldProxy.getBlock(world, this.x, this.y, this.z);
		this.blockMeta = WorldProxy.getBlockMetadata(world, this.x, this.y, this.z);
		this.tool = this.block.getHarvestTool(this.blockMeta);
		this.lowestHarvestLevel = Integer.MAX_VALUE;
	}

	public void addSlots(int slot, ArrayList<Integer> bestSlots, IToolRegistry tools, int currentHarvest, float strVsBlock, ItemStack stack)
	{
		if (tools.canHarvest(this.block, this.blockMeta, stack))
			if (!CoreConfig.getInstance().useBestTool && currentHarvest == this.lowestHarvestLevel && strVsBlock == this.bestFloat)
			{
				if (currentHarvest == -1)
					return;
				if (Texasjake95Core.isTesting)
					System.out.println("Adding slot " + slot);
				bestSlots.add(slot);
			}
			else if (CoreConfig.getInstance().useBestTool && strVsBlock == this.bestFloat)
			{
				if (Texasjake95Core.isTesting)
					System.out.println("Adding slot " + slot);
				bestSlots.add(slot);
			}
	}

	public boolean canToolHarvest(IToolRegistry tools, ItemStack stack)
	{
		return tools.canHarvest(this.block, this.blockMeta, stack);
	}

	public void checkAndUpdate(int slot, int currentHarvest, float strVsBlock)
	{
		if (!CoreConfig.getInstance().useBestTool && currentHarvest <= this.lowestHarvestLevel)
		{
			if (currentHarvest == -1)
				return;
			if (this.bestFloat < strVsBlock)
			{
				this.bestFloat = strVsBlock;
				this.bestSlot = slot;
			}
			if (currentHarvest < this.lowestHarvestLevel)
			{
				this.bestFloat = strVsBlock;
				this.bestSlot = slot;
				this.lowestHarvestLevel = currentHarvest;
				return;
			}
			if (currentHarvest == this.lowestHarvestLevel)
			{
				this.bestSlot = slot;
				return;
			}
			this.bestFloat = strVsBlock;
			this.bestSlot = slot;
			this.lowestHarvestLevel = currentHarvest;
			if (Texasjake95Core.isTesting)
				System.out.println("Best Item at " + this.bestSlot + " with strength of " + this.bestFloat);
		}
		else if (CoreConfig.getInstance().useBestTool && this.bestFloat < strVsBlock)
		{
			this.bestFloat = strVsBlock;
			this.bestSlot = slot;
			if (Texasjake95Core.isTesting)
				System.out.println("Best Item at " + this.bestSlot + " with strength of " + this.bestFloat);
		}
	}

	public void clear()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.block = null;
		this.blockMeta = 0;
		this.hotBarSize = 0;
		this.tool = null;
		this.blockHardness = 0;
		this.lowestHarvestLevel = 0;
		this.bestFloat = 0;
		this.bestSlot = 0;
	}

	public float getBreakSpeed(EntityClientPlayerMP player)
	{
		return EntityPlayerProxy.getBreakSpeed(player, this.block, true, this.blockMeta, this.x, this.y, this.z);
	}

	public int getHarvestLevel(ItemStack stack)
	{
		return stack == null ? -1 : ItemProxy.getHarvestLevel(stack, this.tool);
	}

	public void reset(World world, MovingObjectPosition mop)
	{
		this.x = mop.blockX;
		this.y = mop.blockY;
		this.z = mop.blockZ;
		this.block = world.getBlock(this.x, this.y, this.z);
		this.blockMeta = world.getBlockMetadata(this.x, this.y, this.z);
		this.hotBarSize = InventoryPlayer.getHotbarSize();
		this.tool = this.block.getHarvestTool(this.blockMeta);
		this.blockHardness = this.block.getHarvestLevel(this.blockMeta);
		this.lowestHarvestLevel = Integer.MAX_VALUE;
		this.bestFloat = 0;
		this.bestSlot = 0;
	}
}
