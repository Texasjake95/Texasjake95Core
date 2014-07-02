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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.tile.TileEntityFarm;
import com.texasjake95.core.tile.TileEntityQuarry;

public class BlockFarm extends Block implements ITileEntityProvider {
	
	private IIcon side;
	private IIcon top;
	private final Random rand = new Random();
	
	public BlockFarm()
	{
		super(Material.wood);
		this.setHardness(.5F);
		this.setBlockName("txfarm");
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		switch (meta)
		{
			case 1:
				return new TileEntityQuarry();
		}
		return new TileEntityFarm();
	}
	
	@SuppressWarnings("rawtypes")
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.5F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public int quantityDropped(Random rand)
	{
		return 1;
	}
	
	public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_)
	{
		return Item.getItemFromBlock(this);
	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityFarm)
		{
			TileEntityFarm farm = (TileEntityFarm) tile;
			for (int slot = 0; slot < farm.getSizeInventory(); ++slot)
			{
				ItemStack itemstack = farm.getStackInSlot(slot);
				if (itemstack != null)
				{
					float xChange = this.rand.nextFloat() * 0.8F + 0.1F;
					float yChange = this.rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;
					for (float zChange = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
					{
						int dropSize = this.rand.nextInt(21) + 10;
						if (dropSize > itemstack.stackSize)
						{
							dropSize = itemstack.stackSize;
						}
						itemstack.stackSize -= dropSize;
						entityitem = new EntityItem(world, (double) ((float) x + xChange), (double) ((float) y + yChange), (double) ((float) z + zChange), new ItemStack(itemstack.getItem(), dropSize, itemstack.getItemDamage()));
						entityitem.motionX = (double) ((float) this.rand.nextGaussian() * 0.05F);
						entityitem.motionY = (double) ((float) this.rand.nextGaussian() * 0.05F + 0.2F);
						entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * 0.05F);
						if (itemstack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}
					}
				}
			}
			farm.getSeedInv().dropItemStacks(world, x, y, z, rand);
			world.func_147453_f(x, y, z, block);
		}
		if (tile instanceof TileEntityQuarry)
		{
			TileEntityQuarry quarry = (TileEntityQuarry) tile;
			for (int slot = 0; slot < quarry.getSizeInventory(); ++slot)
			{
				ItemStack itemstack = quarry.getStackInSlot(slot);
				if (itemstack != null)
				{
					float xChange = this.rand.nextFloat() * 0.8F + 0.1F;
					float yChange = this.rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;
					for (float zChange = this.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
					{
						int dropSize = this.rand.nextInt(21) + 10;
						if (dropSize > itemstack.stackSize)
						{
							dropSize = itemstack.stackSize;
						}
						itemstack.stackSize -= dropSize;
						entityitem = new EntityItem(world, (double) ((float) x + xChange), (double) ((float) y + yChange), (double) ((float) z + zChange), new ItemStack(itemstack.getItem(), dropSize, itemstack.getItemDamage()));
						entityitem.motionX = (double) ((float) this.rand.nextGaussian() * 0.05F);
						entityitem.motionY = (double) ((float) this.rand.nextGaussian() * 0.05F + 0.2F);
						entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * 0.05F);
						if (itemstack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}
					}
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	
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
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iIconRegister)
	{
		this.side = iIconRegister.registerIcon(CoreInfo.modId + ":farm_side");
		this.top = iIconRegister.registerIcon(CoreInfo.modId + ":farm_top");
	}
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
	{
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
	}
}
