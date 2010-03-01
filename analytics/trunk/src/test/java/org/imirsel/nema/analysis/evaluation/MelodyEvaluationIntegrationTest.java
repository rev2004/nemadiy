package org.imirsel.nema.analysis.evaluation;


import static org.imirsel.nema.test.matchers.NemaMatchers.fileContentEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationTextFile;
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

public class MelodyEvaluationIntegrationTest extends BaseManagerTestCase{

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
        task.setSubjectTrackMetadataName(NemaDataConstants.MELODY_EXTRACTION_DATA);
        
        dataset = new NemaDataset();
        dataset.setId(task.getDatasetId());
        dataset.setName("dataset name");
        dataset.setDescription("some description");
    }
	
	
	@Test
	public void testEvaluateKD() { 
	File groundTruthDirectory = new File("src/test/resources/melody/groundtruth");
	File resultsDirectory = new File("src/test/resources/melody/KD");
	String	systemName = "KD-System";
	MelodyEvaluator evaluator = null;
	try {
		evaluator = new MelodyEvaluator(task, dataset, outputDirectory, workingDirectory);
		SingleTrackEvalFileType reader = new MelodyTextFile();
		
		List<NemaData> groundTruth = reader.readDirectory(groundTruthDirectory, ".txt");
		evaluator.setGroundTruth(groundTruth);
	
		List<NemaData> resultsForAllTracks = reader.readDirectory(resultsDirectory, null);
		evaluator.addResults(systemName, systemName, resultsForAllTracks);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		fail(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		fail(e.getMessage());
	}
	
	
	try {
		Map<String,NemaData> jobIdToAggregateResults = evaluator.evaluate();
		for(String key:jobIdToAggregateResults.keySet()){
			assertTrue(key.equals(systemName));
		}
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
		fail(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		fail(e.getMessage());
	}
		
	  //File resultFile = new File("src/test/resources/classification/evaluation/GT1/report.txt");
	  //File outputFile = new File(outputDirectory,systemName+System.getProperty("file.separator")+"report.txt");
	 
	  //assertThat(resultFile, fileContentEquals(outputFile));
	
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
