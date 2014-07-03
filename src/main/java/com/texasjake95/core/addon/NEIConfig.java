package com.texasjake95.core.addon;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.TxCoreCommonProxy;
import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.items.CoreItems;
import com.texasjake95.core.items.ItemToolBase;

public class NEIConfig implements IConfigureNEI {
	
	@Override
	public void loadConfig()
	{
		API.registerRecipeHandler(new ShapelessDamageRecipeHandler());
		API.registerUsageHandler(new ShapelessDamageRecipeHandler());
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Pickaxes", new ItemFilter() {
			
			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
				{
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("pickaxe"))
							return true;
				}
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Axes", new ItemFilter() {
			
			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
				{
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("axe"))
							return true;
				}
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Shovels", new ItemFilter() {
			
			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
				{
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("shovel"))
							return true;
				}
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Swords", new ItemFilter() {
			
			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
				{
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && ((ItemToolBase) stack.getItem()).isSword())
							return true;
				}
				return false;
			}
		});
	}
	
	@Override
	public String getName()
	{
		return "Texasjake95-Core NEI Config";
	}
	
	@Override
	public String getVersion()
	{
		return CoreInfo.modVersion;
	}
}
