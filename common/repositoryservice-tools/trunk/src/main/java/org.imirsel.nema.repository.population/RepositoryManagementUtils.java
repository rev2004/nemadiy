/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository.population;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

import org.imirsel.m2k.evaluation.classification.ClassificationTextFile;
import org.imirsel.m2k.evaluation.tagsClassification.TagClassificationBinaryFileReader;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.migration.CbrowserClient;


/**
 *
 * @author kriswest
 */
public class RepositoryManagementUtils {

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
    
    private static final Logger logger = Logger.getLogger(RepositoryManagementUtils.class.getName());
	
    public static void insertDirOfAudioFiles(File root, List<String[]> fileMetadataTags, int collection_id) {
        try{
            logger.info("");
            logger.info("Collection id: " + collection_id);
            logger.info("Directory: " + root.getAbsolutePath());
            logger.info("File metadata tags:");
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String[] strings = it.next();
                logger.info("\t" + strings[0] + ":\t" + strings[1]);
            }
            logger.info("");

            RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();

            client.setAutocommit(false);

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
                    int file_id = client.insertFile(track, idMap.get(track).getAbsolutePath());
                    if (file_id == -1){
                        numFilesFailed++;
                    }else{
                        //insert metadata for file
                        for (int i = 0; i < fileMetaIDs.length; i++){
                            int metaValId = fileMetaIDs[i];
                            client.insertFileMetaLink(file_id, metaValId);
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
            client.commit();
            client.setAutocommit(true);

        }catch (SQLException ex){
            logger.log(Level.SEVERE, null, ex);
        }
    }


    public static final String USAGE = "Insert directory tree of audio files\n" +
            "-f /path/to/files/to/insert collection_id(int) metakey1 metaval1 ... metakeyN metavalN\n\n" +
            "Migrate metadata from cbrowser\n" +
            "-m\n\n" +
            "Insert test/train dataset\n" +
            "-dtt \"name\" \"description\" SubjectMetadata FilterMetadata(null if none) /path/subset/file /path/test/set/file/1 ... /path/test/set/file/N\n\n" +
            "Insert test only dataset\n" +
            "-dt \"name\" \"description\" SubjectMetadata FilterMetadata(null if none) /path/subset/file\n\n" +
            "Insert track metadata from a tabbed list file\n" +
            "-lf /path/to/list/file trackMetadataTypeName\n\n" +
            "Insert tag metadata from a list file\n" +
            "-tag /path/to/tag/list/file tagMetadataTypeName\n\n";
    public static void main(String[] args){
        if (args[0].equals("-f")){
            String dir = args[1];
            int collection_id;
            try{
                collection_id = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                throw new RuntimeException("Failed to parse collection id: " + args[2] + "\n" + USAGE);
            }

            List<String[]> meta = new ArrayList<String[]>();
            if (args.length % 2 != 1){
                throw new RuntimeException("Wrong number of arguments received!\n" + USAGE);
            }
            if(args.length <= 3){
                logger.info("WARNING: No metadata arguments received!");
            }
            for (int i = 3; i < args.length; i+=2){
                String key = args[i];
                String val = args[i+1];
                meta.add(new String[]{key,val});
            }

            insertDirOfAudioFiles(new File(dir), meta, collection_id);
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
	            logger.info("Inserting metadata definition for: " + metadatatype);
	
	            int togo = 10;
	            while(togo > 0){
	                logger.info("Commencing metadata definition in " + togo + " seconds");
	                togo -= 5;
	                try{
	                    Thread.sleep(5000);
	                }catch (InterruptedException ex){}
	            }
	            client.insertTrackMetaDef(metadatatype);
	            metaId = client.getTrackMetadataID(metadatatype);
	        }
	        Map<String,String> map = ClassificationTextFile.readClassificationFile(listFile, true);
	        logger.info("got data for " + map.size() + " tracks");
	
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
	
	        String track;
	        int valId;
	        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
	            track = it.next();
	            valId = client.insertTrackMeta(metaId, map.get(track));
	            client.insertTrackMetaLink(track, valId);
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
	                    	client.insertTrackMetaLink(RemapMusicDBFilenamesClass.convertFileToMIREX_ID(new File(lineComps[0].trim())), val_id);
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
	                Logger.getLogger(TagClassificationBinaryFileReader.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            client.endTransation();
	        }
        }catch(SQLException e){
        	try {
				client.rollback();
			} catch (SQLException e1) {
				Logger.getLogger(TagClassificationBinaryFileReader.class.getName()).log(Level.SEVERE, "Exception occured while rolling back transaction", e);
			}
        	throw new RuntimeException("SQLException occured while inserting data from file: " + tag_file.getAbsolutePath(), e);
        }
    }

}
