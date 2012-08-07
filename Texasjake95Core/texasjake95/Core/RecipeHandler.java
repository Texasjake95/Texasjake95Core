package texasjake95.Core;

import java.util.ArrayList;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.oredict.ShapedOreRecipe;
import net.minecraft.src.forge.oredict.ShapelessOreRecipe;

import codechicken.core.CommonUtils;
import codechicken.core.IStringMatcher;
import codechicken.nei.IConfigureNEI;

public class RecipeHandler {

	public static void getRecipes()
{
	ArrayList<Class<?>> configclasses = ClassReader.findClasses(new ClassMatcher()
	{
		public boolean matches(String test)
		{
			return test.startsWith("TX") && test.endsWith("Recipes.class");
		}
	}, new Class<?>[]{IRecipeHandler.class});
	
	for(Class<?> class1 : configclasses)
	{String MODNAME = null;
		try
		{
			IRecipeHandler Recipe = (IRecipeHandler)class1.newInstance();
			Recipe.getRecipes();
			MODNAME = Recipe.getModName();
            System.out.println("Loaded "+MODNAME + " Recipes");
            ModLoader.getLogger().finer(getLogMessage("Recipes loaded for " + MODNAME));
		}
		catch(Exception e)
		{
            System.out.println("Failed to Load "+ MODNAME + " Recipes");
			e.printStackTrace();
		}
	}
}	
public static String getLogMessage(String logMessage) {
	return logMessage;
}


}
