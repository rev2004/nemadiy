/*
 * @(#) PrintObject.java @VERSION@
 *
 * Copyright (c) 2007+ Amit Kumar
 *
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.imirsel.nema.components.io;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;

/**This component prints the string representation of the object.
 * If the object is an array it prints each array list member
 *
 * @author Amit Kumar
 * Created on Feb 13, 2008 10:53:16 AM
 *
 */
@Component(name="PrintObject",creator="Amit Kumar",
description="Print Object information", tags="print object")
public class PrintObject implements ExecutableComponent {

	@ComponentInput(description="Object Information", name="object" )
	final static String DATA_PORT_IN_1 ="object";
	

	private Logger logger;

	public void initialize(ComponentContextProperties ccp) {
		logger = ccp.getLogger();
	}


	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		Object object= (Object)cc.getDataComponentFromInput(DATA_PORT_IN_1);
		if(object.getClass().isArray()){
			Object[] objects = (Object[]) object;
			List l =Arrays.asList(objects);
			Iterator<Object> itObject=	l.iterator();
			while(itObject.hasNext()){
				System.out.println(itObject.next().toString());
			}
		}else{
			System.out.println(object.toString());
		}
			
	}
	


	public void dispose(ComponentContextProperties ccp) {

	}

}
