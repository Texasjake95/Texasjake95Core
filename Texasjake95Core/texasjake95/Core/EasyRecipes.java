package texasjake95.Core;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.oredict.ShapedOreRecipe;
import net.minecraft.src.forge.oredict.ShapelessOreRecipe;

public class EasyRecipes {

/**
 * Add a Forge ShapedOre Recipe
 *
 * @param itemstack
 * @param recipe
 */
public static void ForgeOre(ItemStack itemstack, Object... recipe)
{
	CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(itemstack, recipe));
}
/**
 * Add a Forge ShapelessOre Recipe
 *
 * @param itemstack
 * @param recipe
 */
public static void ForgeOreShapeless(ItemStack itemstack, Object... recipe)
{		
	CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(itemstack, recipe));
}
/**
 * Add a Meta Sensitive Smelting Recipe
 *
 * @param itemID
 * @param meta
 * @param itemstack
 */
public static void MetaSmelt(int itemID, int meta, ItemStack itemstack)
{
	FurnaceRecipes.smelting().addSmelting(itemID, meta, itemstack);
}
/**
 * Add a Shaped Recipe
 *
 * @param output
 * @param params
 */
public static void addRecipe(ItemStack output, Object... params)
{
    ModLoader.addRecipe(output, params);
}
}
