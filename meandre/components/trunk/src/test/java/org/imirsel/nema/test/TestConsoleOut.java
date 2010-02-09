package org.imirsel.nema.test;



import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

import org.imirsel.nema.components.NEMAComponent;
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
		
public class TestConsoleOut extends NEMAComponent {


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
    
    public void initialize(ComponentContextProperties ccp) throws ComponentContextException, ComponentExecutionException{
        //initialize logging
    	super.initialize(ccp);
    	
    	text = String.valueOf(ccp.getProperty(DATA_PROPERTY_1));	
        number = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_2));
        wtime = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_3));
    }
    
    //as we're just calling the super classes dispose method we don't need this declaration
	public void dispose(ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}
	
	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
				
		_logger.info("Here is the text output:\n");
		for (int i=0;i<number;i++){
			_logger.info(i + ":\t"+ text+"\n");
			wait(wtime);
		}
		
		try{
			throw new RuntimeException("Dummy exception");
			
		}catch(RuntimeException e){
			_logger.log(Level.SEVERE,"Here is an exception that we caught:",e);
		}
		
		_logger.info("--exit--");
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
	