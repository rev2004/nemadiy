package org.imirsel.nema.components.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
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


@Component(creator = "Kris West and Mert Bay", description = "Select a Melody task from the Nema Repository Service. "
		+ "Outputs 5 objects: \n"
		+ "1) a NemaTask Object defining the task,\n"
		+ "2) a NemaDataset Object defining the dataset,\n"
		+ "3) List NemaData Objects encoding the list of tracks used in the experiment (with ground-truth data),\n"
		+ "4) A Map of test NemaTrackList Objects to a List NemaData Objects encoding the test set data", name = "MelodyTaskSelector", resources={"../../../../../RepositoryProperties.properties"},
tags = "input, collection, train/test", firingPolicy = Component.FiringPolicy.all)
public class MelodyTaskSelector extends NemaComponent {

	@ComponentOutput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_OUTPUT_NEMATASK = "NemaTask";

	@ComponentOutput(description = "NemaDataset Object defining the task.", name = "NemaDataset")
	public final static String DATA_OUTPUT_DATASET = "NemaDataset";

	@ComponentOutput(description = "List of NemaData Objects defining the ground-truth list.", name = "GroundTruthList")
	public final static String DATA_OUTPUT_GROUNDTRUTH_LIST = "GroundTruthList";

	@ComponentOutput(description = "Map of NemaTrackList to List of NemaData Objects defining each test set (no ground-truth data).", name = "TestSets")
	public final static String DATA_OUTPUT_TEST_SETS = "TestSets";

	//TODO: where are we going with annotations - do I need a TaskRender for a menu containing 'tasks' from the repository (we get dataset from that)
	@StringDataType(renderer = CollectionRenderer.class)
	@ComponentProperty(defaultValue = "0", description = "The ID number of the Melody Nema task to be loaded.", name = "taskID")
	final static String DATA_PROPERTY_TASK_ID = "taskID";
	private int taskID = 1;

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
	
	private NemaData convertToMelodyDataObject(String id, NemaMetadataEntry metadata) throws IOException {
		//write meta to file and read up again
		//TODO do this in memory
		File temp = File.createTempFile(id + ".", ".txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		out.write(metadata.getValue());
		out.flush();
		out.close();
		
		MelodyTextFile reader = new MelodyTextFile();
		NemaData aTrack = reader.readFile(temp);
		aTrack.setMetadata(NemaDataConstants.PROP_ID, id);
		return aTrack;
	}
	
	private List<NemaData> getGroundtruthData(RepositoryClientInterface client, int tracklist_id, int metadata_id) throws SQLException, IOException{
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
            
            meta_list = trackToMeta.get(id);
            if (meta_list.size() > 0){
            	metadata = meta_list.iterator().next();
            	aTrack = convertToMelodyDataObject(id, metadata);
            	
            	if(meta_list.size() > 1) {
            		//use only first value and print warning
            		getLogger().warning("Found " + meta_list.size() + " melody metadata records for id '" + id + "', using only first value");
            	}
            }else {
            	getLogger().severe("No melody metadata found for id '" + id + "', it is being dropped!");
            	continue;
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
		List<NemaData> gtList = null;
		Map<NemaTrackList,List<NemaData>> testSets = new HashMap<NemaTrackList,List<NemaData>>();
		
		try {
			RepositoryClientInterface client = new RepositoryClientImpl();
			task = client.getTask(taskID);
	        dataset = client.getDataset(task.getDatasetId());

	        //produce Ground-truth list
	        gtList = getGroundtruthData(client, dataset.getSubsetTrackListId(), task.getSubjectTrackMetadataId());
	        
	        //produce experiment sets
	        List<List<NemaTrackList>> sets = client.getExperimentTrackLists(dataset);
	        for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();){
	            List<NemaTrackList> list = it.next();
	            
	            for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();){
	                NemaTrackList trackList = it1.next();
	                testSets.put(trackList,getTestData(trackList.getId(), client));
	            }
	        }
		} catch (Exception e) {
			throw new ComponentExecutionException("Exception in "
					+ this.getClass().getName(), e);
		}

		// Push the data out
		ccp.pushDataComponentToOutput(DATA_OUTPUT_NEMATASK, task);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_DATASET, dataset);
		ccp.pushDataComponentToOutput(DATA_OUTPUT_GROUNDTRUTH_LIST, gtList);		
		ccp.pushDataComponentToOutput(DATA_OUTPUT_TEST_SETS, testSets);
	}

	
}
