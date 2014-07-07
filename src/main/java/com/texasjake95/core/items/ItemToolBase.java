package com.texasjake95.core.items;

import java.util.Set;

import com.google.common.collect.Sets;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import com.texasjake95.commons.helpers.Checker;
import com.texasjake95.commons.util.CustomArrayList;

public class ItemToolBase extends ItemTool {

	public static final Set<Block> pickaxe = ReflectionHelper.getPrivateValue(ItemPickaxe.class, null, 0);
	public static final Set<Block> shovel = ReflectionHelper.getPrivateValue(ItemSpade.class, null, 0);
	public static final Set<Block> axe = ReflectionHelper.getPrivateValue(ItemAxe.class, null, 0);
	public static final float pickaxeDamage = 2.0F;
	public static final float axeDamage = 3.0F;
	public static final float shovelDamage = 1.0F;
	public static final float swordDamage = 4.0F;

	protected static Set<Block> createSet(boolean Pickaxe, boolean Shovel, boolean Axe, Block... extra)
	{
		CustomArrayList<Block> list = new CustomArrayList<Block>();
		if (Pickaxe)
		{
			list.addAll(pickaxe);
		}
		if (Shovel)
		{
			list.addAll(shovel);
		}
		if (Axe)
		{
			list.addAll(axe);
		}
		list.addAll(extra);
		return Sets.newHashSet(list);
	}

	private final String prefix;
	protected boolean isSword = false;

	public ItemToolBase(float damageMod, ToolMaterial toolMaterial, Set<Block> blocksEffectiveAgainst, String texturePrefix, String... types)
	{
		super(damageMod, toolMaterial, blocksEffectiveAgainst);
		this.prefix = texturePrefix;
		for (String toolClass : types)
		{
			this.setHarvestLevel(toolClass, this.toolMaterial.getHarvestLevel());
		}
	}

	protected final boolean axe(Block block, int meta)
	{
		return Checker.doAnyMatch(block.getMaterial(), Material.wood, Material.plants, Material.vine);
	}

	protected final boolean canAxe(Block block)
	{
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		for (String type : this.getToolClasses(stack))
		{
			if (type.equals("pickaxe") && this.canPickaxe(block))
				return true;
			if (type.equals("axe") && this.canAxe(block))
				return true;
			if (type.equals("shovel") && this.canShovel(block))
				return true;
		}
		if (this.isSword && this.canSword(block))
			return true;
		return super.canHarvestBlock(block, stack);
	}

	protected final boolean canPickaxe(Block block)
	{
		if (block == Blocks.obsidian)
			return this.toolMaterial.getHarvestLevel() >= 3;
		if (Checker.doAnyMatch(block, Blocks.emerald_ore, Blocks.emerald_block, Blocks.diamond_ore, Blocks.diamond_block, Blocks.gold_ore, Blocks.gold_block, Blocks.redstone_ore, Blocks.lit_redstone_ore))
			return this.toolMaterial.getHarvestLevel() >= 2;
		if (Checker.doAnyMatch(block, Blocks.iron_ore, Blocks.iron_block, Blocks.lapis_ore, Blocks.lapis_block))
			return this.toolMaterial.getHarvestLevel() >= 1;
		if (Checker.doAnyMatch(block.getMaterial(), Material.rock, Material.iron, Material.anvil))
			return true;
		return false;
	}

	protected final boolean canShovel(Block block)
	{
		return Checker.doAnyMatch(block, Blocks.snow_layer, Blocks.snow);
	}

	protected final boolean canSword(Block block)
	{
		if (Checker.doAnyMatch(block, Blocks.web))
			return this.toolMaterial.getEfficiencyOnProperMaterial() >= 0;
		return false;
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		CustomArrayList<CreativeTabs> tabs = new CustomArrayList<CreativeTabs>();
		tabs.add(CreativeTabs.tabTools);
		if (this.isSword)
		{
			tabs.add(CreativeTabs.tabCombat);
		}
		return tabs.toArray(new CreativeTabs[tabs.list.size()]);
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		boolean isValid = false;
		for (String type : this.getToolClasses(stack))
		{
			if (type.equals("pickaxe") && this.pickaxe(block, meta))
			{
				isValid = true;
			}
			if (type.equals("axe") && this.axe(block, meta))
			{
				isValid = true;
			}
		}
		if (isValid)
			return this.getEfficiency(stack, block.getHarvestTool(meta));
		return super.getDigSpeed(stack, block, meta);
	}

	public float getEfficiency(ItemStack stack, String type)
	{
		return this.efficiencyOnProperMaterial;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return this.isSword ? EnumAction.block : super.getItemUseAction(par1ItemStack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return this.isSword ? 72000 : super.getMaxItemUseDuration(par1ItemStack);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if (this.isSword)
		{
			par1ItemStack.damageItem(1, par3EntityLivingBase);
		}
		else
		{
			par1ItemStack.damageItem(2, par3EntityLivingBase);
		}
		return true;
	}

	public boolean isSword()
	{
		return this.isSword;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (this.isSword)
		{
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		}
		return par1ItemStack;
	}

	protected final boolean pickaxe(Block block, int meta)
	{
		return Checker.doAnyMatch(block.getMaterial(), Material.iron, Material.anvil, Material.rock);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(String.format("%s:%s", this.prefix, this.iconString));
	}
}
