package texasjake95.Core.AddOns;

import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import texasjake95.ZeldaOoT.Config;

public class AddonHandlerCore {
public static boolean IC2 = false;
public static boolean RPW = false;
public static boolean TC2 = false;
public static boolean NEI = false;
public static boolean EE2 = false;
public static boolean x = false;

public static final String MOD_NAME = "Texasjake95 Core";

public static final String LOGGER_PREFIX = "[" + MOD_NAME + "] ";

public static String getLogMessage(String logMessage) {
	return LOGGER_PREFIX + logMessage;
}

public static void initAddons() {
	ModLoader.getLogger().finer(getLogMessage("Checking for addons needing initializing"));
	boolean addonsFound = false;

	
	for (BaseMod mod : ModLoader.getLoadedMods()) {
		
		if (mod.toString().contains("mod_IC2")) {
			IC2_AddOnCore.init();
			IC2 = true;
			addonsFound = true;
		}
		if (mod.toString().contains("mod_ThaumCraft")) {
			ThaumCraft_AddOnCore.init();
			TC2 = true;
			addonsFound = true;
		}
		if (mod.toString().contains("mod_RedPowerWorld")) {
			//RedPower_AddOnCore.init();
			RPW = true;
			addonsFound = true;
		}
		if (mod.toString().contains("mod_NotEnoughItems")) {
            NEI = true;
			addonsFound = true;
		}
		if (mod.toString().contains("mod_EE")) {
			//EE_AddonCore.init();
			EE2 = true;
			addonsFound = true;
		}
		
	}
	if (addonsFound)
		{ModLoader.getLogger().finer(getLogMessage("Done initializing addons"));
	
		}
	
	else
		ModLoader.getLogger().finer(getLogMessage("No addons for loaded mods found"));
	
}


}
