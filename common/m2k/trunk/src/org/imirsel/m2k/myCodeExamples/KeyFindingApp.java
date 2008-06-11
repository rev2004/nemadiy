/*
 * KeyFindingApp.java
 *
 * Created on 20 April 2007, 11:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.myCodeExamples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kw
 */
public class KeyFindingApp {
    
    /** Creates a new instance of KeyFindingApp */
    public KeyFindingApp() {
    }
    
    public static final String USAGE = "usage:\n inputFilePath outputFilePath";
    
    public static void main(String[] args){
        if (args.length != 2)
        {
            throw new RuntimeException("Wrong number of args (" + args.length + ")\n" + USAGE);
        }
        
        File inFile = new File(args[0]);
        if (!inFile.exists()){
            throw new RuntimeException("The input file (" + inFile.getAbsolutePath() + ") did not exist!");
        }
        File outFile = new File(args[1]);
        
        Signal keySignal = KeyFinder.findKey(inFile);
        String[] key;
        try {
            key = keySignal.getStringArrayMetadata(Signal.PROP_KEY);
        } catch (noMetadataException ex) {
            throw new RuntimeException("Key metadata not found!",ex);
        }
        
        BufferedWriter textBuffer;
        try {
            textBuffer = new BufferedWriter( new FileWriter(outFile, false) );
        
            textBuffer.write(key[0] + "\t" + key[1] + "\n");
            textBuffer.flush();
            textBuffer.close();       
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("An IOException occured while opening file: " + outFile.getPath() + " for writing\n" + ioe);
        }
    }
    
}
