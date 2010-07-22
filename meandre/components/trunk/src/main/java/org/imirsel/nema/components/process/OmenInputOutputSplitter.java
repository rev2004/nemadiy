package org.imirsel.nema.components.process;

import java.io.File;
import java.io.IOException;
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
@Component(creator = "Kris West", description = "This component takes a process template and " +
		"models representing the data to process with it, resolves the data to file system " +
		"paths at NEMA sites and prepares individual sets of input and output files for " +
		"each of the sites that will be used.",
		name = "OmenInputOutputSplitter", tags = "profile process execution")
public class OmenInputOutputSplitter extends ContentRepositoryBase{
	
	private static String DEFAULT_SITE = "imirsel";
	
	
	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";
	
	@ComponentInput(description = "Process Template", name = "processTemplate")
	private static final String DATA_INPUT_TEMPLATE ="processTemplate";

	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects defining each track list (encoding any required metadata).", name = "DataToProcess")
	public final static String DATA_INPUT_DATA = "DataToProcess";

	@ComponentOutput(description = "Map of NemaTrackList to List of File Objects defining expected output files.", name = "expectedOutput")
	private static final String DATA_OUT_EXPECTED_OUTPUTS ="expectedOutput";

	@ComponentOutput(description = "Process template to be used to perform executions.", name = "processTemplate")
	private static final String DATA_OUT_PROCESS_TEMPLATE ="processTemplate";

	@ComponentOutput(description = "Class representing the output file type that is to be read.", name = "FileType")
	private static final String DATA_OUT_OUTPUT_TYPE ="FileType";

	@ComponentOutput(description = "Input files map", name = "inputFilesMap")
	private static final String DATA_OUT_INPUT_FILES_MAP ="inputFilesMap";

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
		
		getLogger().info("Getting inputs...");
		
		//get inputs
		task = (NemaTask)cc.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		pTemplate = (ProcessTemplate)cc.getDataComponentFromInput(DATA_INPUT_TEMPLATE);
		Map<NemaTrackList,List<NemaData>> dataToProcess = (Map<NemaTrackList,List<NemaData>>)cc.getDataComponentFromInput(DATA_INPUT_DATA);

		cc.getOutputConsole().println("Getting command formatting string...");
		//get command formatting string and parse
		CommandLineTemplate cTemplate = pTemplate.getCommandLineTemplate();
		String commandlineFormat = cTemplate.getCommandLineFormatter();
		getLogger().info("Parsing command formatting string: " + commandlineFormat);
		if (commandlineFormat.contains("\n")){
			commandlineFormat = commandlineFormat.replaceAll("\n", " ");
			cc.getOutputConsole().println("Command format string contained new line characters. These were replaced with spaces");
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
		cc.getOutputConsole().println(args);
		
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
		
		cc.getOutputConsole().println("Resolving tracks to audio paths...");
		
		//resolve tracks using repository
		RepositoryClientInterface client = null;
		try {
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			client.resolveTracksToFiles(dataToProcess,encodingConstraint);
		}catch(Exception e){
			throw new ComponentExecutionException("Exception occured while resolving tracks to files using properties: " + propsString,e);
		}finally {
			if(client!=null){
				RepositoryClientConnectionPool.getInstance().returnToPool(client);
			}
		}
		
		
		
		cc.getOutputConsole().println("Preparing process input files...");
		//retrieve list of audio files to process - other types not currently supported (as they have to written and marshalled over)
		  //subdivided by site
		Map<String,Map<NemaTrackList,List<String>>> siteToInputPaths = null;
		try {
			siteToInputPaths = FileConversionUtil.prepareOmenProcessInput(new File(getAbsoluteProcessWorkingDirectory()), task, dataToProcess, inputType1, DEFAULT_SITE);
		} catch (Exception e) {
			throw new ComponentExecutionException(e);
		}
		cc.getOutputConsole().println("got process input files spanning " + siteToInputPaths.size() + " sites");
		
		
		if(!inputType1.equals(RawAudioFile.class)) { // audio files are never distributed and don't go into JCR
			cc.getOutputConsole().println("Saving locally created input files to JCR...");
			//save to JCR and replace path with JCR URI.
			for(Iterator<String> siteIt = siteToInputPaths.keySet().iterator(); siteIt.hasNext();){
				String site = siteIt.next();
				int JcrForSite = 0;
				int totalForSite = 0;
				Map<NemaTrackList,List<String>> siteMap = siteToInputPaths.get(site);
				for(Iterator<List<String>> listIt = siteMap.values().iterator(); listIt.hasNext();){
					List<String> pathList = listIt.next();
					totalForSite += pathList.size();
					for(int i = 0;i<pathList.size();i++){
						String path = pathList.get(i);
						File aFile = new File(path);
						if (aFile.exists()){
							//is a local file that can go into JCR
							try {
								ResourcePath URI = saveFileToContentRepository(aFile, inputType1.getName());
								pathList.set(i, URI.getURIAsString());
								JcrForSite++;
							} catch (IOException e) {
								throw new ComponentExecutionException("Failed to save file: " + aFile.getAbsolutePath() + " to the JCR", e);
							} catch (ContentRepositoryServiceException e) {
								throw new ComponentExecutionException("Failed to save file: " + aFile.getAbsolutePath() + " to the JCR", e);
							}
						}else{
							getLogger().warning("Path '" + path + "' did not exist, assuming it is a path available at remote site");
						}
					}
				}
				cc.getOutputConsole().println("Saved " + JcrForSite + " of " + totalForSite + " input files for site '" + site + "' to the JCR");
			}
		}
		
		cc.getOutputConsole().println("Preparing process output file names...");
		//prepare per site output file names
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
		
		Map<String,Map<NemaTrackList,List<File>>> siteToOutputFiles = FileConversionUtil.createOMENOutputFileNames(
				dataToProcess, inputType1, outputTypeInstance, ExecutorConstants.REMOTE_PATH_TOKEN);
		cc.getOutputConsole().println("got process output files spanning " + siteToOutputFiles.size() + " sites");
		
		//merge into overall output files
		Map<NemaTrackList,List<File>> overallOutputFiles = new HashMap<NemaTrackList, List<File>>();
		for (Iterator<NemaTrackList> setIt = dataToProcess.keySet().iterator(); setIt.hasNext();) {
			NemaTrackList set = setIt.next();
			overallOutputFiles.put(set, new ArrayList<File>());
		}
		
		for (Iterator<Map<NemaTrackList,List<File>>> siteIt = siteToOutputFiles.values().iterator(); siteIt.hasNext();) {
			Map<NemaTrackList,List<File>> siteData = siteIt.next();
			for (Iterator<NemaTrackList> setIt = siteData.keySet().iterator(); setIt.hasNext();) {
				NemaTrackList set = setIt.next();
				List<File> setRemoteFiles = siteData.get(set);
				List<File> setLocalFiles = overallOutputFiles.get(set);
				for (Iterator<File> it = setRemoteFiles.iterator();it.hasNext();)
				{
					File file = it.next();
					File localFile = new File(file.getPath().replaceFirst(ExecutorConstants.REMOTE_PATH_TOKEN, getAbsoluteResultLocationForJob()));
					setLocalFiles.add(localFile);
				}
			}
		}
		
		cc.pushDataComponentToOutput(DATA_OUT_PROCESS_TEMPLATE, pTemplate);
		
		cc.pushDataComponentToOutput(DATA_OUT_OUTPUT_TYPE, outputType1);
		
		cc.pushDataComponentToOutput(DATA_OUT_INPUT_FILES_MAP, siteToInputPaths);
		
		cc.pushDataComponentToOutput(DATA_OUT_OUTPUT_FILES_MAP, siteToOutputFiles);
		
		cc.pushDataComponentToOutput(DATA_OUT_EXPECTED_OUTPUTS, overallOutputFiles);
		
	}
}
