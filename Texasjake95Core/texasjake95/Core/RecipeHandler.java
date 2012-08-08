package texasjake95.Core;

import java.util.LinkedList;

public class RecipeHandler {

	public static void getRecipes() {
		for (IRecipeHandler handler : Recipes)
    {
		handler.getRecipes();
    }
	}	
		 
		public static void registerRecipeHandler(IRecipeHandler handler)
	    {
			Recipes.add(handler);
	    }
		private static LinkedList<IRecipeHandler> Recipes = new LinkedList<IRecipeHandler>();
}
		
	
		
