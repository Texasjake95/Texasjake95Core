package texasjake95.Core;

import java.util.ArrayList;

import net.minecraft.src.ModLoader;

public class RegisterHandler {

	public static void Register() {
		ArrayList<Class<?>> configclasses = ClassReader.findClasses(new ClassMatcher()
		{
			public boolean matches(String test)
			{
				return test.startsWith("TX") && test.endsWith("Names.class");
			}
		}, new Class<?>[]{IRegisterHandler.class});
		
		for(Class<?> class1 : configclasses)
		{String MODNAME = null;
			try
			{
				IRegisterHandler Register = (IRegisterHandler)class1.newInstance();
				Register.Register();
				MODNAME = Register.getModName();
	            System.out.println("Registered "+MODNAME + " Items");
	            ModLoader.getLogger().finer(getLogMessage("Registered "+MODNAME + " Items"));
			}
			catch(Exception e)
			{
	            System.out.println("Failed to Registered "+ MODNAME + " Items");
				e.printStackTrace();
			}
		}
	}	
		
	public static String getLogMessage(String logMessage) {
		return logMessage;
	}

}
