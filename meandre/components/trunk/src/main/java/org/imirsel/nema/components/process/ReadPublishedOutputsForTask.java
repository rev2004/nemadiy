package org.imirsel.nema.components.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * Retrieves all published output for a task id and outputs it as
 * a Map of submission code to a map of NemaTrackList to a list Files 
 * representing the output.
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
@Component(creator = "Kris West", description = "Takes a task ID and retrieves " +
		"all the published results on the task, reads them up and outputs a map " +
		"of submission code to a datastructure containing NemaData Objects " +
		"representing the data produced on the specified task, filtered to only " +
		"include the test sets (eliminating feature extraction and training sets " +
		"etc.).", 
		name = "ReadPublishedOutputsForTask",
		resources={"../../../../../RepositoryProperties.properties"},
		tags = "publish results repository", firingPolicy = Component.FiringPolicy.all)
public class ReadPublishedOutputsForTask extends NemaComponent {

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";
	
	@ComponentInput(description = "Map of NemaTrackList to a List of NemaData Objects defining each test set (no ground-truth data).", name = "TestSets")
	public final static String DATA_INPUT_TEST_SETS = "TestSets";
	
	@ComponentOutput(description = "Job ID to Output data map", name = "jobIdToOutputDataMap")
	private static final String DATA_OUT_OUTPUT_DATA_MAP ="jobIdToOutputDataMap";

	@ComponentOutput(description = "Job ID to Job Name map", name = "jobIdToJobNameMap")
	private static final String DATA_OUT_JOBID_TO_JOBNAME_MAP ="jobIdToJobNameMap";

	@Override
	public void initialize(ComponentContextProperties cc) throws ComponentExecutionException, ComponentContextException {
		super.initialize(cc);
	}
	
	@Override
	public void dispose(ComponentContextProperties cc) throws ComponentContextException {
		super.dispose(cc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		NemaTask task = (NemaTask)ccp.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		Map<NemaTrackList,List<NemaData>> testSets = (Map<NemaTrackList,List<NemaData>>)ccp.getDataComponentFromInput(DATA_INPUT_TEST_SETS);
		
		RepositoryClientInterface client = null;
		Map<String,Map<NemaTrackList,List<NemaData>>> output = null;
		Map<String,String> jobIdToJobName = null;
		Map<File,Class<NemaFileType>> pathToType = null;
		Map<String,Class<NemaFileType>> typeNameToClassCache = null;
		try{
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			
			ccp.getOutputConsole().println("Retrieving published outputs for task: " + task.getId());
			
			List<NemaPublishedResult> resultList = client.getPublishedResultsForTask(task.getId());
			
			Map<String,Map<Integer,List<File>>> subCodeTotrackListIdToFiles = new HashMap<String,Map<Integer,List<File>>>();
			pathToType = new HashMap<File, Class<NemaFileType>>();
			jobIdToJobName = new HashMap<String, String>();
			typeNameToClassCache = new HashMap<String, Class<NemaFileType>>();
			for (NemaPublishedResult thisResult:resultList ){	
				String subCode = thisResult.getSubmissionCode();
				
				int setId = thisResult.getSetId();
				File path = new File(thisResult.getResult_path());
				String typeName = thisResult.getFileType();
				Class<NemaFileType> typeClass = typeNameToClassCache.get(typeName);
				if (typeClass == null){
					try {
						typeClass = (Class<NemaFileType>)Class.forName(typeName);
					} catch (ClassNotFoundException e) {
						throw new ComponentExecutionException("Failed to resolve type '" + typeName + "' for file '" + path.getAbsolutePath() + "', submission code '" + subCode + "'",e);
					}
					typeNameToClassCache.put(typeName, typeClass);
				}
				pathToType.put(path, typeClass);
				
				Map<Integer,List<File>> jobResults = subCodeTotrackListIdToFiles.get(subCode);
				if (jobResults == null){
					jobResults = new HashMap<Integer, List<File>>();
					subCodeTotrackListIdToFiles.put(subCode,jobResults);
					//String subName = thisResult.getName();
					jobIdToJobName.put(subCode,subCode);
				}
				
				List<File> files = jobResults.get(setId);
				if (files == null){
					files = new ArrayList<File>();
					jobResults.put(setId, files);
				}
				files.add(path);
			}	
			
			output = new HashMap<String, Map<NemaTrackList,List<NemaData>>>(subCodeTotrackListIdToFiles.size());
			
			for(Iterator<String> jobIt = subCodeTotrackListIdToFiles.keySet().iterator();jobIt.hasNext();){
				String jobId = jobIt.next();
				Map<Integer,List<File>> trackListIdToFiles = subCodeTotrackListIdToFiles.get(jobId);
				Map<NemaTrackList,List<NemaData>> out = new HashMap<NemaTrackList, List<NemaData>>(trackListIdToFiles.size());
				output.put(jobId,out);
				for (Iterator<Integer> iterator = trackListIdToFiles.keySet().iterator(); iterator.hasNext();) {
					int trackListId = iterator.next();
					NemaTrackList trackList = client.getTrackList(trackListId);
					if(trackList == null){
						throw new ComponentExecutionException("Failed to retreive NemaTrackList id: " + trackListId);
					}
					if(!testSets.containsKey(trackList)){
						ccp.getOutputConsole().println("Ignoring data for set " + trackListId + " from job " + jobId + " as it was not in the list of test sets.");
						continue;
					}
					List<File> filesToRead = trackListIdToFiles.get(trackListId);
					List<NemaData> setDataList = new ArrayList<NemaData>();
					for (File file:filesToRead){
						Class<NemaFileType> fileType = pathToType.get(file);
						List<NemaData> data;
						try {
							data = FileConversionUtil.readData(file, task, fileType);
						} catch (Exception e) {
							throw new ComponentExecutionException("Failed to read file: " + file.getAbsolutePath(),e);
						}
						setDataList.addAll(data);
					}
					
					out.put(trackList,setDataList);
				}
			}
			
			
			
			
		} catch (SQLException e) {
			throw new ComponentExecutionException("SQLException in " + this.getClass().getName(),e);
		} 
		finally{
			if(client!=null){
				RepositoryClientConnectionPool.getInstance().returnToPool(client);
			}
		}

		ccp.pushDataComponentToOutput(DATA_OUT_OUTPUT_DATA_MAP, output);
		ccp.pushDataComponentToOutput(DATA_OUT_JOBID_TO_JOBNAME_MAP, jobIdToJobName);
	}
	
	
		
}
