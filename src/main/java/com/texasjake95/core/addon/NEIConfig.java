package com.texasjake95.core.addon;

import java.util.List;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemInfo.Layout;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.texasjake95.core.Texasjake95Core;
import com.texasjake95.core.TxCoreCommonProxy;
import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.items.ItemToolBase;

/**
 * {@link Texasjake95Core}'s implementation of {@link IConfigureNEI}.
 *
 * @author Texasjake95
 *
 */
public class NEIConfig implements IConfigureNEI {

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
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("pickaxe"))
							return true;
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Axes", new ItemFilter() {

			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("axe"))
							return true;
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Shovels", new ItemFilter() {

			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && stack.getItem().getToolClasses(stack).contains("shovel"))
							return true;
				return false;
			}
		});
		API.addSubset("Mod." + CoreInfo.modName + ".Items.Multi Tools.Swords", new ItemFilter() {

			@Override
			public boolean matches(ItemStack stack)
			{
				if (GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(CoreInfo.modId))
					if (stack.getItem() != Item.getItemFromBlock(TxCoreCommonProxy.machine))
						if (stack.getItem() instanceof ItemToolBase && ((ItemToolBase) stack.getItem()).isSword())
							return true;
				return false;
			}
		});
		API.registerHighlightHandler(new IHighlightHandler() {

			@Override
			public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, Layout layout)
			{
				if (player.isSneaking())
					currenttip.add("Value: " + DataMapWrapper.getValue(itemStack));
				return currenttip;
			}

			@Override
			public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop)
			{
				Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
				int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
				return new ItemStack(block, 1, meta);
			}
		}, Layout.FOOTER);
	}
}
