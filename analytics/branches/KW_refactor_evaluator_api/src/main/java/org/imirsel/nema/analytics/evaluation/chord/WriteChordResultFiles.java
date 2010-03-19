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
