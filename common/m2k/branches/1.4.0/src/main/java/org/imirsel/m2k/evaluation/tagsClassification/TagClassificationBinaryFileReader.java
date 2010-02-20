/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.tagsClassification;

import org.imirsel.m2k.evaluation.*;

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
import org.imirsel.m2k.util.retrieval.MIREXMatrixQueryUtil;

/**
 *
 * @author kris
 */
public class TagClassificationBinaryFileReader /*implements EvalFileType*/ {
    private boolean verbose = false;
    private boolean MIREX_submissionMode = false;
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

    private void addToHash(HashMap<String,HashSet<String>> pathsToRelevantTags, HashSet<String> allTags, String path, String tag){
        if(verbose){
            System.out.println("   appending tag: " + tag + " to path: " + path);
        }
        String key,cleanTag;
        if (MIREX_submissionMode){
            key = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(new File(path));
        }else{
            key = path;
        }
        cleanTag = TagClassificationGroundTruthFileReader.cleanTag(tag);
        allTags.add(cleanTag);

        HashSet<String> tagSet = pathsToRelevantTags.get(key);
        if (tagSet == null){
            tagSet = new HashSet<String>();
            pathsToRelevantTags.put(key, tagSet);
        }
        tagSet.add(cleanTag);
    }
    
    public DataObj readFile(File theFile) {
        if(verbose){
            System.out.println("Reading binary relevance file: " + theFile.getAbsolutePath());
        }
        //init file path as evaluation file
        DataObj output = new DataObj(theFile.getAbsolutePath());
        BufferedReader textBuffer = null;
        try {
            textBuffer = new BufferedReader(new FileReader(theFile));

            int errorCount = 0;
            
            HashMap<String,HashSet<String>> pathsToRelevantTags = new HashMap<String,HashSet<String>>();
            HashSet<String> allTags = new HashSet<String>();
            String line = textBuffer.readLine();
            String[] lineComps;
            int lineNum = 0;
            while (line != null){
                lineNum++;
                line = line.trim();
                if (!line.equals("")){
                    lineComps = line.split("\t");
                    if (lineComps.length < 2){
                        System.out.println("short line found, line: " + lineNum + ", file: " + theFile.getAbsolutePath());
                        errorCount++;
                        if (errorCount <= FILE_FORMAT_ERROR_TOLERANCE){
                            System.out.println("ignoring line " + lineNum);
                        }else{
                            throw new RuntimeException("Too many file format errors!\n" +
                                    "check if file is correctly deliminated with tabs (rather than other whitespace characters or commas) and has no preamble");
                        }
                    }else if(lineComps.length > 2){
                        //3 component format
                        if(lineComps[2].trim().equals("1")){
                            addToHash(pathsToRelevantTags,allTags,lineComps[0].trim(),lineComps[1].trim());
                        }//else assume a 0 (non-relevant) so ignore the line
                    }else{
                        //2 component format
                        addToHash(pathsToRelevantTags,allTags,lineComps[0].trim(),lineComps[1].trim());
                    }
                }else{
                    System.out.println("empty line ignored, line: " + lineNum + ", file: " + theFile.getAbsolutePath());
                }
                line = textBuffer.readLine();
            }
            
            
            //append data to the DataObj Object
            output.setMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP, pathsToRelevantTags);
            output.setMetadata(DataObj.TAG_NAME_SET, allTags);
        } catch (IOException ex) {
            throw new RuntimeException("IOExcecption occured while reading file: " + theFile.getAbsolutePath(), ex);
        } finally {
            try {
                textBuffer.close();
            } catch (Exception ex) {
                Logger.getLogger(TagClassificationBinaryFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(verbose){
            System.out.println("Done reading binary relevance file: " + theFile.getAbsolutePath());
        }
        return output;
    }

}
