package com.texasjake95.core;

import com.texasjake95.core.recipe.RecipeProviders;
import com.texasjake95.core.tile.TileEntityFurnaceBase;

public class BOTH extends TileEntityFurnaceBase {

	public BOTH()
	{
		super(4, 1, 10, RecipeProviders.furnaceMacerator);
	}
}
