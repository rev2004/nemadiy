package org.imirsel.nema.test.components.remote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.InvalidProcessTemplateException;
import org.imirsel.nema.components.RemoteProcessExecutorComponent;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.service.executor.NemaProcess;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * @deprecated
 * @author kumaramit01
 * @since 0.2.0
 */
@Component(creator = "Amit Kumar", description = "Test Remote Component Based on abstract class",
		name = "ExtendedRemoteComponent", tags = "test remote abstract")
public class ExtendedRemoteComponent extends RemoteProcessExecutorComponent {
	
	
	@ComponentOutput(description = "This is the process artifact data",name = "processResult")
	private static final String DATA_OUTPUT_1="processResult";
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
	}
	
	public void dispose(ComponentContextProperties ccp)
	throws ComponentContextException {
		//super.dispose(ccp);
	}

	
	

	@Override
	public void execute(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
				ProcessArtifact pa = new ProcessArtifact("/tmp/0.date","file");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(pa);
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				pep.setId(componentContext.getExecutionInstanceID());
				pep.setOutputs(outputs);
				pep.setInputs(null);
				//pep.setEnvironmentVariables(envorinmentVariables);
				//pep.setCommandLineFlags(commandLineFlags);
				
		
		
		    try {
		    	System.out.println("Running process now: " + this.getProfileName());
				@SuppressWarnings("unused")
				final NemaProcess np=this.executeProcess(pep);
				System.out.println("After running process. Now waiting for the process to finish.");
				this.waitForProcess(np);
				System.out.println("Process finished -now getting result.");
				
				List<ProcessArtifact> list=this.getResult(np);
				if(list==null){
					throw new ComponentExecutionException("Process result is null");
				}else{
					componentContext.pushDataComponentToOutput(DATA_OUTPUT_1, list);
				}
				this.cleanProcess(np);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			} catch (InvalidProcessMonitorException e) {
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			} catch (InvalidProcessTemplateException e) {
				e.printStackTrace();
				throw new ComponentExecutionException(e);
			}
		
	
	}

}
