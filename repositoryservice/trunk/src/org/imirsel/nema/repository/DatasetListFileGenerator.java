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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kriswest
 */
public class DatasetListFileGenerator {

    
    /**
     * Utility method to help build file type constraints.
     * 
     * @param bitrate     valid values are: 128k, 96k
     * @param channels    valid values are: 1, 2
     * @param clip_type   valid values are: 30, full
     * @param encoding    valid values are: mp3, wav
     * @param sample_rate valid values are: 22050, 44100
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
     *
     * @param dataset_id
     * @param directory
     * @return a list of File object arrays where each entry in the list is
     * an array containing {/path/to/Train/File, /path/to/Test/File} for a
     * particular iteration
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
     *
     * @param dataset_id
     * @param delimiter
     * @param directory
     * @param file_encoding_constraint
     * @return {gt_file,fl_file}
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
        List<NEMATrack> tracks = client.getTracks(set_id);
        List<NEMAFile> files = client.getFiles(tracks, file_encoding_constraint);
        List<String> out = new ArrayList<String>();
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            out.add(it.next().getPath());
        }
        return out;
    }

    private static List<String[]> getGroundtruthData(RepositoryClientInterface client, int set_id, int metadata_id, Set<NEMAMetadataEntry> file_encoding_constraint) throws SQLException{
        List<NEMATrack> tracks = client.getTracks(set_id);
        List<NEMAFile> files = client.getFiles(tracks, file_encoding_constraint);
        List<String[]> out = new ArrayList<String[]>();
        NEMAFile file;
        NEMAMetadataEntry meta;
        for (Iterator<NEMAFile> it = files.iterator(); it.hasNext();){
            file = it.next();
            meta = client.getTrackMetadataByID(file.getTrackId(), metadata_id);
            out.add(new String[]{file.getPath(),meta.getValue()});
        }
        return out;

    }

    private static File writeExtractOrTestListFile(File directory, String set_name, int set_id,
                                    List<String[]> data){
        File out = new File(directory.getAbsolutePath() + File.separator + set_name + "-" + set_id + ".txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(out));
            for (Iterator<String[]> it = data.iterator(); it.hasNext();){
                String string = it.next()[0];
                writer.write(string);
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
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(out));
            for (Iterator<String> it = data.iterator(); it.hasNext();){
                String string = it.next();
                writer.write(string);
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

}
