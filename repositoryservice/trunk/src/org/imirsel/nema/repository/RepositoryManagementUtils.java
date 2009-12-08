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

/**
 *
 * @author kriswest
 */
public class RepositoryManagementUtils {
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

            RepositoryClientImpl client = new RepositoryClientImpl();

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
            "-f /path/to/files/to/insert metakey1 metaval1 ... metakeyN metavalN";
    public static void main(String[] args){
        if (args[0].equals("-f")){
            String dir = args[1];
            int collection_id;
            try{
                collection_id = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                throw new RuntimeException("Failed to parse collection id: " + args[2]);
            }

            List<String[]> meta = new ArrayList<String[]>();
            if (args.length % 2 != 1){
                throw new RuntimeException("Wrong number of arguments received!");
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
        }else{
            System.out.println("Unrecognised argument: " + args[0]);
        }
    }


//    public void migrate_metadata(){
//        //get tracks from new DB
//
//        //for each track get title artist album genre
//        //SELECT cbrowser.tracks.track_title, cbrowser.artists.artist_name, cbrowser.albums.album_name, cbrowser.genres.genre_label
//        //FROM cbrowser.tracks, cbrowser.artists, cbrowser.albums, cbrowser.genres
//        //WHERE cbrowser.tracks.track_internal_id=?
//        //AND cbrowser.tracks.track_artist=cbrowser.artists.artist_id
//        //AND cbrowser.tracks.track_album=cbrowser.albums.album_id
//        //AND cbrowser.tracks.track_genre=cbrowser.genres.genre_id
//
//        //insert against track id
//
//        //report tracks with missing metadata
//    }
//
//    public int insert_dataset(File dataset_subset_file, NEMADataset){
//
//
//        //return dataset id
//    }
//
//    public void insert_split_for_dataset(File testset_file, int dataset_id){
//        //read up test set tracks
//
//        //get subset for dataset
//
//        //subtract test set tracks
//
//        //insert test and training sets
//
//    }


}
