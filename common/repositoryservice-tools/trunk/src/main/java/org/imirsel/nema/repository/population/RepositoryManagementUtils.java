/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository.population;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.fileTypes.ClassificationTextFile;
import org.imirsel.nema.model.fileTypes.MultipleTrackEvalFileType;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.model.fileTypes.SingleTrackEvalFileType;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.model.util.PathAndTagCleaner;
import org.imirsel.nema.repository.DatabaseConnector;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.population.legacy.CbrowserClient;


/**
 *
 * @author kris.west@gmail.com
 * @since 0.2.0
 */
public class RepositoryManagementUtils {

	public static final DecimalFormat SIX_DIGIT_INTEGER = new DecimalFormat();
	static {
		SIX_DIGIT_INTEGER.setMaximumFractionDigits(0);
		SIX_DIGIT_INTEGER.setMinimumIntegerDigits(6);
		SIX_DIGIT_INTEGER.setGroupingUsed(false);
	}
	
    public static final String TRACK_TITLE = "Title";
    public static final String TRACK_ARTIST = "Artist";
    public static final String TRACK_ALBUM = "Album";
    public static final String TRACK_GENRE = "Genre";
    
    public static final String TRACK_TAG_MAJORMINER = "Tag - MajorMiner";
    public static final String TRACK_TAG_MOOD = "Tag - Mood";
    public static final String TRACK_TAG_TAGATUNE = "Tag - Tagatune";
    
    static HashSet<String> audio_extensions;
    static{
        audio_extensions = new HashSet<String>();
        audio_extensions.add(".wav");
        audio_extensions.add(".WAV");
        audio_extensions.add(".mp3");
        audio_extensions.add(".MP3");
    }
    
    static final Logger logger = Logger.getLogger(RepositoryManagementUtils.class.getName());
	
    public static void insertDirOfAudioFiles(File root, List<String[]> fileMetadataTags, int collection_id, String site) {
    	insertDirOfAudioFiles(root, fileMetadataTags, collection_id, null, site);
    }
    
	public static void insertDirOfAudioFiles(File root, List<String[]> fileMetadataTags, int collection_id, Map<File,File> newFileToOldFile, String site) throws IllegalArgumentException{
        try{
            logger.info("Collection id: " + collection_id);
            logger.info("Directory: " + root.getAbsolutePath());
            logger.info("File metadata tags:");
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String[] strings = it.next();
                logger.info("\t" + strings[0] + ":\t" + strings[1]);
            }
            
            

            RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();


            int siteId = client.getSiteId(site);
            logger.info("Site: " + siteId + " (" + site + ")");
            
            if (siteId == -1){
            	throw new IllegalArgumentException("Site name '" + site + "' could not be resolved to a site ID!");
            }
            
            client.startTransation();

            //get list of all audio files path
            ArrayList<File> todo = new ArrayList<File>();
            HashMap<String, File> idMap = new HashMap<String, File>();
            todo.add(root);
            String id;
            String name;
            String ext;
            int extIndex = -1;
            File file;
            File fileOld;
            while (!todo.isEmpty()){
                File dir = todo.remove(0);
                File[] contents = dir.listFiles();
                for (int i = 0; i < contents.length; i++){
                    file = contents[i];
                    if (file.isDirectory()){
                        todo.add(file);
                    }else{
                        name = file.getName();
                        extIndex = name.lastIndexOf(".");
                        ext = name.substring(extIndex);
                        if (audio_extensions.contains(ext)){
                            id = name.substring(0, extIndex);
                            //create map of id to path - checking for duplication
                            fileOld = idMap.put(id, file);
                            if (fileOld != null){
                                logger.info("WARNING: more than one file found for ID: " + id + "\nOld: " + fileOld.getAbsolutePath() + "\nNew: " + file.getAbsolutePath());
                            }
                        }else{
                            logger.info("ignorning file " + name + " with extension " + ext);
                        }
                    }
                }
            }
            logger.info("Found " + idMap.size() + " files to insert for collection id: " + collection_id);

            int togo = 10;
            while(togo > 0){
                logger.info("Commencing metadata definition insertions in " + togo + " seconds");
                togo -= 5;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }


            logger.info("Inserting metadata definitions");
            //insert metadata defs, fail on duplication
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String tag = it.next()[0];
                try{
                    client.insertFileMetaDef(tag);
                }catch (SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            logger.info("Retrieving definitions");
            try{
                //retrieve ids of definitions
                client.initTypesMaps();
            }catch (SQLException ex){
                logger.log(Level.SEVERE, null, ex);
            }
            logger.info("Inserting metadata values");
            //insert metadata values, fail on duplication
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String[] tag = it.next();
                try{
                    client.insertFileMeta(client.getFileMetadataID(tag[0]), tag[1]);
                }catch (SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            logger.info("Retrieving metadata value to ID map");
            Map<Integer, Map<String, Integer>> fileMetaValueMap;
            int[] fileMetaIDs = new int[fileMetadataTags.size()];
            int idx = 0;
            try{
                //retrieve ids of values
                fileMetaValueMap = client.getFileMetadataValueIDs();
                for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                    String[] tag = it.next();
                    fileMetaIDs[idx] = fileMetaValueMap.get(client.getFileMetadataID(tag[0])).get(tag[1]);
                    logger.info(fileMetaIDs[idx] + " = " + tag[0] + ":" + tag[1]);
                    idx++;
                }
            }catch (SQLException ex){
                logger.log(Level.SEVERE, null, ex);
            }


            togo = 10;
            while(togo > 0){
                logger.info("Commencing file insertions in " + togo + " seconds");
                togo -= 5;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
            }

            logger.info("Inserting files");
            int numFilesInserted = 0;
            int numFilesFailed = 0;
            int num = 0;
            //for each track id, insert and fail on duplication
            for (Iterator<String> it = idMap.keySet().iterator(); it.hasNext();){
                String track = it.next();
                try{
                    client.insertTrack(track);
                }catch (SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
                try{
                    //insert collection link
                    client.insertTrackCollectionLink(collection_id, track);
                }catch (SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
                try{
                    //insert file against track
                	File toInsert = idMap.get(track);
                    int file_id = client.insertFile(track, toInsert.getAbsolutePath(),siteId);
                    if (file_id == -1){
                        numFilesFailed++;
                    }else{
                        //insert metadata for file
                        for (int i = 0; i < fileMetaIDs.length; i++){
                            int metaValId = fileMetaIDs[i];
                            client.insertFileMetaLink(file_id, metaValId);
                        }
                        //if available insert legacy file path
                        if (newFileToOldFile != null){
                        	File oldFile = newFileToOldFile.get(toInsert);
                        	if (oldFile != null){
                        		client.insertLegacyFilePath(file_id, oldFile.getAbsolutePath());
                        	}
                        }
                        numFilesInserted++;
                    }
                    
                }catch (SQLException ex){
                    logger.log(Level.SEVERE, null, ex);
                }
                num++;
                if (num % 100 == 0){
                    logger.info("inserted " + num + " of " + idMap.size());
                }
            }
            logger.info("Inserted " + numFilesInserted + ", failed " + numFilesFailed);

            logger.info("Commiting...");
            client.endTransation();

        }catch (SQLException ex){
            logger.log(Level.SEVERE, null, ex);
        }
    }


    public static final String USAGE = "Insert directory tree of audio files\n" +
            "-f /path/to/files/to/insert collection_id(int) metakey1 metaval1 ... metakeyN metavalN\n\n" +
            "Migrate metadata from cbrowser\n" +
            "-m\n" +
            "\n" +
            "Insert test/train dataset\n" +
            "-dtt \"name\" \"description\" SubjectMetadata FilterMetadata(null if none) /path/subset/file /path/test/set/file/1 ... /path/test/set/file/N\n" +
            "\n" +
            "Insert test only dataset\n" +
            "-dt \"name\" \"description\" SubjectMetadata FilterMetadata(null if none) /path/subset/file\n" +
            "\n" +
            "Insert test only dataset from directory of files\n" +
            "-sdt \"name\" \"description\" SubjectMetadata FilterMetadata(null if none) /path/dataset/directory\n" +
            "\n" +
            "Insert track metadata from a tabbed list file\n" +
            "-lf /path/to/list/file trackMetadataTypeName\n" +
            "\n" +
            "Insert track metadata from a file or directory of files corresponding to a SingleTrackEvalFileType\n" +
            "-sf /path/to/list/file trackMetadataTypeName readerClassName writerClassName\n" +
            "\n" +
            "Insert tag metadata from a list file\n" +
            "-tag /path/to/tag/list/file tagMetadataTypeName\n" +
            "\n";
    public static void main(String[] args) throws ClassNotFoundException{
        if (args[0].equals("-f")){
            String dir = args[1];
            int collection_id;
            try{
                collection_id = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                throw new RuntimeException("Failed to parse collection id: " + args[2] + "\n" + USAGE);
            }
            
            String site = args[3];

            List<String[]> meta = new ArrayList<String[]>();
            if (args.length % 2 != 0){
                throw new RuntimeException("Wrong number of arguments received!\n" + USAGE);
            }
            if(args.length <= 4){
                logger.info("WARNING: No metadata arguments received!");
            }
            for (int i = 4; i < args.length; i+=2){
                String key = args[i];
                String val = args[i+1];
                meta.add(new String[]{key,val});
            }

            insertDirOfAudioFiles(new File(dir), meta, collection_id, site);
        }else if (args[0].equals("-m")){
            migrate_metadata();
        }else if (args[0].equals("-dtt")){
            logger.info("Inserting test/train dataset");
            try{
            	RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();
                if (args.length < 6){
                    throw new RuntimeException("Insufficent arguments!\n" + USAGE);

                }

                String name = args[1];
                String description = args[2];
                int subject_track_metadata_type_id = client.getTrackMetadataID(args[3]);
                int filter_track_metadata_type_id = -1;
                if (!args[4].equalsIgnoreCase("null")){
                    filter_track_metadata_type_id = client.getTrackMetadataID(args[4]);
                }
                File dataset_subset_file = new File(args[5]);
                if (!dataset_subset_file.exists()){
                    throw new RuntimeException("Data set subset file did not exist: " + dataset_subset_file.getAbsolutePath());
                }
                List<File> testset_files = new ArrayList<File>();

                //get test files
                for (int i = 6; i < args.length; i++){
                    File aFile = new File(args[i]);
                    testset_files.add(aFile);
                    if (!aFile.exists()){
                        throw new RuntimeException("Set file did not exist: " + aFile.getAbsolutePath());
                    }
                }

                logger.info("Name:                " + name);
                logger.info("Description:\n" + description);
                logger.info("Subject metadata:    " + args[3]);
                logger.info("Subject metadata id: " + subject_track_metadata_type_id);
                logger.info("Filter metadata:     " + args[4]);
                logger.info("Filter metadata id:  " + filter_track_metadata_type_id);
                logger.info("Subset file path:    " + dataset_subset_file.getAbsolutePath());
                System.out.print(  "Test set files:\n");
                for (Iterator<File> it = testset_files.iterator(); it.hasNext();){
                    logger.info(it.next().getAbsolutePath());
                }

                int togo = 10;
                while(togo > 0){
                    logger.info("Commencing dataset insert in " + togo + " seconds");
                    togo -= 5;
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        logger.log(Level.SEVERE, null, ex);
                    }
                }

                client.insertTestTrainDataset(name, description,  subject_track_metadata_type_id, filter_track_metadata_type_id, dataset_subset_file, testset_files);
            }catch (Exception ex){
                logger.log(Level.SEVERE, null, ex);
            }

        }else if (args[0].equals("-dt")){
            logger.info("Inserting single test set dataset");

            try{
            	RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();
                if (args.length < 6){
                    logger.info("Insufficent arguments!\n" + USAGE);
                }

                String name = args[1];
                String description = args[2];
                int subject_track_metadata_type_id = client.getTrackMetadataID(args[3]);
                int filter_track_metadata_type_id = -1;
                if (!args[4].equalsIgnoreCase("null")){
                    filter_track_metadata_type_id = client.getTrackMetadataID(args[4]);
                }
                File dataset_subset_file = new File(args[5]);
                
                
                if (!dataset_subset_file.exists()){
                    throw new RuntimeException("Data set subset file did not exist: " + dataset_subset_file.getAbsolutePath());
                }
                

                logger.info("Name:                " + name);
                logger.info("Description:\n" + description);
                logger.info("Subject metadata:    " + args[3]);
                logger.info("Subject metadata id: " + subject_track_metadata_type_id);
                logger.info("Filter metadata:     " + args[4]);
                logger.info("Filter metadata id:  " + filter_track_metadata_type_id);
                logger.info("Subset file path:    " + dataset_subset_file.getAbsolutePath());

                int togo = 10;
                while(togo > 0){
                    logger.info("Commencing dataset insert in " + togo + " seconds");
                    togo -= 5;
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        logger.log(Level.SEVERE, null, ex);
                    }
                }

                client.insertTestOnlyDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, dataset_subset_file);
            }catch (Exception ex){
                logger.log(Level.SEVERE, null, ex);
            }
            
        }else if (args[0].equals("-sdt")){
            logger.info("Inserting single test set dataset from directory of SingleTrackEvalFileType files");

            try{
            	RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();
                if (args.length < 6){
                    logger.info("Insufficent arguments!\n" + USAGE);
                }

                String name = args[1];
                String description = args[2];
                int subject_track_metadata_type_id = client.getTrackMetadataID(args[3]);
                int filter_track_metadata_type_id = -1;
                if (!args[4].equalsIgnoreCase("null")){
                    filter_track_metadata_type_id = client.getTrackMetadataID(args[4]);
                }
                
                
                
                File dataset_directory = new File(args[5]);
                if (!dataset_directory.exists()){
                    throw new RuntimeException("Data-set directory did not exist: " + dataset_directory.getAbsolutePath());
                }
                

                logger.info("Name:                " + name);
                logger.info("Description:\n" + description);
                logger.info("Subject metadata:    " + args[3]);
                logger.info("Subject metadata id: " + subject_track_metadata_type_id);
                logger.info("Filter metadata:     " + args[4]);
                logger.info("Filter metadata id:  " + filter_track_metadata_type_id);
                logger.info("Dataset directory:   " + dataset_directory.getAbsolutePath());

                int togo = 10;
                while(togo > 0){
                    logger.info("Commencing dataset insert in " + togo + " seconds");
                    togo -= 5;
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
                
                client.insertTestOnlyDatasetFromDir(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, dataset_directory);
            }catch (Exception ex){
                logger.log(Level.SEVERE, null, ex);
            }
            
        }else if (args[0].equals("-lf")){
            logger.info("Inserting metadata from list file");
            try{
                RepositoryClientImpl client = new RepositoryClientImpl();
                if (args.length < 3){
                    throw new RuntimeException("Insufficent arguments!\n" + USAGE);

                }

                File listFile = new File(args[1]);
                if (!listFile.exists()){
                    throw new RuntimeException("The list file did not exist: " + listFile.getAbsolutePath());
                }
                String metadata = args[2];
                logger.info("List file path:      " + listFile.getAbsolutePath());
                logger.info("Subject metadata:    " + metadata);
                int metadata_type_id = client.getTrackMetadataID(metadata);
                logger.info("Subject metadata id: " + metadata_type_id);

                client.close();

                insertMetadataFromListFile(listFile, metadata);


            }catch (SQLException ex){
                logger.log(Level.SEVERE, null, ex);
            }

        }else if (args[0].equals("-sf")){
            logger.info("Inserting metadata from single files");
            try{
                RepositoryClientImpl client = new RepositoryClientImpl();
                if (args.length < 5){
                    throw new RuntimeException("Insufficent arguments!\n" + USAGE);

                }

                File fileOrDir = new File(args[1]);
                if (!fileOrDir.exists()){
                    throw new RuntimeException("The file or directory did not exist: " + fileOrDir.getAbsolutePath());
                }
                String metadata = args[2];
                logger.info("List file path:      " + fileOrDir.getAbsolutePath());
                logger.info("Subject metadata:    " + metadata);
                int metadata_type_id = client.getTrackMetadataID(metadata);
                logger.info("Subject metadata id: " + metadata_type_id);
                client.close();
                
                String readerClassName = args[3];
                logger.info("Reader class name:   " + readerClassName);
                Class<? extends SingleTrackEvalFileType> readerFileType = 
                	(Class<? extends SingleTrackEvalFileType>) RepositoryManagementUtils.class.getClassLoader().loadClass(readerClassName);
                String writerClassName = args[4];
                logger.info("Writer class name:   " + fileOrDir.getAbsolutePath());
                Class<? extends SingleTrackEvalFileType> writerFileType = 
                	(Class<? extends SingleTrackEvalFileType>) RepositoryManagementUtils.class.getClassLoader().loadClass(writerClassName);

                insertMetadataFromSingleTrackEvalFileType(fileOrDir, new NemaTask(-1,"","",metadata_type_id,metadata,-1), readerFileType, writerFileType);

            }catch (Exception ex){
                logger.log(Level.SEVERE, null, ex);
            }

        }else if(args[0].equals("-tag")){
        	logger.info("Inserting tag metadata from list file");
        	try{
	        	RepositoryClientImpl client = new RepositoryClientImpl();
	            if (args.length < 3){
	                throw new RuntimeException("Insufficent arguments!\n" + USAGE);
	            }
	            
	        	File listFile = new File(args[1]);
	            if (!listFile.exists()){
	                throw new RuntimeException("The list file did not exist: " + listFile.getAbsolutePath());
	            }
	        	String tagType = args[2];
	        	logger.info("List file path:      " + listFile.getAbsolutePath());
	            logger.info("Subject metadata:    " + tagType);
	            int metadata_type_id = client.getTrackMetadataID(tagType);
	            logger.info("Subject metadata id: " + metadata_type_id);
	
	            client.close();
	            
	            insertTagMetadata(listFile, tagType);
	            
        	}catch (SQLException ex){
                logger.log(Level.SEVERE, null, ex);
            }
        }else{
            logger.info("Unrecognised argument: " + args[0]);
        }

        logger.info("--exit--");
    }

    public static void insertMetadataFromListFile(File listFile, String metadatatype) {
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }
        
        try{
	        client.startTransation();
	        
	        int metaId = client.getTrackMetadataID(metadatatype);
	        if (metaId == -1){
	            logger.info("Inserting metadata type definition for: " + metadatatype);
	
	            int togo = 10;
	            while(togo > 0){
	                logger.info("Commencing metadata type definition in " + togo + " seconds");
	                togo -= 5;
	                try{
	                    Thread.sleep(5000);
	                }catch (InterruptedException ex){}
	            }
	            client.insertTrackMetaDef(metadatatype);
	            metaId = client.getTrackMetadataID(metadatatype);
	        }
	        ClassificationTextFile reader = new ClassificationTextFile(metadatatype);
	        List<NemaData> data = reader.readFile(listFile);
	        logger.info("got data for " + data.size() + " tracks");
	
	        int togo = 10;
	            while(togo > 0){
	            logger.info("Commencing metadata insertion in " + togo + " seconds");
	            togo -= 5;
	            try{
	                Thread.sleep(5000);
	            }catch (InterruptedException ex){
	                logger.log(Level.SEVERE, null, ex);
	            }
	        }
	
            NemaData track;
	        int valId;
	        for (Iterator<NemaData> it = data.iterator(); it.hasNext();){
	            track = it.next();
	            valId = client.insertTrackMeta(metaId, track.getStringMetadata(metadatatype));
	            client.insertTrackMetaLink(track.getId(), valId);
	        }
	        client.endTransation();
        }catch (Exception ex){
        	try {
				client.rollback();
			} catch (SQLException e) {
				throw new RuntimeException("SQLException occured whien rolling back transaction!",e);
			}
            throw new RuntimeException("SQLException occured!",ex);
        }
    }
    
    /**
     * Inserts metadata from a file or directory that contains files that
     * are in the format represented by a SingleTrackEvalFileType implementation. 
     * The files are read and the data inserted into the DB.
     * 
     * @param fileOrDir The file or directory of files to insert data from.
     * @param exemplarTask A sample task with a metadata setting pointing to the 
     * data to insert.
     * @param readerFileType The class representing the SingleTrackEvalFileType 
     * implementation to use to read the data
     * @throws IOException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws FileNotFoundException 
     * @throws IllegalArgumentException 
     */
    public static void insertMetadataFromSingleTrackEvalFileType(File fileOrDir, 
    		NemaTask exemplarTask, 
    		Class<? extends SingleTrackEvalFileType> readerFileType, 
    		Class<? extends SingleTrackEvalFileType> writerFileType) 
    		throws IllegalArgumentException, FileNotFoundException, 
    		InstantiationException, IllegalAccessException, IOException {
    	
    	List<NemaData> data = FileConversionUtil.readData(fileOrDir, exemplarTask, readerFileType);
        logger.info("got data for " + data.size() + " tracks");
        insertMetadataFromSingleTrackEvalFileType(data, exemplarTask.getSubjectTrackMetadataName(), writerFileType);
    }
    		
	/**
     * Inserts metadata from a collection NemaData Objects. The metadata is
     * encoded into Strings using a SingleTrackEvalFileType prior to insertion
     * into the DB.
     * 
     * @param data The List of NemaData Objects to insert.
     * @param metadataType Metadata type name that should be inserted.
     * @param readerFileType The class representing the SingleTrackEvalFileType 
     * implementation to use to read the data
     */
    public static void insertMetadataFromSingleTrackEvalFileType(Collection<NemaData> data, 
    	    String metadataType, 
    		Class<? extends SingleTrackEvalFileType> writerFileType) {
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }
        
        try{
	        client.startTransation();
	        
	        int metaId = client.getTrackMetadataID(metadataType);
	        if (metaId == -1){
	            logger.info("Inserting metadata type definition for: " + metadataType);
	
	            int togo = 10;
	            while(togo > 0){
	                logger.info("Commencing metadata type definition in " + togo + " seconds");
	                togo -= 5;
	                try{
	                    Thread.sleep(5000);
	                }catch (InterruptedException ex){}
	            }
	            client.insertTrackMetaDef(metadataType);
	            metaId = client.getTrackMetadataID(metadataType);
	        }
	        
	        
	
	        int togo = 10;
	            while(togo > 0){
	            logger.info("Commencing metadata insertion in " + togo + " seconds");
	            togo -= 5;
	            try{
	                Thread.sleep(5000);
	            }catch (InterruptedException ex){
	                logger.log(Level.SEVERE, null, ex);
	            }
	        }
	
	        SingleTrackEvalFileType writer = writerFileType.newInstance();
//	        if(writerFileType.equals(ClassificationTextFile.class)) {
//	        	((ClassificationTextFile)writer).setMetadataType(metadataType);
//	        }
	        
	        NemaData track;
	        int valId;
	        for (Iterator<NemaData> it = data.iterator(); it.hasNext();){
	            track = it.next();
	            String strEncData = stringEncodeSingleTrackEvalFileType(track, writer);
	            
	            valId = client.insertTrackMeta(metaId, strEncData);
	            client.insertTrackMetaLink(track.getId(), valId);
	        }
	        client.endTransation();
        }catch (Exception ex){
        	try {
				client.rollback();
			} catch (SQLException e) {
				throw new RuntimeException("SQLException occured whien rolling back transaction!",e);
			}
            throw new RuntimeException("SQLException occured!",ex);
        }
    }
    
    private static String stringEncodeSingleTrackEvalFileType(NemaData toEncode, SingleTrackEvalFileType writer) throws IllegalArgumentException, IOException {
    	String strEncData = "";
    	
    	//write data to a temp file
    	File temp = File.createTempFile(toEncode.getId(), ".txt");
    	writer.writeFile(temp, toEncode);
		
    	//read it up again
    	BufferedReader in = new BufferedReader(new FileReader(temp));
    	String line = in.readLine();
    	while(line != null) {
    		strEncData += line + "\n";
    		line = in.readLine();
    	}
    	
    	//return it
    	return strEncData;
    }
    
    public static void insertTagMetadata(File tag_file, String tag_meta_type_name){
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }
    	
        try{
	        client.startTransation();
        
	        logger.info("Inserting track metadata definition for: " + tag_meta_type_name);
	        int meta_id = -1;
	        try{
	        	client.insertTrackMetaDef(tag_meta_type_name);
	        	meta_id = client.getTrackMetadataID(tag_meta_type_name);
	        	logger.info("Retrieved track metadata type id: " + meta_id + " for type name: " + tag_meta_type_name);
	        }catch (SQLException ex){
	            throw new RuntimeException("Failed to insert or retrieve the track metadata type id for type: " + tag_meta_type_name, ex);
	        }
	        
	        HashMap<String,Integer> tagToId = new HashMap<String,Integer>();
	        
	        logger.info("Reading tag data from file: " + tag_file);
	        BufferedReader textBuffer = null;
	        try {
	            textBuffer = new BufferedReader(new FileReader(tag_file));
	
	            String line = textBuffer.readLine();
	            String[] lineComps;
	            int lineNum = 0;
	            Integer val_id = null;
	            String tag;
	            while (line != null){
	                lineNum++;
	                line = line.trim();
	                if (!line.equals("")){
	                    lineComps = line.split("\t");
	                    if (lineComps.length < 2){
	                        client.rollback();
	                        throw new RuntimeException("short line found, line: " + lineNum + ", file: " + tag_file.getAbsolutePath() + "\n" +
	                                    "check if file is correctly deliminated with tabs (rather than other whitespace characters or commas) and has no preamble");
	                    }else{
	                    	tag = lineComps[1].trim();
	                    	val_id = tagToId.get(tag);
	                    	if(val_id == null){
	                    		val_id = client.insertTrackMeta(meta_id, tag);
	                    		tagToId.put(tag, val_id);
	                    	}
	                    	client.insertTrackMetaLink(PathAndTagCleaner.convertFileToMIREX_ID(new File(lineComps[0].trim())), val_id);
	                    }
	                }else{
	                    logger.info("empty line ignored, line: " + lineNum + ", file: " + tag_file.getAbsolutePath());
	                }
	                line = textBuffer.readLine();
	            }
	            
	        } catch (IOException ex) {
	        	client.rollback();
	            throw new RuntimeException("IOException occured while reading file: " + tag_file.getAbsolutePath(), ex);
	        } finally {
	            try {
	                textBuffer.close();
	            } catch (Exception ex) {
	                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            client.endTransation();
	        }
        }catch(SQLException e){
        	try {
				client.rollback();
			} catch (SQLException e1) {
				Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, "Exception occured while rolling back transaction", e);
			}
        	throw new RuntimeException("SQLException occured while inserting data from file: " + tag_file.getAbsolutePath(), e);
        }
    }

    public static void migrate_metadata() {
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }

        try{
	        client.startTransation();
	        
	        //get tracks from new DB
	        List<String> tracks = client.getAllTracks();
	        
	        logger.info("Inserting track metadata definitions");
            client.insertTrackMetaDef(TRACK_ALBUM);
            client.insertTrackMetaDef(TRACK_ARTIST);
            client.insertTrackMetaDef(TRACK_GENRE);
            client.insertTrackMetaDef(TRACK_TITLE);
	        
	        int albumMetaId = client.getTrackMetadataID(TRACK_ALBUM);
	        int artistMetaId = client.getTrackMetadataID(TRACK_ARTIST);
	        int genreMetaId = client.getTrackMetadataID(TRACK_GENRE);
	        int titleMetaId = client.getTrackMetadataID(TRACK_TITLE);
	
	
	        logger.info("Retrieving definitions");
            //retrieve ids of definitions
            client.initTypesMaps();
	            
	        int togo = 10;
	        while(togo > 0){
	            logger.info("Commencing metadata migration in " + togo + " seconds");
	            togo -= 5;
	            try{
	                Thread.sleep(5000);
	            }catch (InterruptedException ex){}
	        }
	
	        logger.info("Migrating metadata for " + tracks.size() + " tracks");
	
	        CbrowserClient cb_client;
	        try{
	            cb_client = new CbrowserClient();
	        }catch (SQLException ex){
	            throw new RuntimeException("Failed to init conenctions to cbrowser DB");
	        }
	
	        String track;
	        Map<String,String> vals;
	        int valId;
	        int done = 0;
	        for (Iterator<String> it = tracks.iterator(); it.hasNext();){
	            track = it.next();
	            
	            vals = cb_client.getTrackMetadata(track);
                if(vals != null){
                    //insert metadata values, get ids and link to track
                    String album = vals.get(CbrowserClient.ALBUM);
                    valId = client.insertTrackMeta(albumMetaId, album);
                    client.insertTrackMetaLink(track, valId);

                    String artist = vals.get(CbrowserClient.ARTIST);
                    valId = client.insertTrackMeta(artistMetaId, artist);
                    client.insertTrackMetaLink(track, valId);

                    String title = vals.get(CbrowserClient.TITLE);
                    valId = client.insertTrackMeta(titleMetaId, title);
                    client.insertTrackMetaLink(track, valId);

                    String genre = vals.get(CbrowserClient.GENRE);
                    valId = client.insertTrackMeta(genreMetaId, genre);
                    client.insertTrackMetaLink(track, valId);
                }
	            
	            done++;
	            if (done % 100 == 0){
	                logger.info("done " + done + " of " + tracks.size());
	            }
	        }
	        client.endTransation();
	        
        }catch(SQLException e){
        	try {
				client.rollback();
			} catch (SQLException e1) {
				Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, "Exception occured while rolling back transaction", e);
			}
        	throw new RuntimeException("SQLException occured while migrating data from cbrowser DB", e);
        }

    }
}
