package org.imirsel.tests;


import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.NEMAMetadataEntry;

public class TestDataListFileGenerator {

	static int datasetID = 5;
	
	static String bitRate = "";
	
	static String channels = "1";
	
	static String clip_type = "30";
	
	
	static String encoding = "wav";
	
	
	static String sample_rate = "22050";
	
	static String delim = "\t";
	
	
	public  static void main(String[] args){
		
		
		 List<File[]> traintest_split_files;
		 File[] gt_and_featExt_files;
		 Set<NEMAMetadataEntry>  file_encoding_constraint;

		
	    file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		

		File  commonStorageDir = new File("/Users/kriswest/Desktop/test");
		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, delim, commonStorageDir, file_encoding_constraint);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
