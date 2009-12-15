package org.imirsel.tests;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.NEMAMetadataEntry;
import org.imirsel.nema.repository.PublishedResult;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;

public class TestDataListFileGenerator {

	static int datasetID = 5;
	
	static String bitRate = "";
	
	static String channels = "1";
	
	static String clip_type = "30";
	
	
	static String encoding = "wav";
	
	
	static String sample_rate = "22050";
	
	static String delim = "\t";
	
	String[] names;
	String[] paths;
	
	public  static void main(String[] args) throws IOException{
		
		
		 List<File[]> traintest_split_files;
		 File[] gt_and_featExt_files;
		 Set<NEMAMetadataEntry>  file_encoding_constraint;
		 String[] names;
		 String[] paths;
			
		
	//    file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		

		File  commonStorageDir = new File("./tmp");
/*		try {
			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, delim, commonStorageDir, file_encoding_constraint);
			System.out.println("ground truth file: " + gt_and_featExt_files[0].getCanonicalPath() );

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		
		
		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
		RepositoryClientImpl client = pool.getFromPool();
		List<PublishedResult> resultList = null;
		try{
		   resultList = client.getPublishedResultsForDataset(datasetID);
		   names = new String[resultList.size()];
		   paths = new String[resultList.size()];
		   int ctr =0;
			for (PublishedResult thisResult:resultList ){
				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
				names[ctr]=thisResult.getResult_path();
				paths[ctr]=thisResult.getName();
				System.out.println("names " + ctr + " " + names[ctr]);
				System.out.println("paths " + ctr + " " + paths[ctr]);				
				ctr++;
			}		   
		   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		   pool.returnToPool(client);
		}
		
	//	 String groundtruth = gt_and_featExt_files[0].getAbsolutePath();
//		 String featExtList = gt_and_featExt_files[1].getAbsolutePath();
		

		
		
	 /*   try {
	    	traintest_split_files = DatasetListFileGenerator.writeOutExperimentSplitFiles(datasetID, delim, commonStorageDir, file_encoding_constraint);									
	    } catch (SQLException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }*/
	
		
		
		
	}
	
	
}
