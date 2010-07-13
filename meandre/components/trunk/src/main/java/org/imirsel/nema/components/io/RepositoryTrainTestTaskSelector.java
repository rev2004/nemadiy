package org.imirsel.nema.components.io;

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
import org.imirsel.nema.renderers.CollectionRenderer;
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
 * @author kriswest
 * 
 * @deprecated Use TaskSelector or TrainTestTaskSelector
 */
@Deprecated
@Component(creator = "Kris West and Mert Bay", description = "Selects a Train / Test dataset from NEMA repository service. "
		+ "Outputs 5 objects: \n"
		+ "1) a NemaTask Object defining the task,\n"
		+ "2) a NemaDataset Object defining the dataset,\n"
		+ "3) List NemaData Objects encoding the list of tracks used in the experiment,\n"
		+ "4) List NemaData Objects encoding the list of tracks used in the experiment (with ground-truth data),\n"
		+ "5) A Map of training NemaTrackList Objects to a List NemaData Objects encoding the training set with ground-truth data,\n"
		+ "6) A Map of test NemaTrackList Objects to a List NemaData Objects encoding the test set data", name = "RepositoryTrainTestTaskSelector", // resources={"RepositoryProperties.properties"},
tags = "input, collection, train/test", firingPolicy = Component.FiringPolicy.all)
public class RepositoryTrainTestTaskSelector extends NemaComponent {

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

	//TODO: where are we going with annotations - do I need a TaskRender for a menu containing 'tasks' from the repository (we get dataset from that)
	@StringDataType(renderer = CollectionRenderer.class)
	@ComponentProperty(defaultValue = "1", description = "The ID number of the Nema task to be loaded.", name = "taskID")
	final static String DATA_PROPERTY_TASK_ID = "taskID";
	private int taskID = 1;

	// TODO: move all of this to the where its needed on run binary components
	// @StringDataType(hide=true)//valueList={"","96k","128k"})
	// @ComponentProperty(defaultValue = "", description =
	// "bit rate for the mp3 files", name = "BitRate")
	// final static String DATA_PROPERTY_BIT_RATE = "BitRate";
	// private String bitRate = "";
	//	
	// @StringDataType(hide=true)//valueList={"1","2"})
	// @ComponentProperty(defaultValue = "1", description =
	// "Number of channels. 1 for mono 2 for stereo", name = "Channels")
	// final static String DATA_PROPERTY_CHANNELS = "Channels";
	// private String channels = "1";
	//	
	// @StringDataType(hide=true)//valueList={"30","full"})
	// @ComponentProperty(defaultValue = "30", description =
	// "30 sec clips or the full audio files", name = "Clip_Type")
	// final static String DATA_PROPERTY_CLIP_TYPE = "Clip_Type";
	// private String clip_type = "30";
	//	
	// @StringDataType(hide=true)//valueList={"mp3","wav"})
	// @ComponentProperty(defaultValue = "wav", description =
	// "Encoding type: mp3 or wav", name = "Encoding")
	// final static String DATA_PROPERTY_ENCODING = "Encoding";
	// private String encoding = "wav";
	//	
	// @StringDataType(hide=true)//valueList={"22050","44100"})
	// @ComponentProperty(defaultValue = "22050", description =
	// "Sampling rate: 22050 or 44100", name = "Sample_Rate")
	// final static String DATA_PROPERTY_SAMPLE_RATE = "Sample_Rate";
	// private String sample_rate = "22050";
	//	
	// @StringDataType(hide=true)//valueList={"\t",","})
	// @ComponentProperty(defaultValue = "\t", description =
	// "Delimiter for the ground-truth files", name = "Delim")
	// final static String DATA_PROPERTY_DELIM = "Delim";

//	private Set<NemaMetadataEntry> file_encoding_constraint;

	@Override
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		taskID = Integer.valueOf(ccp.getProperty(DATA_PROPERTY_TASK_ID));
//		bitRate = String.valueOf(ccp.getProperty(DATA_PROPERTY_BIT_RATE));
//		channels = String.valueOf(ccp.getProperty(DATA_PROPERTY_CHANNELS));
//		clip_type = String.valueOf(ccp.getProperty(DATA_PROPERTY_CLIP_TYPE));
//		encoding = String.valueOf(ccp.getProperty(DATA_PROPERTY_ENCODING));
//		sample_rate = String
//				.valueOf(ccp.getProperty(DATA_PROPERTY_SAMPLE_RATE));
//		delim = String.valueOf(ccp.getProperty(DATA_PROPERTY_DELIM));

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
	
	private List<NemaData> getGroundtruthData(RepositoryClientInterface client, int tracklist_id, int metadata_id) throws SQLException{
        getLogger().info("Retrieving ground-truth data and file paths for track list: " + tracklist_id);
        List<String> tracks = client.getTrackIDs(tracklist_id);
        List<NemaData> out = new ArrayList<NemaData>();
        String id;
        NemaData aTrack;
        NemaMetadataEntry metadata;
        List<NemaMetadataEntry> meta_list;

        //TODO update this to process metadata from DB with type class if necessary
        
        Map<String,List<NemaMetadataEntry>> trackToMeta = client.getTrackMetadataByID(tracks, metadata_id);
        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
            id = it.next();
            aTrack = new NemaData(id);
            
            meta_list = trackToMeta.get(id);
            if (meta_list.size() == 1){
            	metadata = meta_list.iterator().next();
            	aTrack.setMetadata(metadata.getType(), metadata.getValue());
            }else if(meta_list.size() > 1){
            	List<String> vals = new ArrayList<String>();
            	String type = meta_list.iterator().next().getType();
            	for (Iterator<NemaMetadataEntry> iterator = meta_list.iterator(); iterator.hasNext();) {
            		vals.add(iterator.next().getValue());
				}
            	aTrack.setMetadata(type, vals);
            }
            out.add(aTrack);
        }
        return out;
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
		
		try {
			RepositoryClientInterface client = new RepositoryClientImpl();
			task = client.getTask(taskID);
	        dataset = client.getDataset(task.getDatasetId());

	        //produce feature extraction list
	        featExtractList = getTestData(dataset.getSubsetTrackListId(), client);
	        
	        //produce Ground-truth list
	        gtList = getGroundtruthData(client, dataset.getSubsetTrackListId(), task.getSubjectTrackMetadataId());
	        
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
	                	trainingSets.put(trackList,getGroundtruthData(client, trackList.getId(), task.getSubjectTrackMetadataId()));
	                }
	            }
	        }
		} catch (SQLException e) {
			throw new ComponentExecutionException("SQLException in "
					+ this.getClass().getName(), e);
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
