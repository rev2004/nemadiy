package org.imirsel.nema.components.process;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.imirsel.nema.analytics.util.process.CommandArgument;
import org.imirsel.nema.analytics.util.process.CommandLineFormatParser;
import org.imirsel.nema.model.CommandLineTemplate;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.ProcessExecutionProperties;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.model.fileTypes.MultipleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.fileTypes.OpaqueDirectoryFormat; 
import org.imirsel.nema.model.fileTypes.OpaqueFileFormat;
import org.imirsel.nema.model.fileTypes.SingleTrackEvalFileType;
import org.imirsel.nema.monitor.process.NemaProcess;
import org.imirsel.nema.service.executor.ExecutorConstants;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * TODO: THERE IS  BIG  IN THIS CODE _THE FLOW INSTANCE ID IS SET AS THE ProcessExecutionProperties.id
 * which means that there can only be one execution per flow LINE 217 -FIX REQUIRED -FOR AMIT
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
		
		Map<String,Map<NemaTrackList,List<String>>> perSiteInputPaths = (Map<String,Map<NemaTrackList,List<String>>>)cc.getDataComponentFromInput(DATA_IN_INPUT_PATHS);
		Map<NemaTrackList,List<String>> inputPaths = perSiteInputPaths.get(group);
		
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
			
			//scratch dir -this gets created at the executor end
			String scratch = ExecutorConstants.REMOTE_PATH_SCRATCH_TOKEN;
	
			getLogger().info("Performing executions for test set " + testSet.getFoldNumber() + ", id: " + testSet.getId());
			List<String> inputs1ForFold = inputPaths.get(testSet);
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
			String inputPath;
			File outputFile;
			for (int i=0;i<inputs1ForFold.size();i++) {
				inputPath = inputs1ForFold.get(i);
				if (outputs1ForFold.size() == 1) {
					//output is always the same file
					outputFile = outputs1ForFold.get(0);
				}else {
					outputFile = outputs1ForFold.get(i);
				}
				this.getLogger().info("Running for the output file: " + outputFile);
				formatModel.clearPreparedPaths();
				formatModel.setPreparedPathForInput(1, inputPath);
				
				//set scratch dir path
				formatModel.setPreparedPathForScratchDir(scratch);
				
				//add input file as process artifact (if its is a JCR URI rather than path it will be resolved at remote site)
				ProcessArtifact paInputs = new ProcessArtifact(inputPath, "File", inputType1.getClass().getName());
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
				
				
				inputs.add(paInputs);
				
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				
				
				if(OpaqueDirectoryFormat.class.isAssignableFrom(outputType1)){
					//output is a directory rather than a file!
					ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getPath() + File.separator, "Directory", outputType1.getClass().getName());
					outputs.add(paOutputs);
					formatModel.setPreparedPathForOutput(1, outputFile.getPath() + File.separator);
				}else if(OpaqueFileFormat.class.isAssignableFrom(outputType1)){
					//output is a file
					ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getPath(), "File", outputType1.getClass().getName());
					outputs.add(paOutputs);
					formatModel.setPreparedPathForOutput(1, outputFile.getPath());
				}else if (MultipleTrackEvalFileType.class.isAssignableFrom(inputType1) &&  SingleTrackEvalFileType.class.isAssignableFrom(outputType1)){
					//output is a directory rather than a file!
					ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getPath() + File.separator, "Directory", outputType1.getClass().getName());
					outputs.add(paOutputs);
					formatModel.setPreparedPathForOutput(1, outputFile.getPath() + File.separator);
				}else{
					//output is a file
					ProcessArtifact paOutputs = new ProcessArtifact(outputFile.getPath(), "File", outputType1.getClass().getName());
					outputs.add(paOutputs);
					formatModel.setPreparedPathForOutput(1, outputFile.getPath());
				}
				
				
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				String uuid=UUID.randomUUID().toString();
				pep.setId(uuid);
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
				getLogger().info("Done Waiting..." +i +"\n Now wait to get the result");
				
				//We may not need to do this as we already know the paths to outputTypes on shared storage
				List<ProcessArtifact> list = this.getResult(nemaProcess);
				
				if(list.size()>0){
					getLogger().info("got results pushing the results: " + list.get(0).getResourcePath());
				}else{
					getLogger().info("Error -could not get the results; Aborting the flow");
					throw new ComponentExecutionException("Error -no output results");
				}
				cc.pushDataComponentToOutput(DATA_OUT_PROCESS_ARTIFACT, list);
				
				/*ProcessArtifact processArtifact= list.get(0);
				System.out.println("PROCESS ARTIFACT IN OMEN REMOTE EXECUTOR: " + processArtifact.getResourcePath());
				String path=processArtifact.getResourcePath();
				int loc=path.indexOf("://");
				path = path.substring(loc+"://".length());
				String workspaceName="default";
				String pcol ="jcr";
				
				System.out.println("PROTOCOL: " + pcol + " workspaceName: " + workspaceName + " path: " + path);
				RepositoryResourcePath rrp = new RepositoryResourcePath(pcol, workspaceName, path);
				try {
					NemaResult result=this.getResultStorageService().getNemaResult(this.getCredentials(), rrp);
					System.out.println("====> IN OMEN EXECUTOR " + result.getFileContent().length);
				} catch (ContentRepositoryServiceException e) {
					System.out.println("ERROR "+ e);
				}*/
				
				
				// cleanup the process
				this.cleanProcess(nemaProcess);
				
			}	
		}
	}

}
