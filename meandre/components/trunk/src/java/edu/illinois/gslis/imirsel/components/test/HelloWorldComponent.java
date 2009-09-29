package edu.illinois.gslis.imirsel.components.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.meandre.annotations.Component;
import org.meandre.annotations.*;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

@Component(creator = "Amit Kumar", description = "hello world component",
		name = "HelloWorldComponent", tags = "test")
public class HelloWorldComponent implements ExecutableComponent {
	
	@ComponentProperty(defaultValue = "10", description = "sleep time", name = "sleepTime")
	private static final String DATA_PROP_1 ="sleepTime";
	
	@ComponentOutput(description = "output string", name = "output")
	private static final String DATA_OUT_1 = "output";
	
	private Logger logger;


	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
			logger=ccp.getLogger();
	}
	
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String time=cc.getProperty(DATA_PROP_1);
		int sleepTime = Integer.parseInt(time);
		logger.log(Level.INFO, "Sleep time: "+ sleepTime + " secs");
		try {
			Thread.sleep(sleepTime*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.log(Level.INFO, "Pushing data out...");
		cc.pushDataComponentToOutput(DATA_OUT_1,"Hello World");
	}

	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub

	}
}
