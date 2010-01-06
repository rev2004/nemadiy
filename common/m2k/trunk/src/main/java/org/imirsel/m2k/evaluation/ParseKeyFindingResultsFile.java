/*
 * ParseKeyFindingResultsFile.java
 *
 * Created on 20 April 2007, 14:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author kw
 */
public class ParseKeyFindingResultsFile {
    
    /** Creates a new instance of ParseKeyFindingResultsFile */
    public ParseKeyFindingResultsFile() {
    }
    
    public static final String[] columnNames = new String[]{"Correct keys",
                                                            "Perfect fifth errors",
                                                            "Relative major/minor errors",
                                                            "Parallel major/minor errors",
                                                            "Errors",
                                                            "Final Score",
                                                            "Normalized Score"};
    
    public static String [] getColumnNames(){
        return columnNames;
    }
    
    public static double[] parse(File theFile){
        BufferedReader textBuffer = null;
        double[] output = new double[7];
        try {
        
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(theFile) );
            String line = ""; //not declared within while loop
            
            try{
                //skip to result summary
                while (!line.trim().equals("Final Results:"))
                {
                    line = textBuffer.readLine();
                }
                //skip the empty line
                line = textBuffer.readLine();

                String[] comps;
                //get basic integer counts
                for (int i = 0; i < 5; i++) {
                    line = textBuffer.readLine();
                    comps = line.split(":");
                    output[i] = Double.parseDouble(comps[1].trim());
                }

                //skip the empty line
                line = textBuffer.readLine();

                //get final and normalised scores
                line = textBuffer.readLine();
                comps = line.split(":");
                comps = comps[1].trim().split("\\s");
                output[5] = Double.parseDouble(comps[0].trim());

                line = textBuffer.readLine();
                comps = line.split(":");
                output[6] = Double.parseDouble(comps[1].trim());
                return output;
            }catch(ArrayIndexOutOfBoundsException ex){
                throw new RuntimeException("The file does not correspond to the expected format (" + theFile.getAbsolutePath() + ")!",ex);
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File not found (" + theFile.getAbsolutePath() + ")!",ex);
        } catch (IOException ex) {
            throw new RuntimeException("IOException occured while reading (" + theFile.getAbsolutePath() + ")!",ex);
        }
    }
    
    //test harness
    public static void main(String[] args){
        File testFile = new File("C:\\ak_Ehmann.eval");
        
        double[] out = parse(testFile);
        String[] colLabels = getColumnNames();
        System.out.println("Imported data:");
        for (int i = 0; i < colLabels.length; i++) {
            System.out.println(colLabels[i] + ": " + out[i]);
        }
    }
    
}
