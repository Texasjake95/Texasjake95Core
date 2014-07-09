package com.texasjake95.core;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ItemData {

	public final String unlocName;
	public final String texName;
	public final String modid;
	private IIcon icon;

	public ItemData(ItemDataMap map, String unlocName, String texName, String modid)
	{
		this.unlocName = unlocName;
		this.texName = texName;
		this.modid = modid;
		map.addItemData(this);
	}

	public void registerIIcon(IIconRegister register)
	{
		this.icon = register.registerIcon(String.format("%s:%s", modid, texName));
	}

	public IIcon getIcon()
	{
		return this.icon;
	}
}
