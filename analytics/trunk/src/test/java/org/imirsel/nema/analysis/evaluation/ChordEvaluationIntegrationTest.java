package org.imirsel.nema.analysis.evaluation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.evaluation.chord.ChordEvaluator;
import org.imirsel.nema.analytics.evaluation.chord.ChordShortHandTextFile;
import org.imirsel.nema.analytics.evaluation.melody.MelodyEvaluator;
import org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ChordEvaluationIntegrationTest extends BaseManagerTestCase{


	private NemaTask task;
	private NemaDataset dataset;
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
        task.setSubjectTrackMetadataName(NemaDataConstants.CHORD_LABEL_SEQUENCE);
        
        dataset = new NemaDataset();
        dataset.setId(task.getDatasetId());
        dataset.setName("dataset name");
        dataset.setDescription("some description");
    }
	
	
	
	@Test
	public void testEvaluateShortHandBasedSystem() throws FileNotFoundException, IOException, IllegalArgumentException, IOException{ 
		File groundTruthDirectory = new File("src/test/resources/chord/groundtruth");
		File resultsDirectory = new File("src/test/resources/chord/CH");
		String	systemName = "CH-System";
		ChordEvaluator evaluator = null;
		
		//test reader and setup for evaluation
		evaluator = new ChordEvaluator(task, dataset, outputDirectory, workingDirectory, false, null);
		SingleTrackEvalFileType reader = new ChordShortHandTextFile();
		
		List<NemaData> groundTruth = reader.readDirectory(groundTruthDirectory, null);
		evaluator.setGroundTruth(groundTruth);
	
		List<NemaData> resultsForAllTracks = reader.readDirectory(resultsDirectory, null);
		evaluator.addResults(systemName, systemName, resultsForAllTracks);
		
		
		//test evaluation
		Map<String,NemaData> jobIdToAggregateResults = evaluator.evaluate();
		for(String key:jobIdToAggregateResults.keySet()){
			assertTrue(key.equals(systemName));
		}
	
	}

	@Test
	public void testEvaluateTwoShortHandBasedSystems() throws FileNotFoundException, IOException, IllegalArgumentException, IOException{ 
		File groundTruthDirectory = new File("src/test/resources/chord/groundtruth");
		File resultsDirectory1 = new File("src/test/resources/chord/CH");
		File resultsDirectory2 = new File("src/test/resources/chord/MD");
		String	systemName1 = "CH-System";
		String	systemName2 = "MD-System";
		ChordEvaluator evaluator = null;
		
		//test reader and setup for evaluation
		evaluator = new ChordEvaluator(task, dataset, outputDirectory, workingDirectory, false, new File("/usr/local/bin/matlab"));
		SingleTrackEvalFileType reader = new ChordShortHandTextFile();
		
		List<NemaData> groundTruth = reader.readDirectory(groundTruthDirectory, null);
		evaluator.setGroundTruth(groundTruth);
	
		//read system 1 results
		List<NemaData> resultsForAllTracks = reader.readDirectory(resultsDirectory1, null);
		evaluator.addResults(systemName1, systemName1, resultsForAllTracks);
		
		//read system 2 results
		resultsForAllTracks = reader.readDirectory(resultsDirectory2, null);
		evaluator.addResults(systemName2, systemName2, resultsForAllTracks);
		
		//test evaluation
		Map<String,NemaData> jobIdToAggregateResults = evaluator.evaluate();
			
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
