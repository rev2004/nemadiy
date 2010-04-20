/*
 * @(#) PropertyToInput.java @VERSION@
 *
 * Copyright (c) 2007+ Amit Kumar
 *
 * The software is released under GNU GPL, Please
 * read License.txt
 *
 */
package org.imirsel.nema.components.io;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.Component;
import org.meandre.annotations.*;


@Component(name="PropertyToString",creator="Amit Kumar", description="Convert Property to String input",
		tags="io property string")
public class PropertyToInput implements ExecutableComponent {


	@ComponentProperty(description="property to print as string", name="propertyValue", defaultValue = "test" )
	final static String DATA_PROPERTY_1 =  "propertyValue";


	//OUTPUT
	@ComponentOutput(description="string value from property", name="stringValue")
	final static String DATA_OUTPUT_1 = "stringValue";


	

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String propertyValue = cc.getProperty(DATA_PROPERTY_1);
		cc.pushDataComponentToOutput(DATA_OUTPUT_1,  propertyValue);
	}



	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}

	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}

}
