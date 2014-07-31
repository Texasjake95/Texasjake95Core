package com.texasjake95.core;

import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.chunkloading.TicketHelper;
import com.texasjake95.core.config.CoreConfig;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.FMPValueProvider;
import com.texasjake95.core.data.thread.FluidDiscoveryThread;
import com.texasjake95.core.data.thread.ItemDiscoveryThread;
import com.texasjake95.core.data.thread.OreDiscoveryThread;
import com.texasjake95.core.data.thread.RecipeDiscoveryThread;
import com.texasjake95.core.data.thread.ValueDumpThread;
import com.texasjake95.core.lib.MappingHelper;
import com.texasjake95.core.recipe.ShapelessDamageRecipe;

/**
 * The Main Class for the mod known as Texasjake95 - Core. <br>
 *
 * @author Texasjake95
 *
 */
@Mod(modid = CoreInfo.modId, name = CoreInfo.modName, dependencies = "", version = CoreInfo.modVersion, guiFactory = "com.texasjake95.core.config.client.GuiConfigFactory")
public class Texasjake95Core {

	/**
	 * The Instance of the Mod.
	 */
	@Instance(CoreInfo.modId)
	public static Texasjake95Core INSTANCE;
	/**
	 * A constant that enables printing of debuging information.
	 */
	public static final boolean isTesting = false;
	/**
	 * The proxy used by the mod.
	 */
	@SidedProxy(clientSide = "com.texasjake95.core.client.TxCoreClientProxy", serverSide = "com.texasjake95.core.TxCoreCommonProxy")
	public static TxCoreCommonProxy proxy;

	public Texasjake95Core()
	{
		CoreConfig.getInstance();
		ValueDumpThread.startValueDump();
	}

	@EventHandler
	public void fingerprint(FMLFingerprintViolationEvent event)
	{
	}

	@EventHandler
	public void fixMapping(FMLMissingMappingsEvent event)
	{
		for (MissingMapping mapping : event.get())
			if (mapping.name.equals(CoreInfo.modId + ":FarmBlock"))
				MappingHelper.remapBlock(mapping, TxCoreCommonProxy.machine);
	}

	@EventHandler
	public void imc(IMCEvent event)
	{
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Item multiItem = GameRegistry.findItem("ForgeMicroblock", "microblock");
		DataMapWrapper.addProvider(multiItem, new FMPValueProvider());
		ItemDiscoveryThread.startItemDiscovery();
		FluidDiscoveryThread.startFluidDiscovery();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		RecipeDiscoveryThread.startRecipeDiscovery();
		OreDiscoveryThread.startOreDiscovery();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CoreConfig.getInstance().initProps();
		Iterator<?> iterator = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
		while (iterator.hasNext())
		{
			Object object = iterator.next();
			if (object instanceof Entry)
			{
				Entry<?, ?> entry = (Entry<?, ?>) object;
				if (entry.getKey() instanceof ItemStack && entry.getValue() instanceof ItemStack)
				{
					ItemStack input = (ItemStack) entry.getKey();
					ItemStack output = (ItemStack) entry.getValue();
					if (OreDictionary.itemMatches(new ItemStack(Blocks.redstone_ore, 1, OreDictionary.WILDCARD_VALUE), input, true))
						if (OreDictionary.itemMatches(new ItemStack(Items.redstone, 1, 0), output, true))
							if (1 == output.stackSize)
								iterator.remove();
					if (OreDictionary.itemMatches(new ItemStack(Blocks.lapis_ore, 1, OreDictionary.WILDCARD_VALUE), input, true))
						if (OreDictionary.itemMatches(new ItemStack(Items.dye, 1, 4), output, true))
							if (1 == output.stackSize)
								iterator.remove();
				}
			}
		}
		FurnaceRecipes.smelting().func_151393_a(Blocks.redstone_ore, new ItemStack(Items.redstone, 4, 0), 4 * FurnaceRecipes.smelting().func_151398_b(new ItemStack(Blocks.redstone_ore)));
		FurnaceRecipes.smelting().func_151393_a(Blocks.lapis_ore, new ItemStack(Items.dye, 4, 4), 4 * FurnaceRecipes.smelting().func_151398_b(new ItemStack(Blocks.lapis_ore)));
		RecipeSorter.register("texasjake95:shapelessDamage", ShapelessDamageRecipe.class, Category.SHAPELESS, "after:minecraft:shaped after:minecraft:shapeless");
		proxy.registerEventHandlers();
		proxy.initItemsAndBlocks();
		proxy.registerRecipes();
		TicketHelper.registerChunkLoading(INSTANCE, CoreInfo.modId);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}

	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event)
	{
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
	}
}
