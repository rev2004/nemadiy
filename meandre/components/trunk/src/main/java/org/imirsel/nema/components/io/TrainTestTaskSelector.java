package org.imirsel.nema.components.io;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;


/**
 * 
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
@Component(creator = "Kris West", description = "Select a test/train task from the Nema Repository Service. "
	+ "Outputs 6 objects: \n"
	+ "1) a NemaTask Object defining the task,\n"
	+ "2) a NemaDataset Object defining the dataset,\n"
	+ "3) List NemaData Objects encoding the list of tracks to be used for feature extraction,\n"
	+ "4) List NemaData Objects encoding the list of tracks used in the experiment (with ground-truth data),\n"
	+ "5) A Map of test NemaTrackList Objects to a List NemaData Objects (with ground-truth data) encoding the test set data,\n"
	+ "6) A Map of test NemaTrackList Objects to a List NemaData Objects encoding the test set data.", 
	name = "TrainTestTaskSelector", 
	resources={"../../../../../RepositoryProperties.properties"},
tags = "input, collection, classification", firingPolicy = Component.FiringPolicy.all)
public class TrainTestTaskSelector extends NemaComponent {

	@ComponentOutput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_OUTPUT_NEMATASK = "NemaTask";

	@ComponentOutput(description = "NemaDataset Object defining the task.", name = "NemaDataset")
	public final static String DATA_OUTPUT_DATASET = "NemaDataset";

	@ComponentOutput(description = "List of NemaData Objects defining the feature extraction list.", name = "FeatureExtractionList")
	public final static String DATA_OUTPUT_FEATURE_EXTRACTION_LIST = "FeatureExtractionList";

	@ComponentOutput(description = "List of NemaData Objects defining the ground-truth list.", name = "GroundTruthList")
	public final static String DATA_OUTPUT_GROUNDTRUTH_LIST = "GroundTruthList";

	@ComponentOutput(description = "Map of NemaTrackList to List of NemaData Objects defining each training set (including ground-truth data).", name = "TrainingSet")
	public final static String DATA_OUTPUT_TRAINING_SETS = "TrainingSet";

	@ComponentOutput(description = "Map of NemaTrackList to List of NemaData Objects defining each test set (no ground-truth data).", name = "TestSets")
	public final static String DATA_OUTPUT_TEST_SETS = "TestSets";

	@StringDataType(renderer = CollectionRenderer.class)
	@ComponentProperty(defaultValue = "3", description = "The ID number of the classificaiton (train/test) task to be loaded.", name = "taskID")
	final static String DATA_PROPERTY_TASK_ID = "taskID";
	private int taskID = 3;
	
	@Override
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		taskID = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_TASK_ID));

		getLogger().info("Task ID " + taskID + " is selected.");
	}

	@Override
	public void dispose(ComponentContextProperties ccp)
			throws ComponentContextException {
		super.dispose(ccp);
	}
	private List<NemaData> getTestData(int trackListId,
			RepositoryClientInterface client) throws SQLException {
		List<NemaData> featExtractList;
		featExtractList = new ArrayList<NemaData>();
		List<NemaTrack> tracks = client.getTracks(trackListId);
		for (Iterator<NemaTrack> iterator = tracks.iterator(); iterator.hasNext();) {
			featExtractList.add(new NemaData(iterator.next().getId()));
		}
		return featExtractList;
	}
	
	
	
	private List<NemaData> getGroundtruthData(RepositoryClientInterface client, NemaTask task, int tracklist_id, int metadata_id) throws SQLException, IOException, IllegalArgumentException, InstantiationException, IllegalAccessException{
        getLogger().info("Retrieving ground-truth data and file paths for track list: " + tracklist_id);
        List<String> tracks = client.getTrackIDs(tracklist_id);
        
        Map<String,List<NemaMetadataEntry>> trackToMeta = client.getTrackMetadataByID(tracks, metadata_id);
        return FileConversionUtil.convertMetadataToGroundtruthModel(trackToMeta, task);
    }


	@Override
	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {

		NemaTask task = null;
		NemaDataset dataset = null;
		List<NemaData> featExtractList = null;
		List<NemaData> gtList = null;
		Map<NemaTrackList,List<NemaData>> trainingSets = new HashMap<NemaTrackList,List<NemaData>>();
		Map<NemaTrackList,List<NemaData>> testSets = new HashMap<NemaTrackList,List<NemaData>>();
		
		RepositoryClientInterface client = null;
		try {
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
			task = client.getTask(taskID);
			if(task == null){
				throw new ComponentExecutionException("Task id " + taskID + " was not found in the repository!");
			}
	        dataset = client.getDataset(task.getDatasetId());
	        if(dataset == null){
				throw new ComponentExecutionException("Dataset id " + task.getDatasetId() + 
						" was not found in the repository but was linked from task ID: " + taskID + " in the repository!");
			}

	        //produce feature extraction list
	        featExtractList = getTestData(dataset.getSubsetTrackListId(), client);
	        
	        //produce Ground-truth list
	        gtList = getGroundtruthData(client, task, dataset.getSubsetTrackListId(), task.getSubjectTrackMetadataId());
	        
	        //produce experiment sets
	        List<List<NemaTrackList>> sets = client.getExperimentTrackLists(dataset);
	        for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();){
	            List<NemaTrackList> list = it.next();
	            
	            String trackListType;
	            for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();){
	                NemaTrackList trackList = it1.next();
	                trackListType = trackList.getTrackListTypeName();

	                if (trackListType.equalsIgnoreCase("test")){
	                	testSets.put(trackList,getTestData(trackList.getId(), client));
	                }else{
	                	trainingSets.put(trackList,getGroundtruthData(client, task, trackList.getId(), task.getSubjectTrackMetadataId()));
	                }
	            }
	        }
	        
		} catch (Exception e) {
			throw new ComponentExecutionException("Exception in "
					+ this.getClass().getName(), e);
		}finally{
			if(client!=null){
				RepositoryClientConnectionPool.getInstance().returnToPool(client);
			}
		}

		// Push the data out
		ccp.pushDataComponentToOutput(DATA_OUTPUT_NEMATASK, task);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_DATASET, dataset);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_FEATURE_EXTRACTION_LIST, featExtractList);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_GROUNDTRUTH_LIST, gtList);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_TRAINING_SETS, trainingSets);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_TEST_SETS, testSets);
	}
	
}
