/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.imirsel.m2k.evaluation2.tagsClassification.TagClassificationGroundTruthFileReader;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kriswest
 */
public class ClassificationResultReadClass {

    public static Signal[] readClassificationFileAsSignals(File toRead, File gtFile, boolean MIREXMode){
        HashMap<String,String> data = readClassificationFile(toRead, MIREXMode);
        HashMap<String,String> groundTruth = readClassificationFile(gtFile, MIREXMode);
        
        if (!groundTruth.keySet().containsAll(data.keySet())) {
            String msg = "ERROR: The groundtruth file " + gtFile.getAbsolutePath() + " does not contain all the paths specified in the classification result file " + toRead.getAbsolutePath() + "!";
            HashSet<String> missing = new HashSet<String>();
            missing.addAll(data.keySet());
            missing.removeAll(groundTruth.keySet());
            msg += "\nPaths missing:";
            for (Iterator<String> it = missing.iterator(); it.hasNext();) {
                msg += "\n\t" + it.next();
            }
            throw new noMetadataException(msg);
        }
        
        Signal[] examples = new Signal[data.size()];
        int i = 0;
        String[] paths = data.keySet().toArray(new String[data.size()]);
        for (int j = 0; j < paths.length; j++) {
            String filepath = paths[j];
            examples[i] = new Signal(filepath, groundTruth.get(filepath));
            examples[i].setMetadata(Signal.PROP_CLASSIFICATION, data.get(filepath));
            i++;
        }
        
        return examples;
    }
    
    public static HashMap<String,String> readClassificationFile(File toRead, boolean MIREXMode){
        HashMap<String,String> dataRead = new HashMap<String,String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(toRead));
            try {
                String str = br.readLine(); // one line one sample
                if(MIREXMode){
                    while (str != null){
                        str = str.trim();
                        if (!str.equals("")) {
                            String[] splitted = str.split("\\s+");
                            File aPath = new File(splitted[0]);
                            String key = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(aPath);
                            if(key.equals("")){
                                throw new RuntimeException("Error: an empty track name was read from file: " + toRead.getAbsolutePath());
                            }
                            String className = TagClassificationGroundTruthFileReader.cleanTag(splitted[1]);
                            if(className.equals("")){
                                throw new RuntimeException("Error: an empty class name was read from file: " + toRead.getAbsolutePath());
                            }
                            dataRead.put(key, className);
                        }
                        str = br.readLine();
                    }
                }else{
                    while (str != null){
                        str = str.trim();
                        if (!str.equals("")) {
                            String[] splitted = new String[2];
                            splitted = str.split("\t");
                            dataRead.put(splitted[0].trim(), splitted[1].trim());
                        }
                        str = br.readLine();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception in read in ground truth file.");
            }
            br.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataRead;
    }

    public static List<String> readClassificationFileAsList(File toRead, boolean MIREXMode){
        List<String> dataRead = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(toRead));
            try {
                String str = br.readLine(); // one line one sample
                if(MIREXMode){
                    while (str != null){
                        str = str.trim();
                        if (!str.equals("")) {
                            String[] splitted = str.split("\\s+");
                            File aPath = new File(splitted[0]);
                            String key = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(aPath);
                            if(key.equals("")){
                                throw new RuntimeException("Error: an empty track name was read from file: " + toRead.getAbsolutePath());
                            }

                            dataRead.add(key);
                        }
                        str = br.readLine();
                    }
                }else{
                    while (str != null){
                        str = str.trim();
                        if (!str.equals("")) {
                            String[] splitted = new String[2];
                            splitted = str.split("\t");
                            dataRead.add(splitted[0].trim());
                        }
                        str = br.readLine();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception in read in ground truth file.");
            }
            br.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataRead;
    }
    
    
}
