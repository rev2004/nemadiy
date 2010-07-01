package org.imirsel.nema.repository.population;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.util.io.FileConversionUtil;
import org.imirsel.nema.analytics.util.io.PathAndTagCleaner;
import org.imirsel.nema.analytics.util.io.TrackListTextFile;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.util.IOUtil;

public class ImirselTestTrainDatasetIngestor {

	static final Logger logger = Logger.getLogger(ImirselTestTrainDatasetIngestor.class.getName());
	
	public static void main(String[] args) {
		try {
			File rootAudioDir = new File(args[0]); 
			String site = args[1]; 
			String audioFileExtension = args[2];
			int collection_id = Integer.parseInt(args[3]); 
			File audioDirectory = new File(args[4]); 
			String seriesName = args[5];
			String datasetName = args[6];
			String datasetDescription = args[7];
			String metadataType = args[8];
			Class<? extends SingleTrackEvalFileType> readerFileType;
			Class<? extends SingleTrackEvalFileType> writerFileType;
			try {
				readerFileType = (Class<? extends SingleTrackEvalFileType>) ImirselDatasetIngestor.class.getClassLoader().loadClass(args[9]);
				writerFileType = (Class<? extends SingleTrackEvalFileType>) ImirselDatasetIngestor.class.getClassLoader().loadClass(args[10]);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			File groundtruthFile = new File(args[11]); 
			
			boolean doingFileMeta = false;
			boolean doingTestList = false;
			boolean doingTrainList = false;
			List<File> testDirs = new ArrayList<File>();
			List<File> trainDirs = new ArrayList<File>();
			List<String[]> fileMetadataTags = new ArrayList<String[]>();
			for(int i = 12; i < args.length; i++) {
				if(args[i].equals("-m")) {
					doingFileMeta = true;
					doingTestList = false;
					doingTrainList = false;
					continue;
				}else if(args[i].equals("-test")) {
					doingFileMeta = false;
					doingTestList = true;
					doingTrainList = false;
					continue;
				}else if(args[i].equals("-train")) {
					doingFileMeta = false;
					doingTestList = false;
					doingTrainList = true;
					continue;
				}
				
				if(doingTestList) {
					testDirs.add(new File(args[i]));
				}else if(doingTrainList) {
					trainDirs.add(new File(args[i]));
				}else if (doingFileMeta) {
					String[] comps = args[i].split("=");
					if (comps.length != 2) {
						throw new RuntimeException("Failed to parse file metadata key=value pair: " + args[i]);
					}
					fileMetadataTags.add(comps);
				}else {
					System.out.println("WARNING: ignoring argument: " + args[i]);
				}
			}
			
			moveRenameAndInsertDataset(rootAudioDir, site, audioFileExtension, groundtruthFile, testDirs, trainDirs, fileMetadataTags, collection_id, audioDirectory, seriesName, datasetName, datasetDescription, metadataType, readerFileType, writerFileType);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void moveRenameAndInsertDataset(
			File rootAudioDir, 
			String site,
			String audioFileExtension,
			File groundtruth,
			List<File> testListFiles, 
			List<File> trainListFiles, 
			List<String[]> fileMetadataTags, 
			int collection_id, 
			File newAudioDirectory, 
			String seriesName,
			String datasetName,
			String datasetDescription,
			String metadataType,
			Class<? extends SingleTrackEvalFileType> readerFileType,
			Class<? extends SingleTrackEvalFileType> writerFileType
			) throws IllegalArgumentException, FileNotFoundException, InstantiationException, IllegalAccessException, IOException, SQLException {
		
		System.out.println("Ingesting dataset, moving/renaming files and inserting metadata/paths into DB...");
		System.out.println("Arguments:");
		
		System.out.println("rootAudioDir:       " +  rootAudioDir.getAbsolutePath());
		System.out.println("site:               " +  site);
		System.out.println("audioFileExtension: " +  audioFileExtension);
		System.out.println("collection_id:      " +  collection_id);
		System.out.println("newAudioDirectory:  " + newAudioDirectory.getAbsolutePath());
		System.out.println("seriesName:         " +  seriesName);
		System.out.println("datasetName:        " + datasetName);
		System.out.println("datasetDescription: " + datasetDescription); 
		System.out.println("metadataType:       " + metadataType);
		System.out.println("readerFileType:     " + readerFileType.getName());
		System.out.println("writerFileType:     " + writerFileType.getName()); 
		System.out.println("groundtruth:        " + groundtruth.getAbsolutePath()); 
		System.out.println("Test list files:    ");
		for (Iterator<File> iterator = testListFiles.iterator(); iterator
				.hasNext();) {
			System.out.println("\t" + iterator.next());
		} 
		System.out.println("Train list files:   ");
		for (Iterator<File> iterator = trainListFiles.iterator(); iterator
				.hasNext();) {
			System.out.println("\t" + iterator.next());
		}
		System.out.println("file metadata:      ");
		for (Iterator<String[]> iterator = fileMetadataTags.iterator(); iterator
				.hasNext();) {
			String[] data = iterator.next();
			System.out.println("\t" + data[0] + " = " + data[1]);
		}
		
		//pause so we can cancel if need be
		int togo = 15;
	    while(togo > 0){
	        logger.info("Commencing operation in " + togo + " seconds");
	        togo -= 5;
	        try{
	            Thread.sleep(5000);
	        }catch (InterruptedException ex){
	            logger.log(Level.SEVERE, null, ex);
	        }
	    }
		
		String ext = audioFileExtension.toLowerCase();
		
		//get list of audio tracks
		Map<String,File> idToOldFile = new HashMap<String,File>();
		File[] arr = rootAudioDir.listFiles();
		for (int i = 0; i < arr.length; i++) {
			if(arr[i].getName().toLowerCase().endsWith(ext) && !arr[i].isDirectory()) {
				idToOldFile.put(PathAndTagCleaner.convertFileToMIREX_ID(arr[i]),arr[i]);
			}
		}
		System.out.println("got " + idToOldFile.size() + " files to insert");
		
		//read all groundtruth track metadata
		Map<String, NemaData> idToMetadata = new HashMap<String, NemaData>();
		{
			List<NemaData> aList = FileConversionUtil.readData(groundtruth, null, readerFileType);
			for (Iterator<NemaData> it = aList.iterator(); it
					.hasNext();) {
				NemaData nemaData = it.next();
				idToMetadata.put(nemaData.getId(), nemaData);
			}
		}
		
		//read test and training lists
		TrackListTextFile listFileReader = new TrackListTextFile();
		
		List<List<NemaData>> testLists = new ArrayList<List<NemaData>>(testListFiles.size());
		for (Iterator<File> iterator = testListFiles.iterator(); iterator.hasNext();) {
			File file = iterator.next();
			List<NemaData> testList = listFileReader.readFile(file);
			testLists.add(testList);
			//confirm we have metadata for all tracks
			for (Iterator<NemaData> iterator2 = testList.iterator(); iterator2.hasNext();) {
				NemaData nemaData = iterator2.next();
				if (!idToMetadata.containsKey(nemaData.getId())){
					throw new IllegalArgumentException("Groundtruth did not contain metadata for trackID: " + nemaData.getId() + " from test list file: " + file.getAbsolutePath());
				}
			}
		}

		List<List<NemaData>> trainLists = new ArrayList<List<NemaData>>(trainListFiles.size());
		for (Iterator<File> it = trainListFiles.iterator(); it
				.hasNext();) {
			File file = it.next();
			List<NemaData> trainList = listFileReader.readFile(file);
			trainLists.add(trainList);
			//confirm we have metadata for all tracks
			for (Iterator<NemaData> iterator2 = trainList.iterator(); iterator2.hasNext();) {
				NemaData nemaData = iterator2.next();
				if (!idToMetadata.containsKey(nemaData.getId())){
					throw new IllegalArgumentException("Groundtruth did not contain metadata for trackID: " + nemaData.getId() + " from train list file: " + file.getAbsolutePath());
				}
			}
		}
		
		if(trainLists.size() != testListFiles.size()){
			throw new IllegalArgumentException("Lists of test (" + testListFiles.size() + ") and training (" + trainListFiles.size() + ") files were different lengths");
		}
		
		System.out.println("got " + idToOldFile.size() + " files to insert from " + testLists.size() + " folds");
		
		//pause so we can cancel if need be
		togo = 10;
	    while(togo > 0){
	        logger.info("Commencing copy in " + togo + " seconds");
	        togo -= 5;
	        try{
	            Thread.sleep(5000);
	        }catch (InterruptedException ex){
	            logger.log(Level.SEVERE, null, ex);
	        }
	    }
		
	    //setup new root directory
	    File newHome = new File(newAudioDirectory.getAbsolutePath() + File.separator + seriesName);
		if(newHome.exists()) {
			throw new IllegalArgumentException("The proposed home for the new series '" 
					+ newHome.getAbsolutePath() + "' already exists! Please modify " +
							"the series name to continue with this operation.");
		}
		if(!newHome.mkdirs()) {
			throw new IllegalArgumentException("Failed to create directory for the " +
					"proposed home for the new series '" + newHome.getAbsolutePath() + "'!");
		}
		
		//alter track IDs and copy each audio file to new home with name based on track id and encoding
		System.out.println("Renaming tracks and copying to: " + newHome.getAbsolutePath());
		Map<String,File> newIdToNewFile = new HashMap<String,File>();
		Map<File,File> newFileToOldFile = new HashMap<File,File>();
		
		int trackCount = 0;
		for (Iterator<NemaData> iterator = idToMetadata.values().iterator(); iterator
				.hasNext();) {
			NemaData track = iterator.next();
			String oldId = track.getId();
			String newId = seriesName + RepositoryManagementUtils.SIX_DIGIT_INTEGER.format(trackCount);
			trackCount++;
			File oldFile = idToOldFile.get(oldId);
			File newFile = new File(newHome.getAbsolutePath() + File.separator + newId + ext);
			newIdToNewFile.put(newId, newFile);
			track.setMetadata(NemaDataConstants.PROP_ID, newId);
			
			System.out.println("old id: " + oldId + ", new id: " + newId);
			System.out.println("old path: " + oldFile.getAbsolutePath() + ", new path: " + newFile.getAbsolutePath());
			
			long oldLen = oldFile.length();
			IOUtil.copyFile(oldFile, newFile);
			long newLen = newFile.length();
			System.out.println("old file length: " + oldLen + ", new length: " + newLen);
			if(oldLen != newLen) {
				System.out.println("WARNING: old and new file lengths don't match!");
			}
			
			newFileToOldFile.put(newFile,oldFile);
			
			System.out.println("---");
		}
		
		//insert renamed audio files and file metadata
		RepositoryManagementUtils.insertDirOfAudioFiles(newHome, fileMetadataTags, collection_id, newFileToOldFile, site);
		
		//insert track metadata
		RepositoryManagementUtils.insertMetadataFromSingleTrackEvalFileType(idToMetadata.values(), metadataType, writerFileType);
		
		//collect up new lists of ids for folds
		HashSet<String> subsetList = new HashSet<String>();
		//test
		List<List<String>> newTestTrackIdLists = new ArrayList<List<String>>(testListFiles.size());
		for (Iterator<List<NemaData>> iterator = testLists.iterator(); iterator
				.hasNext();) {
			List<NemaData> aFold = iterator.next();
			List<String> foldList = new ArrayList<String>(aFold.size());
			for (Iterator<NemaData> iterator2 = aFold.iterator(); iterator2
					.hasNext();) {
				String id = iterator2.next().getId();
				//convert to new id
				id = idToMetadata.get(id).getId();
				foldList.add(id);
				subsetList.add(id);
			}
			newTestTrackIdLists.add(foldList);
		}
		//train
		List<List<String>> newTrainTrackIdLists = new ArrayList<List<String>>(trainListFiles.size());
		for (Iterator<List<NemaData>> iterator = trainLists.iterator(); iterator
				.hasNext();) {
			List<NemaData> aFold = iterator.next();
			List<String> foldList = new ArrayList<String>(aFold.size());
			for (Iterator<NemaData> iterator2 = aFold.iterator(); iterator2
					.hasNext();) {
				String id = iterator2.next().getId();
				//convert to new id
				id = idToMetadata.get(id).getId();
				foldList.add(id);
				subsetList.add(id);
			}
			newTrainTrackIdLists.add(foldList);
		}
		
		
		//insert dataset
		RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();
	    try {
	    	int subject_track_metadata_type_id = client.getTrackMetadataID(metadataType);
	        int filter_track_metadata_type_id = -1;
	        
	        System.out.println("Inserting dataset definition...");
	        System.out.println("Name:                " + datasetName);
	        System.out.println("Description:\n" + datasetDescription);
	        System.out.println("Subject metadata:    " + metadataType);
	        System.out.println("Subject metadata id: " + subject_track_metadata_type_id);
	        System.out.println("Filter metadata:     " + null);
	        System.out.println("Filter metadata id:  " + filter_track_metadata_type_id);
	        System.out.println("Tracks in subset:    " + subsetList.size());
	        System.out.println("Number of folds:     " + newTestTrackIdLists.size());
	        
	        togo = 10;
	        while(togo > 0){
	            logger.info("Commencing dataset insert in " + togo + " seconds");
	            togo -= 5;
	            try{
	                Thread.sleep(5000);
	            }catch (InterruptedException ex){
	                logger.log(Level.SEVERE, null, ex);
	            }
	        }
	
	        //client.insertTestOnlyDataset(datasetName, datasetDescription, subject_track_metadata_type_id, filter_track_metadata_type_id, new ArrayList<String>(subsetList), newFoldTrackIdLists);
	        client.insertTestTrainDataset(datasetName, datasetDescription, subject_track_metadata_type_id, filter_track_metadata_type_id, new ArrayList<String>(subsetList), newTestTrackIdLists);
	        
	    }finally {
	    	client.close();
	    }
	}

}
