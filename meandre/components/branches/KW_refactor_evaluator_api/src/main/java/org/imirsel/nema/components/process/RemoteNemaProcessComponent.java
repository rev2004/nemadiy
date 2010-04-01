package org.imirsel.nema.components.process;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.EvalFileType;
import org.imirsel.nema.analytics.evaluation.EvalFileUtil;
import org.imirsel.nema.components.InvalidProcessMonitorException;
import org.imirsel.nema.components.InvalidProcessTemplateException;
import org.imirsel.nema.components.RemoteProcessExecutorComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
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


@Component(creator = "kris.west@gmail.com", description = "Remote execution component that converts input NEMA models" +
		" into file formats (specified in the command formatting template of the chosen process), using file paths that" +
		" will be valid on the remote machine, transfers the input files to the remote machine and executes the process." +
		" The output of the process is harvested from the remote machine and read back into NEMA models and output." 
		, name = "RemoteNemaProcessComponent", tags = "remote execution",
		 firingPolicy = Component.FiringPolicy.all)
public class RemoteNemaProcessComponent extends RemoteProcessExecutorComponent {


	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";

	@ComponentInput(description = "NemaDataset Object defining the task.", name = "NemaDataset")
	public final static String DATA_INPUT_DATASET = "NemaDataset";

	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects defining each track list (encoding any required metadata).", name = "TestSets")
	public final static String DATA_INPUT_DATA = "DataToProcess";

	@ComponentOutput(description = "Map of NemaTrackList to List of NemaData Objects defining each track list (encoding metadata generated by the remote process).",name = "PredictedData")
	private static final String DATA_OUTPUT="PredictedData";
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {		
		super.initialize(ccp);
		
	}
	
	public void dispose(ComponentContextProperties ccp)
	throws ComponentContextException {
		//super.dispose(ccp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		
		Map<NemaTrackList,List<File>> inputFiles = null;
		Map<NemaTrackList,List<File>> outputFiles = null;
		NemaTask task = null;
		NemaDataset dataset = null;
		
		try {
			//get inputs
			task = (NemaTask)cc.getDataComponentFromInput(DATA_INPUT_NEMATASK);
			dataset = (NemaDataset)cc.getDataComponentFromInput(DATA_INPUT_DATASET);
			Map<NemaTrackList,List<NemaData>> dataToProcess = (Map<NemaTrackList,List<NemaData>>)cc.getDataComponentFromInput(DATA_INPUT_DATA);
		
		
			//resolve track IDs to paths on disk as viewed on the remote machine 
			  //and set as NemaDataConstants.PROP_FILE_LOCATION metadata on each NemaData Object
			//TODO get any audio encoding constraints set in the ProcessTemplate (e.g. mono 22khz wav) and use them in the resolution to file paths.
			
			/*
				FileList object = (FileList) component.getDataComponentFromInput("TEST");
				ResourceLocator locator=this.getResourceLocator();
				String fileName=locator.materialize(object);
				String fname=locator.findByTrackId(id);
			 */	
		
			//perform conversion of input data into required formats
			//TODO get real input file type from somewhere
			Class<? extends EvalFileType> fileType = null;
			inputFiles = EvalFileUtil.prepareProcessInput(new File(getAbsoluteProcessWorkingDirectory()), task, dataToProcess, fileType);
			
			//prepare output file names
			//TODO confirm that we should be creating local paths for the outputs rather than somehow discovering the correct root output path and using that to create the output paths
			outputFiles = EvalFileUtil.createOutputFileNames(dataToProcess, fileType, ".out", new File(getAbsoluteResultLocationForJob()));
		
		}catch(Exception e) {
			throw new ComponentExecutionException("Failed to prepare data for remote execution",e);
		}
			
		try {
			//setup remote execution
			
			//TODO have this execute once per fold (preferably with N executions of the remote process where it processes one file from the fold at a time)
			for (Iterator<NemaTrackList> setIt = inputFiles.keySet().iterator(); setIt
					.hasNext();) {
				NemaTrackList testSet = setIt.next();
				List<File> inputsForFold = inputFiles.get(testSet);
				List<File> outputsForFold = outputFiles.get(testSet);
				
				//Fudged code here as I want to send multiple inputs to cause multiple runs where there is more than one file for the fold
				ProcessArtifact paInputs = new ProcessArtifact(outputsForFold,"List<File>");
				List<ProcessArtifact> inputs = new ArrayList<ProcessArtifact>();
				inputs.add(paInputs);
				
				ProcessArtifact paOutputs = new ProcessArtifact(inputsForFold,"List<File>");
				List<ProcessArtifact> outputs = new ArrayList<ProcessArtifact>();
				outputs.add(paOutputs);
				
				ProcessExecutionProperties pep = new ProcessExecutionProperties();
				pep.setId(cc.getExecutionInstanceID());
				pep.setOutputs(outputs);
				pep.setInputs(inputs);
				
				//do I need this? Should prob be in/come from ProcessTemplate with some rewriting logic to be corect on each machine
//				Map<String,String> map = new HashMap<String,String>();
//				map.put("SNDANDIR","/share/apps/sndanweb/sndan");
//				pep.setEnvironmentVariables(map);
				
				//wanna move this into process template
				//pep.setCommandLineFlags(commandLineFlags);
				
				getLogger().info("Executing process...");
				@SuppressWarnings("unused")
				final NemaProcess np=this.executeProcess(pep);
				getLogger().info("Executed process. Waiting for the process to end...");
				this.waitForProcess();
				
				//do we need this if we already know the paths we are expecting the results to get copied over to?
				//List<ProcessArtifact> list = this.getResult();
			}
			
			
		} catch(Exception e) {
			throw new ComponentExecutionException("Exception occurred while executing remote process",e);
		}
		
		
		try {
			getLogger().info("Process ended. Processing results...");
			
			//read and interpret results from all folds
			//TODO get real output file type from somewhere
			Class<? extends EvalFileType> outputFileType = null;
			Map<NemaTrackList,List<NemaData>> outputData = EvalFileUtil.readProcessOutput(outputFiles, task, outputFileType);
			
			if(outputData==null){
				throw new ComponentExecutionException("Process result is null");
			}else{
				cc.pushDataComponentToOutput(DATA_OUTPUT, outputData);
			}
		} catch(Exception e) {
			throw new ComponentExecutionException("Exception occurred while interpreting the results from the remote process",e);
		}	
		
	}


}
