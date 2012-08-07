package texasjake95.Core;

import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.ISoundHandler;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.oredict.OreDictionary;

public class EasyRegister {
	
	public static void RegisterOre(String string, ItemStack itemstack)
	{
		OreDictionary.registerOre(string, itemstack);
	}
	
	public static void RegisterBlock(Block block, Class<? extends ItemBlock> itemclass)
	{
		ModLoader.registerBlock(block, itemclass);
	}
	
	public static void RegisterSound(ISoundHandler handler)
	{
		MinecraftForgeClient.registerSoundHandler(handler);
	}
	public static void RegisterKey(BaseMod mod, KeyBinding keyHandler, boolean allowRepeat)
	{
		ModLoader.registerKey(mod, keyHandler, allowRepeat);
	}
}
