package com.texasjake95.core;

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

import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.chunkloading.TicketHelper;
import com.texasjake95.core.config.CoreConfig;
import com.texasjake95.core.lib.MappingHelper;
import com.texasjake95.core.proxy.entity.EntityLivingProxy;
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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CoreConfig.getInstance().initProps();
		RecipeSorter.register("texasjake95:shapelessDamage", ShapelessDamageRecipe.class, Category.SHAPELESS, "after:minecraft:shaped after:minecraft:shapeless");
		proxy.registerEventHandlers();
		proxy.initItemsAndBlocks();
		proxy.registerRecipes();
		TicketHelper.registerChunkLoading(INSTANCE, CoreInfo.modId);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		EntityLivingProxy.getPostion(null, 0);
	}

	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
	}

	@EventHandler
	public void serverAboutToStart(FMLServerStartedEvent event)
	{
	}

	@EventHandler
	public void serverAboutToStart(FMLServerStartingEvent event)
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
