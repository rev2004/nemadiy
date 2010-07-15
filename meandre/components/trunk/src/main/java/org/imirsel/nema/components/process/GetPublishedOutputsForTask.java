package org.imirsel.nema.components.process;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
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
		"all the published results on the task and outputs a map of submission " +
		"code to its output data on teh specified task.", 
		name = "GetPublishedOutputsForTask",
		resources={"../../../../../RepositoryProperties.properties"},
		tags = "publish results repository", firingPolicy = Component.FiringPolicy.all)
public class GetPublishedOutputsForTask extends NemaComponent {

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";
	
	@ComponentOutput(description = "Job ID to Output files map", name = "jobIdToOutputFilesMap")
	private static final String DATA_OUT_OUTPUT_FILES_MAP ="jobIdToOutputFilesMap";

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
	
	@Override
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		NemaTask task = (NemaTask)ccp.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		
		RepositoryClientInterface client = null;
		Map<String,Map<NemaTrackList,List<File>>> output = null;
		Map<String,String> jobIdToJobName = null;
		try{
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			List<NemaPublishedResult> resultList = client.getPublishedResultsForTask(task.getId());
			
			Map<String,Map<Integer,List<File>>> subCodeTotrackListIdToFiles = new HashMap<String,Map<Integer,List<File>>>();
			jobIdToJobName = new HashMap<String, String>();
			for (NemaPublishedResult thisResult:resultList ){	
				String subCode = thisResult.getSubmissionCode();
				
				int setId = thisResult.getSetId();
				String path = thisResult.getResult_path();
				Map<Integer,List<File>> jobResults = subCodeTotrackListIdToFiles.get(subCode);
				if (jobResults == null){
					jobResults = new HashMap<Integer, List<File>>();
					subCodeTotrackListIdToFiles.put(subCode,jobResults);
					String subName = thisResult.getName();
					jobIdToJobName.put(subCode,subName);
				}
				
				List<File> files = jobResults.get(setId);
				if (files == null){
					files = new ArrayList<File>();
					jobResults.put(setId, files);
				}
				files.add(new File(path));
			}	
			
			output = new HashMap<String, Map<NemaTrackList,List<File>>>(subCodeTotrackListIdToFiles.size());
			
			for(Iterator<String> jobIt = subCodeTotrackListIdToFiles.keySet().iterator();jobIt.hasNext();){
				String jobId = jobIt.next();
				Map<Integer,List<File>> trackListIdToFiles = subCodeTotrackListIdToFiles.get(jobId);
				Map<NemaTrackList,List<File>> out = new HashMap<NemaTrackList, List<File>>(trackListIdToFiles.size());
				output.put(jobId,out);
				for (Iterator<Integer> iterator = trackListIdToFiles.keySet().iterator(); iterator.hasNext();) {
					int trackListId = iterator.next();
					NemaTrackList trackList = client.getTrackList(trackListId);
					if(trackList == null){
						throw new ComponentExecutionException("Failed to retreive NemaTrackList id: " + trackListId);
					}
					out.put(trackList,trackListIdToFiles.get(trackList));
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

		ccp.pushDataComponentToOutput(DATA_OUT_OUTPUT_FILES_MAP, output);
		ccp.pushDataComponentToOutput(DATA_OUT_JOBID_TO_JOBNAME_MAP, jobIdToJobName);
	}
	
	
		
}
