package texasjake95.Core.AddOns;

import net.minecraft.src.ModLoader;
import texasjake95.ZeldaOoT.AddOns.AddonHandler;

public class ThaumCraft_AddOnCore extends AddonHandlerCore{

	public static void init() {
		try {
			ModLoader.getLogger().finer(AddonHandler.getLogMessage("ThaumCraft detected; attempting to initialize ThaumCraft addon"));
			
			

			ModLoader.getLogger().finer(AddonHandler.getLogMessage("ThaumCraft initialized"));
		}
		catch (Exception e) {
			ModLoader.getLogger().fine(AddonHandler.getLogMessage("Failed to initialize ThaumCraft Addon"));
			e.printStackTrace(System.err);
		}
	}
}
