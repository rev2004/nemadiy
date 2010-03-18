/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.chord;

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
public class WriteChordResultFiles extends AbstractWriteResultFiles {

	public static final DecimalFormat DEC = new DecimalFormat("0.00");
    
	/**
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverTracksAndSystems(Map<String,List<List<NemaData>>> jobIDToTrackEval, Map<String,String> jobIDToName, String metricKey) {
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
    	
    	//set column names
    	int numCols = numAlgos + 2;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        colNames[1] = "Track";
        for (int i = 0; i < numAlgos; i++) {
            colNames[i+2] = jobIDandName[i][1];
        }

        //count number of rows to produce
        int numTracks = 0;
        String firstJob = jobIDandName[0][0];
        {
	        List<List<NemaData>> firstResList = jobIDToTrackEval.get(firstJob);
	        for (Iterator<List<NemaData>> iterator = firstResList.iterator(); iterator.hasNext();) {
				List<NemaData> list = iterator.next();
				numTracks += list.size();
			}
        }
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int fold = 0;
        int foldTrackCount = 0;
        int actualRowCount = 0;
        String[] row;
        List<List<NemaData>> firstResList;
        NemaData data;
        while(actualRowCount < numTracks){
        	row = new String[numCols];
        	row[0] = "" + fold;
        	firstResList = jobIDToTrackEval.get(firstJob);
        	row[1] = firstResList.get(fold).get(foldTrackCount).getId();
        	for(int i=0;i<numAlgos;i++){
	        		data = jobIDToTrackEval.get(jobIDandName[i][0]).get(fold).get(foldTrackCount);

	        		if (!data.getId().equals(row[1])){
	        			throw new IllegalArgumentException("Results from job ID: " + jobIDandName[i][0] + " are not ordered the same as results from job ID: " + firstJob);
	        		}
	        		row[i+2] = "" + data.getDoubleMetadata(metricKey);
        	}
        	rows.add(row);

        	actualRowCount++;
        	foldTrackCount++;
        	if (foldTrackCount == firstResList.get(fold).size()){
        		fold++;
        	}
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverFoldsAndSystems(Map<String,List<NemaData>> jobIDToFoldEval, Map<String,String> jobIDToName, String metricKey) {
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
    	
    	//set column names
    	int numCols = numAlgos + 1;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        for (int i = 0; i < numAlgos; i++) {
            colNames[i+1] = jobIDandName[i][1];
        }

        //count number of rows to produce
        int numFolds = jobIDToFoldEval.get(jobIDandName[0][0]).size();
        
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int fold = 0;
        String[] row;
        NemaData data;
        for(int f=0;f<numFolds;f++){
        	row = new String[numCols];
        	row[0] = "" + fold;
        	for(int i=0;i<numAlgos;i++){
        		data = jobIDToFoldEval.get(jobIDandName[i][0]).get(f);
        		row[i+1] = "" + data.getDoubleMetadata(metricKey);
        	}
        	rows.add(row);
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the metrics are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverTracks(List<List<NemaData>> trackEval, String[] metricKeys) {
    	//set column names
    	int numMetrics = metricKeys.length;
    	int numCols = numMetrics + 2;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        colNames[1] = "Track";
        for (int i = 0; i < numMetrics; i++) {
            colNames[i+2] = metricKeys[i];
        }

        //count number of rows to produce
        int numTracks = 0;
        for (Iterator<List<NemaData>> iterator = trackEval.iterator(); iterator.hasNext();) {
			List<NemaData> list = iterator.next();
			numTracks += list.size();
		}
        
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int fold = 0;
        int foldTrackCount = 0;
        int actualRowCount = 0;
        String[] row;
        NemaData data;
        while(actualRowCount < numTracks){
        	row = new String[numCols];
        	row[0] = "" + fold;
        	row[1] = trackEval.get(fold).get(foldTrackCount).getId();
        	for(int i=0;i<numMetrics;i++){
        		data = trackEval.get(fold).get(foldTrackCount);
        		row[i+2] = "" + data.getDoubleMetadata(metricKeys[i]);
        	}
        	rows.add(row);

        	actualRowCount++;
        	foldTrackCount++;
        	if (foldTrackCount == trackEval.get(fold).size()){
        		fold++;
        	}
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the metrics are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverFolds(List<NemaData> foldEval, String[] metricKeys) {
    	//set column names
    	int numMetrics = metricKeys.length;
    	int numCols = numMetrics + 1;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        for (int i = 0; i < numMetrics; i++) {
            colNames[i+1] = metricKeys[i];
        }

        //count number of rows to produce
        int numFolds = foldEval.size();
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        String[] row;
        NemaData data;
        for(int i=0;i<numFolds;i++){
        	row = new String[numCols];
        	row[0] = "" + i;
        	for(int m=0;m<numMetrics;m++){
        		data = foldEval.get(i);
        		row[m+1] = "" + data.getDoubleMetadata(metricKeys[m]);
        	}
        	rows.add(row);
        }
        
        return new Table(colNames, rows);
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
    public static Table prepSummaryTable(Map<String,NemaData> jobIDToAggregateEval, Map<String,String> jobIDToName) {
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
        colNames = new String[3];
        colNames[0] = "Job";
        colNames[1] = NemaDataConstants.CHORD_OVERLAP_RATIO;
        colNames[2] = NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO;
    

        List<String[]> rows = new ArrayList<String[]>();
        for (int i = 0; i < numAlgos; i++) {
            String[] row = new String[colNames.length];
            row[0] = jobIDandName[i][1];
            row[1] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO) * 100.0) + "%";
            row[2] = DEC.format(jobIDToAggregateEval.get(jobIDandName[i][0]).getDoubleMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO) * 100.0) + "%";
            
            rows.add(row);
        }
        return new Table(colNames, rows);
    }
    
}
