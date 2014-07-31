package com.texasjake95.core.lib.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraftforge.common.util.Constants.NBT;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;

public class ItemDataMap {

	public static final Map<String, ItemDataMap> dataMap = Maps.newHashMap();
	public Map<String, Integer> nameToId = Maps.newHashMap();
	public Map<Integer, ItemData> IDtoData = Maps.newHashMap();
	public Map<String, ItemData> nameToData = Maps.newHashMap();
	public final String fileName;

	public ItemDataMap(String string)
	{
		this.fileName = string;
		dataMap.put(string, this);
	}

	public void addItemData(ItemData itemData)
	{
		int ID = this.nameToId.size();
		this.nameToId.put(itemData.unlocName, ID);
		this.IDtoData.put(ID, itemData);
		this.nameToData.put(itemData.unlocName, itemData);
	}

	private Map<String, Integer> createMap(NBTTagCompound compound)
	{
		Map<String, Integer> map = Maps.newHashMap();
		NBTTagList list = compound.getTagList("list", NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			map.put(tag.getString("name"), tag.getInteger("value"));
		}
		return map;
	}

	private NBTTagCompound createNBT()
	{
		NBTTagCompound temp = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (Entry<String, Integer> entry : this.nameToId.entrySet())
		{
			NBTTagCompound set = new NBTTagCompound();
			set.setString("name", entry.getKey());
			set.setInteger("value", entry.getValue());
			list.appendTag(set);
		}
		temp.setTag("list", list);
		return temp;
	}

	private ItemData getData(ItemStack stack)
	{
		int meta = stack.getItemDamage();
		if (meta < 0 || meta > this.IDtoData.size())
			meta = 0;
		return this.IDtoData.get(meta);
	}

	public IIcon getIcon(ItemStack stack)
	{
		return this.getData(stack).getIcon();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int data = 0; data < this.IDtoData.size(); data++)
			list.add(new ItemStack(item, 1, data));
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getData(stack).unlocName;
	}

	public void registerIcons(IIconRegister register)
	{
		for (ItemData data : this.IDtoData.values())
			data.registerIIcon(register);
	}
}
