/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.melody;

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

import org.imirsel.nema.analytics.evaluation.AbstractWriteResultFiles;
import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;
import org.imirsel.nema.model.*;

/**
 * Utility class that can produce CSV result files and Table Objects for use in reporting
 * results, encoding them for use in significance testing (as CSV files) and formatting
 * data for use in the output of HTML result pages.
 * 
 * @author kris.west@gmail.com
 */
public class WriteMelodyResultFiles extends AbstractWriteResultFiles{

	public static final DecimalFormat DEC = new DecimalFormat("0.000");
    
	/**
	 * 
	 * @param jobIdToEval
	 * @param jobIdToName
	 * @return
	 */
	public static Table prepSummaryTableData(Map<String,NemaData> jobIdToEval, Map<String,String> jobIdToName) {

    	// create the table
        String[] colNames = new String[6];
        colNames[0] = "Algorithm";
        colNames[1] = NemaDataConstants.MELODY_RAW_PITCH_ACCURACY;
        colNames[2] = NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY;
        colNames[3] = NemaDataConstants.MELODY_VOICING_RECALL;
        colNames[4] = NemaDataConstants.MELODY_VOICING_FALSE_ALARM;
        colNames[5] = NemaDataConstants.MELODY_OVERALL_ACCURACY;
        
        List<String[]> rows = new ArrayList<String[]>();
        
        NemaData eval;
		String jobId;
		String jobName;
        for (Iterator<String> it = jobIdToEval.keySet().iterator();it.hasNext();) {
        	jobId = it.next();
        	jobName = jobIdToName.get(jobId);
        	eval = jobIdToEval.get(jobId);
        	String[] row = new String[6];
        	row[0] = jobName;
            row[1] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY));
            row[2] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY));
            row[3] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_VOICING_RECALL));
            row[4] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_VOICING_FALSE_ALARM));
            row[5] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_OVERALL_ACCURACY));   
            rows.add(row);
        }

        return new Table(colNames, rows);
    }

    /**
     * 
     * @param evalList
     * @param jobName
     * @return
     */
    public static Table prepPerTrackTableData(List<NemaData> evalList, String jobName) {
    	// create the table
        String[] colNames = new String[6];
        colNames[0] = "Track";
        colNames[1] = NemaDataConstants.MELODY_RAW_PITCH_ACCURACY;
        colNames[2] = NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY;
        colNames[3] = NemaDataConstants.MELODY_VOICING_RECALL;
        colNames[4] = NemaDataConstants.MELODY_VOICING_FALSE_ALARM;
        colNames[5] = NemaDataConstants.MELODY_OVERALL_ACCURACY;
        
        List<String[]> rows = new ArrayList<String[]>();
        
        NemaData eval;
		String trackName;
        for (Iterator<NemaData> it = evalList.iterator();it.hasNext();) {
        	eval = it.next();
        	trackName = eval.getId();
        	String[] row = new String[6];
        	row[0] = trackName;
            row[1] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY));
            row[2] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY));
            row[3] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_VOICING_RECALL));
            row[4] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_VOICING_FALSE_ALARM));
            row[5] = DEC.format(eval.getDoubleMetadata(NemaDataConstants.MELODY_OVERALL_ACCURACY));   
            rows.add(row);
        }

        return new Table(colNames, rows);
    }

}
