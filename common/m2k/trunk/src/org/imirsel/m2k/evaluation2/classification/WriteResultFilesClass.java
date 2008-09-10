/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kriswest
 */
public class WriteResultFilesClass {

    /**
     * A utility function that takes an ArrayList of Signal arrays containing algorithm 
     * name and performance (per class) metadata and outputs the data into a CSV 
     * file to be used to perform significance tests in Matlab or another 
     * suitable environment.
     * 
     * @param sigStore An ArrayList of Signal arrays containing algorithm name and
     * performance metadata. Each Signal Object represents a single fold of the
     * experiment (thus each array should be ordered in the same way).
     * @param outputDirectory The directory to output the CSV file into.
     * @param evaluationName The name of the evaluation (used to name output file.
     * @param outputFileExt The extension to put on the output file.
     * @param verbose Determines wheter the data should be dumped to the console
     * as well.
     * @return A File Object indicating where the output CSV file was written to.
     */
    public static File prepFriedmanTestDataOverClasses(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put(((Signal[]) sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), (Signal[]) sigStore.get(i));
            } catch (noMetadataException ex) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + ex);
            }
        }
        String[] keys = sigArrMap.keySet().toArray(new String[sigStore.size()]);
        Arrays.sort(keys);
        
        sigStore.clear();
        for (int i = 0; i < keys.length; i++) {
            sigStore.add(sigArrMap.get(keys[i]));
        }
        
        int numAlgos = sigStore.size();
        int numRuns = sigStore.get(0).length;
        String EvaluationOutput = "";

        String[] runNames = new String[numAlgos];
        EvaluationOutput += "Class,";
        for (int i = 0; i < numAlgos; i++) {
            try {
                runNames[i] = sigStore.get(i)[0].getStringMetadata(Signal.PROP_ALG_NAME).replaceAll(",", " ");
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
            if (sigStore.get(i).length != numRuns){
                throw new RuntimeException(runNames[i] + " had a different number of results (" + sigStore.get(i).length + ") to " + runNames[0] + " (" + numRuns + ")");
            }
            EvaluationOutput += runNames[i];
            if (i< sigStore.size()-1){
                EvaluationOutput += ",";
            }
        }
        EvaluationOutput += "\n";
        
        
        String[] classNames = sigStore.get(0)[0].getStringArrayMetadata(Signal.PROP_CLASSES);
        for (int c = 0; c < classNames.length; c++) {
            EvaluationOutput += classNames[c].replaceAll(",", " ") + ",";

            for (int j = 0 ; j < numAlgos; j++){
                double avg = 0.0;
                for (int i = 0; i < numRuns; i++) {
                    avg += sigStore.get(j)[i].getDoubleArrayMetadata(Signal.PROP_PERF_PER_CLASS)[c];
                }
                avg /= numRuns;

                try{
                    EvaluationOutput += "" + avg;
                } catch (noMetadataException e) {
                    throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
                }
                if (j< sigStore.size()-1){
                    EvaluationOutput += ",";
                }

            }
            EvaluationOutput += "\n";
        }
        

        
        
        File copyToDirFile = new File(outputDirectory);
        if (!copyToDirFile.isDirectory()) {
            if (!copyToDirFile.mkdirs()) {
                throw new RuntimeException("Could not create the output folder.");
            }
        }
        File testFile = null;
        try {
            testFile = new File(outputDirectory + File.separator + evaluationName + "_results_class" + outputFileExt);
            BufferedWriter output = new BufferedWriter(new FileWriter(testFile));
            output.write(EvaluationOutput);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (verbose) {
            System.out.println(EvaluationOutput);
        }
        return testFile;
    }
    
    /**
     * A utility function that takes an ArrayList of Signal arrays containing 
     * algorithm name and performance metadata and outputs the data into a CSV 
     * file to be used to perform significance tests in Matlab or another 
     * suitable environment.
     * 
     * @param sigStore An ArrayList of Signal arrays containing algorithm name 
     * and performance metadata. Each Signal Object represents a single fold of 
     * the experiment (thus each array should be ordered in the same way)
     * @param outputDirectory The directory to output the CSV file into.
     * @param evaluationName The name of the evaluation (used to name output file.
     * @param outputFileExt The extension to put on the output file.
     * @param verbose Determines wheter the data should be dumped to the console
     * as well.
     * @return A File Object indicating where the output CSV file was written to.
     */
    public static File prepFriedmanTestData(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put(((Signal[]) sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), (Signal[]) sigStore.get(i));
            } catch (noMetadataException ex) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + ex);
            }
        }
        String[] keys = sigArrMap.keySet().toArray(new String[sigStore.size()]);
        Arrays.sort(keys);
        
        sigStore.clear();
        for (int i = 0; i < keys.length; i++) {
            sigStore.add(sigArrMap.get(keys[i]));
        }
        
        int numAlgos = sigStore.size();
        int numRuns = sigStore.get(0).length;
        String EvaluationOutput = "";

        String[] runNames = new String[numAlgos];
        EvaluationOutput += "Classification fold,";
        for (int i = 0; i < numAlgos; i++) {
            try {
                runNames[i] = sigStore.get(i)[0].getStringMetadata(Signal.PROP_ALG_NAME);
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
            if (sigStore.get(i).length != numRuns){
                throw new RuntimeException(runNames[i] + " had a different number of results (" + sigStore.get(i).length + ") to " + runNames[0] + " (" + numRuns + ")");
            }
            EvaluationOutput += runNames[i];
            if (i< sigStore.size()-1){
                EvaluationOutput += ",";
            }
        }
        EvaluationOutput += "\n";
        
        for (int i = 0; i < numRuns; i++) {
            EvaluationOutput += i + ",";
            for (int j = 0 ; j < numAlgos; j++){
                try{
                    EvaluationOutput += "" + sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF);
                } catch (noMetadataException e) {
                    throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
                }
                if (j< sigStore.size()-1){
                    EvaluationOutput += ",";
                }
            }
            EvaluationOutput += "\n";
        }

        
        
        File copyToDirFile = new File(outputDirectory);
        if (!copyToDirFile.isDirectory()) {
            if (!copyToDirFile.mkdirs()) {
                throw new RuntimeException("Could not create the output folder.");
            }
        }
        File testFile = null;
        try {
            testFile = new File(outputDirectory + File.separator + evaluationName + "_results_fold" + outputFileExt);
            BufferedWriter output = new BufferedWriter(new FileWriter(testFile));
            output.write(EvaluationOutput);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (verbose) {
            System.out.println(EvaluationOutput);
        }
        return testFile;
    }
    
    /**
     * A utility function that takes an ArrayList of Signal arrays containing 
     * algorithm name and performance metadata and outputs the mean of each 
     * systems data into a CSV file to be used As a result sumamry.
     * 
     * @param sigStore An ArrayList of Signal arrays containing algorithm name 
     * and performance metadata. Each Signal Object represents a single fold of 
     * the experiment (thus each array should be ordered in the same way)
     * @param outputDirectory The directory to output the CSV file into.
     * @param evaluationName The name of the evaluation (used to name output file.
     * @param outputFileExt The extension to put on the output file.
     * @param verbose Determines wheter the data should be dumped to the console
     * as well.
     * @return A File Object indicating where the output CSV file was written to.
     */
    public static File prepSummaryResultData(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put(((Signal[]) sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), (Signal[]) sigStore.get(i));
            } catch (noMetadataException ex) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + ex);
            }
        }
        String[] keys = sigArrMap.keySet().toArray(new String[sigStore.size()]);
        Arrays.sort(keys);
        
        double[] means = new double[keys.length];
        
        sigStore.clear();
        for (int i = 0; i < keys.length; i++) {
            sigStore.add(sigArrMap.get(keys[i]));
        }
        
        int numAlgos = sigStore.size();
        int numRuns = sigStore.get(0).length;
        String EvaluationOutput = "";
        String[] runNames = new String[numAlgos];
        for (int i = 0; i < numAlgos; i++) {
            try {
                runNames[i] = sigStore.get(i)[0].getStringMetadata(Signal.PROP_ALG_NAME);
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
            if (sigStore.get(i).length != numRuns){
                throw new RuntimeException(runNames[i] + " had a different number of results (" + sigStore.get(i).length + ") to " + runNames[0] + " (" + numRuns + ")");
            }
        }
        for (int i = 0; i < numRuns; i++) {
            for (int j = 0 ; j < numAlgos; j++){
                try{
                    means[j] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF);
                } catch (noMetadataException e) {
                    throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
                }
            }
        }
        for (int i = 0; i < numAlgos; i++) {
            means[i] /= numRuns;
        }
        
        
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);
        EvaluationOutput += "Participant,Average Classifcation Accuracy\n";
        for (int i = 0; i < numAlgos; i++) {
            EvaluationOutput += runNames[i] + ",";
            EvaluationOutput += dec.format(means[i] * 100.0);
            EvaluationOutput += "%\n";
        }
        
        File copyToDirFile = new File(outputDirectory);
        if (!copyToDirFile.isDirectory()) {
            if (!copyToDirFile.mkdirs()) {
                throw new RuntimeException("Could not create the output folder.");
            }
        }
        File testFile = null;
        try {
            testFile = new File(outputDirectory + File.separator + evaluationName + "_avg_results" + outputFileExt);
            BufferedWriter output = new BufferedWriter(new FileWriter(testFile));
            output.write(EvaluationOutput);
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (verbose) {
            System.out.println(EvaluationOutput);
        }
        return testFile;
    }
    
}
