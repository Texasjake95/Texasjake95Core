package texasjake95.Core.AddOns;

import net.minecraft.src.ModLoader;
import texasjake95.Core.AddOns.Interfaces.IIC2AddOn;
import texasjake95.ZeldaOoT.AddOns.AddonHandler;

public class IC2_AddOnCore extends AddonHandlerCore{

	public static void init() {
		try {
			ModLoader.getLogger().finer(AddonHandlerCore.getLogMessage("IC2 detected; attempting to initialize IC2 addon"));
				
			
			
			ModLoader.getLogger().finer(AddonHandlerCore.getLogMessage("IC2 initialized"));
		}
		catch (Exception e) {
			ModLoader.getLogger().fine(AddonHandlerCore.getLogMessage("Failed to initialize IC2 Addon"));
			e.printStackTrace(System.err);
		}
	}
	
}
