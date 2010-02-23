/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.tagsClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.imirsel.m2k.evaluation.DataObj;
import org.imirsel.m2k.evaluation.EvalFileType;
import org.imirsel.m2k.io.file.DeliminatedTextFileUtilities;
import org.imirsel.m2k.io.musicDB.RemapMusicDBFilenamesClass;

/**
 *
 * @author kris
 */
public class TagClassificationGroundTruthFileReader /*implements EvalFileType */{
    private boolean verbose = false;
    private boolean mirexMode = false;
    public boolean getMirexMode() {
        return mirexMode;
    }

    public void setMirexMode(boolean mirexMode) {
        this.mirexMode = mirexMode;
    }

    public static String cleanTag(String tag){
        return tag.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-z0-9]", "");
    }

    private static final int FILE_FORMAT_ERROR_TOLERANCE = 2;
    
    private static HashSet<String> known_headers;
    static{
        known_headers = new HashSet<String>();
        known_headers.add("id");
        known_headers.add("original_filename");
        known_headers.add("renamed_filename");
        known_headers.add("human_assessor");
        known_headers.add("artist");
        known_headers.add("album");
        known_headers.add("track");
        known_headers.add("path");
    }
            
    public boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose_) {
        verbose = verbose_;
    }

    private void addToHash(HashMap<String,HashSet<String>> pathsToRelevantTags, String path, String tag){
        if(verbose){
            System.out.println("   appending tag: " + tag + " to path: " + path);
        }
        //not needed as this is done in readFile()
//        String key;
//        if (mirexMode){
//            key = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(new File(path));
//        }else{
//            key = path;
//        }
        if (pathsToRelevantTags.containsKey(path)){
            HashSet<String> tagSet = pathsToRelevantTags.get(path);
            tagSet.add(tag);
        }else{
            HashSet<String> tagSet = new HashSet<String>();
            tagSet.add(tag);
            pathsToRelevantTags.put(path, tagSet);
        }
    }
    
    public DataObj readFile(File theFile) {
        if(verbose){
            System.out.println("Reading binary relevance file: " + theFile.getAbsolutePath());
        }
        //init file path as evaluation file
        DataObj output = new DataObj(theFile.getAbsolutePath());
        BufferedReader textBuffer = null;
        try {
            
            String[][] data = DeliminatedTextFileUtilities.getDelimTextDataBlock(theFile, "\t", 0);
            String[] headers = DeliminatedTextFileUtilities.loadDelimTextHeaders(theFile, "\t", 0);
            
            //remove single quotes where necessary
            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].replaceAll("'", "").replaceAll("&amp;", "&").trim();
                for (int j = 0; j < data[0].length; j++) {
                    data[j][i] = data[j][i].replaceAll("'", "").trim();
                }
            }
            
            
            HashMap<String,HashSet<String>> pathsToRelevantTags = new HashMap<String,HashSet<String>>();
            HashMap<String,String> pathsToArtist = new HashMap<String,String>();
            ArrayList<String> allTags = new ArrayList<String>();
            
            int id_col = -1, artist_col = -1, first_tag_col = -1;
            
            if (getMirexMode()){
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equalsIgnoreCase("id")){
                        id_col = i;
                    }else if(headers[i].equalsIgnoreCase("artist")){
                        artist_col = i;
                    }else if(!known_headers.contains(headers[i])){
                        first_tag_col = i;
                        break;
                    }
                }
            }else{
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equalsIgnoreCase("path")){
                        id_col = i;
                    }else if(headers[i].equalsIgnoreCase("artist")){
                        artist_col = i;
                    }else if(!known_headers.contains(headers[i])){
                        first_tag_col = i;
                        break;
                    }
                }
            }
            for (int i = first_tag_col; i < headers.length; i++) {
                allTags.add(cleanTag(headers[i]));
            }
            
            for (int i = 0; i < data.length; i++) {
                String artist = data[i][artist_col];
                String id = data[i][id_col];
                if (mirexMode){
                    id = RemapMusicDBFilenamesClass.convertFileToMIREX_ID(new File(id));
                }
                pathsToArtist.put(id, artist);
                for (int j = first_tag_col; j < headers.length; j++) {
                    if (data[i][j].equalsIgnoreCase("1")||data[i][j].equalsIgnoreCase("y")){
                        addToHash(pathsToRelevantTags, id, allTags.get(j-first_tag_col));
                    }
                }
                
            }
            
            //append data to the DataObj Object
            output.setMetadata(DataObj.TAG_BINARY_RELEVANCE_MAP, pathsToRelevantTags);
            output.setMetadata(DataObj.PATH_TO_ARTIST_MAP, pathsToArtist);
            output.setMetadata(DataObj.TAG_LIST, allTags);
            
        } catch (IOException ex) {
            throw new RuntimeException("IOExcecption occured while reading file: " + theFile.getAbsolutePath(), ex);
        }
        
        if(verbose){
            System.out.println("Done reading ground truth tag file file: " + theFile.getAbsolutePath());
        }
        return output;
    }

    

}
