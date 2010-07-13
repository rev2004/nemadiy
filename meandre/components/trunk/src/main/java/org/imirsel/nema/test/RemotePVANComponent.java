package org.imirsel.nema.test;

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
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @deprecated
 */
@Component(creator = "Mert Bay", description = "The remote execution component for phase vocoder analysis. C" 
		, name = "RemotePVANComponent", tags = "remote execution, PVAN",
		 firingPolicy = Component.FiringPolicy.all)
public class RemotePVANComponent extends RemoteProcessExecutorComponent {

	@ComponentInput(description="Input audio file", name="inputFile")
	final static String DATA_INPUT_1= "inputFile";
	
	@ComponentOutput(description = "Output Phase Vocoder Analysis File. Input filename appended with .pv.an",name = "pvanOut")
	private static final String DATA_OUTPUT_1="pvanOut";
	
	
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
			
		/*
				FileList object = (FileList) component.getDataComponentFromInput("TEST");
				ResourceLocator locator=this.getResourceLocator();
				String fileName=locator.materialize(object);
				String fname=locator.findByTrackId(id);
		*/	
				
			
				String inputFile[] = (String[])componentContext.getDataComponentFromInput(DATA_INPUT_1);
				
				ProcessArtifact pa = new ProcessArtifact(inputFile[0]+".pv.an","file");
				ProcessArtifact pa1 = new ProcessArtifact(inputFile[0],"file");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(pa);
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
				inputs.add(pa1);
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				pep.setId(componentContext.getExecutionInstanceID());
				pep.setOutputs(outputs);
				pep.setInputs(inputs);
				Map<String,String> map = new HashMap<String,String>();
				map.put("SNDANDIR","/share/apps/sndanweb/sndan");
				pep.setEnvironmentVariables(map);
				//pep.setCommandLineFlags(commandLineFlags);
				
				
				
				
			
		
		
		    try {
		    	System.out.println("Running process now...");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
	}


}
