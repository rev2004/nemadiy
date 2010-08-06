package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.imirsel.nema.model.NemaCollection;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaFile;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.fileTypes.ChordShortHandTextFile;
import org.imirsel.nema.model.fileTypes.ClassificationTextFile;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.fileTypes.TrackListTextFile;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.RepositoryProperties;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryClientImplTest extends BaseManagerTestCase {

	private RepositoryClientImpl clientImpl;

	@Before
	public void setUp() throws Exception {
		clientImpl = new RepositoryClientImpl();
		this.getLogger().info(
				RepositoryProperties.DB_LOCATOR
						+ "="
						+ RepositoryProperties
								.getProperty(RepositoryProperties.DB_LOCATOR));
		this.getLogger().info(
				RepositoryProperties.DB_NAME
						+ "="
						+ RepositoryProperties
								.getProperty(RepositoryProperties.DB_NAME));
	}

	@After
	public void tearDown() throws Exception {
		clientImpl = null;
	}

	@Test
	public final void testClientConnectionPool() throws SQLException{
		RepositoryClientConnectionPool rcp = RepositoryClientConnectionPool
				.getInstance();
		RepositoryClientInterface rci = rcp.getFromPool();
		rci.close();
	}


	@Test
	public final void testGetDatasets() {

		List<NemaDataset> dataset = null;
		try {
			dataset = clientImpl.getDatasets();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(dataset != null);

		for (NemaDataset nd : dataset) {
			this.getLogger().info(nd.getName());
		}

	}

	@Test
	public final void testGetDataset() {

		NemaDataset dataset = null;
		try {
			dataset = clientImpl.getDataset(10);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		assertTrue(dataset != null);

		this.getLogger().info(dataset.toString());

	}

	@Test
	public final void testGetCollections() {

		List<NemaCollection> collectionList = null;

		try {
			collectionList = clientImpl.getCollections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(collectionList != null);

		for (NemaCollection nc : collectionList) {
			this.getLogger().info(nc.getName());
		}

	}

	@Test
	public final void testGetFile() throws SQLException {

		NemaFile nemaFile = null;
		// setup constraint
		HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
		constraint.add(new NemaMetadataEntry("encoding", "mp3"));

		// try to get file
		nemaFile = clientImpl.getFile("b004001", constraint);

		// check we got the file
		assertTrue(nemaFile != null);

		this.getLogger().info(nemaFile.toString());

	}

	@Test
	public final void testGetTrackIDs() throws SQLException {
		List<String> idTrackList = clientImpl.getTrackIDs(1);

		assertTrue(idTrackList != null);

		for (String tl : idTrackList) {
			if (tl == null) {
				throw new RuntimeException("Retrieved null track ID!");
			}
			this.getLogger().info(tl.toString());
		}
	}

	@Test
	public final void testGetTracks() throws SQLException {
		List<NemaTrack> trackList = clientImpl.getTracks(1);

		assertTrue(trackList != null);

		for (NemaTrack tl : trackList) {
			if (tl == null) {
				throw new RuntimeException("Retrieved null NemaTrack Object!");
			}
			if (tl.getId() == null) {
				throw new RuntimeException(
						"Retrieved null NemaTrack Object with null id!");
			}
			this.getLogger().info(tl.toString());
		}
	}

	@Test
	public final void testGetTrackMetadataName() {
		String trackMetaData = null;
		try {
			trackMetaData = clientImpl.getTrackMetadataName(4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(trackMetaData != null);

		this.getLogger().info(trackMetaData);
	}

	@Test
	public final void testGetTrackMetadataID() {
		int trackMetaDataID = 0;
		try {
			trackMetaDataID = clientImpl.getTrackMetadataID("Key");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(trackMetaDataID != 0);

		this.getLogger().info("Metadata ID = " + trackMetaDataID);
	}

	@Test
	public final void testGetTrackMetadataNemaTrack() {

		NemaTrack nemaTrack = new NemaTrack("00010b0e1e0854b907ff6d88aff9b78b");
		List<NemaMetadataEntry> nemaMetaDataEntryList = null;
		try {
			nemaMetaDataEntryList = clientImpl.getTrackMetadata(nemaTrack);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(nemaMetaDataEntryList != null);

		for (NemaMetadataEntry nd : nemaMetaDataEntryList) {
			this.getLogger().info(nd.getValue());
		}

	}

	@Test
	public final void testGetTrackMetadataListOfNemaTrack() {

		HashMap<String, List<NemaMetadataEntry>> trackMetadata = new HashMap<String, List<NemaMetadataEntry>>();
		List<NemaTrack> trackList = new ArrayList<NemaTrack>();
		trackList.add(new NemaTrack("000150b65660c701e58e9d4cc97f7bdf"));

		try {
			trackMetadata = (HashMap<String, List<NemaMetadataEntry>>) clientImpl
					.getTrackMetadata(trackList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(trackMetadata != null);

		for (Entry<String, List<NemaMetadataEntry>> entry : trackMetadata
				.entrySet()) {
			this.getLogger().info(entry.getKey().toString());

			for (NemaMetadataEntry nemaMetadata : entry.getValue()) {
				this.getLogger().info(
						nemaMetadata.getType() + "=" + nemaMetadata.getValue());
			}

		}

	}

	@Test
	public final void testGetTrackMetadataByIDListOfString() {

		HashMap<String, List<NemaMetadataEntry>> trackMetadata = new HashMap<String, List<NemaMetadataEntry>>();
		List<String> list = new ArrayList<String>();
		list.add("m000321");

		try {
			trackMetadata = (HashMap<String, List<NemaMetadataEntry>>) clientImpl
					.getTrackMetadataByID(list, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(trackMetadata != null);

		for (Entry<String, List<NemaMetadataEntry>> entry : trackMetadata
				.entrySet()) {
			this.getLogger().info(entry.getKey().toString());

			for (NemaMetadataEntry nemaMetadata : entry.getValue()) {
				this.getLogger().info(
						nemaMetadata.getType() + ":" + nemaMetadata.getValue());
			}

		}
	}

	@Test
	public final void testResolveTracksToFilesForMelodyNoConstraint()
			throws SQLException {

		// retrieve a dataset to do resolution on
		NemaDataset dataset = clientImpl.getDataset(28); // MIREX 05 melody

		// produce experiment sets
		Map<NemaTrackList, List<NemaData>> testSets = new HashMap<NemaTrackList, List<NemaData>>();
		List<List<NemaTrackList>> sets = clientImpl
				.getExperimentTrackLists(dataset);
		for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();) {
			List<NemaTrackList> list = it.next();

			for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();) {
				NemaTrackList trackList = it1.next();
				testSets.put(trackList, getTestData(trackList.getId(),
						clientImpl));
			}
		}

		// resolve trackIDs to files
		testSets = clientImpl.resolveTracksToFiles(testSets, null);

		// check all were resolved
		for (Iterator<NemaTrackList> it = testSets.keySet().iterator(); it
				.hasNext();) {
			NemaTrackList list = it.next();
			List<NemaData> trackList = testSets.get(list);
			for (Iterator<NemaData> it2 = trackList.iterator(); it2.hasNext();) {
				// check for file path, will throw exception if not found
				String loc = it2.next().getStringMetadata(
						NemaDataConstants.PROP_FILE_LOCATION);
				if (loc == null) {
					throw new RuntimeException(
							"Null file path returned after resolution. "
									+ "A null must be being set as otherwise an exception would have "
									+ "been thrown when trying to retrieve it!");
				}
			}
		}
	}
	
	@Test
	public final void testResolveTracksToFilesForMelodyConstrained()
			throws SQLException {

		// retrieve a dataset to do resolution on
		NemaDataset dataset = clientImpl.getDataset(28); // MIREX 05 melody

		// produce experiment sets
		Map<NemaTrackList, List<NemaData>> testSets = new HashMap<NemaTrackList, List<NemaData>>();
		List<List<NemaTrackList>> sets = clientImpl
				.getExperimentTrackLists(dataset);
		for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();) {
			List<NemaTrackList> list = it.next();

			for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();) {
				NemaTrackList trackList = it1.next();
				testSets.put(trackList, getTestData(trackList.getId(),
						clientImpl));
			}
		}
		
		//setup good constraint
		HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
		constraint.add(new NemaMetadataEntry("encoding", "wav"));
		constraint.add(new NemaMetadataEntry("sample-rate", "44100"));
		

		// resolve trackIDs to files
		testSets = clientImpl.resolveTracksToFiles(testSets, constraint);

		// check all were resolved
		for (Iterator<NemaTrackList> it = testSets.keySet().iterator(); it
				.hasNext();) {
			NemaTrackList list = it.next();
			List<NemaData> trackList = testSets.get(list);
			for (Iterator<NemaData> it2 = trackList.iterator(); it2.hasNext();) {
				// check for file path, will throw exception if not found
				String loc = it2.next().getStringMetadata(
						NemaDataConstants.PROP_FILE_LOCATION);
				if (loc == null) {
					throw new RuntimeException(
							"Null file path returned after resolution. "
									+ "A null must be being set as otherwise an exception would have "
									+ "been thrown when trying to retrieve it!");
				}
			}
		}
		
	}
	
	@Test
	public final void testResolveTracksToFilesForMelodyBadConstraint()
			throws SQLException {

		// retrieve a dataset to do resolution on
		NemaDataset dataset = clientImpl.getDataset(28); // MIREX 05 melody

		// produce experiment sets
		Map<NemaTrackList, List<NemaData>> testSets = new HashMap<NemaTrackList, List<NemaData>>();
		List<List<NemaTrackList>> sets = clientImpl
				.getExperimentTrackLists(dataset);
		for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();) {
			List<NemaTrackList> list = it.next();

			for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();) {
				NemaTrackList trackList = it1.next();
				testSets.put(trackList, getTestData(trackList.getId(),
						clientImpl));
			}
		}
			
		//setup constraint that will fail
		HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
		constraint.add(new NemaMetadataEntry("encoding", "mp3"));
		constraint.add(new NemaMetadataEntry("sample-rate", "42"));
		
		// resolve trackIDs to files
		testSets = clientImpl.resolveTracksToFiles(testSets, constraint);
		
		// check none were resolved
		for (Iterator<NemaTrackList> it = testSets.keySet().iterator(); it
				.hasNext();) {
			NemaTrackList list = it.next();
			List<NemaData> trackList = testSets.get(list);
			for (Iterator<NemaData> it2 = trackList.iterator(); it2.hasNext();) {
				// check for file path, will throw exception if not found
				try{
					String loc = it2.next().getStringMetadata(
							NemaDataConstants.PROP_FILE_LOCATION);
					fail("Should not have been able to resolve files with the constraint set!");
				}catch(Exception e){
					
				}
			}
		}
	}
	
	@Test
	public final void testResolveTracksToFilesForMelodyUnachievableConstraint()
			throws SQLException {

		// retrieve a dataset to do resolution on
		NemaDataset dataset = clientImpl.getDataset(28); // MIREX 05 melody

		// produce experiment sets
		Map<NemaTrackList, List<NemaData>> testSets = new HashMap<NemaTrackList, List<NemaData>>();
		List<List<NemaTrackList>> sets = clientImpl
				.getExperimentTrackLists(dataset);
		for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();) {
			List<NemaTrackList> list = it.next();

			for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();) {
				NemaTrackList trackList = it1.next();
				testSets.put(trackList, getTestData(trackList.getId(),
						clientImpl));
			}
		}
			
		//setup constraint that will not be matched
		HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
		constraint.add(new NemaMetadataEntry("encoding", "mp3"));
		constraint.add(new NemaMetadataEntry("sample-rate", "44100"));
		
		// resolve trackIDs to files
		testSets = clientImpl.resolveTracksToFiles(testSets, constraint);
		
		// check none were resolved
		for (Iterator<NemaTrackList> it = testSets.keySet().iterator(); it
				.hasNext();) {
			NemaTrackList list = it.next();
			List<NemaData> trackList = testSets.get(list);
			for (Iterator<NemaData> it2 = trackList.iterator(); it2.hasNext();) {
				// check for file path, will throw exception if not found
				try{
					String loc = it2.next().getStringMetadata(
							NemaDataConstants.PROP_FILE_LOCATION);
					fail("Should not have been able to resolve files with the constraint set!");
				}catch(Exception e){
					
				}
			}
		}
	}
	
	@Test
	public final void testChordFileGeneration() throws Exception{
		int taskID = 17;
		File tmpDir = File.createTempFile("chord", "shorthand");
		tmpDir.delete();
		tmpDir.mkdirs();
		prepareTaskDataFiles(taskID,tmpDir,TrackListTextFile.class,ChordShortHandTextFile.class,TrackListTextFile.class, null);
	}
	
	@Test
	public final void testTrainTestFileGeneration() throws Exception{
//		int taskID = 3;//mixed
//		int taskID = 4;//latin
		int taskID = 5;//composer
//		int taskID = 5;//artist
//        int taskID = 8;//mood
        
        File tmpDir = File.createTempFile("classification", "mixed");
        tmpDir.delete();
        tmpDir.mkdir();
        
        Set<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
        constraint.add(new NemaMetadataEntry("channels", "1"));
        constraint.add(new NemaMetadataEntry("sample-rate", "22050"));
        constraint.add(new NemaMetadataEntry("clip-type", "30s"));
        
        prepareTaskDataFiles(taskID,tmpDir,TrackListTextFile.class,ClassificationTextFile.class,TrackListTextFile.class, constraint);
	}
	
	
	public final void prepareTaskDataFiles(int taskID, File tmpDir, 
			Class<? extends NemaFileType> featExtractFileType, 
			Class<? extends NemaFileType> trainingFileType, 
			Class<? extends NemaFileType> testFileType,
			Set<NemaMetadataEntry> constraint
			) throws Exception{
		NemaTask task = null;
		NemaDataset dataset = null;
		Map<NemaTrackList,List<NemaData>> featExtractMap = new HashMap<NemaTrackList, List<NemaData>>(1);;
		Map<NemaTrackList,List<NemaData>> trainingSets = new HashMap<NemaTrackList,List<NemaData>>();
		Map<NemaTrackList,List<NemaData>> testSets = new HashMap<NemaTrackList,List<NemaData>>();
		
		task = clientImpl.getTask(taskID);
		if(task == null){
			throw new Exception("Task id " + taskID + " was not found in the repository!");
		}
        dataset = clientImpl.getDataset(task.getDatasetId());
        if(dataset == null){
			throw new Exception("Dataset id " + task.getDatasetId() + 
					" was not found in the repository but was linked from task ID: " + taskID + " in the repository!");
		}

        //produce feature extraction list
        NemaTrackList featExtractSet = clientImpl.getDatasetSubset(dataset.getId());
        List<NemaData> featExtractList = getTestData(dataset.getSubsetTrackListId(), clientImpl);
        
        featExtractMap.put(featExtractSet, featExtractList);
//        
//        //produce Ground-truth list
//        gtList = getGroundtruthData(clientImpl, task, dataset.getSubsetTrackListId(), task.getSubjectTrackMetadataId());
//        
        //produce experiment sets
        List<List<NemaTrackList>> sets = clientImpl.getExperimentTrackLists(dataset);
        for (Iterator<List<NemaTrackList>> it = sets.iterator(); it.hasNext();){
            List<NemaTrackList> list = it.next();
            
            String trackListType;
            for (Iterator<NemaTrackList> it1 = list.iterator(); it1.hasNext();){
                NemaTrackList trackList = it1.next();
                trackListType = trackList.getTrackListTypeName();

                if (trackListType.equalsIgnoreCase("test")){
                	testSets.put(trackList,getTestData(trackList.getId(), clientImpl));
                }else{
                	trainingSets.put(trackList,getGroundtruthData(clientImpl, task, trackList.getId(), task.getSubjectTrackMetadataId()));
                }
            }
        }
        
        //resolve training sets
        clientImpl.resolveTracksToFiles(trainingSets, constraint);
        clientImpl.resolveTracksToFiles(testSets, constraint);
        clientImpl.resolveTracksToFiles(featExtractMap, constraint);
        
      //prepare training set files
        System.out.println("Outputting to: " + tmpDir.getAbsolutePath());
        File feat = new File(tmpDir.getAbsolutePath() + File.separator + "featExtract");
        feat.mkdir();
        FileConversionUtil.prepareProcessInput(feat, task, featExtractMap, featExtractFileType);
        
        File train = new File(tmpDir.getAbsolutePath() + File.separator + "train");
        train.mkdir();
        FileConversionUtil.prepareProcessInput(train, task, trainingSets, trainingFileType);
        
        File test = new File(tmpDir.getAbsolutePath() + File.separator + "test");
        test.mkdir();
        FileConversionUtil.prepareProcessInput(test, task, testSets, testFileType);
	}
	
	
	private List<NemaData> getTestData(int trackListId, RepositoryClientInterface client) throws SQLException {
		getLogger().info("Retrieving test data for track list: " + trackListId);
        List<NemaData> list = new ArrayList<NemaData>();
		List<String> tracks = client.getTrackIDs(trackListId);
		for (Iterator<String> iterator = tracks.iterator(); iterator.hasNext();) {
			list.add(new NemaData(iterator.next()));
		}
		getLogger().info("Got " + list.size() + " tracks for track list: " + trackListId);
        return list;
	}
	
	private List<NemaData> getGroundtruthData(RepositoryClientInterface client, NemaTask task, int tracklist_id, int metadata_id) throws SQLException, IOException, IllegalArgumentException, InstantiationException, IllegalAccessException{
        getLogger().info("Retrieving ground-truth data for track list: " + tracklist_id);
        List<String> tracks = client.getTrackIDs(tracklist_id);
        getLogger().info("Got " + tracks.size() + " tracks for track list: " + tracklist_id);
        Map<String,List<NemaMetadataEntry>> trackToMeta = client.getTrackMetadataByID(tracks, metadata_id);
        List<NemaData> out = FileConversionUtil.convertMetadataToGroundtruthModel(trackToMeta, task);
        getLogger().info("Returning " + out.size() + " metadata Objects for track list: " + tracklist_id);
        return out;
    }
}
