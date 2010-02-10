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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.imirsel.m2k.evaluation2.DataObj;
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

    public static Table prepTableDataOverClasses(Map<String,DataObj> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey) {
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});
    	
        String[] colNames = new String[numAlgos + 1];
        colNames[0] = "Class";
        for (int i = 0; i < numAlgos; i++) {
            colNames[i+1] = jobIDandName[i][1];
        }

        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);

        List<String[]> rows = new ArrayList<String[]>();
        
        for (int c = 0; c < classNames.size(); c++) {
            String[] row = new String[numAlgos + 1];
            row[0] = classNames.get(c).replaceAll(",", " ");

            for (int j = 0 ; j < numAlgos; j++){  
            	if (jobIDToAggregateEval.get(jobIDandName[j][0]).getMetadata(metadataKey).getClass().getComponentType().isArray()){
            		row[j+1] = dec.format(100.0 * jobIDToAggregateEval.get(jobIDandName[j][0]).get2dDoubleArrayMetadata(metadataKey)[c][c]);
            	}else{
            		//discounted types are 1D as there is no residual confusion after discounting
            		row[j+1] = dec.format(100.0 * jobIDToAggregateEval.get(jobIDandName[j][0]).getDoubleArrayMetadata(metadataKey)[c]);
            	}
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

    /**
     * A utility function that takes a Map of jobID to a <code>DataObj</code> that 
     * contains a confusion matrix and outputs the data into a CSV file to be used 
     * to perform significance tests in Matlab or another suitable environment.
     * 
     * @param jobIDToAggregateEval Map of jobID to overall evaluation data.
     * @param jobIDToName Map of jobID to job Name.
     * @param outputFile The file to write the output to.
     */
    public static void prepFriedmanTestDataCSVOverClasses(Map<String,DataObj> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey, File outputFile) 
    		throws IOException{
    	
        //sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id).replaceAll(",", " ")};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});

        String csv = "*Class,";
        for (int i = 0; i < numAlgos; i++) {
        	csv += jobIDandName[i][1];
            if (i<numAlgos-1){
            	csv += ",";
            }
        }
        csv += "\n";
        
        for (int c = 0; c < classNames.size(); c++) {
            csv += classNames.get(c).replaceAll(",", " ") + ",";

            for (int j = 0 ; j < numAlgos; j++){
            	if (jobIDToAggregateEval.get(jobIDandName[j][0]).getMetadata(metadataKey).getClass().getComponentType().isArray()){
            		csv += "" + jobIDToAggregateEval.get(jobIDandName[j][0]).get2dDoubleArrayMetadata(metadataKey)[c][c];
            	}else{
            		//discounted types are 1D as there is no residual confusion after discounting
            		csv += "" + jobIDToAggregateEval.get(jobIDandName[j][0]).get2dDoubleArrayMetadata(metadataKey)[c];
            	}
                if (j < numAlgos-1){
                	csv += ",";
                }
            }
            csv += "\n";
        }
        
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputFile));
            output.write(csv);
        }finally{
        	try {
	        	if(output != null){
	        		output.flush();
	        		output.close();
	        	}
        	} catch (IOException ex) {}
        }
    }

    public static Table prepTableDataOverFolds(Map<String,List<DataObj>> jobIDToFoldEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey) {
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});

        int numFolds = jobIDToFoldEval.get(jobIDandName[0][0]).size();

        String[] colNames = new String[numAlgos + 1];
        colNames[0] = "Classification fold";
        for (int i = 0; i < numAlgos; i++) {
            try {
                colNames[i+1] = jobIDandName[i][1];
            } catch (noMetadataException e) {
                throw new RuntimeException("prepFriedmanTestData: Required metadata not found!\n" + e);
            }
        }

        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);

        List<String[]> rows = new ArrayList<String[]>();
        for (int r = 0; r < numFolds; r++) {
            String[] row = new String[numAlgos + 1];
            row[0] = "" + (r+1);

            for (int j = 0 ; j < numAlgos; j++){
                row[j+1] = dec.format(100.0 * jobIDToFoldEval.get(jobIDandName[j][0]).get(r).getDoubleMetadata(metadataKey));
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
    public static void prepFriedmanTestDataCSVOverFolds(Map<String,List<DataObj>> jobIDToFoldEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey, File outputFile) 
    		throws IOException{
        //sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});

        int numFolds = jobIDToFoldEval.get(jobIDandName[0][0]).size();

        String csv = "*Classification fold,";
        for (int i = 0; i < numAlgos; i++) {
            csv += jobIDandName[i][1];
            if (i< numAlgos-1){
                csv += ",";
            }
        }
        csv += "\n";
        
        for (int i = 0; i < numFolds; i++) {
            csv += i + ",";
            for (int j = 0 ; j < numAlgos; j++){
                csv += "" + jobIDToFoldEval.get(jobIDandName[j][0]).get(i).getDoubleMetadata(metadataKey);
                if (j<numAlgos-1){
                    csv += ",";
                }
            }
            csv += "\n";
        }

        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputFile));
            output.write(csv);
        }finally{
        	try {
	        	if(output != null){
	        		output.flush();
	        		output.close();
	        	}
        	} catch (IOException ex) {}
        }
    }

    public static Table prepSummaryTable(Map<String,DataObj> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, boolean usingAHierarchy) {
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});

        String[] colNames;
        if(usingAHierarchy){
            colNames = new String[5];
            colNames[0] = "Job";
            colNames[1] = "Overall Accuracy";
            colNames[2] = "Normalised Overall Accuracy";
            colNames[3] = "Overall Discounted Accuracy";
            colNames[4] = "Normalised Overall Discounted Accuracy";
        }else{
            colNames = new String[3];
            colNames[0] = "Job";
            colNames[1] = "Overall Accuracy";
            colNames[2] = "Normalised Overall Accuracy";
        }

        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);

        List<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < numAlgos; i++) {
            String[] row = new String[colNames.length];
            row[0] = jobIDandName[i][1];
            row[1] = "" + jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_ACCURACY);
            row[2] = "" + jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY);
            if(usingAHierarchy){
            	row[3] = "" + jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY);
            	row[4] = "" + jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY);
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

    public static void prepSummaryResultDataCSV(Map<String,DataObj> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, File outputFile, boolean usingAHierarchy) 
    		throws IOException{
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});
        
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(2);
        
        String csv = "*Participant,Overall Accuracy,Overall Normalised Accuracy";
        if (usingAHierarchy){
        	csv += ",Overall Discounted Accuracy,Overall Normalised Accuracy";
        }
        csv += "\n";
        for (int i = 0; i < numAlgos; i++) {
        	csv += jobIDandName[i][1];
        	csv += "," + dec.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_ACCURACY) * 100.0) + "%";
        	csv += "," + dec.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY) * 100.0) + "%";
        	if(usingAHierarchy){
        		csv += "," + dec.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100.0) + "%";
            	csv += "," + dec.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100.0) + "%";
        	}
        	csv += "\n";
        }
        
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputFile));
            output.write(csv);
        }finally{
        	try {
	        	if(output != null){
	        		output.flush();
	        		output.close();
	        	}
        	} catch (IOException ex) {}
        }
    }
    
}
