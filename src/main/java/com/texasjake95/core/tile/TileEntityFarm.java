package com.texasjake95.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import com.texasjake95.core.inventory.InventoryBase;
import com.texasjake95.core.lib.helper.InventoryHelper;
import com.texasjake95.core.lib.pair.ItemIntPair;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageTileFarm;
import com.texasjake95.core.proxy.inventory.IInventoryProxy;
import com.texasjake95.core.proxy.item.ItemStackProxy;
import com.texasjake95.core.proxy.world.WorldProxy;
import com.texasjake95.core.tile.farm.IGrowthChecker;
import com.texasjake95.core.tile.farm.IHarvester;
import com.texasjake95.core.tile.farm.IHarvester.MachineType;
import com.texasjake95.core.tile.farm.ISeedProvider;
import com.texasjake95.core.tile.farm.InventorySeed;
import com.texasjake95.core.tile.farm.QuadrantFarm;
import com.texasjake95.core.tile.farm.VanillaChecker;

public class TileEntityFarm extends TileEntityCore implements IInventory {
	
	public static ArrayList<ItemStack> getHarvests(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
	{
		IHarvester harvest = harvestRegistry.get(block);
		if (harvest != null)
			return harvest.getDrops(player, world, x, y, z, block, meta, MachineType.FARM);
		else
			return getNormalDrops(player, world, x, y, z, block, meta);
	}
	
	private InventoryBase inv = new InventoryBase(10);
	private InventorySeed seedInv = new InventorySeed(10);
	private static HashMap<Block, HashMap<Integer, ItemIntPair>> seedRegistry = Maps.newHashMap();
	private static HashMap<Block, IGrowthChecker> growthRegistry = Maps.newHashMap();
	private static HashMap<Block, IHarvester> harvestRegistry = Maps.newHashMap();
	private static HashMap<Block, ISeedProvider> seedProRegistry = Maps.newHashMap();
	private static HashSet<ISeedProvider> seedSet = Sets.newHashSet();
	private static HashMap<Item, HashSet<Integer>> seeds = Maps.newHashMap();
	private static VanillaChecker vanillaChecker = new VanillaChecker();
	private QuadrantFarm NW = new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.NORTH);
	private QuadrantFarm NE = new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.NORTH);
	private QuadrantFarm SW = new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.SOUTH);
	private QuadrantFarm SE = new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH);;
	private int syncTicks = 0;
	static
	{
		registerSeed(Blocks.wheat, 7, Items.wheat_seeds, 0);
		registerSeed(Blocks.potatoes, 7, Items.potato, 0);
		registerSeed(Blocks.carrots, 7, Items.carrot, 0);
		registerSeed(Blocks.cactus, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.reeds, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.pumpkin, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.melon_block, vanillaChecker, vanillaChecker, vanillaChecker);
		registerSeed(Blocks.nether_wart, 3, Items.nether_wart, 0);
	}
	
	public static ArrayList<ItemStack> getNormalDrops(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
	{
		ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();
		ArrayList<ItemStack> dropsList = block.getDrops(world, x, y, z, meta, 0);
		float dropChance = ForgeEventFactory.fireBlockHarvesting(dropsList, world, block, x, y, z, meta, 0, 1.0F, false, player);
		for (ItemStack s : dropsList)
		{
			if (world.rand.nextFloat() <= dropChance)
			{
				returnList.add(s);
			}
		}
		world.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
		world.setBlockToAir(x, y, z);
		return returnList;
	}
	
	public static ItemIntPair getSeed(Block block, int meta)
	{
		ISeedProvider seed = seedProRegistry.get(block);
		if (seed != null)
		{
			ItemIntPair pair = seed.getSeed(block, meta);
			if (pair != null)
				return pair;
		}
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap == null)
			return null;
		return metaMap.get(meta);
	}
	
	public static boolean isFullGrown(Block block, int meta, World world, int x, int y, int z)
	{
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap != null && metaMap.containsKey(meta))
			return true;
		IGrowthChecker checker = growthRegistry.get(block);
		if (checker == null)
			return false;
		return checker.isGrown(block, meta, world, x, y, z);
	}
	
	public static boolean isSeed(ItemStack stack)
	{
		for (ISeedProvider seed : seedSet)
		{
			if (seed != null && seed.isSeed(ItemStackProxy.getItem(stack), ItemStackProxy.getMetadata(stack)))
				return true;
		}
		HashSet<Integer> meta = seeds.get(ItemStackProxy.getItem(stack));
		if (meta == null)
			return false;
		return meta.contains(ItemStackProxy.getMetadata(stack));
	}
	
	public static void registerSeed(Block block, IGrowthChecker growth, IHarvester harvester, ISeedProvider seed)
	{
		growthRegistry.put(block, growth);
		harvestRegistry.put(block, harvester);
		seedProRegistry.put(block, seed);
		seedSet.add(seed);
	}
	
	public static void registerSeed(Block block, int meta, Item seed, int seedMeta)
	{
		ItemIntPair pair = new ItemIntPair(seed, seedMeta);
		HashMap<Integer, ItemIntPair> metaMap = seedRegistry.get(block);
		if (metaMap == null)
		{
			metaMap = Maps.newHashMap();
		}
		metaMap.put(meta, pair);
		seedRegistry.put(block, metaMap);
		HashSet<Integer> seedList = seeds.get(seed);
		if (seedList == null)
		{
			seedList = Sets.newHashSet();
		}
		seedList.add(seedMeta);
		seeds.put(seed, seedList);
	}
	
	private void checkQuadrant(QuadrantFarm quad)
	{
		quad.validate(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public void closeInventory()
	{
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int decr)
	{
		return this.inv.decrStackSize(slot, decr);
	}
	
	private boolean empty()
	{
		for (int invSlot = 0; invSlot < IInventoryProxy.getSizeInventory(this); invSlot++)
		{
			ItemStack stack = IInventoryProxy.getStackInSlot(this, invSlot);
			if (stack != null)
				return false;
		}
		return true;
	}
	
	private IInventory getChestInv(World world, int x, int y, int z, Block block)
	{
		IInventory inv = (IInventory) WorldProxy.getTileEntity(world, x, y, z);
		if (WorldProxy.getBlock(world, x - 1, y, z) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x - 1, y, z), inv);
		}
		if (WorldProxy.getBlock(world, x + 1, y, z) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x + 1, y, z), inv);
		}
		if (WorldProxy.getBlock(world, x, y, z - 1) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x, y, z - 1), inv);
		}
		if (WorldProxy.getBlock(world, x, y, z + 1) == block)
		{
			inv = new InventoryLargeChest("container.chestDouble", (IInventory) WorldProxy.getTileEntity(world, x, y, z + 1), inv);
		}
		return inv;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CorePacketHandler.INSTANCE.getPacketFrom(new MessageTileFarm(this));
	}
	
	@Override
	public String getInventoryName()
	{
		return "tileEntity.farm.name";
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return this.inv.getInventoryStackLimit();
	}
	
	public ItemStack getItemStack(Item seed, int meta)
	{
		return this.seedInv.getStack(seed, meta);
	}
	
	public ItemStack getItemStack(ItemIntPair seed)
	{
		return this.seedInv.getStack(seed.getItem(), seed.getMeta());
	}
	
	public InventorySeed getSeedInv()
	{
		return this.seedInv;
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inv.getSizeInventory();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inv.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return this.inv.getStackInSlotOnClosing(slot);
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}
	
	@Override
	public void load(NBTTagCompound nbtTagCompound)
	{
		this.inv.readFromNBT(nbtTagCompound.getCompoundTag("inv"));
		this.seedInv.load(nbtTagCompound.getCompoundTag("seedInv"));
		this.NE.load(nbtTagCompound.getCompoundTag("NE"));
		this.NW.load(nbtTagCompound.getCompoundTag("NW"));
		this.SE.load(nbtTagCompound.getCompoundTag("SE"));
		this.SW.load(nbtTagCompound.getCompoundTag("SW"));
	}
	
	@Override
	public void openInventory()
	{
	}
	
	public void printInv()
	{
		for (int invSlot = 0; invSlot < IInventoryProxy.getSizeInventory(this); invSlot++)
		{
			ItemStack stack = IInventoryProxy.getStackInSlot(this, invSlot);
			if (stack == null)
			{
				System.out.println(invSlot + "= null");
			}
			else
			{
				System.out.println(invSlot + "= " + stack.getItem().getUnlocalizedName() + ":" + stack.getItemDamage() + ":" + stack.stackSize);
			}
		}
		System.out.println("Seeds");
		this.seedInv.printItems();
	}
	
	public void printOutActiveQuad()
	{
		if (this.NW.isValid())
		{
			System.out.println("NW");
		}
		if (this.NE.isValid())
		{
			System.out.println("NE");
		}
		if (this.SW.isValid())
		{
			System.out.println("SW");
		}
		if (this.SE.isValid())
		{
			System.out.println("SE");
		}
	}
	
	private void pushToChest()
	{
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = WorldProxy.getTileEntity(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (tile instanceof TileEntityChest)
			{
				Block chest = WorldProxy.getBlock(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
				IInventory temp = this.getChestInv(this.worldObj, this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ, chest);
				this.pushToInv(temp);
			}
			else if (tile instanceof IInventory)
			{
				IInventory temp = (IInventory) tile;
				this.pushToInv(temp);
			}
		}
	}
	
	private void pushToInv(IInventory inv)
	{
		for (int invSlot = 0; invSlot < IInventoryProxy.getSizeInventory(this); invSlot++)
		{
			ItemStack stack = IInventoryProxy.getStackInSlot(this, invSlot);
			if (stack == null)
			{
				continue;
			}
			if (stack.stackSize == 0)
			{
				IInventoryProxy.setInventorySlotContents(this, invSlot, null);
				continue;
			}
			InventoryHelper.addToInventory(inv, stack);
		}
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (data != null)
		{
			this.inv.readFromPacket(data, byteBuf);
			this.seedInv.readFromPacket(data, byteBuf, clazz);
			this.NE.readFromPacket(data, byteBuf, clazz);
			this.NW.readFromPacket(data, byteBuf, clazz);
			this.SE.readFromPacket(data, byteBuf, clazz);
			this.SW.readFromPacket(data, byteBuf, clazz);
		}
	}
	
	private void runQuadrant(QuadrantFarm quad)
	{
		quad.run(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this);
	}
	
	@Override
	public void save(NBTTagCompound nbtTagCompound)
	{
		NBTTagCompound data = new NBTTagCompound();
		this.inv.writeToNBT(data);
		nbtTagCompound.setTag("inv", data);
		data = new NBTTagCompound();
		this.seedInv.save(data);
		nbtTagCompound.setTag("seedInv", data);
		data = new NBTTagCompound();
		this.NE.save(data);
		nbtTagCompound.setTag("NE", data);
		data = new NBTTagCompound();
		this.NW.save(data);
		nbtTagCompound.setTag("NW", data);
		data = new NBTTagCompound();
		this.SE.save(data);
		nbtTagCompound.setTag("SE", data);
		data = new NBTTagCompound();
		this.SW.save(data);
		nbtTagCompound.setTag("SW", data);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inv.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (this.syncTicks++ % 200 == 0)
			{
				CorePacketHandler.INSTANCE.sendToAllAround(new MessageTileFarm(this), this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 30);
				this.syncTicks = 1;
			}
			this.checkQuadrant(this.NW);
			this.checkQuadrant(this.NE);
			this.checkQuadrant(this.SW);
			this.checkQuadrant(this.SE);
			if (this.empty())
			{
				this.runQuadrant(this.NW);
				this.runQuadrant(this.NE);
				this.runQuadrant(this.SW);
				this.runQuadrant(this.SE);
			}
			this.pushToChest();
		}
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (dos != null)
		{
			this.inv.writeToPacket(dos, byteBuf);
			this.seedInv.writeToPacket(dos, byteBuf, clazz);
			this.NE.writeToPacket(dos, byteBuf, clazz);
			this.NW.writeToPacket(dos, byteBuf, clazz);
			this.SE.writeToPacket(dos, byteBuf, clazz);
			this.SW.writeToPacket(dos, byteBuf, clazz);
		}
	}
}
