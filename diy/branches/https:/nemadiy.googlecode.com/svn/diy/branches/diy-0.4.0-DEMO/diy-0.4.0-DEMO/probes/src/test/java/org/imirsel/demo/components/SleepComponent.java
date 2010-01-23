package org.imirsel.demo.components;

import org.meandre.annotations.*;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**This test component sleeps for default 5 seconds before pushing the data out
 * 
 * @author amitku
 *
 */
public class SleepComponent implements ExecutableComponent{

	@ComponentInput(description = "input object", name = "input")
	private static final String INPUT_1="input";
	
	
	@ComponentOutput(description = "output object", name = "output")
	private static final String OUTPUT_1="output";
	
	@ComponentProperty(defaultValue = "5", description = "sleep time in seconds", name = "sleepTime")
	private static final String PROP_1 ="sleepTime";
	
	
	
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub
	}
	
	
	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	
		
		
	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String sleepTimeString = cc.getProperty(PROP_1);
		Object objectIn = cc.getDataComponentFromInput(INPUT_1);
		int sleepTime = Integer.parseInt(sleepTimeString);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cc.pushDataComponentToOutput(OUTPUT_1, objectIn);
	}


}
