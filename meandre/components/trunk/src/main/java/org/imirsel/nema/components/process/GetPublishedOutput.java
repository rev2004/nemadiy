package org.imirsel.nema.components.process;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**
 * Retrieves published output for task id and submission code and outputs it as
 * a Map of NemaTrackList to a list Files representing the output.
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
@Component(creator = "Kris West", description = "Takes the collection ID and pushes the names, paths for the publised results for the collection and associated groundtruth", name = "GetPublishedOutput",resources={"../../../../RepositoryProperties.properties"},
		tags = "publish results repository", firingPolicy = Component.FiringPolicy.all)
public class GetPublishedOutput extends NemaComponent {

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";
	
	@ComponentInput(description = "The submission code of the results to be retrieved.", name = "submissionCode")
	public final static String DATA_INPUT_SUBMISSION_CODE = "submissionCode";
	
	@ComponentOutput(description = "Output files map", name = "outputFilesMap")
	private static final String DATA_OUT_OUTPUT_FILES_MAP ="outputFilesMap";

	
	public void initialize(ComponentContextProperties cc) throws ComponentExecutionException, ComponentContextException {
		super.initialize(cc);
	}
	
	public void dispose(ComponentContextProperties cc) throws ComponentContextException {
		super.dispose(cc);
	}
	
	public void execute(ComponentContext ccp)
	throws ComponentExecutionException, ComponentContextException {
		NemaTask task = (NemaTask)ccp.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		String submissionCode = (String)ccp.getDataComponentFromInput(DATA_INPUT_SUBMISSION_CODE);
		
		RepositoryClientInterface client = null;
		Map<NemaTrackList,List<File>> output = null;
		try{
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			List<NemaPublishedResult> resultList = client.getPublishedResultsForTaskAndSubmissionCode(task.getId(), submissionCode);
			
			Map<Integer,List<File>> trackListIdToFiles = new HashMap<Integer,List<File>>();
			
			for (NemaPublishedResult thisResult:resultList ){	
				int setId = thisResult.getSetId();
				String path = thisResult.getResult_path();
				List<File> files = trackListIdToFiles.get(setId);
				if (files == null){
					files = new ArrayList<File>();
					trackListIdToFiles.put(setId, files);
				}
				files.add(new File(path));
			}	
			
			output = new HashMap<NemaTrackList,List<File>>(trackListIdToFiles.size());
			for (Iterator<Integer> iterator = trackListIdToFiles.keySet().iterator(); iterator.hasNext();) {
				int trackListId = iterator.next();
				NemaTrackList trackList = client.getTrackList(trackListId);
				if(trackList == null){
					throw new ComponentExecutionException("Failed to retreive NemaTrackList id: " + trackListId);
				}
				output.put(trackList,trackListIdToFiles.get(trackList));
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
	}
	
	
		
}
