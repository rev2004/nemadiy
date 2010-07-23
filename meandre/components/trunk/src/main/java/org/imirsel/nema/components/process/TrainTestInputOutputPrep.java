package org.imirsel.nema.components.process;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.util.process.CommandArgument;
import org.imirsel.nema.analytics.util.process.CommandLineFormatParser;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.model.CommandLineTemplate;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.ProcessTemplate;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.fileTypes.ClassificationTextFile;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.fileTypes.RawAudioFile;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.service.executor.ExecutorConstants;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
@Component(creator = "Kris West", description = "This component takes a process " +
		"template for a classifier train and classify binary, models that represent " +
		"the training data and test queries to process with it, resolves the data to file system " +
		"paths and prepares a set of input and output files that will be used. Finally, " +
		"a model representing a resource directory (which may be empty or contain feature " +
		"feature files produced by another job) is also taken as input so that it can be " +
		"made available to the process. ",
		name = "TrainTestInputOutputPrep", tags = "profile process execution")
public class TrainTestInputOutputPrep extends ContentRepositoryBase{
	
	private static String DEFAULT_SITE = "imirsel";
	
	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";
	
	@ComponentInput(description = "Process Template", name = "processTemplate")
	private static final String DATA_INPUT_TEMPLATE ="processTemplate";

	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects defining each training track list (encoding required metadata).", name = "$i1: TrainingDataToProcess")
	public final static String DATA_INPUT_TRAINING_DATA = "$i1: TrainingDataToProcess";

	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects defining each test track list (with no extra metadata).", name = "$i2: TestDataToProcess")
	public final static String DATA_INPUT_TESTING_DATA = "$i2: TestDataToProcess";
	
	@ComponentInput(description = "Map of NemaTrackList to List of File Objects defining each defining resources that will be made available in working directory for the process.", name = "$i3: Resources")
	public final static String DATA_INPUT_RESOURCE_DIR = "$i3: Resources";

	@ComponentOutput(description = "Process template to be used to perform executions.", name = "processTemplate")
	private static final String DATA_OUT_PROCESS_TEMPLATE ="processTemplate";

	@ComponentOutput(description = "Class representing the output file type that is to be read.", name = "FileType")
	private static final String DATA_OUT_OUTPUT_TYPE ="FileType";

	@ComponentOutput(description = "Training Input files map", name = "$i1: TrainingInputFilesMap")
	private static final String DATA_OUT_TRAINING_INPUT_FILES_MAP ="$i1: TrainingInputFilesMap";
	
	@ComponentOutput(description = "Testing Input files map", name = "$i2: TestingInputFilesMap")
	private static final String DATA_OUT_TESTING_INPUT_FILES_MAP ="$i2: TestingInputFilesMap";
	
	@ComponentOutput(description = "Resource directory path where the required resources have been made available", name = "$i3: ResourceDir")
	private static final String DATA_OUT_RESOURCE_DIR ="$i3: ResourceDir";

	@ComponentOutput(description = "Output files map", name = "outputFilesMap")
	private static final String DATA_OUT_OUTPUT_FILES_MAP ="outputFilesMap";

	@Override
	public void initializeNema(ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException {

	}

	@Override
	public void disposeNema(ComponentContextProperties ccp) throws ComponentContextException {

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeNema(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		
		
		NemaTask task = null;
		ProcessTemplate pTemplate = null;
		
		cc.getOutputConsole().println("Executing TrainTestInputOutputPrep...");
		
		//get inputs
		task = (NemaTask)cc.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		pTemplate = (ProcessTemplate)cc.getDataComponentFromInput(DATA_INPUT_TEMPLATE);
		Map<NemaTrackList,List<NemaData>> trainDataToProcess = (Map<NemaTrackList,List<NemaData>>)cc.getDataComponentFromInput(DATA_INPUT_TRAINING_DATA);
		Map<NemaTrackList,List<NemaData>> testDataToProcess = (Map<NemaTrackList,List<NemaData>>)cc.getDataComponentFromInput(DATA_INPUT_TESTING_DATA);
		Map<NemaTrackList,List<File>> resourceDataToProcess = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_INPUT_RESOURCE_DIR);

		cc.getOutputConsole().println("TrainTestInputOutputPrep received:\n" +
				"\t" + trainDataToProcess.size() + " training sets\n" +
				"\t" + testDataToProcess.size() + " testing sets\n" + 
				"\t" + resourceDataToProcess.size() + " resource file sets.");
		
		String rsc = "Resource file sets contain:\n";
		for(NemaTrackList list:resourceDataToProcess.keySet()){
			rsc += "\tid: " + list.getId() + "\t" + resourceDataToProcess.get(list).size() + " paths\n";
		}
		cc.getOutputConsole().println(rsc);
		
		cc.getOutputConsole().println("Getting command formatting string...");
		CommandLineTemplate cTemplate = pTemplate.getCommandLineTemplate();
		String commandlineFormat = cTemplate.getCommandLineFormatter();
		cc.getOutputConsole().println("Parsing command formatting string: " + commandlineFormat);
		if (commandlineFormat.contains("\n")){
			commandlineFormat = commandlineFormat.replaceAll("\n", " ");
			cc.getOutputConsole().println("Comamnd format string contained new line characters. These were replaced with spaces");
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
		getLogger().info(args);
		
		//Extract constraints from inputs
		//only dealing with input 1 as this is a 1 input component
		Class<? extends NemaFileType> inputTypeTraining = formatModel.getInputType(1);
		Map<String,String> propertiesTraining = formatModel.getInputProperties(1);
		
		Class<? extends NemaFileType> inputTypeTest = formatModel.getInputType(2);
		Map<String,String> propertiesTest = formatModel.getInputProperties(2);
		
		//don't care about type of resources, just copy 'em over and pass on the path
//		Class<? extends NemaFileType> inputTypeResources = formatModel.getInputType(1);
//		Map<String,String> propertiesResources = formatModel.getInputProperties(1);
		
		HashSet<NemaMetadataEntry> encodingConstraintTraining = new HashSet<NemaMetadataEntry>();
		if(propertiesTraining != null) {
			cc.getOutputConsole().println("Processing audio encoding for training properties...");
			
			for (Iterator<String> iterator = propertiesTraining.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String val = propertiesTraining.get(key);
				if (!val.trim().equals("")) {
					encodingConstraintTraining.add(new NemaMetadataEntry(key, val));
				}
			}
		}else {
			cc.getOutputConsole().println("No audio encoding properties for testing to process...");
		}
		
		HashSet<NemaMetadataEntry> encodingConstraintTesting = new HashSet<NemaMetadataEntry>();
		if(propertiesTest != null) {
			cc.getOutputConsole().println("Processing audio encoding for testing properties...");
			
			for (Iterator<String> iterator = propertiesTest.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String val = propertiesTraining.get(key);
				if (!val.trim().equals("")) {
					encodingConstraintTesting.add(new NemaMetadataEntry(key, val));
				}
			}
		}else {
			cc.getOutputConsole().println("No audio encoding properties for testing to process...");
		}
		
		
		cc.getOutputConsole().println("Resolving tracks to audio paths...");
		
		//resolve tracks using repository
		RepositoryClientInterface client = null;
		try {
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			try {
				client.resolveTracksToFiles(trainDataToProcess,encodingConstraintTraining);
			}catch(Exception e){
				throw new ComponentExecutionException("Exception occured while resolving training tracks to files using properties: " + encodingConstraintTraining,e);
			}
			try{
				client.resolveTracksToFiles(testDataToProcess,encodingConstraintTesting);
			}catch(Exception e){
				throw new ComponentExecutionException("Exception occured while resolving test tracks to files using properties: " + encodingConstraintTesting,e);
			}
		}catch(SQLException e){
			throw new ComponentExecutionException("Exception occured while retrieving repository client",e);
		}finally {
			if(client!=null){
				RepositoryClientConnectionPool.getInstance().returnToPool(client);
			}
		}
		
		cc.getOutputConsole().println("Preparing process training input files...");
		Map<NemaTrackList,List<File>> inputTrainingPaths = null;
		try {
			inputTrainingPaths = FileConversionUtil.prepareProcessInput(new File(getAbsoluteProcessWorkingDirectory()), task, trainDataToProcess, inputTypeTraining);
		} catch (Exception e) {
			throw new ComponentExecutionException(e);
		}
		
		cc.getOutputConsole().println("Preparing process test input files...");
		Map<NemaTrackList,List<File>> inputTestPaths = null;
		try {
			inputTestPaths = FileConversionUtil.prepareProcessInput(new File(getAbsoluteProcessWorkingDirectory()), task, testDataToProcess, inputTypeTest);
		} catch (Exception e) {
			throw new ComponentExecutionException(e);
		}
		
		cc.getOutputConsole().println("Preparing resource directory...");
		
		//throw exception if more than one resource as we don't yet know what to do with that
		File resource = null;
		if (resourceDataToProcess.size() > 0)
		{
			if (resourceDataToProcess.size() > 1)
			{
				throw new ComponentExecutionException("Received resources for more than one trackList to copy, this is not curently supported");
			}else if(resourceDataToProcess.values().iterator().next().size() > 1){
				throw new ComponentExecutionException("Received more than one resource to copy, this is not curently supported");
			}
			resource = resourceDataToProcess.values().iterator().next().get(0);
		}else{
			cc.getOutputConsole().println("No resources found to use");
		}
		
		
		
		
		
		cc.getOutputConsole().println("Preparing process output file names...");
		//prepare output file names
		Class<? extends NemaFileType> outputType1 = formatModel.getOutputType(1);
		Map<String,String> outputProperties1 = formatModel.getOutputProperties(1);
		NemaFileType outputTypeInstance=null;
		try {
			outputTypeInstance = outputType1.newInstance();
		} catch (InstantiationException e) {
			throw new ComponentExecutionException(e);
		} catch (IllegalAccessException e) {
			throw new ComponentExecutionException(e);
		}
		if(outputType1.equals(ClassificationTextFile.class)) {
			((ClassificationTextFile)outputTypeInstance).setMetadataType(task.getSubjectTrackMetadataName());
		}
		
		Map<NemaTrackList,List<File>> outputFiles = FileConversionUtil.createOutputFileNames(
				testDataToProcess, inputTypeTest, outputTypeInstance, new File(getAbsoluteResultLocationForJob()));
		cc.getOutputConsole().println("got process output files spanning " + outputFiles.size() + " sites");
		
		
		
		cc.pushDataComponentToOutput(DATA_OUT_PROCESS_TEMPLATE, pTemplate);
		
		cc.pushDataComponentToOutput(DATA_OUT_OUTPUT_TYPE, outputType1);
		
		cc.pushDataComponentToOutput(DATA_OUT_TRAINING_INPUT_FILES_MAP, inputTrainingPaths);
		
		cc.pushDataComponentToOutput(DATA_OUT_TESTING_INPUT_FILES_MAP, inputTestPaths);
		
		cc.pushDataComponentToOutput(DATA_OUT_RESOURCE_DIR, resource);
		
		cc.pushDataComponentToOutput(DATA_OUT_OUTPUT_FILES_MAP, outputFiles);
		
	}
}
