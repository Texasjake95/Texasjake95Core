package com.texasjake95.core.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.lib.item.ItemData;
import com.texasjake95.core.lib.item.ItemDataMap;

public class ItemMisc extends Item {

	private static ItemDataMap map = new ItemDataMap("TXCOREMISC");
	public static final int ironDust;
	public static final int goldDust;
	static
	{
		ironDust = map.nameToId.get(new ItemData(map, "txironDust", "ironDust", CoreInfo.modId).unlocName);
		goldDust = map.nameToId.get(new ItemData(map, "txgoldDust", "goldDust", CoreInfo.modId).unlocName);
	}

	public ItemMisc()
	{
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setHasSubtypes(true);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		map.getSubItems(item, tab, list);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return map.getUnlocalizedName(stack);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		map.registerIcons(register);
	}
}
