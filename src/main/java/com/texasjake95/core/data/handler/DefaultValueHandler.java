package com.texasjake95.core.data.handler;

import java.io.File;
import java.util.HashSet;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Loader;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import test.DataMap.RemovalType;

import com.texasjake95.commons.util.pair.ObjectPair;
import com.texasjake95.core.OreStack;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.WrappedStack.WrappedType;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.FloatValue;
import com.texasjake95.core.data.ValueMap;

public class DefaultValueHandler {

	static class ValueHolder extends ObjectPair<WrappedStack, Float> {

		private static final long serialVersionUID = -1305364468608123882L;

		public ValueHolder(WrappedStack e, Float t)
		{
			super(e, t);
		}

		public WrappedStack getStack()
		{
			return this.getObject1();
		}

		public float getValue()
		{
			return this.getObject2();
		}
	}

	private static boolean init = false;

	private static void addValue(Object object, float value)
	{
		DataMapWrapper.addValue(object, value, ValueMap.Type.DEFAULT);
	}

	public static void init()
	{
		if (!init)
		{
			addValue(new OreStack(new ItemStack(Blocks.cobblestone)), 1);
			for (int meta = 0; meta < 16; meta++)
				addValue(new OreStack(new ItemStack(Items.dye, 1, meta)), 8);
			// addValue(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 8);
			addValue(new OreStack(new ItemStack(Blocks.log)), 32);
			addValue(new OreStack(new ItemStack(Blocks.diamond_ore)), 8192);
			addValue(new OreStack(new ItemStack(Blocks.emerald_ore)), 8192);
			addValue(new OreStack(new ItemStack(Blocks.gold_ore)), 2048);
			addValue(new OreStack(new ItemStack(Blocks.iron_ore)), 256);
			addValue(new OreStack(new ItemStack(Blocks.lapis_ore)), 864 * 4);
			addValue(new OreStack(new ItemStack(Blocks.quartz_ore)), 256);
			addValue(new OreStack(new ItemStack(Blocks.redstone_ore)), 32 * 4);
			addValue(new OreStack(new ItemStack(Blocks.planks)), 8);
			addValue(new OreStack(new ItemStack(Items.record_11)), 2048);
			addValue(new OreStack(new ItemStack(Blocks.wooden_slab)), 4);
			addValue(new OreStack(new ItemStack(Blocks.oak_stairs)), 12);
			addValue(new OreStack(new ItemStack(Items.stick)), 4);
			addValue(new OreStack(new ItemStack(Blocks.stone)), 1);
			addValue(new OreStack(new ItemStack(Blocks.leaves)), 1);
			addValue(new OreStack(new ItemStack(Blocks.sapling)), 32);
			addValue(new OreStack(new ItemStack(Blocks.sandstone)), 4);
			// Fluids
			addValue(new FluidStack(FluidRegistry.LAVA, 1), .064F);
			addValue(new FluidStack(FluidRegistry.getFluid("milk"), 1), .064F);
			addValue(new FluidStack(FluidRegistry.WATER, 1), .001F);
			/* Building Blocks */
			addValue(Blocks.stone, 1);
			addValue(Blocks.grass, 1);
			addValue(new ItemStack(Blocks.dirt, 1, OreDictionary.WILDCARD_VALUE), 1);
			addValue(Blocks.cobblestone, 1);
			// Bedrock
			addValue(new ItemStack(Blocks.sand, 1, OreDictionary.WILDCARD_VALUE), 1);
			addValue(Blocks.gravel, 4);
			addValue(Blocks.coal_ore, 32);
			// Sponge
			addValue(Blocks.glass, 1);
			addValue(Blocks.sandstone, 4);
			addValue(Blocks.mossy_cobblestone, 1);
			addValue(Blocks.obsidian, 64);
			addValue(Blocks.ice, 1);
			addValue(Blocks.pumpkin, 144);
			addValue(Blocks.netherrack, 1);
			addValue(Blocks.soul_sand, 49);
			addValue(new ItemStack(Blocks.stonebrick, 1, OreDictionary.WILDCARD_VALUE), 1);
			addValue(new ItemStack(Blocks.stonebrick, 1, 1), 1);
			addValue(Blocks.mycelium, 1);
			addValue(Blocks.end_stone, 1);
			addValue(Blocks.hardened_clay, 256);
			/* Decoration Blocks */
			addValue(Blocks.web, 12);
			addValue(new ItemStack(Blocks.tallgrass, 1, OreDictionary.WILDCARD_VALUE), 1);
			addValue(Blocks.deadbush, 1);
			addValue(Blocks.yellow_flower, 16);
			addValue(new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), 16);
			addValue(Blocks.brown_mushroom, 32);
			addValue(Blocks.red_mushroom, 32);
			addValue(Blocks.snow, 0.5f);
			addValue(Blocks.cactus, 8);
			// Stone Monster Egg
			// Cobblestone Monster Egg
			// Stone Brick Monster Egg
			addValue(Blocks.vine, 8);
			addValue(Blocks.waterlily, 16);
			// End Portal
			addValue(new ItemStack(Blocks.double_plant, 1, OreDictionary.WILDCARD_VALUE), 16);
			// Skeleton Skull
			// Wither Skeleton Skull
			// Zombie Head
			// Head
			// Creeper Head
			/* Redstone */
			// addValue(Items.redstone, 32);
			/* Transportation */
			addValue(Items.saddle, 192);
			/* Miscellaneous */
			addValue(Items.snowball, 0.25f);
			// Milk
			addValue(Items.slime_ball, 24);
			addValue(Items.bone, 24);
			addValue(Items.ender_pearl, 1024);
			// Bottle o'Enchanting
			// Firework Star
			/* Foodstuffs */
			addValue(Items.apple, 128);
			addValue(Items.porkchop, 64);
			addValue(Items.cooked_porkchop, 64);
			addValue(new ItemStack(Items.fish, 1, OreDictionary.WILDCARD_VALUE), 64);
			addValue(new ItemStack(Items.cooked_fished, 1, OreDictionary.WILDCARD_VALUE), 64);
			addValue(Items.melon, 16);
			addValue(Items.beef, 64);
			addValue(Items.cooked_beef, 64);
			addValue(Items.chicken, 64);
			addValue(Items.cooked_chicken, 64);
			addValue(Items.rotten_flesh, 24);
			addValue(Items.spider_eye, 128);
			addValue(Items.carrot, 24);
			addValue(Items.potato, 24);
			addValue(Items.baked_potato, 64);
			addValue(Items.poisonous_potato, 24);
			/* Tools */
			// Name Tag
			/* Combat */
			// Chain Helmet
			// Chain Chestplate
			// Chain Leggings
			// Chain Boots
			/* Brewing */
			addValue(Items.ghast_tear, 4096);
			/* Materials */
			addValue(new ItemStack(Items.coal, 1, 0), 32);
			addValue(new ItemStack(Items.coal, 1, 1), 32);
			addValue(Items.diamond, 8192);
			addValue(Items.string, 12);
			addValue(Items.feather, 48);
			addValue(Items.gunpowder, 192);
			addValue(Items.wheat_seeds, 16);
			addValue(Items.wheat, 24);
			addValue(Items.flint, 4);
			addValue(Items.leather, 64);
			addValue(Items.brick, 64);
			addValue(Items.clay_ball, 64);
			addValue(Items.reeds, 32);
			addValue(Items.egg, 32);
			addValue(Items.glowstone_dust, 384);
			addValue(new ItemStack(Items.dye, 1, 4), 864);
			addValue(Items.blaze_rod, 1536);
			addValue(Items.nether_wart, 24);
			addValue(Items.emerald, 8192);
			addValue(Items.nether_star, 24576);
			addValue(Items.netherbrick, 1);
			addValue(Items.quartz, 256);
			init = true;
		}
	}

	public static void initConfig()
	{
		Configuration config = new Configuration(new File(Loader.instance().getConfigDir().getAbsolutePath() + "/DataValues.cfg"));
		config.load();
		int removeType = 0;
		Property removeTypeProp = config.get(Configuration.CATEGORY_GENERAL.toLowerCase(), "Value Remove Type", removeType);
		removeType = removeTypeProp.getInt(removeType);
		if (removeType < 0)
			removeType = 0;
		if (removeType > 1)
			removeType = 1;
		int remapType = 1;
		Property remapTypeProp = config.get(Configuration.CATEGORY_GENERAL.toLowerCase(), "Value Remap Type", remapType);
		remapType = remapTypeProp.getInt(remapType);
		if (remapType < 0)
			remapType = 0;
		if (remapType > 1)
			remapType = 1;
		remapTypeProp.set(remapType);
		remapTypeProp.comment = "How to remap the values.\n0 for a \"shallow\" remap meaning just the item.\n1 for a \"deep\" remap meaning reamp the value of all items the need it to determine their own values\nNODE: a \"deep\" remap will take time to initalize";
		removeTypeProp.set(removeType);
		removeTypeProp.comment = "How to remove the values.\n0 for a \"shallow\" removal meaning just the item.\n1 for a \"deep\" removal meaning remove the value of all items the need it to determine their own values\nNODE: a \"deep\" removal will take time to initalize";
		config.addCustomCategoryComment("data", "Values for all valid stacks.\nSet to -1 to remove value.\nSet to 0 to remap the value to default if one is present");
		HashSet<WrappedStack> toRemove = Sets.newHashSet();
		HashSet<ValueHolder> toChange = Sets.newHashSet();
		while (true)
		{
			try
			{
				for (WrappedStack stack : DataMapWrapper.getAllNodes())
				{
					if (stack.type == WrappedType.ORESTACK)
						continue;
					if (stack.type == WrappedType.ITEMSTACK)
					{
						ItemStack itemStack = (ItemStack) stack.getEffectiveStack();
						if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
							continue;
					}
					FloatValue floatValue = DataMapWrapper.getValue(stack);
					float value = 0;
					Property valueProp = config.get("data", stack.toStringNoType(), floatValue != null ? floatValue.value : value);
					value = Float.parseFloat(valueProp.getString());
					if (value == 0)
						if (floatValue != null)
							value = floatValue.value;
					valueProp.set(value);
					if (value > -1)
						if (floatValue == null || !valueProp.isDefault())
							toChange.add(new ValueHolder(stack, value));
					if (value == -1)
						if (removeType == 0)
							DataMapWrapper.removeValue(stack, true, RemovalType.SHALLOW);
						else if (removeType == 1)
							toRemove.add(stack);
				}
			}
			catch (NullPointerException e)
			{
				continue;
			}
			break;
		}
		while (true)
		{
			try
			{
				for (ValueHolder remove : toChange)
				{
					if (remapType == 0)
						DataMapWrapper.removeValue(remove.getStack(), true, RemovalType.SHALLOW);
					else if (remapType == 1)
						DataMapWrapper.removeValue(remove.getStack(), false, RemovalType.DEEP);
					DataMapWrapper.addValue(remove.getStack(), remove.getValue(), ValueMap.Type.USER);
				}
				for (WrappedStack remove : toRemove)
					DataMapWrapper.removeValue(remove, true, RemovalType.DEEP);
				for (int pass = 0; pass < 3; pass++)
					DataMapWrapper.resolveValues(true);
			}
			catch (NullPointerException e)
			{
				continue;
			}
			break;
		}
		if (config.hasChanged())
			config.save();
	}
}
