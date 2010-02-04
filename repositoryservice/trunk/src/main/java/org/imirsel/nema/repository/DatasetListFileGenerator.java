/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.nema.repositoryservice.*;
import org.imirsel.nema.model.*;


/**
 *
 * @author kriswest
 */
public class DatasetListFileGenerator {

    
    /**
     * Utility method to help build file type constraints which are used to
     * select audio files corresponding to each track with the specified
     * encoding.
     * 
     * @param bitrate     valid values are: 128k, 96k
     * @param channels    valid values are: 1, 2
     * @param clip_type   valid values are: 30, full
     * @param encoding    valid values are: mp3, wav
     * @param sample_rate valid values are: 22050, 44100
     *
     * @return Constraints that can be passed to retrieve file sets.
     */
    public static Set<NEMAMetadataEntry> buildConstraints(String bitrate, String channels, String clip_type, String encoding, String sample_rate){
        HashSet<NEMAMetadataEntry> constraint = new HashSet<NEMAMetadataEntry>();
        if (bitrate != null && !bitrate.trim().equals("")){
            constraint.add(new NEMAMetadataEntry("bitrate", bitrate));
        }
        if (channels != null && !channels.trim().equals("")){
            constraint.add(new NEMAMetadataEntry("channels", channels));
        }
        if (clip_type != null && !clip_type.trim().equals("")){
            constraint.add(new NEMAMetadataEntry("clip-type", clip_type));
        }
        if (encoding != null && !encoding.trim().equals("")){
            constraint.add(new NEMAMetadataEntry("encoding", encoding));
        }
        if (sample_rate != null && !sample_rate.trim().equals("")){
            constraint.add(new NEMAMetadataEntry("sample-rate", sample_rate));
        }

        return constraint;
    }

    /**
     * Writes out a set of files describing the test and training sets for each
     * iteration of an experiment. For a single split a one entry list is
     * returned. For an N-fold experiment an N-entry list is returned.
     * The list contains an 2 entry array with the files written out indexed
     * {train,test}.
     *
     * @param dataset_id The ID of the dataset to retrieve data from.
     * @param delimiter The delimiter to use to separate the file path from
     * the class name in the training file.
     * @param directory The directory to write the files into.
     * @param file_encoding_constraint The file metadata constraints to use to
     * select audio files corresponding to each track.
     *
     * @return a list of File object arrays where each entry in the list is
     * an array containing {/path/to/Train/File, /path/to/Test/File} for a
     * particular iteration
     * @throws SQLException
     */
    public static List<File[]> writeOutExperimentSplitFiles(int dataset_id, String delimiter, File directory, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        System.out.println("Writing out train and test files for dataset_id=" + dataset_id);
        System.out.println("File encoding constraints: ");
        for (Iterator<NEMAMetadataEntry> it = file_encoding_constraint.iterator(); it.hasNext();){
            NEMAMetadataEntry meta = it.next();
            System.out.println("\t" + meta.getType() + ":\t" + meta.getValue());
        }

        RepositoryClientInterface client = new RepositoryClientImpl();
        NEMADataset dataset = client.getDataset(dataset_id);
        int subjectMetadata = dataset.getSubjectTrackMetadataId();

        ArrayList<File[]> out = new ArrayList<File[]>();
        List<List<NEMASet>> sets = client.getExperimentSets(dataset);
        for (Iterator<List<NEMASet>> it = sets.iterator(); it.hasNext();){
            List<NEMASet> list = it.next();
            LinkedList<File> files = new LinkedList<File>();
            String setType;
            for (Iterator<NEMASet> it1 = list.iterator(); it1.hasNext();){
                NEMASet set = it1.next();
                setType = set.getSetTypeName();

                if (setType.equalsIgnoreCase("test")){
                    files.add(writeOutSingleTestFile(client, setType, set.getId(), directory, file_encoding_constraint));
                }else{
                    files.addFirst(writeOutGTFile(client, setType, delimiter, set.getId(), subjectMetadata, directory, file_encoding_constraint));
                }
            }
            out.add(files.toArray(new File[files.size()]));
        }

        return out;
    }

    /**
     * Writes out a ground-truth file and feature extraction list file for a
     * dataset. These both list the paths to all the audio files in the
     * collection (one per line). The ground-truth file also contains the
     * class name of the track separated from the path by a delimiter string.
     * Conventionally the delimiter is a tab '\t' or comma ','.
     * @param dataset_id The ID of the dataset to retrieve data from.
     * @param delimiter The delimiter to use to separate the file path from
     * the class name in the ground-truth file.
     * @param directory The directory to write the files into.
     * @param file_encoding_constraint The file metadata constraints to use to
     * select audio files corresponding to each track.
     *
     * @return An array indexed {gt_file,fl_file}
     * @throws SQLException
     */
    public static File[] writeOutGroundTruthAndExtractionListFile(int dataset_id, String delimiter, File directory, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        System.out.println("Writing out Groundtruth and Extraction list files for dataset_id=" + dataset_id);
        System.out.println("File encoding constraints: ");
        for (Iterator<NEMAMetadataEntry> it = file_encoding_constraint.iterator(); it.hasNext();){
            NEMAMetadataEntry meta = it.next();
            System.out.println("\t" + meta.getType() + ":\t" + meta.getValue());
        }
        RepositoryClientInterface client = new RepositoryClientImpl();
        NEMADataset dataset = client.getDataset(dataset_id);
        int subset = dataset.getSubsetSetId();
        int subjectMetadata = dataset.getSubjectTrackMetadataId();
        return writeOutGTAndExtractListFiles(client, delimiter, subset, subjectMetadata, directory, file_encoding_constraint);
    }

    //==========================================================================
    //Private methods
    //==========================================================================

    private static List<String> getTestData(RepositoryClientInterface client, int set_id, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        System.out.println("Retrieving test file paths for set: " + set_id);
        List<String> tracks = client.getTrackIDs(set_id);
        System.out.println("Got " + tracks.size() + " tracks, resolving to files") ;
        List<NEMAFile> files = client.getFilesByID(tracks, file_encoding_constraint);
        System.out.println("Got file list length: " + files.size() + ", preparing output");
        List<String> out = new ArrayList<String>();
        int idx = 0;
        Object file;
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            file = it.next();
            if (file == null){
                //System.out.println("WARNING: a file could not be found for track: " + tracks.get(idx));
                out.add(null);
            }else{
                out.add(((NEMAFile)file).getPath());
            }

            idx++;
        }
        return out;
    }

    private static List<String[]> getGroundtruthData(RepositoryClientInterface client, int set_id, int metadata_id, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        System.out.println("Retrieving ground-truth data and file paths for set: " + set_id);
        List<String> tracks = client.getTrackIDs(set_id);
        System.out.println("Got " + tracks.size() + " tracks, resolving to files") ;
        List<NEMAFile> files = client.getFilesByID(tracks, file_encoding_constraint);
        System.out.println("Got file list length: " + files.size() + ", retrieving metadata");
        List<String[]> out = new ArrayList<String[]>();
        Object obj;
        NEMAFile file;
        List<NEMAMetadataEntry> meta_list;
        Iterator<NEMAMetadataEntry> meta_it;
        String path;
        int idx = 0;

        Map<String,List<NEMAMetadataEntry>> trackToMeta = client.getTrackMetadataByID(tracks, metadata_id);
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            obj = it.next();
            if (obj == null){
                //System.out.println("WARNING: a file could not be found for track: " + tracks.get(idx));
                out.add(null);
            }else{
                file = (NEMAFile)obj;
                path = file.getPath();
                meta_list = trackToMeta.get(file.getTrackId());
                for(meta_it = meta_list.iterator();meta_it.hasNext();){
                    out.add(new String[]{path,meta_it.next().getValue()});
                }
            }

            idx++;
        }
        return out;

    }

    private static File writeExtractOrTestListFile(File directory, String set_name, int set_id,
                                    List<String[]> data){
    	//use a set to filter out duplicated paths as tag sets will have more than one entry per path
    	Set<String> pathsDone = new HashSet<String>();
    	
        File out = new File(directory.getAbsolutePath() + File.separator + set_name + "-" + set_id + ".txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(out));
            for (Iterator<String[]> it = data.iterator(); it.hasNext();){
                String string = it.next()[0];
                if (!pathsDone.contains(string)){
	                writer.write(string);
	                writer.newLine();
	                pathsDone.add(string);
                }
            }
        }catch (IOException ex){
            Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (writer != null){
                try{
                    writer.flush();
                    writer.close();
                }catch (IOException ex){
                    Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return out;
    }

    private static File writeGTFile(File directory, String set_name, int set_id,
                                    List<String[]> data, String delimiter){
        File out = new File(directory.getAbsolutePath() + File.separator + set_name + "-" + set_id + ".txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(out));
            for (Iterator<String[]> it = data.iterator(); it.hasNext();){
                String[] strings = it.next();
                writer.write(strings[0] + delimiter + strings[1]);
                writer.newLine();
            }
        }catch (IOException ex){
            Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (writer != null){
                try{
                    writer.flush();
                    writer.close();
                }catch (IOException ex){
                    Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return out;
    }

    private static File[] writeOutGTAndExtractListFiles(RepositoryClientInterface client, String delimiter, int set_id, int metadata_id, File directory, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        List<String[]> data = getGroundtruthData(client, set_id, metadata_id, file_encoding_constraint);
        File gt = writeGTFile(directory, "gt", set_id, data, delimiter);
        File el = writeExtractOrTestListFile(directory, "extractlist", set_id, data);
        return new File[]{gt,el};
    }

    private static File writeOutGTFile(RepositoryClientInterface client, String set_name, String delimiter, int set_id, int metadata_id, File directory, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        List<String[]> data = getGroundtruthData(client, set_id, metadata_id, file_encoding_constraint);
        File out = writeGTFile(directory, set_name, set_id, data, delimiter);
        return out;
    }

    private static File writeOutSingleTestFile(RepositoryClientInterface client, String set_name, int set_id, File directory, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        List<String> data = getTestData(client, set_id, file_encoding_constraint);
        File out = new File(directory.getAbsolutePath() + File.separator + set_name + "-" + set_id + ".txt");
        
        //use a set to filter out duplicated paths as tag sets will have more than one entry per path
    	Set<String> pathsDone = new HashSet<String>();
    	
    	BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(out));
            for (Iterator<String> it = data.iterator(); it.hasNext();){
                String string = it.next();
                if (!pathsDone.contains(string)){
	                writer.write(string);
	                writer.newLine();
	                pathsDone.add(string);
                }
            }
        }catch (IOException ex){
            Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (writer != null){
                try{
                    writer.flush();
                    writer.close();
                }catch (IOException ex){
                    Logger.getLogger(DatasetListFileGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return out;
    }

}
