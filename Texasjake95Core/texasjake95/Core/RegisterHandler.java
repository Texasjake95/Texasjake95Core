package texasjake95.Core;

import java.util.LinkedList;

public class RegisterHandler {

	public static void Register() {
		for (IRegisterHandler handler : Registers)
    {
		handler.Register();
    }
	}	
		 
		public static void registerRegisterHandler(IRegisterHandler handler)
	    {
			Registers.add(handler);
	    }
		private static LinkedList<IRegisterHandler> Registers = new LinkedList<IRegisterHandler>();
		
	
		

}
