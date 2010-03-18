package org.imirsel.nema.components;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.service.executor.ProcessExecutorService;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

public abstract class RemoteProcessExecutorComponent implements ExecutableComponent {

	@ComponentProperty(defaultValue = "timeOfTheDay", description = "name of the execution Profile", name = "execProfile")
	private static final String PROPERTY_1 ="execProfile";
	@ComponentProperty(defaultValue = "localhost", description = "Executor Service Host", name = "host")
	private static final String PROPERTY_2 = "host";
	
	@ComponentProperty(defaultValue = "1098", description = "Executor Service port", name = "port")
	private static final String PROPERTY_3 = "port";
	
	
	@ComponentProperty(defaultValue = "ExecutorService", description = "Executor Service Name", name = "serviceName")
	private static final String PROPERTY_4 = "serviceName";

	
	private CountDownLatch latch = new CountDownLatch(1);
	private BlockingQueue<List<ProcessArtifact>> resultQueue = new LinkedBlockingQueue<List<ProcessArtifact>>();
	
	private ProcessExecutorService  executorService;
	
	
	
	
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}


	public abstract void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException;

	


	public void dispose(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}

	
	
	public ProcessExecutorService getExecutorService() {
		return executorService;
	}


}
