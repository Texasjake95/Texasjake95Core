package texasjake95.Core;

import java.util.ArrayList;

import net.minecraft.src.ModLoader;

public class NameHandler {
	public static void getNames()
	{
		ArrayList<Class<?>> configclasses = ClassReader.findClasses(new ClassMatcher()
		{
			public boolean matches(String test)
			{
				return test.startsWith("TX") && test.endsWith("Names.class");
			}
		}, new Class<?>[]{INameHandler.class});
		
		for(Class<?> class1 : configclasses)
		{String MODNAME = null;
			try
			{
				INameHandler Name = (INameHandler)class1.newInstance();
				Name.getNames();
				MODNAME = Name.getModName();
	            System.out.println("Loaded "+MODNAME + " Names");
	            ModLoader.getLogger().finer(getLogMessage("Names loaded for " + MODNAME));
			}
			catch(Exception e)
			{
	            System.out.println("Failed to Load "+ MODNAME + " Names");
				e.printStackTrace();
			}
		}
	}	
	public static String getLogMessage(String logMessage) {
		return logMessage;
	}

}
