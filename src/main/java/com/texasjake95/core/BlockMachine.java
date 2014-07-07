package com.texasjake95.core;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraftforge.common.util.ForgeDirection;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.lib.utils.InventoryUtils;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.FurnaceTest;
import com.texasjake95.core.tile.TileEntityFarm;
import com.texasjake95.core.tile.TileEntityQuarry;

public class BlockMachine extends Block implements ITileEntityProvider {

	private IIcon side;
	private IIcon top;
	private final Random rand = new Random();

	public BlockMachine()
	{
		super(Material.wood);
		this.setHardness(.5F);
		this.setBlockName("txfarm");
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntity tile = WorldProxy.getTileEntity(world, x, y, z);
		boolean isValid = false;
		if (tile instanceof IInventory)
		{
			IInventory inv = (IInventory) tile;
			InventoryUtils.explodeInventory(inv, this.rand, world, x, y, z);
			isValid = true;
		}
		if (tile instanceof TileEntityFarm)
		{
			TileEntityFarm farm = (TileEntityFarm) tile;
			farm.getSeedInv().dropItemStacks(world, x, y, z, this.rand);
			isValid = true;
		}
		if (isValid)
		{
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		switch (meta)
		{
			case 1:
				return new TileEntityQuarry();
			case 2:
				return new FurnaceTest();
		}
		return new TileEntityFarm();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		switch (side)
		{
			case 2:
			case 3:
			case 4:
			case 5:
				return this.side;
			case 0:
			case 1:
				return this.top;
		}
		return this.side;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		switch (side)
		{
			case DOWN:
				return false;
			case EAST:
				break;
			case NORTH:
				break;
			case SOUTH:
				break;
			case UNKNOWN:
				break;
			case UP:
				break;
			case WEST:
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntity tile = WorldProxy.getTileEntity(world, x, y, z);
		if (!world.isRemote)
			if (tile instanceof FurnaceTest)
			{
				((FurnaceTest) tile).printInv();
			}
		return false;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIconRegister)
	{
		this.side = iIconRegister.registerIcon(CoreInfo.modId + ":farm_side");
		this.top = iIconRegister.registerIcon(CoreInfo.modId + ":farm_top");
	}
}
