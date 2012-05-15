package org.imirsel.nema.repository.tests;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.junit.Test;

public class TestDataListFileGenerator {

	static int datasetID = 33; //for Chord
	static String bitRate = "";
	static String channels = "1";
	static String clip_type = "full";
	static String encoding = "wav";
	static String sample_rate = "44100";
	static String delim = "\t";
	
	private static final Logger logger = Logger.getLogger(TestDataListFileGenerator.class.getName());
	
	@Test
	public void testGenerator() throws IOException{
		List<File[]> traintest_split_files;
		File[] gt_and_featExt_files;
		Set<NemaMetadataEntry>  file_encoding_constraint;
		
		file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		
		
		File  commonStorageDir = File.createTempFile("DataSetListFileGenTest", "");
		commonStorageDir.delete();
		commonStorageDir.mkdirs();
		System.out.println("storage dir canonical path: " + commonStorageDir.getCanonicalPath() );
		System.out.println("storage dir absoulte path: " + commonStorageDir.getAbsolutePath() );

		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, delim, commonStorageDir, file_encoding_constraint);
			System.out.println("ground truth file canonical path: " + gt_and_featExt_files[0].getCanonicalPath() );
			System.out.println("ground truth file absoulte path: " + gt_and_featExt_files[0].getAbsolutePath() );


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	    try {
	    	traintest_split_files = DatasetListFileGenerator.writeOutExperimentSplitFiles(datasetID, delim, commonStorageDir, file_encoding_constraint);									
	    } catch (SQLException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	
		
		
		
	}
	
	
}
