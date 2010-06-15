package org.imirsel.nema.analysis.evaluation;


//import static org.imirsel.nema.test.matchers.NemaMatchers.fileContentEquals;

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
import org.imirsel.nema.analytics.evaluation.multif0.MultiF0EstTextFile;
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

public class MultiF0EstEvaluationIntegrationTest extends BaseManagerTestCase{

	private NemaTask singleSetTask;
	private NemaTask twoSetTask;
	private NemaDataset singleSetDataset;
	private NemaDataset twoSetDataset;
	private List<NemaTrackList> singleTestSet;
	private List<NemaTrackList> twoTestSets;
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
		
    }
	
	
	@Test
	public void testEvaluateKDTwoFolds()  throws IllegalArgumentException, IOException, InstantiationException, IllegalAccessException{ 
		MultiF0EstTextFile multiF0Est = new MultiF0EstTextFile();
		File theFile = new File("/Users/mertbay/tmp/part1_hn_cl_bs.wav.task1.txt");
		String[][] multiF0EstDataStrArray = multiF0Est.readFile(theFile);
		
		/* Convert the 2D string data to a 2D double array */
		int nrows = multiF0EstDataStrArray.length;
		int ncols = multiF0EstDataStrArray[0].length;
		double[][] multiF0EstRaw = new double[nrows][ncols];
		for(int r = 0; r < nrows; r++) {
			try{
				System.out.println("Number of Cols: " + multiF0EstDataStrArray[r].length);
				for(int c = 0; c < multiF0EstDataStrArray[r].length; c++) {
					//multiF0EstRaw[r][c] = Double.valueOf(multiF0EstDataStrArray[r][c]);
					System.out.print(multiF0EstDataStrArray[r][c]+"\t");
				}
				System.out.println("\n");
			}catch(Exception e){
				String msg = "Failed to parse line " + r + " of file " + theFile.getAbsolutePath() + "\n" +
				"Content: \n";
				for (int i = 0; i < multiF0EstDataStrArray[r].length; i++) {
					msg += "'" + multiF0EstDataStrArray[r][i] + "'";
					if (i<multiF0EstDataStrArray[r].length-1){
						msg += ",";
					}
					
				}
				msg += "\n";
				throw new IllegalArgumentException(msg,e);
			}
		}

	
	}
	

	
	
	

	@After
	public void tearDown() throws Exception {
	}

}
