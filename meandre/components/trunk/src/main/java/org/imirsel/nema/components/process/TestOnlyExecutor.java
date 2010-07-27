package org.imirsel.nema.components.process;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
@Component(creator = "Kris West", description = "This component performs " +
		"multiple executions of a train/test binary using a process template, " +
		"sets of input training files, input test files and a resource path " +
		"to feed it and output files for it to produce.",
		name = "TestOnlyExecutor", tags = "profile process execution")
public class TestOnlyExecutor extends RemoteExecutorBase {

	@ComponentInput(description = "Testing Input files map (Map<NemaTrackList,List<File>>)", name = "$i1: TestingInputFilesMap")
	private static final String DATA_IN_TESTING_INPUT_FILES_MAP ="$i1: TestingInputFilesMap";
	
	@ComponentInput(description = "Map of Resource directory paths where the required resources have been made available (Map<NemaTrackList,List<File>>)", name = "$i2: ResourcePaths")
	private static final String DATA_IN_RESOURCE_DIR ="$i2: ResourcePaths";
	
	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects File Objects representing the paths that output files should be written to.", name = "OutputPaths")
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
		
		
		Map<NemaTrackList,List<File>> inputTestPaths = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_IN_TESTING_INPUT_FILES_MAP);
		Map<NemaTrackList,List<File>>  resourcePaths = (Map<NemaTrackList,List<File>> )cc.getDataComponentFromInput(DATA_IN_RESOURCE_DIR);
			
		Map<NemaTrackList,List<File>> outputPaths = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_IN_OUTPUT_PATHS);

		if(outputPaths == null){
			cc.getOutputConsole().println("No output paths found!");
		}
		
		cc.getOutputConsole().println("Getting command formatting string...");
		//get command formatting string and parse
		CommandLineTemplate cTemplate = pTemplate.getCommandLineTemplate();
		String commandlineFormat = cTemplate.getCommandLineFormatter();
		cc.getOutputConsole().println("Parsing command formatting string: " + commandlineFormat);
		if (commandlineFormat.contains("\n")){
			commandlineFormat = commandlineFormat.replaceAll("\n", " ");
			getLogger().warning("Comamnd format string contained new line characters. These were replaced with spaces");
		}
		
		//parse and extract I/O classes and parameters
		CommandLineFormatParser formatModel = new CommandLineFormatParser(commandlineFormat);
		cc.getOutputConsole().println("Format string parsed as: " + formatModel.toConfigString());
		
		String args = "Number of command argument parts: " + formatModel.getArguments().size() + "\n"; 
		int count = 0;
		for (Iterator<CommandArgument> iterator = formatModel.getArguments().iterator(); iterator.hasNext();) {
			CommandArgument arg = iterator.next();
			args += "\t" + count++ + ": " + arg.toConfigString() + "\n";  
		}
		getLogger().fine(args);
		
		//Extract constraints from inputs
		//only dealing with input 1 as this is a 1 input component
		Class<? extends NemaFileType> inputType1 = formatModel.getInputType(1);
		Map<String,String> properties1 = formatModel.getInputProperties(1);
		
		HashSet<NemaMetadataEntry> encodingConstraint = new HashSet<NemaMetadataEntry>();
		String propsString = "";
		if(properties1 != null) {
			cc.getOutputConsole().println("Processing audio encoding properties...");
			
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
			cc.getOutputConsole().println("No audio encoding properties to process...");
		}
		
		//get output type
		Class<? extends NemaFileType> outputType1 = formatModel.getOutputType(1);
		
		//count number of executions to perform
		int executionTotal = 0;
		for (Iterator<NemaTrackList> setIt = inputTestPaths.keySet().iterator(); setIt.hasNext();) {
			NemaTrackList testSet = setIt.next();
			List<File> inputs1ForFold = inputTestPaths.get(testSet);
			executionTotal += inputs1ForFold.size();
		}
		
		
		//sort test sets by fold number
		List<NemaTrackList> testSets = new ArrayList<NemaTrackList>(inputTestPaths.keySet());
		
		Comparator<NemaTrackList> setCompare = new Comparator<NemaTrackList>() {
			public int compare(NemaTrackList o1, NemaTrackList o2) {
				return o1.getFoldNumber() - o2.getFoldNumber();
			}
		};

		Collections.sort(testSets,setCompare);
		
		if (!resourcePaths.keySet().containsAll(testSets)){
			throw new ComponentExecutionException("Did not receive resource paths for each test set!");
		}
		
		
		//setup Process Execution Properties for each execution
		int executionCount = 0;
		int foldCount = 0;
		for (int s=0;s<testSets.size();s++) {
			NemaTrackList testSet = testSets.get(s);
			//scratch dir -this gets created at the executor end
			String scratch = ExecutorConstants.REMOTE_PATH_SCRATCH_TOKEN;
	
			cc.getOutputConsole().println("Performing executions for fold " + testSet.getFoldNumber() + ", test set " + testSet.getId());
			List<File> inputs1ForFold = inputTestPaths.get(testSet);
			List<File> inputsResourcesForFold = resourcePaths.get(testSet);
			
			List<File> outputs1ForFold = outputPaths.get(testSet);
			if(outputs1ForFold == null){
				String msg = "No output paths were found for test set " + testSet.getId() + " however inputs were available. ";
				cc.getOutputConsole().println(msg);
			}
			
			//check inputs and outputTypes are matched if we are doing a 1 in 1 out process
			if (SingleTrackEvalFileType.class.isAssignableFrom(inputType1.getClass()) &&
					SingleTrackEvalFileType.class.isAssignableFrom(outputType1.getClass())) {
				if(inputs1ForFold.size() != outputs1ForFold.size()) {
					throw new ComponentExecutionException("List of input files (" + inputs1ForFold.size() + ") and output files (" + outputs1ForFold.size() + ") are different lengths, when they should be matched!");
				}
			}
			
			//check we either have one resource path per fold or same number as test files
			if(inputsResourcesForFold == null){
				throw new ComponentExecutionException("The resource paths for test set " + testSet.getId() + " were null!");
			}
			
			if(inputsResourcesForFold.size() != 1){
				if(inputsResourcesForFold.size() != inputs1ForFold.size()){
					throw new ComponentExecutionException("The number of resource paths (" + inputsResourcesForFold.size() + ") for test set " + testSet.getId() + " was not 1 or equal to the number of test inputs (" + inputs1ForFold.size() + ")");
				}
			}
			
			//execute for this fold, once per input file
			File resourcePath;
			File inputTestPath;
			File outputFile;
			for (int i=0;i<inputs1ForFold.size();i++) {
				inputTestPath = inputs1ForFold.get(i);
				if (inputsResourcesForFold.size() == 1){
					//resource path is always the same for this fold
					resourcePath = inputsResourcesForFold.get(0);
				}else{
					resourcePath = inputsResourcesForFold.get(i);
				}
				if (outputs1ForFold.size() == 1) {
					//output is always the same file
					outputFile = outputs1ForFold.get(0);
				}else {
					outputFile = outputs1ForFold.get(i);
				}
				cc.getOutputConsole().println("Running for the output file: " + outputFile);
				formatModel.clearPreparedPaths();
				
				formatModel.setPreparedPathForInput(1, inputTestPath.getAbsolutePath());
				formatModel.setPreparedPathForInput(2, resourcePath.getAbsolutePath());
				
				//set scratch dir path
				formatModel.setPreparedPathForScratchDir(scratch);
				
				//add input file as process artifact (if its is a JCR URI rather than path it will be resolved before execution)
				ProcessArtifact paTestInput = new ProcessArtifact(inputTestPath.getAbsolutePath(), "File", inputType1.getClass().getName());
				ProcessArtifact paResourceInput;
				if (resourcePath.isDirectory()){
					paResourceInput = new ProcessArtifact(resourcePath.getAbsolutePath(), "Directory", OpaqueDirectoryFormat.class.getName());
				}else{
					paResourceInput = new ProcessArtifact(resourcePath.getAbsolutePath(), "File", OpaqueFileFormat.class.getName());
				}
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>(2);
				inputs.add(paTestInput);
				inputs.add(paResourceInput);
				
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>(1);
				
				
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
			
				cc.getOutputConsole().println("Executing process... " + (i+1) + " of " + inputs1ForFold.size() + " for fold " + foldCount + " of " + testSets.size());
				NemaProcess nemaProcess=null;
				try {
					nemaProcess=this.executeProcess(pep);
				} catch (RemoteException e) {
					throw new ComponentExecutionException(e);
				} 
//				getLogger().info("Executed process. Waiting for the process to end..." + i);
				try {
					this.waitForProcess(nemaProcess);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				getLogger().info("Done Waiting..." +i +"\n Now wait to get the result");
				
				//We may not need to do this as we already know the paths to outputTypes on shared storage
				List<ProcessArtifact> list = this.getResult(nemaProcess);
				
				if(list != null && list.size()>0){
					cc.getOutputConsole().println("got results pushing the results: " + list.get(0).getResourcePath());
				}else{
					cc.getOutputConsole().println("Error -could not get the results; Aborting the flow");
					throw new ComponentExecutionException("Error -no output results");
				}
				
				cc.getOutputConsole().println("== Completed " + ++executionCount + " of " + executionTotal + " executions ==");
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
				if(cc.isFlowAborting()){
					throw new ComponentExecutionException("Flow is aborting");
				}
			}	
		}
	}

}
