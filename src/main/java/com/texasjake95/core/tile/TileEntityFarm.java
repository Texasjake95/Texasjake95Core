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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

import com.texasjake95.core.lib.pair.ItemIntPair;
import com.texasjake95.core.network.CorePacketHandler;
import com.texasjake95.core.network.message.MessageTileFarm;
import com.texasjake95.core.proxy.item.ItemStackProxy;
import com.texasjake95.core.tile.farm.IGrowthChecker;
import com.texasjake95.core.tile.farm.IHarvester;
import com.texasjake95.core.tile.farm.IHarvester.MachineType;
import com.texasjake95.core.tile.farm.ISeedProvider;
import com.texasjake95.core.tile.farm.InventorySeed;
import com.texasjake95.core.tile.farm.QuadrantFarm;
import com.texasjake95.core.tile.farm.VanillaChecker;

public class TileEntityFarm extends TileEntityQuad<TileEntityFarm, QuadrantFarm> {
	
	public static ArrayList<ItemStack> getHarvests(EntityPlayer player, World world, int x, int y, int z, Block block, int meta)
	{
		IHarvester harvest = harvestRegistry.get(block);
		if (harvest != null)
			return harvest.getDrops(player, world, x, y, z, block, meta, MachineType.FARM);
		else
			return getNormalDrops(player, world, x, y, z, block, meta);
	}
	
	private InventorySeed seedInv = new InventorySeed(10);
	private static HashMap<Block, HashMap<Integer, ItemIntPair>> seedRegistry = Maps.newHashMap();
	private static HashMap<Block, IGrowthChecker> growthRegistry = Maps.newHashMap();
	private static HashMap<Block, IHarvester> harvestRegistry = Maps.newHashMap();
	private static HashMap<Block, ISeedProvider> seedProRegistry = Maps.newHashMap();
	private static HashSet<ISeedProvider> seedSet = Sets.newHashSet();
	private static HashMap<Item, HashSet<Integer>> seeds = Maps.newHashMap();
	private static VanillaChecker vanillaChecker = new VanillaChecker();
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
	
	public TileEntityFarm()
	{
		super(10);
		this.addQuad(new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.NORTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.NORTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.SOUTH));
		this.addQuad(new QuadrantFarm(ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH));
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return CorePacketHandler.INSTANCE.getPacketFrom(new MessageTileFarm(this));
	}
	
	@Override
	public String getInventoryName()
	{
		return "tileEntity.txFarm.name";
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
	public void load(NBTTagCompound nbtTagCompound)
	{
		super.load(nbtTagCompound);
		this.seedInv.load(nbtTagCompound.getCompoundTag("seedInv"));
	}
	
	@Override
	public void readFromPacket(ByteBufInputStream data, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (data != null)
		{
			super.readFromPacket(data, byteBuf, clazz);
			this.seedInv.readFromPacket(data, byteBuf, clazz);
		}
	}
	
	@Override
	public void save(NBTTagCompound nbtTagCompound)
	{
		super.save(nbtTagCompound);
		NBTTagCompound data = new NBTTagCompound();
		this.seedInv.save(data);
		nbtTagCompound.setTag("seedInv", data);
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
			this.validateAndRunQuads(this.empty());
			this.pushToChest();
		}
	}
	
	@Override
	public void writeToPacket(ByteBufOutputStream dos, ByteBuf byteBuf, Class<? extends IMessage> clazz) throws IOException
	{
		if (dos != null)
		{
			super.writeToPacket(dos, byteBuf, clazz);
			this.seedInv.writeToPacket(dos, byteBuf, clazz);
		}
	}
}
