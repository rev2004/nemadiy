package org.imirsel.nema.analysis.evaluation;


import static org.junit.Assert.*;
import static org.imirsel.nema.test.matchers.NemaMatchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.EvalFileType;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationTextFile;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.imirsel.nema.test.BaseManagerTestCase;

public class ClassificationEvaluationIntegrationTest extends BaseManagerTestCase{

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
        task.setDatasetId(10);
        task.setSubjectTrackMetadataId(1);
        task.setSubjectTrackMetadataName("track name");
        
        dataset = new NemaDataset();
        dataset.setId(task.getDatasetId());
        dataset.setName("dataset name");
        dataset.setDescription("some description");
    }
	
	
	

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluateGT1() { 
	File groundTruthFile = new File("src/test/resources/audiolatin.all.gt.txt");
	File hierarchyFile = null;
	File resultsDirectory = new File("src/test/resources/GT1");
	String	systemName = "GT1-System";
	ClassificationEvaluator evaluator = null;
	try {
		evaluator = new ClassificationEvaluator(task, dataset, outputDirectory, workingDirectory, false, null, hierarchyFile);
		EvalFileType reader = new ClassificationTextFile("track name");
		List<NemaData> groundTruth = reader.readFile(groundTruthFile);
		evaluator.setGroundTruth(groundTruth);
	
		List<List<NemaData>> resultsForAllFolds = reader.readDirectory(resultsDirectory, null);
		for (Iterator<List<NemaData>> iterator = resultsForAllFolds.iterator(); iterator
		.hasNext();) {	
			List<NemaData> oneFoldResults = (List<NemaData>) iterator.next();
			evaluator.addResults(systemName, systemName, oneFoldResults);
		}
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
			assertTrue(key.equals("GT1-System"));
		}
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
		fail(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		fail(e.getMessage());
	}
		
	  File resultFile = new File("src/test/resources/evaluation/results/GT1/report.txt");
	  File outputFile = new File(outputDirectory,systemName+System.getProperty("file.separator")+"report.txt");
	  assertThat(resultFile, fileContentEquals(outputFile));
	
	}
	
	@Test
	public void testEvaluateHNOS1() { 
	File groundTruthFile = new File("src/test/resources/audiolatin.all.gt.txt");
	File hierarchyFile = null;
	File resultsDirectory = new File("src/test/resources/HNOS1");
	String	systemName = "HNOS1-System";
	ClassificationEvaluator evaluator = null;
	try {
		evaluator = new ClassificationEvaluator(task, dataset, outputDirectory, workingDirectory, false, null, hierarchyFile);
		EvalFileType reader = new ClassificationTextFile("track name");
		List<NemaData> groundTruth = reader.readFile(groundTruthFile);
		evaluator.setGroundTruth(groundTruth);
	
		List<List<NemaData>> resultsForAllFolds = reader.readDirectory(resultsDirectory, null);
		for (Iterator<List<NemaData>> iterator = resultsForAllFolds.iterator(); iterator
		.hasNext();) {	
			List<NemaData> oneFoldResults = (List<NemaData>) iterator.next();
			evaluator.addResults(systemName, systemName, oneFoldResults);
		}
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
			assertTrue(key.equals("HNOS1-System"));
		}
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
		fail(e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		fail(e.getMessage());
	}
	
	  File resultFile = new File("src/test/resources/evaluation/results/HNOS1/report.txt");
	  File outputFile = new File(outputDirectory,systemName+System.getProperty("file.separator")+"report.txt");
	  assertThat(resultFile, fileContentEquals(outputFile));

	
	}

}
