package org.imirsel.nema.test.components.remote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.RemoteProcessExecutorComponent;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "Test Remote Component Based on abstract class",
		name = "ExtendedRemoteComponent", tags = "test remote abstract")
public class ExtendedRemoteComponent extends RemoteProcessExecutorComponent {
	
	
	@ComponentProperty(defaultValue = "timeOfTheDay", description = "name of the execution Profile", name = "execProfile")
	private static final String PROPERTY_1 ="execProfile";
	
	@ComponentOutput(description = "This is the process artifact data",name = "processResult")
	private static final String DATA_OUTPUT_1="processResult";
	
	
	

	@Override
	public void execute(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
		    String profile = componentContext.getProperty(PROPERTY_1);
		    try {
		    	ProcessExecutionProperties pep = new ProcessExecutionProperties();
		    	
		    	ProcessArtifact pa = new ProcessArtifact("/tmp/0.date","file");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(pa);
				pep.setId(componentContext.getExecutionInstanceID());
				pep.setOutputs(outputs);
				pep.setInputs(null);
				ProcessTemplate pt = this.getProcessTemplate(profile);
				pep.setProcessTemplate(pt);
				pep.setInputs(null);
				pep.setOutputs(outputs);
				//pep.setEnvironmentVariables(envorinmentVariables);
				//pep.setCommandLineFlags(commandLineFlags);
				
				
				System.out.println("Running process now...");
				@SuppressWarnings("unused")
				final NemaProcess np=this.executeProcess(pep);
				System.out.println("After running process. Now waiting for the process to finish.");
				this.waitForProcess();
				System.out.println("Process finished -now getting result.");
				
				List<ProcessArtifact> list=this.getResult();
				if(list==null){
					throw new ComponentExecutionException("Process result is null");
				}else{
					componentContext.pushDataComponentToOutput(DATA_OUTPUT_1, list);
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			} catch (InvalidProcessMonitorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			}
		
	
	}

}
