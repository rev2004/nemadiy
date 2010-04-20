package org.imirsel.nema.test.components.remote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.InvalidProcessTemplateException;
import org.imirsel.nema.components.RemoteProcessExecutorComponent;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @author kumaramit01
 * @since 0.2.0
 */
@Component(creator = "Amit Kumar", description = "Test Remote Component Based on abstract class",
		name = "ExtendedRemoteComponent", tags = "test remote abstract")
public class ExtendedRemoteComponent extends RemoteProcessExecutorComponent {
	
	
	@ComponentOutput(description = "This is the process artifact data",name = "processResult")
	private static final String DATA_OUTPUT_1="processResult";
	
	
	@ComponentProperty(defaultValue = "/tmp/1.date", description = "cmdline for the shell script", name = "cmdLine")
	private static final String DATA_PROPERTY_1 = "cmdLine";
	
	
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
		
				String cmdLine = componentContext.getProperty(DATA_PROPERTY_1);
				ProcessArtifact pa = new ProcessArtifact("/tmp/0.date","file");
				ProcessArtifact pa1 = new ProcessArtifact("/tmp/1.date","file");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(pa);
				
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
				inputs.add(pa1);
				
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				pep.setId(componentContext.getExecutionInstanceID());
				// output must be set... it must list the files/directory expected
				// that that the binary will create...
				pep.setOutputs(outputs);
				// setting inputs is optional 
				pep.setInputs(inputs);
				Map<String,String> map = new HashMap<String,String>();
				map.put("SNDANDIR","/share/apps/sndanweb/sndan");
				pep.setEnvironmentVariables(map);
				
				try {
					String commandLineTemplate=this.getProcessTemplate().getCommandLineTemplate().getCommandLineFormatter();
					pep.setCommandLineFlags(cmdLine);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidProcessTemplateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
		    try {
		    	System.out.println("Running process now: " + this.getProfileName());
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
