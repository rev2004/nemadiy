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


@Component(creator="Amit Kumar", description="TESTING COMPONENT", name="StringToCharacterComponent", tags="TEST")
public class StringToCharacterComponent implements ExecutableComponent {
	
	@ComponentProperty(defaultValue="true", description="debug default value", name="debug")
	private static final String DATA_PROP_1 = "debug";
	
	

	@ComponentInput(description="input string1", name="string1")
	private static final String DATA_INPUT_1="string1";
	

	
	@ComponentOutput(description="output char", name="singleChar")
	private static final String DATA_OUTPUT_1="singleChar";
	
	@ComponentOutput(description="debug value", name="debug")
	private static final String DATA_OUTPUT_2="debug";
	
	
	private Logger logger;
	
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		logger = ccp.getLogger();
		logger.info("here is the logger info " + this.getClass().getName());
	}

	
	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		

	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String val1 =(String) cc.getDataComponentFromInput(DATA_INPUT_1);
		String debugProp = cc.getProperty(DATA_PROP_1);
		Boolean debugBoolean = Boolean.parseBoolean(debugProp);
		logger.info("returning as character array on char at a time " + val1 );
		for(char c:val1.toCharArray()){
			if(c=='i'){
				continue;
			}
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, c);
		}
		
		//cc.pushDataComponentToOutput(DATA_OUTPUT_1, 'x');
		
		cc.pushDataComponentToOutput(DATA_OUTPUT_2, debugBoolean);
		
	}


}
