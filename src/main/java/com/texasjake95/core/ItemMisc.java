package com.texasjake95.core;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.texasjake95.core.api.CoreInfo;

public class ItemMisc extends Item {

	private static ItemDataMap map = new ItemDataMap();
	static
	{
		new ItemData(map, "txironDust", "ironDust", CoreInfo.modId);
		new ItemData(map, "txgoldDust", "goldDust", CoreInfo.modId);
	}

	public ItemMisc()
	{
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setHasSubtypes(true);
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

	@Override
	@SuppressWarnings("rawtypes")
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		map.getSubItems(item, tab, list);
	}
}
