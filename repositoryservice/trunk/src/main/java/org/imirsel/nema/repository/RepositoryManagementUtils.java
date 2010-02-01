/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.evaluation2.classification.ClassificationResultReadClass;
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


    static HashSet<String> audio_extensions;
    static{
        audio_extensions = new HashSet<String>();
        audio_extensions.add(".wav");
        audio_extensions.add(".WAV");
        audio_extensions.add(".mp3");
        audio_extensions.add(".MP3");
    }
    

    public static void insertDirOfAudioFiles(File root, List<String[]> fileMetadataTags, int collection_id) {
        try{
            System.out.println("");
            System.out.println("Collection id: " + collection_id);
            System.out.println("Directory: " + root.getAbsolutePath());
            System.out.println("File metadata tags:");
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String[] strings = it.next();
                System.out.println("\t" + strings[0] + ":\t" + strings[1]);
            }
            System.out.println("");

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
                                System.out.println("WARNING: more than one file found for ID: " + id + "\nOld: " + fileOld.getAbsolutePath() + "\nNew: " + file.getAbsolutePath());
                            }
                        }else{
                            System.out.println("ignorning file " + name + " with extension " + ext);
                        }
                    }
                }
            }
            System.out.println("Found " + idMap.size() + " files to insert for collection id: " + collection_id);

            int togo = 10;
            while(togo > 0){
                System.out.println("Commencing metadata definition insertions in " + togo + " seconds");
                togo -= 5;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


            System.out.println("Inserting metadata definitions");
            //insert metadata defs, fail on duplication
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String tag = it.next()[0];
                try{
                    client.insertFileMetaDef(tag);
                }catch (SQLException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Retrieving definitions");
            try{
                //retrieve ids of definitions
                client.initTypesMaps();
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Inserting metadata values");
            //insert metadata values, fail on duplication
            for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                String[] tag = it.next();
                try{
                    client.insertFileMeta(client.getFileMetadataID(tag[0]), tag[1]);
                }catch (SQLException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Retrieving metadata value to ID map");
            Map<Integer, Map<String, Integer>> fileMetaValueMap;
            int[] fileMetaIDs = new int[fileMetadataTags.size()];
            int idx = 0;
            try{
                //retrieve ids of values
                fileMetaValueMap = client.getFileMetadataValueIDs();
                for (Iterator<String[]> it = fileMetadataTags.iterator(); it.hasNext();){
                    String[] tag = it.next();
                    fileMetaIDs[idx] = fileMetaValueMap.get(client.getFileMetadataID(tag[0])).get(tag[1]);
                    System.out.println(fileMetaIDs[idx] + " = " + tag[0] + ":" + tag[1]);
                    idx++;
                }
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }


            togo = 10;
            while(togo > 0){
                System.out.println("Commencing file insertions in " + togo + " seconds");
                togo -= 5;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("Inserting files");
            int numFilesInserted = 0;
            int numFilesFailed = 0;
            int num = 0;
            //for each track id, insert and fail on duplication
            for (Iterator<String> it = idMap.keySet().iterator(); it.hasNext();){
                String track = it.next();
                try{
                    client.insertTrack(track);
                }catch (SQLException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                try{
                    //insert collection link
                    client.insertTrackCollectionLink(collection_id, track);
                }catch (SQLException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                num++;
                if (num % 100 == 0){
                    System.out.println("inserted " + num + " of " + idMap.size());
                }
            }
            System.out.println("Inserted " + numFilesInserted + ", failed " + numFilesFailed);

            System.out.println("Commiting...");
            client.commit();
            client.setAutocommit(true);

        }catch (SQLException ex){
            Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
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
            "-lf /path/to/list/file trackMetadataTypeName\n\n";
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
                System.out.println("WARNING: No metadata arguments received!");
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
            System.out.println("Inserting test/train dataset");
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

                System.out.println("Name:                " + name);
                System.out.println("Description:\n" + description);
                System.out.println("Subject metadata:    " + args[3]);
                System.out.println("Subject metadata id: " + subject_track_metadata_type_id);
                System.out.println("Filter metadata:     " + args[4]);
                System.out.println("Filter metadata id:  " + filter_track_metadata_type_id);
                System.out.println("Subset file path:    " + dataset_subset_file.getAbsolutePath());
                System.out.print(  "Test set files:\n");
                for (Iterator<File> it = testset_files.iterator(); it.hasNext();){
                    System.out.println(it.next().getAbsolutePath());
                }

                int togo = 10;
                while(togo > 0){
                    System.out.println("Commencing dataset insert in " + togo + " seconds");
                    togo -= 5;
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                client.insertTestTrainDataset(name, description,  subject_track_metadata_type_id, filter_track_metadata_type_id, dataset_subset_file, testset_files);
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else if (args[0].equals("-dt")){
            System.out.println("Inserting single test set dataset");

            try{
            	RepositoryUpdateClientImpl client = new RepositoryUpdateClientImpl();
                if (args.length < 6){
                    System.out.println("Insufficent arguments!\n" + USAGE);
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
                

                System.out.println("Name:                " + name);
                System.out.println("Description:\n" + description);
                System.out.println("Subject metadata:    " + args[3]);
                System.out.println("Subject metadata id: " + subject_track_metadata_type_id);
                System.out.println("Filter metadata:     " + args[4]);
                System.out.println("Filter metadata id:  " + filter_track_metadata_type_id);
                System.out.println("Subset file path:    " + dataset_subset_file.getAbsolutePath());

                int togo = 10;
                while(togo > 0){
                    System.out.println("Commencing dataset insert in " + togo + " seconds");
                    togo -= 5;
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException ex){
                        Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                client.insertTestOnlyDataset(name, description, subject_track_metadata_type_id, filter_track_metadata_type_id, dataset_subset_file);
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else if (args[0].equals("-lf")){
            System.out.println("Inserting metadata from list file");
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
                System.out.println("List file path:      " + listFile.getAbsolutePath());
                System.out.println("Subject metadata:    " + metadata);
                int metadata_type_id = client.getTrackMetadataID(metadata);
                System.out.println("Subject metadata id: " + metadata_type_id);

                client.close();

                insertMetadataFromListFile(listFile, metadata);


            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else{
            System.out.println("Unrecognised argument: " + args[0]);
        }

        System.out.println("--exit--");
    }

    public static void insertMetadataFromListFile(File listFile, String metadatatype) throws SQLException{
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }

        int metaId = client.getTrackMetadataID(metadatatype);
        if (metaId == -1){
            System.out.println("Inserting metadata definition for: " + metadatatype);

            int togo = 10;
            while(togo > 0){
                System.out.println("Commencing metadata definition in " + togo + " seconds");
                togo -= 5;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){
                    Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            client.insertTrackMetaDef(metadatatype);
            metaId = client.getTrackMetadataID(metadatatype);
        }
        Map<String,String> map = ClassificationResultReadClass.readClassificationFile(listFile, true);
        System.out.println("got data for " + map.size() + " tracks");

        int togo = 10;
            while(togo > 0){
            System.out.println("Commencing metadata insertion in " + togo + " seconds");
            togo -= 5;
            try{
                Thread.sleep(5000);
            }catch (InterruptedException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String track;
        int valId;
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
            track = it.next();
            valId = client.insertTrackMeta(metaId, map.get(track));
            client.insertTrackMetaLink(track, valId);
        }
    }

    public static void migrate_metadata() {
    	RepositoryUpdateClientImpl client;
        try{
            client = new RepositoryUpdateClientImpl();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to init conenctions to repository DB");
        }

        //get tracks from new DB
        List<String> tracks;
        try{
            tracks = client.getAllTracks();
        }catch (SQLException ex){
            throw new RuntimeException("Failed to retrieve tracks from repository DB");
        }

        System.out.println("Inserting track metadata definitions");
        try{
            client.insertTrackMetaDef(TRACK_ALBUM);
            client.insertTrackMetaDef(TRACK_ARTIST);
            client.insertTrackMetaDef(TRACK_GENRE);
            client.insertTrackMetaDef(TRACK_TITLE);
        }catch (SQLException ex){
            Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        int albumMetaId = client.getTrackMetadataID(TRACK_ALBUM);
        int artistMetaId = client.getTrackMetadataID(TRACK_ARTIST);
        int genreMetaId = client.getTrackMetadataID(TRACK_GENRE);
        int titleMetaId = client.getTrackMetadataID(TRACK_TITLE);


        System.out.println("Retrieving definitions");
            try{
                //retrieve ids of definitions
                client.initTypesMaps();
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

        int togo = 10;
        while(togo > 0){
            System.out.println("Commencing metadata migration in " + togo + " seconds");
            togo -= 5;
            try{
                Thread.sleep(5000);
            }catch (InterruptedException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Migrating metadata for " + tracks.size() + " tracks");

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
            
            try{
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
            }catch (SQLException ex){
                Logger.getLogger(RepositoryManagementUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            done++;
            if (done % 100 == 0){
                System.out.println("done " + done + " of " + tracks.size());
            }
        }


    }

    



}
