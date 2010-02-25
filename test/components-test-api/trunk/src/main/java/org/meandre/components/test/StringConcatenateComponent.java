/*
 * @(#) StringConcatenateComponent.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.components.test;

import java.util.logging.Logger;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;


@Component(creator="Amit Kumar", description="TESTING COMPONENT", name="StringConcatenateComponent", tags="TEST")
public class StringConcatenateComponent implements ExecutableComponent {
	
	@ComponentProperty(defaultValue="true", description="debug default value", name="debug")
	private static final String DATA_PROP_1 = "debug";
	
	@ComponentProperty(defaultValue="false", description="print to screen?", name="printToScreen")
	private static final String DATA_PROP_2 = "printToScreen";
	
	
	

	@ComponentInput(description="input string1", name="string1")
	private static final String DATA_INPUT_1="string1";
	
	@ComponentInput(description="input string2", name="string2")
	private static final String DATA_INPUT_2="string2";
	
	
	@ComponentOutput(description="output string", name="string3")
	private static final String DATA_OUTPUT_1="string3";
	
	@ComponentOutput(description="debug value", name="debug")
	private static final String DATA_OUTPUT_2="debug";
	
	
	@ComponentOutput(description="print to screen Boolean object", name="printToScreen")
	private static final String DATA_OUTPUT_3="printToScreen";
	
	
	
	private Logger logger;
	
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
	
		String prop1 = ccp.getProperty(DATA_PROP_1);
		
		System.out.println("Property value: "+ prop1);
		logger = ccp.getLogger();
		logger.info("here is the logger info");
	

	}

	
	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		

	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String val1 =(String) cc.getDataComponentFromInput(DATA_INPUT_1);
		String val2 = (String)cc.getDataComponentFromInput(DATA_INPUT_2);
		String debugProp = cc.getProperty(DATA_PROP_1);
		String printToScreenProp = cc.getProperty(DATA_PROP_2);
		Boolean debugBoolean = Boolean.parseBoolean(debugProp);
		Boolean printToScreenBoolean = Boolean.parseBoolean(printToScreenProp);
		logger.info("Returning: " + val1+val2);
		if(printToScreenBoolean){
		System.out.println("Returning: " + val1+val2);	
		}
		
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, val1+val2);
		cc.pushDataComponentToOutput(DATA_OUTPUT_2, debugBoolean);
		cc.pushDataComponentToOutput(DATA_OUTPUT_3, printToScreenBoolean);
		
	}


}
