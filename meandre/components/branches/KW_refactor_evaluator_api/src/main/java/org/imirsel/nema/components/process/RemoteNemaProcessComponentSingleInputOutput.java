package org.imirsel.nema.components.process;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.imirsel.nema.analytics.util.process.CommandArgument;
import org.imirsel.nema.analytics.util.process.CommandLineFormatParser;
import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.InvalidProcessTemplateException;
import org.imirsel.nema.components.RemoteProcessExecutorComponent;
import org.imirsel.nema.model.CommandLineTemplate;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "This component takes input string array of files and pushes the string array of files produced by the execution of binary code.", name = "RemoteSingleInputOutput", tags = "remote")
public class RemoteNemaProcessComponentSingleInputOutput extends RemoteProcessExecutorComponent{
	
	@ComponentInput(description = "String input files", name = "inputFiles")
	public final static String DATA_INPUT_1 = "inputFiles";
	
	@ComponentInput(description = "Extension", name = "extension")
	public final static String DATA_INPUT_2 = "extension";
	
	
	@ComponentOutput(description = "String input files", name = "outputFiles")
	public final static String DATA_OUTPUT_1 = "outputFiles";



	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		String[] inputFiles = (String[]) cc.getDataComponentFromInput(DATA_INPUT_1);
		String extn = (String) cc.getDataComponentFromInput(DATA_INPUT_2);
		ProcessTemplate pTemplate = null;
		try {
			pTemplate = this.getProcessTemplate();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (InvalidProcessTemplateException e1) {
			e1.printStackTrace();
		}
		CommandLineTemplate cTemplate = pTemplate.getCommandLineTemplate();
		String commandlineFormat = cTemplate.getCommandLineFormatter();
		getLogger().info("Parsing command formatting string: " + commandlineFormat);
		if (commandlineFormat.contains("\n")){
			commandlineFormat = commandlineFormat.replaceAll("\n", " ");
			getLogger().warning("Comamnd format string contained new line characters. These were replaced with spaces");
		}
		CommandLineFormatParser formatModel = new CommandLineFormatParser(commandlineFormat);
		getLogger().info("Format string parsed as: " + formatModel.toConfigString());
		
		String args = "Number of command argument parts: " + formatModel.getArguments().size() + "\n"; 
		int count = 0;
		for (Iterator<CommandArgument> iterator = formatModel.getArguments().iterator(); iterator.hasNext();) {
			CommandArgument arg = iterator.next();
			args += "\t" + count++ + ": " + arg.toConfigString() + "\n";  
		}
		getLogger().info(args);
	
		ArrayList<String> result = new ArrayList<String>();
		int count1=0;
		for(String fname:inputFiles){
			File file = new File(fname);
			File outputFile = new File(fname+extn);
			
			formatModel.clearPreparedPaths();
			formatModel.setPreparedPathForInput(1, file.getAbsolutePath());
			formatModel.setPreparedPathForOutput(1, outputFile.getAbsolutePath());
		
			
			ProcessArtifact paInputs = new ProcessArtifact(file.getAbsolutePath(),"File");
			List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
			inputs.add(paInputs);
			
			ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getAbsolutePath(),"File");
			List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
			outputs.add(paOutputs);
			
			ProcessExecutionProperties pep = new ProcessExecutionProperties();
			pep.setId(cc.getExecutionInstanceID()+""+count1);
			pep.setOutputs(outputs);
			pep.setInputs(inputs);
			
			String formattedArgs = formatModel.toFormattedString();
			pep.setCommandLineFlags(formattedArgs);
			
			getLogger().info("Arguments to the shell script are: " + formattedArgs);
		
			
			NemaProcess nemaProcess=null;
			try {
				nemaProcess=this.executeProcess(pep);
			} catch (RemoteException e) {
				throw new ComponentExecutionException(e);
			} catch (InvalidProcessMonitorException e) {
				throw new ComponentExecutionException(e);
			} catch (InvalidProcessTemplateException e) {
				throw new ComponentExecutionException(e);
			}
			getLogger().info("Executed process. Waiting for the process to end..." + count1 + "\n");
			try {
				this.waitForProcess(nemaProcess);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getLogger().info("Done Waiting..." +count1 +"\n");
			//We do not need to do this as we already know the paths to outputTypes on shared storage
			List<ProcessArtifact> list = this.getResult(nemaProcess);
			String fileName=list.get(0).getResourcePath();
			System.out.println("Got file: " + fileName);
			result.add(fileName);
			// cleanup the process
			this.cleanProcess(nemaProcess);
		}
		
		String[] fileList = result.toArray(new String[result.size()]);
		
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, fileList);
		
		
	}

}
