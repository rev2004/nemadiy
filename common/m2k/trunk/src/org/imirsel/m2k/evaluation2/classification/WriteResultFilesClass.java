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
import java.util.List;
import org.imirsel.m2k.util.Signal;
import org.imirsel.m2k.util.noMetadataException;

/**
 *
 * @author kriswest
 */
public class WriteResultFilesClass {

    static class Table{
        private String[] colHeaders;
        private List<String[]> rows;

        public Table(String[] colHeaders,
                     List<String[]> rows){
            this.colHeaders = colHeaders;
            this.rows = rows;
        }

        /**
         * @return the colHeaders
         */
        public String[] getColHeaders(){
            return colHeaders;
        }

        /**
         * @param colHeaders the colHeaders to set
         */
        public void setColHeaders(String[] colHeaders){
            this.colHeaders = colHeaders;
        }

        /**
         * @return the rows
         */
        public List<String[]> getRows(){
            return rows;
        }

        /**
         * @param rows the rows to set
         */
        public void setRows(List<String[]> rows){
            this.rows = rows;
        }
    }

    public static Table prepTableDataOverClasses(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String perfMetadataKey, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put((sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), sigStore.get(i));
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

        String[] colNames = new String[numAlgos + 1];
        colNames[0] = "Class";
        for (int i = 0; i < numAlgos; i++) {
            try {
                colNames[i+1] = sigStore.get(i)[0].getStringMetadata(Signal.PROP_ALG_NAME).replaceAll(",", " ");
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
        }

        List<String[]> rows = new ArrayList<String[]>();
        String[] classNames = sigStore.get(0)[0].getStringArrayMetadata(Signal.PROP_CLASSES);
        for (int c = 0; c < classNames.length; c++) {
            String[] row = new String[numAlgos + 1];
            row[0] = classNames[c].replaceAll(",", " ");

            for (int j = 0 ; j < numAlgos; j++){
                double avg = 0.0;
                for (int i = 0; i < numRuns; i++) {
                    avg += sigStore.get(j)[i].getDoubleArrayMetadata(perfMetadataKey)[c];
                }
                avg /= numRuns;

                row[j+1] = "" + avg;
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

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
     * @param perfMetadataKey
     * @param outputFileExt The extension to put on the output file.
     * @param verbose Determines wheter the data should be dumped to the console
     * as well.
     * @return A File Object indicating where the output CSV file was written to.
     */
    public static File prepFriedmanTestDataOverClassesCSV(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String perfMetadataKey, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put((sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), sigStore.get(i));
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
        EvaluationOutput += "*Class,";
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
                    avg += sigStore.get(j)[i].getDoubleArrayMetadata(perfMetadataKey)[c];
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
            testFile = new File(outputDirectory + File.separator + evaluationName + "_" + perfMetadataKey.replaceAll("\\s+", "_") + outputFileExt);
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

    public static Table prepTableData(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String perfMetadataKey, String outputFileExt, boolean verbose) {
        //sort systems alphabetically
        HashMap<String,Signal[]> sigArrMap = new HashMap<String,Signal[]>();
        for (int i = 0; i < sigStore.size(); i++) {
            try {
                sigArrMap.put((sigStore.get(i))[0].getStringMetadata(Signal.PROP_ALG_NAME), sigStore.get(i));
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

        String[] colNames = new String[numAlgos + 1];
        colNames[0] = "Classification fold";
        for (int i = 0; i < numAlgos; i++) {
            try {
                colNames[i+1] = sigStore.get(i)[0].getStringMetadata(Signal.PROP_ALG_NAME).replaceAll(",", " ");
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
        }

        List<String[]> rows = new ArrayList<String[]>();
        String[] classNames = sigStore.get(0)[0].getStringArrayMetadata(Signal.PROP_CLASSES);
        for (int r = 0; r < numRuns; r++) {
            String[] row = new String[numAlgos + 1];
            row[0] = "" + (r+1);

            for (int j = 0 ; j < numAlgos; j++){
                row[j+1] = "" + sigStore.get(j)[r].getDoubleMetadata(perfMetadataKey);
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
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
    public static File prepFriedmanTestDataCSV(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String perfMetadataKey, String outputFileExt, boolean verbose) {
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
        EvaluationOutput += "*Classification fold,";
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
                    EvaluationOutput += "" + sigStore.get(j)[i].getDoubleMetadata(perfMetadataKey);
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
            testFile = new File(outputDirectory + File.separator + evaluationName + "_" + perfMetadataKey.replaceAll("\\s+", "_") + outputFileExt);
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

    public static Table prepSummaryTable(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String outputFileExt, boolean usingAHierarchy, boolean verbose) {
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


        double[][] means = null;
        if(usingAHierarchy){
            means = new double[keys.length][4];
        }else{
            means = new double[keys.length][2];
        }

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
                    means[j][0] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_ACC);
                    means[j][1] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_NORM_ACC);
                    if(usingAHierarchy){
                        means[j][2] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_DISCOUNTED_ACC);
                        means[j][3] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_NORM_DISCOUNTED_ACC);
                    }
                } catch (noMetadataException e) {
                    throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
                }
            }
        }
        int numMetrics = 2;
        if(usingAHierarchy){
            numMetrics = 4;
        }
        for (int i = 0; i < numAlgos; i++) {
            for (int j = 0; j < numMetrics; j++){
                means[i][j] /= numRuns;
            }
        }


        String[] colNames;
        if(usingAHierarchy){
            colNames = new String[numAlgos+3];
            colNames[0] = "Participant";
            colNames[1] = "Mean Accuracy";
            colNames[2] = "Mean Discounted Accuracy";
        }else{
            colNames = new String[numAlgos+2];
            colNames[0] = "Participant";
            colNames[1] = "Mean Accuracy";
        }

        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);

        List<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < numAlgos; i++) {
            String[] row = new String[colNames.length];
            row[0] += runNames[i];
            //for (int j = 0; j < numMetrics; j++){
            //TODO: undo this hack
            for (int j = 0; j < numMetrics; j++){
                row[j+1] += dec.format(means[i][j*2] * 100.0) + "%";
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
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
    public static File prepSummaryResultDataCSV(ArrayList<Signal[]> sigStore, String outputDirectory, String evaluationName, String outputFileExt, boolean usingAHierarchy, boolean verbose) {
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


        double[][] means = null;
        if(usingAHierarchy){
            means = new double[keys.length][4];
        }else{
            means = new double[keys.length][2];
        }

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
                    means[j][0] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_ACC);
                    means[j][1] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_NORM_ACC);
                    if(usingAHierarchy){
                        means[j][2] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_DISCOUNTED_ACC);
                        means[j][3] += sigStore.get(j)[i].getDoubleMetadata(Signal.PROP_PERF_NORM_DISCOUNTED_ACC);
                    }
                } catch (noMetadataException e) {
                    throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
                }
            }
        }
        int numMetrics = 2;
        if(usingAHierarchy){
            numMetrics = 4;
        }
        for (int i = 0; i < numAlgos; i++) {
            for (int j = 0; j < numMetrics; j++){
                means[i][j] /= numRuns;
            }
        }
        
        
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);
        //TODO: undo this hack
        //EvaluationOutput += "*Participant,Mean Accuracy,Mean Normalised Accuracy";
        EvaluationOutput += "*Participant,Mean Accuracy";
        if (usingAHierarchy){
            //TODO: undo this hack
            //EvaluationOutput += ",Mean Discounted Accuracy,Mean Normalised Accuracy";
            EvaluationOutput += ",Mean Discounted Accuracy";
        }
        EvaluationOutput += "\n";
        for (int i = 0; i < numAlgos; i++) {
            EvaluationOutput += runNames[i];
            //for (int j = 0; j < numMetrics; j++){
            //TODO: undo this hack
            for (int j = 0; j < numMetrics; j+=2){
                EvaluationOutput += "," + dec.format(means[i][j] * 100.0) + "%";
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
            testFile = new File(outputDirectory + File.separator + "summary_" + evaluationName + outputFileExt);
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
