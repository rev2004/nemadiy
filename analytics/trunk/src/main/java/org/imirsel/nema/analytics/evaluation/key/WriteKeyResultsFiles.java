package org.imirsel.nema.analytics.evaluation.key;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.AbstractWriteResultFiles;
import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;

public class WriteKeyResultsFiles extends AbstractWriteResultFiles {
	
public static final DecimalFormat DEC1 = new DecimalFormat("0.000");
public static final DecimalFormat DEC2 = new DecimalFormat("0.0");
    
	/**
	 * 
	 * @param jobIdToEval
	 * @param jobIdToName
	 * @return
	 */
	public static Table prepSummaryTableData(Map<String,NemaData> jobIdToEval, Map<String,String> jobIdToName) {

    	// create the table
        String[] colNames = new String[7];
        colNames[0] = "Algorithm";
        colNames[1] = NemaDataConstants.KEY_DETECTION_CORRECT;
        colNames[2] = NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR;
        colNames[3] = NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR;
        colNames[4] = NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR;
        colNames[5] = NemaDataConstants.KEY_DETECTION_ERROR;
        colNames[6] = NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE;
        
        List<String[]> rows = new ArrayList<String[]>();
        
        NemaData eval;
		String jobId;
		String jobName;
        for (Iterator<String> it = jobIdToEval.keySet().iterator();it.hasNext();) {
        	jobId = it.next();
        	jobName = jobIdToName.get(jobId);
        	eval = jobIdToEval.get(jobId);
        	String[] row = new String[7];
        	row[0] = jobName;
            row[1] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_CORRECT));
            row[2] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR));
            row[3] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR));
            row[4] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR));
            row[5] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_ERROR));
            row[6] = DEC1.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE));
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
        String[] colNames = new String[7];
        colNames[0] = "Track";
        colNames[1] = NemaDataConstants.KEY_DETECTION_CORRECT;
        colNames[2] = NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR;
        colNames[3] = NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR;
        colNames[4] = NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR;
        colNames[5] = NemaDataConstants.KEY_DETECTION_ERROR;
        colNames[6] = NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE;
        
        List<String[]> rows = new ArrayList<String[]>();
        
        NemaData eval;
		String trackName;
        for (Iterator<NemaData> it = evalList.iterator();it.hasNext();) {
        	eval = it.next();
        	trackName = eval.getId();
        	String[] row = new String[7];
        	row[0] = trackName;
        	row[1] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_CORRECT));
            row[2] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR));
            row[3] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR));
            row[4] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR));
            row[5] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_ERROR));
            row[6] = DEC2.format(eval.getDoubleMetadata(NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE));
            rows.add(row);
        }

        return new Table(colNames, rows);
    }

}



