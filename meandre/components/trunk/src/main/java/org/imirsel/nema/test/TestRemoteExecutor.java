package org.imirsel.nema.test;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.imirsel.nema.components.process.RemoteExecutorBase;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;


@Component(creator = "Amit Kumar", description = "Test Executor",
		name = "TestRemoteExecutor", tags = "test process execution")
public class TestRemoteExecutor extends RemoteExecutorBase {
	
	@ComponentOutput(description = "the message", name = "message")
	private static final String DATA_OUT_1 ="message";

	@Override
	public void initializeNema(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		this.getLogger().info("TestRemoteExecutor initialized");
	}
	

	@Override
	public void executeNema(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
		ProcessTemplate pTemplate = this.getProcessTemplate();
		
		this.getLogger().log(Level.INFO, "INFO MESSAGE");
		this.getLogger().log(Level.ALL, "ALL MESSAGE");
		this.getLogger().log(Level.CONFIG, "CONFIG MESSAGE");
		this.getLogger().log(Level.FINE, "FINE MESSAGE");
		this.getLogger().log(Level.FINER,"FINER");
		this.getLogger().log(Level.FINEST,"FINEST");
		this.getLogger().log(Level.SEVERE,"SEVERE");
		this.getLogger().log(Level.WARNING,"WARNING");
		
		System.out.println("HERE IS THE OUT STREAM");
		System.err.println("HERE IS THE ERR STREAM");
		
		
		this.getLogger().info("Process Template is: " + pTemplate.getName());
		
		componentContext.getOutputConsole().println("Print this to the output console...");

		ProcessExecutionProperties pep = new ProcessExecutionProperties();
		String uuid=UUID.randomUUID().toString();
		pep.setId(uuid);
		pep.setOutputs(null);
		pep.setInputs(null);
		pep.setCommandLineFlags(null);
	
	
		NemaProcess nemaProcess=null;
		try {
			nemaProcess=this.executeProcess(pep);
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e);
		} 

		try {
			this.waitForProcess(nemaProcess);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<ProcessArtifact> list = this.getResult(nemaProcess);
		
		if(list != null && list.size()>0){
			componentContext.getOutputConsole().println("got results pushing the results: " + list.get(0).getResourcePath());
		}else{
			componentContext.getOutputConsole().println("no result produced by the executor");
		}
		
		componentContext.pushDataComponentToOutput(DATA_OUT_1, System.currentTimeMillis()+"");

	}


	@Override
	public void disposeNema(
			ComponentContextProperties componentContextProperties)
			throws ComponentContextException {
		this.getLogger().info("TestRemoteExecutor disposed");

	}


}
