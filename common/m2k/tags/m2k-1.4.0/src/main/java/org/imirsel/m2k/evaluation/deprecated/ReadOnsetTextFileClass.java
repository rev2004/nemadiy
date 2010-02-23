/*
 * ReadOnsetTextFileClass.java
 *
 * Created on 09 February 2006, 18:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation.deprecated;

import java.io.*;
import java.util.ArrayList;
import org.imirsel.m2k.util.Signal;

/**
 *
 * @author kris
 */
public class ReadOnsetTextFileClass {
    boolean verbose = false;
    boolean readingGT = false;
    
    /** Creates a new instance of ReadOnsetTextFileClass */
    public ReadOnsetTextFileClass(boolean readingGT_, boolean verbose_) {
        readingGT = readingGT_;
        verbose = verbose_;
    }
    
    public Signal readTextFile(File textFile, ArrayList wavFiles) throws RuntimeException {
        
        /* check for the existance of a corresponding .wav file */
        String textFileName = textFile.getName();
        String textFileParent = textFile.getParentFile().getName();
        String[] textFileTokens = {"init"};
        

        String wavFileFullPath = "init";
        String wavFileElementName = "init";
        String[] wavFileElementTokens = {"init"};
        
        boolean errorFlag = true;
        for (int i = 0; i < wavFiles.size(); i++)
        {
            File wavFileElement = (File)wavFiles.get(i);
            wavFileElementName = wavFileElement.getName();
            wavFileFullPath = wavFileElement.getAbsolutePath();
            wavFileElementTokens = wavFileElementName.split("[.]");
            textFileTokens = textFileName.split("[.]");
            if (wavFileElementTokens[0].compareTo(textFileTokens[0]) == 0)
            {
                errorFlag = false;
                break;
            }
        }
        if (verbose)
        {    
            System.out.println("ReadOnsetTextFile: \nText File Name: " + textFileName);
            System.out.println("Text File Token: " + textFileTokens[0]);
            System.out.println("Text File Parent: " + textFileParent);
        }
        
        if (errorFlag)
        {
            throw new RuntimeException("No matching .wav file found for text " +
                    "file: " + textFileName);
        }
        
        if (verbose)
        {
            System.out.println("ReadOnsetTextFile: \nWav File Name: " + wavFileElementName);
            System.out.println("Wav File Token: " + wavFileElementTokens[0]);
            System.out.println("Wav File Full Path : " + wavFileFullPath);
            System.out.println("Number of .wav's present: " + wavFiles.size());
        }
        
        /* read in the data file and construct a data Vector */
        ArrayList onsetData = null;
        BufferedReader textBuffer = null;
        double init = 1.0;
        //Double dataValue = new Double(init);
        try
        {
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(textFile) );
            String line = null; //not declared within while loop
            Double lastTime = new Double(-1.0);
            while (( line = textBuffer.readLine()) != null)
            {
                if (onsetData == null)
                {
                    onsetData = new ArrayList();
                }
                if (line.compareTo("") != 0){
                    try{
                        Double time = Double.valueOf(line);
                        if (time.doubleValue() < lastTime.doubleValue())
                        {
                            break;
                        }
                        lastTime = time;
                        onsetData.add(time);
                        
                    }catch(NumberFormatException nfe){
                        throw new RuntimeException("NumberFormatException for " + textFile, nfe);
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (textBuffer!= null)
                {
                    //flush and close both "input" and its underlying FileReader
                    textBuffer.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        
        /* construct a Signal object from the data Double Vector */
        Signal outputSignal = new Signal(wavFileFullPath);
        
        double[] onsetDataArray = new double[onsetData.size()];
        for (int j = 0; j < onsetData.size(); j++ )
        {
            onsetDataArray[j] = ((Double)onsetData.get(j)).doubleValue();
        }
        if (this.readingGT)
        {
            if (onsetData.size() <= 0)
            {
                throw new RuntimeException("No GT onsets found!");
            }
            outputSignal.setMetadata(Signal.PROP_ONSET_TIMES_GT, onsetDataArray);
        }
        else
        {
            outputSignal.setMetadata(Signal.PROP_ONSET_TIMES, onsetDataArray);
        }
        if (textFileTokens.length >= 4) {
            outputSignal.setMetadata("fileNameToken", textFileTokens[0]);
            outputSignal.setMetadata(Signal.PROP_CLASS, textFileTokens[2]);
            outputSignal.setMetadata("annotator", textFileTokens[1]);
            
        }
        else if(textFileTokens.length == 3){
            outputSignal.setMetadata("fileNameToken", textFileTokens[0]);
            outputSignal.setMetadata("annotator", textFileTokens[1]);
            outputSignal.setMetadata(Signal.PROP_CLASS,textFileParent);
        }
        else {
            outputSignal.setMetadata("fileNameToken", textFileTokens[0]);
            
        }
        return outputSignal;
    }
    
}
