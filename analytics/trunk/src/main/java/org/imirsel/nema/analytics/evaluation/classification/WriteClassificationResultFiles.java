/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.model.*;
import org.imirsel.nema.analytics.evaluation.AbstractWriteResultFiles;
import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;

/**
 * Utility class that can produce CSV result files and Table Objects for use in reporting
 * results, encoding them for use in significance testing (as CSV files) and formatting
 * data for use in the output of HTML result pages.
 * 
 * @author kris.west@gmail.com
 */
public class WriteClassificationResultFiles extends AbstractWriteResultFiles {

	public static final DecimalFormat DEC = new DecimalFormat("0.00");
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the different classes of data in the evaluation.
     * 
     * @param jobIDToAggregateEval Map linking jobID to its overall evaluation data Object.
     * @param jobIDToName Map linking jobID to the Job name to use in the Table for each set of results.
     * @param classNames A list of the class names used.
     * @param metadataKey The evaluation metadata type to use. This method expects the metadata to point to
     * a 1 or 2 dimensional double array giving the accuracies or confusions for each class.
     * @return The prepared table.
     */
    public static Table prepTableDataOverClasses(Map<String,NemaData> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey) {
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

        List<String[]> rows = new ArrayList<String[]>();
        
        for (int c = 0; c < classNames.size(); c++) {
            String[] row = new String[numAlgos + 1];
            row[0] = classNames.get(c).replaceAll(",", " ");

            for (int j = 0 ; j < numAlgos; j++){  
            	if (jobIDToAggregateEval.get(jobIDandName[j][0]).getMetadata(metadataKey).getClass().getComponentType().isArray()){
            		row[j+1] = DEC.format(100.0 * jobIDToAggregateEval.get(jobIDandName[j][0]).get2dDoubleArrayMetadata(metadataKey)[c][c]);
            	}else{
            		//discounted types are 1D as there is no residual confusion after discounting
            		row[j+1] = DEC.format(100.0 * jobIDToAggregateEval.get(jobIDandName[j][0]).getDoubleArrayMetadata(metadataKey)[c]);
            	}
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

    /**
     * Prepares a CSV file representing the specified evaluation metadata, where the systems are the columns
     * of the CSV and the rows are the different classes of data in the evaluation.
     * 
     * @param jobIDToAggregateEval Map linking jobID to its overall evaluation data Object.
     * @param jobIDToName Map linking jobID to the Job name to use in the CSV for each set of results.
     * @param classNames A list of the class names used.
     * @param metadataKey The evaluation metadata type to use. This method expects the metadata to point to
     * a 1 or 2 dimensional double array giving the accuracies or confusions for each class.
     * @param outputFile The file to write the CSV encoded data to.
     */
    public static void prepFriedmanTestDataCSVOverClasses(Map<String,NemaData> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey, File outputFile) 
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
            		csv += "" + jobIDToAggregateEval.get(jobIDandName[j][0]).getDoubleArrayMetadata(metadataKey)[c];
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

    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the folds or iterations of the experiment evaluated.
     * 
     * @param jobIDToFoldEval Map linking jobID to a list of evaluation data Objects for each fold/iteration
     * of the experiment. This list is expected to be consistently ordered across all systems.
     * @param jobIDToName Map linking jobID to the Job name to use in the Table for each set of results.
     * @param classNames A list of the class names used.
     * @param metadataKey The evaluation metadata type to use. This method expects the metadata to point to
     * a single double value giving the accuracy or performance estimate for the experiment fold.
     * @return The prepared table.
     */
    public static Table prepTableDataOverFolds(Map<String,List<NemaData>> jobIDToFoldEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey) {
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
            colNames[i+1] = jobIDandName[i][1];
        }

        List<String[]> rows = new ArrayList<String[]>();
        for (int r = 0; r < numFolds; r++) {
            String[] row = new String[numAlgos + 1];
            row[0] = "" + (r+1);

            for (int j = 0 ; j < numAlgos; j++){
                row[j+1] = DEC.format(100.0 * jobIDToFoldEval.get(jobIDandName[j][0]).get(r).getDoubleMetadata(metadataKey));
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

    /**
     * Prepares a CSV file representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the folds or iterations of the experiment evaluated.
     * 
     * @param jobIDToFoldEval Map linking jobID to a list of evaluation data Objects for each fold/iteration
     * of the experiment. This list is expected to be consistently ordered across all systems.
     * @param jobIDToName Map linking jobID to the Job name to use in the CSV for each set of results.
     * @param classNames A list of the class names used.
     * @param metadataKey The evaluation metadata type to use. This method expects the metadata to point to
     * a single double value giving the accuracy or performance estimate for the experiment fold.
     * @param outputFile The file to write the CSV encoded data to.
     */
    public static void prepFriedmanTestDataCSVOverFolds(Map<String,List<NemaData>> jobIDToFoldEval, Map<String,String> jobIDToName, List<String> classNames, String metadataKey, File outputFile) 
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

    /**
     * Prepares a summary table for the classification task which displays multiple evaluation metrics
     * averaged over all folds of the experiment.
     * 
     * @param jobIDToAggregateEval Map linking jobID to its overall evaluation data Object.
     * @param jobIDToName Map linking jobID to the Job name to use in the Table for each set of results.
     * @param classNames A list of the class names used.
     * @param usingAHierarchy A flag that indicates whether hierarchical discounting was performed. If 
     * true then additional metrics based on the hierarchical discounting procedure are reported.
     * @return The prepared table.
     */
    public static Table prepSummaryTable(Map<String,NemaData> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, boolean usingAHierarchy) {
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

        List<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < numAlgos; i++) {
            String[] row = new String[colNames.length];
            row[0] = jobIDandName[i][1];
            row[1] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY) * 100.0) + "%";
            row[2] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY) * 100.0) + "%";
            if(usingAHierarchy){
            	row[3] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100.0) + "%";
            	row[4] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100.0) + "%";
            }
            rows.add(row);
        }
        return new Table(colNames, rows);
    }

    /**
     * Prepares a summary CSV file for the classification task which encodes multiple evaluation metrics
     * averaged over all folds of the experiment.
     * 
     * @param jobIDToAggregateEval Map linking jobID to its overall evaluation data Object.
     * @param jobIDToName Map linking jobID to the Job name to use in the CSV for each set of results.
     * @param classNames A list of the class names used.
     * @param outputFile The file to write the CSV encoded data to.
     * @param usingAHierarchy A flag that indicates whether hierarchical discounting was performed. If 
     * true then additional metrics based on the hierarchical discounting procedure are reported.
     */
    public static void prepSummaryResultDataCSV(Map<String,NemaData> jobIDToAggregateEval, Map<String,String> jobIDToName, List<String> classNames, File outputFile, boolean usingAHierarchy) 
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
        
        String csv = "*Participant,Overall Accuracy,Overall Normalised Accuracy";
        if (usingAHierarchy){
        	csv += ",Overall Discounted Accuracy,Overall Normalised Accuracy";
        }
        csv += "\n";
        for (int i = 0; i < numAlgos; i++) {
        	csv += jobIDandName[i][1];
        	csv += "," + DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY) * 100.0) + "%";
        	csv += "," + DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY) * 100.0) + "%";
        	if(usingAHierarchy){
        		csv += "," + DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100.0) + "%";
            	csv += "," + DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100.0) + "%";
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
