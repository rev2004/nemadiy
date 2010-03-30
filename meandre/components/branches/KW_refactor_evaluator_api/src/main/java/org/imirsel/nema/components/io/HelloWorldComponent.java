package org.imirsel.nema.components.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
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
		
		System.out.println("CREATING FILES...");
		/*create two files as results*/
		try {
			
			String dir=ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).getResultLocationForJob(cc.getFlowExecutionInstanceID());
			File f = new File(dir,"result1.txt");
			f.createNewFile();
			logger.log(Level.INFO,"Create file "+ f.getAbsolutePath());
			File f1 = new File(dir,"result2.txt");
			f1.createNewFile();
			logger.log(Level.INFO,"Create file "+ f1.getAbsolutePath());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		cc.pushDataComponentToOutput(DATA_OUT_1,"Hello World");
	}

	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		// TODO Auto-generated method stub

	}
}
