package com.texasjake95.core.items;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.util.EnumHelper;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;

import com.texasjake95.core.ItemMisc;
import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.items.multi.MultiPickAxe;
import com.texasjake95.core.items.multi.MultiPickShovel;
import com.texasjake95.core.items.multi.MultiPickShovelAxe;
import com.texasjake95.core.items.multi.MultiPickShovelSword;
import com.texasjake95.core.items.multi.MultiPickSword;
import com.texasjake95.core.items.multi.MultiPickSwordAxe;
import com.texasjake95.core.items.multi.MultiShovelAxe;
import com.texasjake95.core.items.multi.MultiShovelSword;
import com.texasjake95.core.items.multi.MultiSwordAxe;
import com.texasjake95.core.items.multi.MultiWeapon;

public class CoreItems {

	public static Item diamondMulti;
	public static Item diamondPaxe;
	public static Item diamondPaxel;
	public static Item diamondPaxerd;
	public static Item diamondPicord;
	public static Item diamondPicvel;
	public static Item diamondPiverd;
	public static Item diamondShaxe;
	public static Item diamondShord;
	public static Item diamondSwaxe;
	public static Item diamondSwaxel;
	public static Item goldenMulti;
	public static Item goldenPaxe;
	public static Item goldenPaxel;
	public static Item goldenPaxerd;
	public static Item goldenPicord;
	public static Item goldenPicvel;
	public static Item goldenPiverd;
	public static Item goldenShaxe;
	public static Item goldenShord;
	public static Item goldenSwaxe;
	public static Item goldenSwaxel;
	public static Item ironMulti;
	public static Item ironPaxe;
	public static Item ironPaxel;
	public static Item ironPaxerd;
	public static Item ironPicord;
	public static Item ironPicvel;
	public static Item ironPiverd;
	public static Item ironShaxe;
	public static Item ironShord;
	public static Item ironSwaxe;
	public static Item ironSwaxel;
	public static Item stoneMulti;
	public static Item stonePaxe;
	public static Item stonePaxel;
	public static Item stonePaxerd;
	public static Item stonePicord;
	public static Item stonePicvel;
	public static Item stonePiverd;
	public static Item stoneShaxe;
	public static Item stoneShord;
	public static Item stoneSwaxe;
	public static Item stoneSwaxel;
	public static Item woodenMulti;
	public static Item woodenPaxe;
	public static Item woodenPaxel;
	public static Item woodenPaxerd;
	public static Item woodenPicord;
	public static Item woodenPicvel;
	public static Item woodenPiverd;
	public static Item woodenShaxe;
	public static Item woodenShord;
	public static Item woodenSwaxe;
	public static Item woodenSwaxel;
	public static Item misc;
	private static final ToolMaterial diamond4 = create(ToolMaterial.EMERALD, 4);
	private static final ToolMaterial diamond3 = create(ToolMaterial.EMERALD, 3);
	private static final ToolMaterial diamond2 = create(ToolMaterial.EMERALD, 2);
	private static final ToolMaterial gold4 = create(ToolMaterial.GOLD, 4);
	private static final ToolMaterial gold3 = create(ToolMaterial.GOLD, 3);
	private static final ToolMaterial gold2 = create(ToolMaterial.GOLD, 2);
	private static final ToolMaterial iron4 = create(ToolMaterial.IRON, 4);
	private static final ToolMaterial iron3 = create(ToolMaterial.IRON, 3);
	private static final ToolMaterial iron2 = create(ToolMaterial.IRON, 2);
	private static final ToolMaterial stone4 = create(ToolMaterial.STONE, 4);
	private static final ToolMaterial stone3 = create(ToolMaterial.STONE, 3);
	private static final ToolMaterial stone2 = create(ToolMaterial.STONE, 2);
	private static final ToolMaterial wood4 = create(ToolMaterial.WOOD, 4);
	private static final ToolMaterial wood3 = create(ToolMaterial.WOOD, 3);
	private static final ToolMaterial wood2 = create(ToolMaterial.WOOD, 2);
	static final String[] types = new String[] { "Wooden", "Stone", "Iron", "Golden", "Diamond" };
	private static final ArrayList<ToolMaterial> toolMaterials2 = Lists.newArrayList(wood2, stone2, iron2, gold2, diamond2);
	private static final ArrayList<ToolMaterial> toolMaterials3 = Lists.newArrayList(wood3, stone3, iron3, gold3, diamond3);
	private static final ArrayList<ToolMaterial> toolMaterials4 = Lists.newArrayList(wood4, stone4, iron4, gold4, diamond4);

	private static ToolMaterial create(ToolMaterial base, int useMultiplier)
	{
		return EnumHelper.addToolMaterial("TX" + base.name() + useMultiplier, base.getHarvestLevel(), base.getMaxUses() * useMultiplier, base.getEfficiencyOnProperMaterial(), base.getDamageVsEntity(), base.getEnchantability());
	}

	static Item getItem(String name)
	{
		return GameRegistry.findItem(CoreInfo.modId, name);
	}

	public static void initItems()
	{
		for (int i = 0; i < 5; i++)
		{
			Item item = multi(i).setTextureName(types[i].toLowerCase() + "_Multi").setUnlocalizedName(types[i].toLowerCase() + "_Multi");
			GameRegistry.registerItem(item, types[i] + " MultiTool");
			item = paxe(i).setTextureName(types[i].toLowerCase() + "_Paxe").setUnlocalizedName(types[i].toLowerCase() + "_Paxe");
			GameRegistry.registerItem(item, types[i] + " Paxe");
			item = paxel(i).setTextureName(types[i].toLowerCase() + "_Paxel").setUnlocalizedName(types[i].toLowerCase() + "_Paxel");
			GameRegistry.registerItem(item, types[i] + " Paxel");
			item = paxerd(i).setTextureName(types[i].toLowerCase() + "_Paxerd").setUnlocalizedName(types[i].toLowerCase() + "_Paxerd");
			GameRegistry.registerItem(item, types[i] + " Paxerd");
			item = picord(i).setTextureName(types[i].toLowerCase() + "_Picord").setUnlocalizedName(types[i].toLowerCase() + "_Picord");
			GameRegistry.registerItem(item, types[i] + " Picord");
			item = picvel(i).setTextureName(types[i].toLowerCase() + "_Picvel").setUnlocalizedName(types[i].toLowerCase() + "_Picvel");
			GameRegistry.registerItem(item, types[i] + " Picvel");
			item = piverd(i).setTextureName(types[i].toLowerCase() + "_Piverd").setUnlocalizedName(types[i].toLowerCase() + "_Piverd");
			GameRegistry.registerItem(item, types[i] + " Piverd");
			item = shaxe(i).setTextureName(types[i].toLowerCase() + "_Shaxe").setUnlocalizedName(types[i].toLowerCase() + "_Shaxe");
			GameRegistry.registerItem(item, types[i] + " Shaxe");
			item = shord(i).setTextureName(types[i].toLowerCase() + "_Shord").setUnlocalizedName(types[i].toLowerCase() + "_Shord");
			GameRegistry.registerItem(item, types[i] + " Shord");
			item = swaxe(i).setTextureName(types[i].toLowerCase() + "_Swaxe").setUnlocalizedName(types[i].toLowerCase() + "_Swaxe");
			GameRegistry.registerItem(item, types[i] + " Swaxe");
			item = swaxel(i).setTextureName(types[i].toLowerCase() + "_Swaxel").setUnlocalizedName(types[i].toLowerCase() + "_Swaxel");
			GameRegistry.registerItem(item, types[i] + " Swaxel");
		}
		diamondMulti = getItem("Diamond MultiTool");
		diamondPaxe = getItem("Diamond Paxe");
		diamondPaxel = getItem("Diamond Paxel");
		diamondPaxerd = getItem("Diamond Paxerd");
		diamondPicord = getItem("Diamond Picord");
		diamondPicvel = getItem("Diamond Picvel");
		diamondPiverd = getItem("Diamond Piverd");
		diamondShaxe = getItem("Diamond Shaxe");
		diamondShord = getItem("Diamond Shord");
		diamondSwaxe = getItem("Diamond Swaxe");
		diamondSwaxel = getItem("Diamond Swaxel");
		goldenMulti = getItem("Golden MultiTool");
		goldenPaxe = getItem("Golden Paxe");
		goldenPaxel = getItem("Golden Paxel");
		goldenPaxerd = getItem("Golden Paxerd");
		goldenPicord = getItem("Golden Picord");
		goldenPicvel = getItem("Golden Picvel");
		goldenPiverd = getItem("Golden Piverd");
		goldenShaxe = getItem("Golden Shaxe");
		goldenShord = getItem("Golden Shord");
		goldenSwaxe = getItem("Golden Swaxe");
		goldenSwaxel = getItem("Golden Swaxel");
		ironMulti = getItem("Iron MultiTool");
		ironPaxe = getItem("Iron Paxe");
		ironPaxel = getItem("Iron Paxel");
		ironPaxerd = getItem("Iron Paxerd");
		ironPicord = getItem("Iron Picord");
		ironPicvel = getItem("Iron Picvel");
		ironPiverd = getItem("Iron Piverd");
		ironShaxe = getItem("Iron Shaxe");
		ironShord = getItem("Iron Shord");
		ironSwaxe = getItem("Iron Swaxe");
		ironSwaxel = getItem("Iron Swaxel");
		stoneMulti = getItem("Stone MultiTool");
		stonePaxe = getItem("Stone Paxe");
		stonePaxel = getItem("Stone Paxel");
		stonePaxerd = getItem("Stone Paxerd");
		stonePicord = getItem("Stone Picord");
		stonePicvel = getItem("Stone Picvel");
		stonePiverd = getItem("Stone Piverd");
		stoneShaxe = getItem("Stone Shaxe");
		stoneShord = getItem("Stone Shord");
		stoneSwaxe = getItem("Stone Swaxe");
		stoneSwaxel = getItem("Stone Swaxel");
		woodenMulti = getItem("Wooden MultiTool");
		woodenPaxe = getItem("Wooden Paxe");
		woodenPaxel = getItem("Wooden Paxel");
		woodenPaxerd = getItem("Wooden Paxerd");
		woodenPicord = getItem("Wooden Picord");
		woodenPicvel = getItem("Wooden Picvel");
		woodenPiverd = getItem("Wooden Piverd");
		woodenShaxe = getItem("Wooden Shaxe");
		woodenShord = getItem("Wooden Shord");
		woodenSwaxe = getItem("Wooden Swaxe");
		woodenSwaxel = getItem("Wooden Swaxel");
		MultiItemMap.initItemMaps();
		misc = new ItemMisc();
		GameRegistry.registerItem(misc, "TXMisc");

	}

	private static Item multi(int tool)
	{
		return new MultiWeapon(tool4(tool), CoreInfo.modId);
	}

	private static Item paxe(int tool)
	{
		return new MultiPickAxe(tool2(tool), CoreInfo.modId);
	}

	private static Item paxel(int tool)
	{
		return new MultiPickShovelAxe(tool3(tool), CoreInfo.modId);
	}

	private static Item paxerd(int tool)
	{
		return new MultiPickSwordAxe(tool3(tool), CoreInfo.modId);
	}

	private static Item picord(int tool)
	{
		return new MultiPickSword(tool2(tool), CoreInfo.modId);
	}

	private static Item picvel(int tool)
	{
		return new MultiPickShovel(tool2(tool), CoreInfo.modId);
	}

	private static Item piverd(int tool)
	{
		return new MultiPickShovelSword(tool3(tool), CoreInfo.modId);
	}

	private static Item shaxe(int tool)
	{
		return new MultiShovelAxe(tool2(tool), CoreInfo.modId);
	}

	private static Item shord(int tool)
	{
		return new MultiShovelSword(tool2(tool), CoreInfo.modId);
	}

	private static Item swaxe(int tool)
	{
		return new MultiSwordAxe(tool2(tool), CoreInfo.modId);
	}

	private static Item swaxel(int tool)
	{
		return new MultiPickAxe(tool2(tool), CoreInfo.modId);
	}

	private static ToolMaterial tool2(int index)
	{
		return toolMaterials2.get(index);
	}

	private static ToolMaterial tool3(int index)
	{
		return toolMaterials3.get(index);
	}

	private static ToolMaterial tool4(int index)
	{
		return toolMaterials4.get(index);
	}
}
