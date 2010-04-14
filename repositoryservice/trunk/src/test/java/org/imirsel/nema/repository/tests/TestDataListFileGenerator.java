//package org.imirsel.nema.repository.tests;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Logger;
//
//import org.imirsel.nema.model.NemaMetadataEntry;
//import org.imirsel.nema.model.NemaPublishedResult;
//import org.imirsel.nema.repository.RepositoryClientConnectionPool;
//import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
//
//public class TestDataListFileGenerator {
//
//	static int datasetID = 5;
//	
//	static String bitRate = "";
//	
//	static String channels = "1";
//	
//	static String clip_type = "30";
//	
//	
//	static String encoding = "wav";
//	
//	
//	static String sample_rate = "22050";
//	
//	static String delim = "\t";
//	
//	String[] names;
//	String[] paths;
//	
//	private static final Logger logger = Logger.getLogger(TestDataListFileGenerator.class.getName());
//	
//	public static void main(String[] args) throws IOException{
//		
//		
//		 List<File[]> traintest_split_files;
//		 File[] gt_and_featExt_files;
//		 Set<NemaMetadataEntry>  file_encoding_constraint;
//		 String[] names;
//		 String[] paths;
//			
//		
//	 //   file_encoding_constraint = DatasetListFileGenerator.buildConstraints(bitRate, channels, clip_type, encoding, sample_rate);		
//
///*		File  commonStorageDir = new File("./tmp");
//		System.out.println("storage dir canonical path: " + commonStorageDir.getCanonicalPath() );
//		System.out.println("storage dir absoulte path: " + commonStorageDir.getAbsolutePath() );
//
//		try {
//			gt_and_featExt_files = DatasetListFileGenerator.writeOutGroundTruthAndExtractionListFile(datasetID, delim, commonStorageDir, file_encoding_constraint);
//			System.out.println("ground truth file canonical path: " + gt_and_featExt_files[0].getCanonicalPath() );
//			System.out.println("ground truth file absoulte path: " + gt_and_featExt_files[0].getAbsolutePath() );
//
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} */
//		
//		 //Testing publish a result
///*		int dataset_id = 3;
//		String username = "nutcracker";
//		String systemName = "imirsel";
//		String result_path = "/tmp/nutcrakcer";
//			RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
//			RepositoryClientImpl client = pool.getFromPool();
//			try{
//			   
//			   
//			   client.publishResultForDataset(dataset_id, username, systemName, result_path);	   
//			   
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			finally{
//			   pool.returnToPool(client);
//			} 
//			*/
//			
//		 
//		 
//		//Testing getting results for dataset id 
//		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
//		RepositoryClientInterface client = pool.getFromPool();
//		List<NemaPublishedResult> resultList = null;
//		try{
//		   resultList = client.getPublishedResultsForDataset(datasetID);
//		   names = new String[resultList.size()];
//		   paths = new String[resultList.size()];
//		   int ctr =0;
//			for (NemaPublishedResult thisResult:resultList ){
//				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
//				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
//				paths[ctr]=thisResult.getResult_path();
//				names[ctr]=thisResult.getName();
//				logger.info("names " + ctr + " " + names[ctr]);
//				logger.info("paths " + ctr + " " + paths[ctr]);				
//				ctr++;
//			}		   
//		   
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally{
//		   pool.returnToPool(client);
//		} 
//		
//		//Testing delete results 
///*		RepositoryClientConnectionPool pool = RepositoryClientConnectionPool.getInstance();
//		RepositoryClientImpl client = pool.getFromPool();
//		List<NemaPublishedResult> resultList = null;
//		try{
//		   resultList = client.getPublishedResultsForDataset(dataset_id);
//		   names = new String[resultList.size()];
//		   paths = new String[resultList.size()];
//		   int ctr =0;
//			for (NemaPublishedResult thisResult:resultList ){
//				//cout.println("Train File: " + thisFile[0].getCanonicalPath());
//				//cout.println("Test File: " + thisFile[1].getCanonicalPath());
//				paths[ctr]=thisResult.getResult_path();
//				names[ctr]=thisResult.getName();
//				System.out.println("names " + ctr + " " + names[ctr]);
//				System.out.println("paths " + ctr + " " + paths[ctr]);				
//				ctr++;
//			}		   
//		   
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally{
//		   pool.returnToPool(client);
//		} */
//
//		
//		
//		
//		
//		
//
//		
//		
//		
//	//	 String groundtruth = gt_and_featExt_files[0].getAbsolutePath();
////		 String featExtList = gt_and_featExt_files[1].getAbsolutePath();
//		
//
//		
//		
//	 /*   try {
//	    	traintest_split_files = DatasetListFileGenerator.writeOutExperimentSplitFiles(datasetID, delim, commonStorageDir, file_encoding_constraint);									
//	    } catch (SQLException e) {
//	    	// TODO Auto-generated catch block
//	    	e.printStackTrace();
//	    }*/
//	
//		
//		
//		
//	}
//	
//	
//}
