/*
 * @(#) JndiComponentGetServiceTest.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.imirsel.nema.components.io;

import java.util.logging.Logger;

import org.imirsel.nema.directoryservice.JndiHelper;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;


@Component(creator="Amit Kumar", description="does testing", name="JndiComponentGetService", tags="test jndi")
public class JndiComponentServiceTest implements ExecutableComponent {
	private Logger logger;
	
	@ComponentOutput(description="data source 1 testing", name="dataSource1")
	private static final String DATA_OUTPUT_1="dataSource2";
	
	

	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub

	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
	
		
		logger.info("getting data source 2");
		Object obj2 = null;
		try {
			obj2=JndiHelper.getJobStatusDataSource();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logger.info("pushing datasources out");
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, obj2);
	}

	public void initialize(ComponentContextProperties ccp) {
		logger = ccp.getLogger();

	}

}
