package com.texasjake95.core.lib.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.texasjake95.core.proxy.item.ItemStackProxy;

public class ItemDataMap {

	ArrayList<ItemData> list = Lists.newArrayList();

	public void addItemData(ItemData itemData)
	{
		this.list.add(itemData);
	}

	private ItemData getData(ItemStack stack)
	{
		int meta = ItemStackProxy.getMetadata(stack);
		if (meta < 0 || meta > this.list.size())
			meta = 0;
		return this.list.get(meta);
	}

	public IIcon getIcon(ItemStack stack)
	{
		return this.getData(stack).getIcon();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int data = 0; data < this.list.size(); data++)
			list.add(new ItemStack(item, 1, data));
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getData(stack).unlocName;
	}

	public void registerIcons(IIconRegister register)
	{
		for (ItemData data : this.list)
			data.registerIIcon(register);
	}
}
