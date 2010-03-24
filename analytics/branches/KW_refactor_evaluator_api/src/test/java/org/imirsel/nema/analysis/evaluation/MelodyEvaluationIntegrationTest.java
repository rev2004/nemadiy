package org.imirsel.nema.analysis.evaluation;


//import static org.imirsel.nema.test.matchers.NemaMatchers.fileContentEquals;

import static org.junit.Assert.assertTrue;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.evaluation.melody.MelodyEvaluator;
import org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MelodyEvaluationIntegrationTest extends BaseManagerTestCase{

	private NemaTask task;
	private NemaDataset dataset;
	List<NemaTrackList> testSets;
	private static File workingDirectory;
	private static File outputDirectory;
	
	@BeforeClass
	public static void  prepareWorkingLocation(){
		 String tempLocation = System.getProperty("java.io.tmpdir");
	     workingDirectory = new File(tempLocation);
	     outputDirectory = new File(workingDirectory,(System.currentTimeMillis())+"");
	     outputDirectory.mkdirs();
	}
	
	
	@Before
	public void setUp() throws Exception {
		task = new NemaTask();
        task.setId(-1);
        task.setName("test name");
        task.setDescription("test description");
        task.setDatasetId(-1);
        task.setSubjectTrackMetadataId(-1);
        task.setSubjectTrackMetadataName(NemaDataConstants.MELODY_EXTRACTION_DATA);
        
        dataset = new NemaDataset();
        dataset.setId(task.getDatasetId());
        dataset.setName("dataset name");
        dataset.setDescription("some description");
        
        ArrayList<NemaTrack> trackList = new ArrayList<NemaTrack>(4);
        trackList.add(new NemaTrack("daisy1"));
        trackList.add(new NemaTrack("daisy2"));
        trackList.add(new NemaTrack("daisy3"));
        trackList.add(new NemaTrack("daisy4"));
        testSets = new ArrayList<NemaTrackList>(1);
        int id = 0;
        testSets.add(new NemaTrackList(id, task.getDatasetId(), 3, "test", id, trackList));
        id++;
    }
	
	
	@Test
	public void testEvaluateKD()  throws IllegalArgumentException, IOException{ 
		File groundTruthDirectory = new File("src/test/resources/melody/groundtruth");
		File resultsDirectory = new File("src/test/resources/melody/KD");
		String	systemName = "KD-System";
		MelodyEvaluator evaluator = null;
		
		evaluator = new MelodyEvaluator(task, dataset, outputDirectory, workingDirectory, testSets);
		SingleTrackEvalFileType reader = new MelodyTextFile();
		
		List<NemaData> groundTruth = reader.readDirectory(groundTruthDirectory, ".txt");
		evaluator.setGroundTruth(groundTruth);
	
		List<NemaData> resultsForAllTracks = reader.readDirectory(resultsDirectory, null);
		evaluator.addResults(systemName, systemName, testSets.get(0), resultsForAllTracks);
	
		NemaEvaluationResultSet results = evaluator.evaluate();
		assertTrue(results != null);
		
		
	  //File resultFile = new File("src/test/resources/classification/evaluation/GT1/report.txt");
	  //File outputFile = new File(outputDirectory,systemName+System.getProperty("file.separator")+"report.txt");
	 
	  //assertThat(resultFile, fileContentEquals(outputFile));
	
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
