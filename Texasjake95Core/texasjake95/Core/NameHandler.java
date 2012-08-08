package texasjake95.Core;

import java.util.LinkedList;

public class NameHandler {

	public static void getNames() {
		for (INameHandler handler : Names)
    {
		handler.getNames();
    }
	}	
		 
		public static void registerNameHandler(INameHandler handler)
	    {
			Names.add(handler);
	    }
		private static LinkedList<INameHandler> Names = new LinkedList<INameHandler>();
}
		
	
		
