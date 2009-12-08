package org.imirsel.nema.util;



import java.util.logging.Logger;
//import java.net.*;
import java.io.*;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;

@Component(creator = "Mert Bay", 
		description = "Test console output", 
		name = "TestConsoleOut", 
		tags = "test, console, output", 
		resources = "",
		firingPolicy = Component.FiringPolicy.all)
		
public class TestConsoleOut implements ExecutableComponent {




    @ComponentProperty(defaultValue="Hello World !",
                      description="text to be displayed on console",
                      name="Text to Display")
    final static String DATA_PROPERTY_1 = "Text to Display";

    @ComponentProperty(defaultValue="10",
                      description="Number of times to display the text",
                      name="Number of Times to Display Text")
    final static String DATA_PROPERTY_2 = "Number of Times to Display Text";
    
    @ComponentProperty(defaultValue="100",
            description="wait time between message in miliseconds",
            name="Wait Time in miliseconds Between Text Displays")
            final static String DATA_PROPERTY_3 = "Wait Time in miliseconds Between Text Displays";

    
    

    
   
    private String text = "Hello World !";
    private int number = 10;
    private int wtime = 10;
    private Logger logger = null;
    private java.io.PrintStream out;
    
    public void initialize(ComponentContextProperties ccp) {     
        out = ccp.getOutputConsole();
        logger = ccp.getLogger();
        text = String.valueOf(ccp.getProperty(DATA_PROPERTY_1));	
        number = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_2));
        wtime = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_3));
        //logger.fine(e.printStack);
    }
    
	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
		out.close();
		
	}
	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
				
		out.println("Here is the text output:\n");
		
		for (int i=0;i<number;i++){
			out.println(i + ":\t"+ text+"\n");
			out.flush();
			wait(wtime);
		}
	}

	
	public static void wait (int n){
        long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<n);
}

	
}
	