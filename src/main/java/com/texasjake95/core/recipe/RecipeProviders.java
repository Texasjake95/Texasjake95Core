package com.texasjake95.core.recipe;

public class RecipeProviders {

	public static final IRecipeProvider furnace = new FurnaceRecipeProvider();
	public static final MaceratorRecipeProvider macerator = new MaceratorRecipeProvider();
	public static final IRecipeProvider furnaceMacerator = new FurnaceMaceratorRecipeProvider();
}
