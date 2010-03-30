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

/**
 * Utility to write CSV result files for Key evaluations.
 * 
 * @author afe405@gmail.com
 * @author kris.west@gmail.com
 * @since 0.1.0
 *
 */
public class WriteKeyResultFiles extends AbstractWriteResultFiles {
	
	public static final DecimalFormat DEC1 = new DecimalFormat("0.000");
	public static final DecimalFormat DEC2 = new DecimalFormat("0.0");
    
	/**
	 * Write 
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

}



