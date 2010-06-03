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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.SingleTrackEvalFileType;
import org.imirsel.nema.analytics.util.io.FileConversionUtil;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.analytics.util.io.PathAndTagCleaner;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;

public class ImirselDatasetIngestor {

	static final Logger logger = Logger.getLogger(ImirselDatasetIngestor.class.getName());
	
	//root audio directory
	// audio file extension
	// collection id
	// copy the files here /data/raid3/audio/format/??/??
	// series name - All audio starts with that name a,b,c,...
	// dataSetname -name for the dataset
	// dataSet Description
	// metadataType
	public static void main(String[] args) {
		try {
		File rootAudioDir = new File(args[0]); 
		String audioFileExtension = args[1];
		int collection_id = Integer.parseInt(args[2]); 
		File audioDirectory = new File(args[3]); 
		String seriesName = args[4];
		String datasetName = args[5];
		String datasetDescription = args[6];
		String metadataType = args[7];
		Class<? extends SingleTrackEvalFileType> readerFileType;
		Class<? extends SingleTrackEvalFileType> writerFileType;
		try {
			readerFileType = (Class<? extends SingleTrackEvalFileType>) ImirselDatasetIngestor.class.getClassLoader().loadClass(args[8]);
			writerFileType = (Class<? extends SingleTrackEvalFileType>) ImirselDatasetIngestor.class.getClassLoader().loadClass(args[9]);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		boolean doingFileMeta = false;
		boolean doingFoldDirs = false;
		List<File> foldDirs = new ArrayList<File>();
		List<String[]> fileMetadataTags = new ArrayList<String[]>();
		for(int i = 10; i < args.length; i++) {
			if(args[i].equals("-m")) {
				doingFileMeta = true;
				doingFoldDirs = false;
				continue;
			}else if(args[i].equals("-d")) {
				doingFileMeta = false;
				doingFoldDirs = true;
				continue;
			}
			
			if(doingFoldDirs) {
				foldDirs.add(new File(args[i]));
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
		
		moveRenameAndInsertDataset(rootAudioDir, audioFileExtension, foldDirs, fileMetadataTags, collection_id, audioDirectory, seriesName, datasetName, datasetDescription, metadataType, readerFileType, writerFileType);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**Puts whole metadata file in the database. Melody,chord, a whole chunk of data
	 * gets added
	 * 
	 * @param rootAudioDir Where the files are present
	 * @param audioFileExtension the extension of the file
	 * @param foldDirs  Fold directory 
	 * @param fileMetadataTags corresponds to key=value pair; bitrate=96k etc...
	 * @param collection_id id of a collection -new collection gets created.
	 * @param newAudioDirectory store the files over -copy from rootAudioDir
	 * @param seriesName the collection prefix -see the /data/raid3/audio/wavs/44s 
	 * @param datasetName -new dataset is created.
	 * @param datasetDescription description of the dataset
	 * @param metadataType name of the track metadata definition table /chordfile file type- single track eval file
	 * @param readerFileType for reading metadata for example chord shorthand format.
	 * @param writerFileType write to new file -for example chord shorthandformat is converted to number 
	 * format and then stored in the database
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void moveRenameAndInsertDataset(
			File rootAudioDir, 
			String audioFileExtension,
			List<File> foldDirs, 
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
		System.out.println("audioFileExtension: " +  audioFileExtension);
		System.out.println("collection_id:      " +  collection_id);
		System.out.println("newAudioDirectory:  " + newAudioDirectory.getAbsolutePath());
		System.out.println("seriesName:         " +  seriesName);
		System.out.println("datasetName:        " + datasetName);
		System.out.println("datasetDescription: " + datasetDescription); 
		System.out.println("metadataType:       " + metadataType);
		System.out.println("readerFileType:     " + readerFileType.getName());
		System.out.println("writerFileType:     " + writerFileType.getName()); 
		System.out.println("Fold directories:   ");
		for (Iterator<File> iterator = foldDirs.iterator(); iterator
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
		
		//read all track metadata
		Map<String, NemaData> idToMetadata = new HashMap<String, NemaData>();
		List<List<NemaData>> foldLists = new ArrayList<List<NemaData>>(foldDirs.size());
		for (Iterator<File> iterator = foldDirs.iterator(); iterator
				.hasNext();) {
			File toRead = iterator.next();
			System.out.println("reading: " + toRead.getAbsolutePath());
			List<NemaData> aList = FileConversionUtil.readData(toRead, null, readerFileType);
			foldLists.add(aList);
			for (Iterator<NemaData> iterator2 = aList.iterator(); iterator2
					.hasNext();) {
				NemaData nemaData = iterator2.next();
				idToMetadata.put(nemaData.getId(), nemaData);
			}
		}
		System.out.println("got " + idToOldFile.size() + " files to insert from " + foldLists.size() + " folds");
		
		//confirm we have metadata for all tracks
		if(!idToOldFile.keySet().containsAll(idToMetadata.keySet())) {
			System.out.println("WARNING: the list of files did not contain all the tracks metadata was found for!");
			System.out.println("Tracks with metadata but no files:");
			Set<String> noFile = new HashSet<String>(idToMetadata.keySet());
			noFile.removeAll(idToOldFile.keySet());
			for (Iterator<String> iterator = noFile.iterator(); iterator
					.hasNext();) {
				System.out.println("\t" + iterator.next());
			}
			System.out.println("---");
		}
		
		if(!idToOldFile.keySet().containsAll(idToMetadata.keySet())) {
			System.out.println("WARNING: the list of metadata files did not contain all the tracks we found files for!");
			System.out.println("Tracks with files but no metadata:");
			Set<String> noMeta = new HashSet<String>(idToOldFile.keySet());
			noMeta.removeAll(idToMetadata.keySet());
			for (Iterator<String> iterator = noMeta.iterator(); iterator
					.hasNext();) {
				System.out.println("\t" + iterator.next());
			}
			System.out.println("---");
		}
		
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
		RepositoryManagementUtils.insertDirOfAudioFiles(newHome, fileMetadataTags, collection_id, newFileToOldFile);
		
		//insert track metadata
		RepositoryManagementUtils.insertMetadataFromSingleTrackEvalFileType(idToMetadata.values(), metadataType, writerFileType);
		
		//collect up new lists of ids for folds
		List<String> subsetList = new ArrayList<String>(newIdToNewFile.keySet());
		List<List<String>> newFoldTrackIdLists = new ArrayList<List<String>>(foldDirs.size());
		for (Iterator<List<NemaData>> iterator = foldLists.iterator(); iterator
				.hasNext();) {
			List<NemaData> aFold = iterator.next();
			List<String> foldList = new ArrayList<String>(aFold.size());
			for (Iterator<NemaData> iterator2 = aFold.iterator(); iterator2
					.hasNext();) {
				foldList.add(iterator2.next().getId());
			}
			newFoldTrackIdLists.add(foldList);
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
	        System.out.println("Number of folds:     " + newFoldTrackIdLists.size());
	        
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
	
	        client.insertTestOnlyDataset(datasetName, datasetDescription, subject_track_metadata_type_id, filter_track_metadata_type_id, subsetList, newFoldTrackIdLists);
	        
	    }finally {
	    	client.close();
	    }
	}

}
