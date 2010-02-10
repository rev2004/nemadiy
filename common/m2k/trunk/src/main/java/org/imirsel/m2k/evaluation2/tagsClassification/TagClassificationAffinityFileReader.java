/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.tagsClassification;

import org.imirsel.m2k.evaluation2.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;

/**
 *
 * @author kris
 */
public class TagClassificationAffinityFileReader /*implements EvalFileType*/ {
    private boolean verbose = true;
    private boolean MIREX_submissionMode = false;

    private double minAffinity = Double.POSITIVE_INFINITY;
    private double maxAffinity = Double.NEGATIVE_INFINITY;


    public boolean getMIREX_submissionMode() {
        return MIREX_submissionMode;
    }

    public void setMIREX_submissionMode(boolean mirexMode) {
        this.MIREX_submissionMode = mirexMode;
    }
    
    private static final HashSet<String> ACCEPTED_FILE_EXTENSIONS = new HashSet<String>();
    static{
        ACCEPTED_FILE_EXTENSIONS.add(".mp3");
        ACCEPTED_FILE_EXTENSIONS.add(".wav");
        ACCEPTED_FILE_EXTENSIONS.add(".mid");
    }

    private static final int FILE_FORMAT_ERROR_TOLERANCE = 2;
    
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }

    private void addToHash(HashMap<String,HashMap<String,Double>> pathsToRelevantTags, String path, String tag, double value){
        if(verbose){
            System.out.println("   appending tag: " + tag + ", affinity: " + value + " to path: " + path);
        }

        if((value != Double.NaN)&&(value != Double.NEGATIVE_INFINITY)&&(value != Double.POSITIVE_INFINITY)){
            if (value > maxAffinity){
                maxAffinity = value;
            }else if(value < minAffinity){
                minAffinity = value;
            }
        }

        String key,cleanTag;
        if (MIREX_submissionMode){
            key = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(new File(path));
        }else{
            key = path;
        }
        cleanTag = TagClassificationGroundTruthFileReader.cleanTag(tag);

        HashMap<String,Double> tagMap = pathsToRelevantTags.get(key);
        if (tagMap == null){
            tagMap = new HashMap<String,Double>();
            pathsToRelevantTags.put(key, tagMap);
        }
        tagMap.put(cleanTag,value);
    }
    
    public DataObj readFile(File theFile) {
        if(verbose){
            System.out.println("Reading affinity file: " + theFile.getAbsolutePath());
        }

        minAffinity = Double.POSITIVE_INFINITY;
        maxAffinity = Double.NEGATIVE_INFINITY;

        //init file path as evaluation file
        DataObj output = new DataObj(theFile.getAbsolutePath());
        BufferedReader textBuffer = null;
        try {
            textBuffer = new BufferedReader(new FileReader(theFile));

            int errorCount = 0;
            
            HashMap<String,HashMap<String,Double>> pathsToRelevantTags = new HashMap<String,HashMap<String,Double>>();
            String line = textBuffer.readLine();
            String[] lineComps;
            int lineNum = 0;
            while (line != null){
                lineNum++;
                line = line.trim();
                if (!line.equals("")){
                    lineComps = line.split("\t");
                    if (lineComps.length < 3){
                        System.out.println("short line found, line: " + lineNum + ", file: " + theFile.getAbsolutePath());
                        errorCount++;
                        if (errorCount <= FILE_FORMAT_ERROR_TOLERANCE){
                            System.out.println("ignoring line " + lineNum);
                        }else{
                            throw new RuntimeException("Too many file format errors!\n" +
                                    "check if file is correctly deliminated with tabs (rather than other whitespace characters or commas) and has no preamble");
                        }
                    }else{
                        //assume there are 3 components
                        addToHash(pathsToRelevantTags,lineComps[0].trim(),lineComps[1].trim(),Double.parseDouble(lineComps[2]));
                    }
                }else{
                    System.out.println("empty line ignored, line: " + lineNum + ", file: " + theFile.getAbsolutePath());
                }
                line = textBuffer.readLine();
            }

            //normalise affinity values
            double affRange = maxAffinity - minAffinity;
            System.out.println("Normalising affinity scores:");
            System.out.println("\tMin affinity: " + minAffinity);
            System.out.println("\tMax affinity: " + maxAffinity);
            System.out.println("\tRange:        " + affRange);
            String key;
            double normval;

            for (Iterator<HashMap<String,Double>> it = pathsToRelevantTags.values().iterator(); it.hasNext();){
                HashMap<String,Double> aMap = it.next();
                for (Iterator<String> it1 = aMap.keySet().iterator(); it1.hasNext();){
                    key = it1.next();
                    normval = (aMap.get(key) - minAffinity) / affRange;
                    aMap.put(key, normval);
                }
            }
            
            //append data to the DataObj Object
            if(verbose){
                System.out.println("   appending relevant tags for " + pathsToRelevantTags.size() + " paths");
            }
            output.setMetadata(DataObj.TAG_AFFINITY_MAP, pathsToRelevantTags);
        } catch (IOException ex) {
            throw new RuntimeException("IOExcecption occured while reading file: " + theFile.getAbsolutePath(), ex);
        } finally {
            try {
                textBuffer.close();
            } catch (IOException ex) {
                Logger.getLogger(TagClassificationBinaryFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(verbose){
            System.out.println("Done reading affinity file: " + theFile.getAbsolutePath());
        }
        return output;
    }

}
