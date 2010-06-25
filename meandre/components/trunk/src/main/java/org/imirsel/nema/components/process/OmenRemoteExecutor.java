package org.imirsel.nema.components.process;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.util.io.NemaFileType;
import org.imirsel.nema.analytics.util.process.CommandArgument;
import org.imirsel.nema.analytics.util.process.CommandLineFormatParser;
import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.InvalidProcessTemplateException;
import org.imirsel.nema.components.RemoteExecutorBase;
import org.imirsel.nema.model.CommandLineTemplate;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
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
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
@Component(creator = "Kris West", description = "This component performs " +
		"multiple remote executions of a binary using a process template, " +
		"sets of input files to feed it and output files for it to produce.",
		name = "OmenRemoteExecutor", tags = "profile process execution")
public class OmenRemoteExecutor extends RemoteExecutorBase {

	@ComponentInput(description = "Map of NemaTrackList to List of File Objects representing audio file paths to process.", name = "InputAudioFiles")
	public final static String DATA_IN_INPUT_PATHS = "InputAudioFiles";

	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects File Objects representing the paths that output files should be written to (after being retrieved from the JCR).", name = "OutputPaths")
	public final static String DATA_IN_OUTPUT_PATHS = "OutputPaths";

	@ComponentOutput(description = "Process Artifacts representing files produced by executions.", name = "ProcessArtifact")
	public final static String DATA_OUT_PROCESS_ARTIFACT = "ProcessArtifact";


	@Override
	public void initializeNema(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
	}

	@Override
	public void disposeNema(
			ComponentContextProperties componentContextProperties)
			throws ComponentContextException {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeNema(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		//get inputs
		ProcessTemplate pTemplate = this.getProcessTemplate();
		String group = this.getGroup();
		
		Map<String,Map<NemaTrackList,List<File>>> perSiteInputPaths = (Map<String,Map<NemaTrackList,List<File>>>)cc.getDataComponentFromInput(DATA_IN_INPUT_PATHS);
		Map<NemaTrackList,List<File>> inputPaths = perSiteInputPaths.get(group);
		
		Map<String,Map<NemaTrackList,List<File>>> perSiteOutputPaths = (Map<String,Map<NemaTrackList,List<File>>>)cc.getDataComponentFromInput(DATA_IN_OUTPUT_PATHS);
		Map<NemaTrackList,List<File>> outputPaths = perSiteOutputPaths.get(group);

		if(inputPaths == null){
			getLogger().info("Nothing to do for site '" + group + "'");
			return;
		}
		
		if(outputPaths == null){
			getLogger().warning("No output paths found for site '" + group + "'");
		}
		
		getLogger().info("Getting command formatting string...");
		//get command formatting string and parse
		CommandLineTemplate cTemplate = pTemplate.getCommandLineTemplate();
		String commandlineFormat = cTemplate.getCommandLineFormatter();
		getLogger().info("Parsing command formatting string: " + commandlineFormat);
		if (commandlineFormat.contains("\n")){
			commandlineFormat = commandlineFormat.replaceAll("\n", " ");
			getLogger().warning("Comamnd format string contained new line characters. These were replaced with spaces");
		}
		
		//parse and extract I/O classes and parameters
		CommandLineFormatParser formatModel = new CommandLineFormatParser(commandlineFormat);
		getLogger().info("Format string parsed as: " + formatModel.toConfigString());
		
		String args = "Number of command argument parts: " + formatModel.getArguments().size() + "\n"; 
		int count = 0;
		for (Iterator<CommandArgument> iterator = formatModel.getArguments().iterator(); iterator.hasNext();) {
			CommandArgument arg = iterator.next();
			args += "\t" + count++ + ": " + arg.toConfigString() + "\n";  
		}
		getLogger().info(args);
		
		//Extract constraints from inputs
		//only dealing with input 1 as this is a 1 input component
		Class<? extends NemaFileType> inputType1 = formatModel.getInputType(1);
		Map<String,String> properties1 = formatModel.getInputProperties(1);
		
		HashSet<NemaMetadataEntry> encodingConstraint = new HashSet<NemaMetadataEntry>();
		String propsString = "";
		if(properties1 != null) {
			getLogger().info("Processing audio encoding properties...");
			
			for (Iterator<String> iterator = properties1.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String val = properties1.get(key);
				if (!val.trim().equals("")) {
					encodingConstraint.add(new NemaMetadataEntry(key, val));
				}
				propsString += key + "=" + val;
				if (iterator.hasNext()) {
					propsString += ",";
				}
			}
		}else {
			getLogger().info("No audio encoding properties to process...");
		}
		
		//get output type
		Class<? extends NemaFileType> outputType1 = formatModel.getOutputType(1);
		
		
		//setup Process Execution Properties for each execution - this needs to be updated
		for (Iterator<NemaTrackList> setIt = inputPaths.keySet().iterator(); setIt.hasNext();) {
			NemaTrackList testSet = setIt.next();
			
			//create scratch dir
			String scratch = this.getAbsoluteProcessWorkingDirectory() + File.separator + "testSet" + testSet.getId();
			if(!new File(scratch).mkdirs()){
				throw new ComponentExecutionException("Failed to create scratch dir for a process execution at: " + scratch);
			}
			
			getLogger().info("Performing executions for test set " + testSet.getFoldNumber() + ", id: " + testSet.getId());
			List<File> inputs1ForFold = inputPaths.get(testSet);
			List<File> outputs1ForFold = outputPaths.get(testSet);
			if(outputs1ForFold == null){
				String msg = "\n\nNo output paths were found for test set " + testSet.getId() + " however inputs were available. " +
				"\nSets that had inputs: ";
				for(Iterator<NemaTrackList> it = inputPaths.keySet().iterator(); it.hasNext();){
					msg += "" + it.next().getId();
					if (it.hasNext()){
						msg += ", ";
					}
				}
				msg += "\nSets that had outputs: ";
				for(Iterator<NemaTrackList> it = outputPaths.keySet().iterator(); it.hasNext();){
					msg += "" + it.next().getId();
					if (it.hasNext()){
						msg += ", ";
					}
				}
				msg += "\n";
				getLogger().severe(msg);
			}
			
			//check inputs and outputTypes are matched if we are doing a 1 in 1 out process
			if (SingleTrackEvalFileType.class.isAssignableFrom(inputType1.getClass()) &&
					SingleTrackEvalFileType.class.isAssignableFrom(outputType1.getClass())) {
				if(inputs1ForFold.size() != outputs1ForFold.size()) {
					throw new ComponentExecutionException("List of input files (" + inputs1ForFold.size() + ") and output files (" + outputs1ForFold.size() + ") are different lengths, when they should be matched!");
				}
			}
			
			//execute for this fold, once per input file
			File inputFile,outputFile;
			for (int i=0;i<inputs1ForFold.size();i++) {
				inputFile = inputs1ForFold.get(i);
				if (outputs1ForFold.size() == 1) {
					//output is always the same file
					outputFile = outputs1ForFold.get(0);
				}else {
					outputFile = outputs1ForFold.get(i);
				}
				this.getLogger().info("Running for the output file: " + outputFile);
				formatModel.clearPreparedPaths();
				formatModel.setPreparedPathForInput(1, inputFile.getAbsolutePath());
				formatModel.setPreparedPathForOutput(1, outputFile.getAbsolutePath());
				
				//set scratch dir path
				formatModel.setPreparedPathForScratchDir(scratch);
				

				ProcessArtifact paInputs = new ProcessArtifact(inputFile.getAbsolutePath(),"File");
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
				inputs.add(paInputs);
				
				ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getAbsolutePath(),"File");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(paOutputs);
				
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				pep.setId(cc.getFlowExecutionInstanceID());
				pep.setOutputs(outputs);
				pep.setInputs(inputs);
				
				//set the formatted command arguments to run from formatModel
				String formattedArgs = formatModel.toFormattedString();
				pep.setCommandLineFlags(formattedArgs);
			
				getLogger().info("Executing process... " + i + "\n");
				NemaProcess nemaProcess=null;
				try {
					nemaProcess=this.executeProcess(pep);
				} catch (RemoteException e) {
					throw new ComponentExecutionException(e);
				} 
				getLogger().info("Executed process. Waiting for the process to end..." + i + "\n");
				try {
					this.waitForProcess(nemaProcess);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getLogger().info("Done Waiting..." +i +"\n");
				
				
				//We may not need to do this as we already know the paths to outputTypes on shared storage
				List<ProcessArtifact> list = this.getResult(nemaProcess);
				
				cc.pushDataComponentToOutput(DATA_OUT_PROCESS_ARTIFACT, list);
				
				// cleanup the process
				this.cleanProcess(nemaProcess);
				
			}	
		}
	}

}
