package com.texasjake95.core.items;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.texasjake95.core.recipe.ShapelessDamageRecipe;

public class MultiItemMap {
	
	public static Item[] PA;
	public static Item[] PS;
	public static Item[] PSA;
	public static Item[] PSSw;
	public static Item[] PSw;
	public static Item[] PSwA;
	public static Item[] SA;
	public static Item[] SSw;
	public static Item[] SSwA;
	public static Item[] SwA;
	public static Item[] Multi;
	public static Item[] Pick = new Item[] { Items.wooden_pickaxe, Items.stone_pickaxe, Items.iron_pickaxe, Items.golden_pickaxe, Items.diamond_pickaxe };
	public static Item[] Axe = new Item[] { Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.golden_axe, Items.diamond_axe };
	public static Item[] Shovel = new Item[] { Items.wooden_shovel, Items.stone_shovel, Items.iron_shovel, Items.golden_shovel, Items.diamond_shovel };
	public static Item[] Sword = new Item[] { Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.golden_sword, Items.diamond_sword };
	
	public static void initItemMaps()
	{
		ArrayList<Item> pA = new ArrayList<Item>();
		ArrayList<Item> pS = new ArrayList<Item>();
		ArrayList<Item> pSA = new ArrayList<Item>();
		ArrayList<Item> pSSw = new ArrayList<Item>();
		ArrayList<Item> pSw = new ArrayList<Item>();
		ArrayList<Item> pSwA = new ArrayList<Item>();
		ArrayList<Item> sA = new ArrayList<Item>();
		ArrayList<Item> sSw = new ArrayList<Item>();
		ArrayList<Item> sSwA = new ArrayList<Item>();
		ArrayList<Item> swA = new ArrayList<Item>();
		ArrayList<Item> multi = new ArrayList<Item>();
		ArrayList<Item> items = new ArrayList<Item>();
		for (String name : CoreItems.types)
		{
			Item Multi = CoreItems.getItem(String.format("%s MultiTool", name));
			multi.add(Multi);
			items.add(Multi);
			Item pa = CoreItems.getItem(String.format("%s Paxe", name));
			pA.add(pa);
			items.add(pa);
			Item ps = CoreItems.getItem(String.format("%s Picvel", name));
			pS.add(ps);
			items.add(ps);
			Item psa = CoreItems.getItem(String.format("%s Paxel", name));
			pSA.add(psa);
			items.add(psa);
			Item pssw = CoreItems.getItem(String.format("%s Piverd", name));
			pSSw.add(pssw);
			items.add(pssw);
			Item psw = CoreItems.getItem(String.format("%s Picord", name));
			pSw.add(psw);
			items.add(psw);
			Item pswa = CoreItems.getItem(String.format("%s Paxerd", name));
			pSwA.add(pswa);
			items.add(pswa);
			Item sa = CoreItems.getItem(String.format("%s Shaxe", name));
			sA.add(sa);
			items.add(sa);
			Item ssw = CoreItems.getItem(String.format("%s Shord", name));
			sSw.add(ssw);
			items.add(ssw);
			Item sswa = CoreItems.getItem(String.format("%s Swaxel", name));
			sSwA.add(sswa);
			items.add(sswa);
			Item swa = CoreItems.getItem(String.format("%s Swaxe", name));
			swA.add(swa);
			items.add(swa);
		}
		PA = pA.toArray(new Item[0]);
		PS = pS.toArray(new Item[0]);
		PSA = pSA.toArray(new Item[0]);
		PSSw = pSSw.toArray(new Item[0]);
		PSw = pSw.toArray(new Item[0]);
		PSwA = pSwA.toArray(new Item[0]);
		SA = sA.toArray(new Item[0]);
		SSw = sSw.toArray(new Item[0]);
		SSwA = sSwA.toArray(new Item[0]);
		SwA = swA.toArray(new Item[0]);
		Multi = multi.toArray(new Item[0]);
		MultiRecipe();
	}
	
	private static void MultiRecipe()
	{
		for (int a = 0; a < 5; a++)
		{
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Sword[a]), new ItemStack(Shovel[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PA[a]), new ItemStack(Sword[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PS[a]), new ItemStack(Sword[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PSw[a]), new ItemStack(Shovel[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PSwA[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PSSw[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PSA[a]), new ItemStack(Sword[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Sword[a]), new ItemStack(SA[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SSw[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SSwA[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PS[a]), new ItemStack(SwA[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(PA[a]), new ItemStack(SSw[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(SA[a]), new ItemStack(PSw[a]) });
			addShapelessDamage(new ItemStack(Multi[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SwA[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(PA[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(PS[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(PSA[a]), new Object[] { new ItemStack(PS[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(PSA[a]), new Object[] { new ItemStack(PA[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(PSA[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SA[a]) });
			addShapelessDamage(new ItemStack(PSA[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Shovel[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(PSSw[a]), new Object[] { new ItemStack(PS[a]), new ItemStack(Sword[a]) });
			addShapelessDamage(new ItemStack(PSSw[a]), new Object[] { new ItemStack(PSw[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(PSSw[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SSw[a]) });
			addShapelessDamage(new ItemStack(PSSw[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Sword[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(PSw[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Sword[a]) });
			addShapelessDamage(new ItemStack(PSwA[a]), new Object[] { new ItemStack(PSw[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(PSwA[a]), new Object[] { new ItemStack(PA[a]), new ItemStack(Sword[a]) });
			addShapelessDamage(new ItemStack(PSwA[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(SwA[a]) });
			addShapelessDamage(new ItemStack(PSwA[a]), new Object[] { new ItemStack(Pick[a]), new ItemStack(Sword[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(SA[a]), new Object[] { new ItemStack(Shovel[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(SSw[a]), new Object[] { new ItemStack(Sword[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(SSwA[a]), new Object[] { new ItemStack(Sword[a]), new ItemStack(Shovel[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(SSwA[a]), new Object[] { new ItemStack(SwA[a]), new ItemStack(Shovel[a]) });
			addShapelessDamage(new ItemStack(SSwA[a]), new Object[] { new ItemStack(SSw[a]), new ItemStack(Axe[a]) });
			addShapelessDamage(new ItemStack(SSwA[a]), new Object[] { new ItemStack(Sword[a]), new ItemStack(SA[a]) });
			addShapelessDamage(new ItemStack(SwA[a]), new Object[] { new ItemStack(Sword[a]), new ItemStack(Axe[a]) });
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void addShapelessDamage(ItemStack itemStack, Object... items)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapelessDamageRecipe(itemStack, items));
	}
}
