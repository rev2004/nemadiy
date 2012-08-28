package org.imirsel.nema.analytics.evaluation.qbsht;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.model.*;

/**
 * Evaluator for Query by Singing/Humming (QBSH) Task2 subtask.
 * @author cwillis
 */
public class QBSHTask2Evaluator extends EvaluatorImpl
{
    
    /**
	 * Constructor (no arg - task, dataset, output and working dirs, training
	 * and test sets must be set manually).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public QBSHTask2Evaluator() {
		super();
	}
  
	
    protected void setupEvalMetrics() 
    {
		trackEvalMetrics.clear();
		trackEvalMetrics.add(NemaDataConstants.QBSHT_QUERY_RAW_COUNT);

		overallEvalMetrics.clear();
		overallEvalMetrics.add(NemaDataConstants.QBSHT_QUERY_RAW_COUNT);
		
		foldEvalMetrics.clear();
		foldEvalMetrics.add(NemaDataConstants.QBSHT_QUERY_RAW_COUNT);
	}
    

    
    /**
     * Perform the evaluation and block until the results are fully written to the output directory.
     * Also return a map encoding the evaluation results for each job in case they are needed for further processing.
     * 
     * @return a map encoding the evaluation results for each job and other data. 
     * @throws IllegalArgumentException Thrown if required metadata is not found, either in the ground-truth
     * data or in one of the system's results.
     */
    public NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException{
    	int numJobs = jobIDToFoldResults.size();
        String jobId, jobName;
        
        //check that all systems have the same number of results
        checkFolds();

        /* prepare NemaEvaluationResultSet*/
		NemaEvaluationResultSet results = getEmptyEvaluationResultSet();

		Map<NemaTrackList,List<NemaData>> sysResults;
		
		//evaluate each fold for each system
		Map<String, Map<NemaTrackList,NemaData>> jobIdToFoldEvaluation = new HashMap<String, Map<NemaTrackList,NemaData>>(numJobs);
		for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
			jobId = it.next();
			getLogger().info("Evaluating experiment folds for jobID: " + jobId);
			sysResults = jobIDToFoldResults.get(jobId);
			Map<NemaTrackList,NemaData> foldEvals = new HashMap<NemaTrackList,NemaData>(testSets.size());
			for (Iterator<NemaTrackList> trackIt = sysResults.keySet().iterator(); trackIt.hasNext();) {
				//make sure we use the evaluators copy of the track list
				NemaTrackList trackList = testSets.get(testSets.indexOf(trackIt.next()));
				NemaData result = evaluateResultFold(jobId, trackList, sysResults.get(trackList));
				foldEvals.put(trackList, result);
			}

			jobIdToFoldEvaluation.put(jobId, foldEvals);
		}
		
		int totalResults = trackIDToGT.size();
		/* Aggregated evaluation to produce overall results */
		Map<String, NemaData> jobIdToOverallEvaluation = new HashMap<String, NemaData>(numJobs);
		for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();) {
			jobId = it.next();
			getLogger().info("Aggregating results for jobID: " + jobId);
			Map<NemaTrackList,NemaData> foldEvals = jobIdToFoldEvaluation.get(jobId);
			NemaData overall = averageFoldMetrics(jobId, foldEvals.values(), totalResults);
			jobIdToOverallEvaluation.put(jobId, overall);
		}
		
		/* Populate NemaEvaluationResultSet */
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
			jobId = it.next();
			jobName = jobIDToName.get(jobId);
			results.addCompleteResultSet(jobId, jobName, jobIdToOverallEvaluation.get(jobId), jobIdToFoldEvaluation.get(jobId), jobIDToFoldResults.get(jobId));
		}
        
        return results;
    }
    
	protected NemaData averageFoldMetrics(String jobId, Collection<NemaData> perFoldEvaluations, int totalResults){
		NemaData[] foldData = perFoldEvaluations.toArray(new NemaData[perFoldEvaluations.size()]);
		NemaData overall = new NemaData(jobId);
		for (Iterator<String> metricIt = foldEvalMetrics.iterator(); metricIt.hasNext();) {
			String metric = metricIt.next();
			double accum = 0.0;
			
			for (int i = 0; i < foldData.length; i++) {
				int numFoldResults = foldData[i].getIntMetadata(NemaDataConstants.QBSHT_NUM_FOLD_RESULTS);
				double foldWeight = (double)numFoldResults/(double)totalResults;
				double foldScoreAvg = foldData[i].getDoubleMetadata(metric);

				accum += (foldScoreAvg * foldWeight);
				
				
				//System.out.println(jobId + "," + metric + "," + foldData[i].getId() + "," +
				// foldData[i].getDoubleMetadata(metric) + "," + numFoldResults + "," + foldData.length);
			}
			overall.setMetadata(metric, accum);
		}
		return overall;
	}
  

	public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> results) 
    {
    	int numResults = results.size(); 
    	
        NemaData outObj = new NemaData(jobID);
      
        double totalRawCount = 0;
        for (NemaData data: results)
        {
        	String queryId = data.getId();
        	NemaData gtData = trackIDToGT.get(queryId);
        	
        	String relevant = gtData.getStringMetadata(NemaDataConstants.QBSHT_DATA);
        	String[] top10 = data.getStringArrayMetadata(NemaDataConstants.QBSHT_DATA);
        	

        	double rawCount = scoreRawCount(top10, relevant);
        	
        	totalRawCount += rawCount;
        	
        	data.setMetadata(NemaDataConstants.QBSHT_QUERY_RAW_COUNT, rawCount);
        }
        
        double avgRawCount = totalRawCount/(double)numResults;
        outObj.setMetadata(NemaDataConstants.QBSHT_QUERY_RAW_COUNT, avgRawCount);
        outObj.setMetadata(NemaDataConstants.QBSHT_NUM_FOLD_RESULTS, numResults);
        
        return outObj;
    }
    
    
    private double scoreRawCount (String[] results, String relevant)
    {
    	int count = 0;
    	String relLast = relevant.substring(relevant.length()-3);
    	for (int pos=0; pos < results.length; pos++)
    	{
    		String res = results[pos];
    		String resLast = res.substring(res.length()-3);
    		if (resLast.equals(relLast)) {
    			count++;
    		}
    	}
    	return count;
    }

}
