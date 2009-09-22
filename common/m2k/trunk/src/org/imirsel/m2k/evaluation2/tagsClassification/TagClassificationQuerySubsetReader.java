/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.tagsClassification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kris
 */
public class TagClassificationQuerySubsetReader {
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

    private void addToHash(HashSet<String> paths, String path){
        if(verbose){
            System.out.println("   appending path: " + path);
        }
        String key,ext;
        if (MIREX_submissionMode){
            key = new File(path).getName();
            for (Iterator<String> it = ACCEPTED_FILE_EXTENSIONS.iterator(); it.hasNext();) {
                ext = it.next();
                if(key.endsWith(ext)){
                    key = key.replaceAll(ext, "");
                    break;
                }
            }
        }else{
            key = path;
        }
        if (!paths.contains(key)){
            paths.add(key);
        }
    }
    
    public HashSet<String> readFile(File theFile) {
        if(verbose){
            System.out.println("Reading query subset file: " + theFile.getAbsolutePath());
        }
        //init file path as evaluation file
        HashSet<String> paths = new HashSet<String>();
        BufferedReader textBuffer = null;
        try {
            textBuffer = new BufferedReader(new FileReader(theFile));

            int errorCount = 0;
            
            
            String line = textBuffer.readLine();
            String[] lineComps;
            int lineNum = 0;
            while (line != null){
                lineNum++;
                line = line.trim();
                if (!line.equals("")){
                   addToHash(paths,line);
                }else{
                    System.out.println("empty line ignored, line: " + lineNum + ", file: " + theFile.getAbsolutePath());
                }
                line = textBuffer.readLine();
            }
            
            
        } catch (IOException ex) {
            throw new RuntimeException("IOExcecption occured while reading file: " + theFile.getAbsolutePath(), ex);
        } finally {
            try {
                textBuffer.close();
            } catch (Exception ex) {
                Logger.getLogger(TagClassificationQuerySubsetReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(verbose){
            System.out.println("Done reading query subset file: " + theFile.getAbsolutePath());
            System.out.println("\tsubset size: " + paths.size());
        }
        return paths;
    }

}
