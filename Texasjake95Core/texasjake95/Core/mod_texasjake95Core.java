package texasjake95.Core;

import net.minecraft.src.BaseMod;

public class mod_texasjake95Core extends BaseMod
{

	@Override
	public String getVersion() {
		return "Version 1.0.0";
	}
	@Override
	public void load() {
		
	}

	public String getPriorities()
    {
        return "before:*";
    }
	public void modsLoaded() {
		RegisterHandler.Register();
		NameHandler.getNames();
		RecipeHandler.getRecipes();
		
	}
	
}
