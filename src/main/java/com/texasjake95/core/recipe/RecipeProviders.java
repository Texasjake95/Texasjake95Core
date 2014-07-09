package com.texasjake95.core.recipe;

public class RecipeProviders {

	public static final IRecipeProvider furnace = new FurnaceRecipeProvider();
	public static final IRecipeProvider macerator = new MaceratorRecipeProvider();
	public static final IRecipeProvider furnaceMacerator = new FurnaceMaceratorRecipeProvider();

}
