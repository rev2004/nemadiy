package org.imirsel.nema.analysis.evaluation;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.EvaluatorFactory;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.ResultRendererFactory;
import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile;
import org.imirsel.nema.analytics.evaluation.structure.StructureTextFile;
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

public class StructureEvaluationIntegrationTest extends BaseManagerTestCase {
	
	private NemaTask singleSetTask;
	private NemaDataset singleSetDataset;
	private List<NemaTrackList> singleTestSet;
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
		singleSetTask = new NemaTask();
        singleSetTask.setId(1);
        singleSetTask.setName("single fold task name");
        singleSetTask.setDescription("single fold task description");
        singleSetTask.setDatasetId(1);
        singleSetTask.setSubjectTrackMetadataId(11);
        singleSetTask.setSubjectTrackMetadataName(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA);
        
        singleSetDataset = new NemaDataset();
        singleSetDataset.setId(singleSetTask.getDatasetId());
        singleSetDataset.setName("Single fold dataset name");
        singleSetDataset.setDescription("Single fold dataset description");
        
        int idtrackListId = 0;
        {
	        ArrayList<NemaTrack> trackList = new ArrayList<NemaTrack>(1);
	        trackList.add(new NemaTrack("01__a_hard_days_night"));
	        singleTestSet = new ArrayList<NemaTrackList>(1);
	        singleTestSet.add(new NemaTrackList(idtrackListId, singleSetTask.getDatasetId(), 3, "test", idtrackListId, trackList));
	        idtrackListId++;
        }

    }
	
	@Test
	public void testEvaluateStruct()  throws IllegalArgumentException, IOException, InstantiationException, IllegalAccessException{ 
		File groundTruthDirectory = new File("src/test/resources/structure/groundtruth");
		File resultsDirectory = new File("src/test/resources/structure/result");
		String	systemName = "SystemName";
		Evaluator evaluator = null;
		ResultRenderer renderer = null;
		
		//evaluator = new MelodyEvaluator(task, dataset, outputDirectory, workingDirectory, testSets);
		evaluator = EvaluatorFactory.getEvaluator(singleSetTask.getSubjectTrackMetadataName(), singleSetTask, singleSetDataset, null, singleTestSet);
		renderer = ResultRendererFactory.getRenderer(singleSetTask.getSubjectTrackMetadataName(), outputDirectory, workingDirectory, false, null);
		SingleTrackEvalFileType reader = new StructureTextFile();
		
		List<NemaData> groundTruth = reader.readDirectory(groundTruthDirectory, ".txt");
		evaluator.setGroundTruth(groundTruth);
	
		List<NemaData> resultsForAllTracks = reader.readDirectory(resultsDirectory, null);
		evaluator.addResults(systemName, "**FlowID**", singleTestSet.get(0), resultsForAllTracks);
	
		NemaEvaluationResultSet results = evaluator.evaluate();
		assertTrue(results != null);
		
		//test rendering
		renderer.renderResults(results);
		
	  //File resultFile = new File("src/test/resources/classification/evaluation/GT1/report.txt");
	  //File outputFile = new File(outputDirectory,systemName+System.getProperty("file.separator")+"report.txt");
	 
	  //assertThat(resultFile, fileContentEquals(outputFile));
	
	}
	
	
	

	@After
	public void tearDown() throws Exception {
	}


}
