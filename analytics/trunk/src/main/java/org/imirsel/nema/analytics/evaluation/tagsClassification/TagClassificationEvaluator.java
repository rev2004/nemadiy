/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.tagsClassification;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.model.*;

/**
 * Tag Classification evaluation.
 * 
 * @author kris.west@gmail.com
 * @since 0.4.0
 */
public class TagClassificationEvaluator extends EvaluatorImpl{


	private Set<String> tags = null;
	
    /**
	 * Constructor (no arg - task, dataset, output and working dirs, training
	 * and test sets must be set manually).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public TagClassificationEvaluator() {
		super();
	}
  
	
    protected void setupEvalMetrics() {
    	
		this.trackEvalMetrics.clear();
		//no per track metrics... 
		
		this.foldEvalMetrics.clear();
		this.foldEvalMetrics.add(NemaDataConstants.TAG_ACCURACY_TAG_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_POS_ACCURACY_TAG_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_NEG_ACCURACY_TAG_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_PRECISION_TAG_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_RECALL_TAG_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_FMEASURE_TAG_MAP);
		
		this.foldEvalMetrics.add(NemaDataConstants.TAG_ACCURACY_TRACK_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_POS_ACCURACY_TRACK_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_NEG_ACCURACY_TRACK_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_PRECISION_TRACK_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_RECALL_TRACK_MAP);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_FMEASURE_TRACK_MAP);
		
		this.foldEvalMetrics.add(NemaDataConstants.TAG_ACCURACY_AVERAGE);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_POS_ACCURACY_AVERAGE);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_NEG_ACCURACY_AVERAGE);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_PRECISION_AVERAGE);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_RECALL_AVERAGE);
		this.foldEvalMetrics.add(NemaDataConstants.TAG_FMEASURE_AVERAGE);
		
		//same as fold metrics
		this.overallEvalMetrics = this.foldEvalMetrics;
	}
    
    @SuppressWarnings("unchecked")
	private Set<String> getAllTags(){
    	HashSet<String> tags = new HashSet<String>();
    	for (NemaData data:this.getGroundTruth()){
    		tags.addAll((Set<String>)data.getMetadata(NemaDataConstants.TAG_CLASSIFICATIONS));
    	}
    	return tags;
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
        
        this.tags = getAllTags();
        
		/* prepare NemaEvaluationResultSet*/
		NemaEvaluationResultSet results = getEmptyEvaluationResultSet();

		{
			/* Perform the evaluations on all jobIds (systems) */
			Map<NemaTrackList,List<NemaData>> sysResults;
			Map<String, Map<NemaTrackList,NemaData>> jobIdToFoldEvaluation = new HashMap<String, Map<NemaTrackList,NemaData>>(numJobs);
			for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				getLogger().info("Evaluating experiment for jobID: " + jobId);
				sysResults = jobIDToFoldResults.get(jobId);
				Map<NemaTrackList,NemaData> foldEvals = new HashMap<NemaTrackList,NemaData>(testSets.size());
				for (Iterator<NemaTrackList> trackIt = sysResults.keySet().iterator(); trackIt.hasNext();) {
					NemaTrackList trackList = testSets.get(testSets.indexOf(trackIt.next()));
					getLogger().fine("Evaluating fold " + trackList.getFoldNumber() + ", set " + trackList.getId() + "...");
					NemaData result = evaluateResultFold(jobId, trackList, sysResults.get(trackList));
					foldEvals.put(trackList, result);
				}
				jobIdToFoldEvaluation.put(jobId, foldEvals);
			}
			
			/* Aggregated evaluation to produce overall results */
			Map<String, NemaData> jobIdToOverallEvaluation = new HashMap<String, NemaData>(numJobs);
			for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				getLogger().info("Aggregating results for jobID: " + jobId);
				Map<NemaTrackList,NemaData> foldEvals = jobIdToFoldEvaluation.get(jobId);
				// note we are using an overridden version of averageFoldMetrics as the confusion matrices have to be averaged for classification
				NemaData overall = averageFoldMetrics(jobId, foldEvals.values());
				jobIdToOverallEvaluation.put(jobId, overall);
			}
			
			/* Populate NemaEvaluationResultSet */
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				jobName = jobIDToName.get(jobId);
				results.addCompleteResultSet(jobId, jobName, jobIdToOverallEvaluation.get(jobId), jobIdToFoldEvaluation.get(jobId), jobIDToFoldResults.get(jobId));
			}
		}	
        
        return results;
    }
    

    @SuppressWarnings("unchecked")
	private void averageFoldMaps(String metric, Collection<NemaData> foldEvals, NemaData aggregateEval){
    	HashMap<String,Double> out = new HashMap<String, Double>();
    	int numFolds = foldEvals.size();
    	for(NemaData fold:foldEvals){
    		Map<String,Double> foldData = (Map<String,Double>)fold.getMetadata(metric);
    		for(String key:foldData.keySet()){
    			double val = foldData.get(key)/numFolds;
    			if(out.containsKey(key)){
    				out.put(key, out.get(key) + val);
    			}else{
    				out.put(key, val);
    			}
    		}
    	}
    	
    	aggregateEval.setMetadata(metric, out);
    }
    
	private void averageFoldVals(String metric, Collection<NemaData> foldEvals, NemaData aggregateEval){
    	double out = 0;
    	int numFolds = foldEvals.size();
    	for(NemaData fold:foldEvals){
    		out += fold.getDoubleMetadata(metric);
    	}
    	out /= numFolds;
    	
    	aggregateEval.setMetadata(metric, out);
    }

	@Override
	protected NemaData averageFoldMetrics(String jobId, Collection<NemaData> perFoldEvaluations) {
		int numFolds = this.testSets.size();
		NemaData aggregateEval = new NemaData(jobId);

		if(perFoldEvaluations.size() != numFolds){
			throw new IllegalArgumentException("Job ID " + jobId + 
					" returned " + perFoldEvaluations.size() + " folds, expected " + numFolds);
		}
		
		averageFoldMaps(NemaDataConstants.TAG_ACCURACY_TAG_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_POS_ACCURACY_TAG_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_NEG_ACCURACY_TAG_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_PRECISION_TAG_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_RECALL_TAG_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_FMEASURE_TAG_MAP, perFoldEvaluations,aggregateEval);
		
		averageFoldMaps(NemaDataConstants.TAG_ACCURACY_TRACK_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_POS_ACCURACY_TRACK_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_NEG_ACCURACY_TRACK_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_PRECISION_TRACK_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_RECALL_TRACK_MAP, perFoldEvaluations,aggregateEval);
		averageFoldMaps(NemaDataConstants.TAG_FMEASURE_TRACK_MAP, perFoldEvaluations,aggregateEval);
		
		averageFoldVals(NemaDataConstants.TAG_ACCURACY_AVERAGE, perFoldEvaluations,aggregateEval);
		averageFoldVals(NemaDataConstants.TAG_POS_ACCURACY_AVERAGE, perFoldEvaluations,aggregateEval);
		averageFoldVals(NemaDataConstants.TAG_NEG_ACCURACY_AVERAGE, perFoldEvaluations,aggregateEval);
		averageFoldVals(NemaDataConstants.TAG_PRECISION_AVERAGE, perFoldEvaluations,aggregateEval);
		averageFoldVals(NemaDataConstants.TAG_RECALL_AVERAGE, perFoldEvaluations,aggregateEval);
		averageFoldVals(NemaDataConstants.TAG_FMEASURE_AVERAGE, perFoldEvaluations,aggregateEval);
		
        //Store tag names
		//aggregateEval.setMetadata(NemaDataConstants.TAG_EXPERIMENT_CLASSNAMES, classNames);
		
		return aggregateEval;
	}

	private void incrementTagCount(HashMap<String, AtomicInteger> map, String tag) {
        if (!map.containsKey(tag)) {
            map.put(tag, new AtomicInteger(1));
        } else {
            map.get(tag).incrementAndGet();
        }
    }
    
    @SuppressWarnings("unchecked")
	public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> theData) {

    	//count the number of examples returned and search for any missing tracks in the results returned for the fold
    	int numExamples = checkFoldResultsAreComplete(jobID, testSet, theData);
    	
        NemaData outObj = new NemaData(jobID);
        
        NemaData data;
        NemaData gtData;
        
        Set<String> trueSet;
        Set<String> returnedSet;
        Set<String> remainder;
        
        HashMap<String, AtomicInteger> tag2truePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falsePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> tag2falseNegative = new HashMap<String, AtomicInteger>();
        HashMap<String, Integer> tag2numPositiveExamples = new HashMap<String, Integer>();
        HashMap<String, Integer> tag2numNegativeExamples = new HashMap<String, Integer>();
        
        HashMap<String, AtomicInteger> track2truePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> track2falsePositive = new HashMap<String, AtomicInteger>();
        HashMap<String, AtomicInteger> track2falseNegative = new HashMap<String, AtomicInteger>();
        HashMap<String, Integer> track2numPositiveExamples = new HashMap<String, Integer>();
        HashMap<String, Integer> track2numNegativeExamples = new HashMap<String, Integer>();
        
        int totalTruePositive = 0;
        int totalFalsePositive = 0;
        int totalFalseNegative = 0;
        int totalTrueNegative = 0;

        //result objects
        HashMap<String, Double> tag2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2PosAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2NegAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> tag2Precision = new HashMap<String, Double>();
        HashMap<String, Double> tag2Recall = new HashMap<String, Double>();
        HashMap<String, Double> tag2FMeasure = new HashMap<String, Double>();
        
        HashMap<String, Double> track2Accuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2PosAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2NegAccuracy = new HashMap<String, Double>();
        HashMap<String, Double> track2Precision = new HashMap<String, Double>();
        HashMap<String, Double> track2Recall = new HashMap<String, Double>();
        HashMap<String, Double> track2FMeasure = new HashMap<String, Double>();
        
        double totalAccuracy;
        double totalPosAccuracy;
        double totalNegAccuracy;
        double totalPrecision;
        double totalRecall;
        double totalFmeasure;
        
        String tag, id;
        
        getLogger().fine("Computing per track evaluations...");
        for(int x=0; x < theData.size(); x++) {
            //Do simple evaluation
        	data = theData.get(x);
        	id = data.getId();
        	gtData = trackIDToGT.get(id);
            
        	
        	returnedSet = (Set<String>)data.getMetadata(NemaDataConstants.TAG_CLASSIFICATIONS);
        	trueSet = (Set<String>)gtData.getMetadata(NemaDataConstants.TAG_CLASSIFICATIONS);
        	
        	for (Iterator<String> it = returnedSet.iterator(); it.hasNext();) {
                tag = it.next();
                if (trueSet.contains(tag)) {
                    totalTruePositive++;
                    incrementTagCount(tag2truePositive, tag);
                    incrementTagCount(track2truePositive, id);
                } else {
                    totalFalsePositive++;
                    incrementTagCount(tag2falsePositive, tag);
                    incrementTagCount(track2falsePositive, id);
                }
            }
            remainder = new HashSet<String>(trueSet);
            remainder.removeAll(returnedSet);
            for (Iterator<String> it = remainder.iterator(); it.hasNext();) {
                tag = it.next();
                totalFalseNegative++;
                incrementTagCount(tag2falseNegative, tag);
                incrementTagCount(track2falseNegative, id);
            }
        }
        
      //compute per track stats
        getLogger().fine("Computing per track statistics...");
        for (Iterator<NemaData> it = theData.iterator(); it.hasNext();) {
            id = it.next().getId();
            //fill in any gaps in maps
            if (!track2truePositive.containsKey(id)) {
                track2truePositive.put(id, new AtomicInteger(0));
            }
            if (!track2falsePositive.containsKey(id)) {
                track2falsePositive.put(id, new AtomicInteger(0));
            }
            if (!track2falseNegative.containsKey(id)) {
                track2falseNegative.put(id, new AtomicInteger(0));
            }
            int tp = track2truePositive.get(id).intValue();
            int fp = track2falsePositive.get(id).intValue();
            int fn = track2falseNegative.get(id).intValue();
            int tn = tags.size() - (tp + fp + fn);
            
            double accuracy = (double) (tp + tn) / (double) tags.size();
            double posAccuracy = (double) tp / (double)(tp+fn);
            double negAccuracy = (double) tn / (double)(fp+tn);
            
            double precision = (double) tp / (double) (tp + fp);
            if ((tp + fp)==0){
                precision = 0.0;
            }
            double recall = (double) tp / (double) (tp + fn);
            if ((tp + fn)==0){
                recall = 0.0;
            }
            double fMeasure = (2 * recall * precision) / (recall + precision);
            if ((precision == 0.0)||(recall == 0)){
                fMeasure = 0.0;
            }
            track2Accuracy.put(id, accuracy);
            track2PosAccuracy.put(id, posAccuracy);
            track2NegAccuracy.put(id, negAccuracy);
            track2Precision.put(id, precision);
            track2Recall.put(id, recall);
            track2FMeasure.put(id, fMeasure);
            
            int numPositivesExamples = tp + fn;
            int numNegativeExamples = tn + fp;
            track2numPositiveExamples.put(id,numPositivesExamples);
            track2numNegativeExamples.put(id, numNegativeExamples);
        }
        
        
      //compute per tag stats
        getLogger().fine("Computing per tag statistics...");
        for (Iterator<String> it = tags.iterator(); it.hasNext();) {
            tag = it.next();

            if(!tag2truePositive.containsKey(tag) && !tag2falseNegative.containsKey(tag)){
                //Don't fill in any gaps in maps and don't evaluate
                
            }else{
                //fill in any gaps in maps
                if (!tag2truePositive.containsKey(tag)) {
                    tag2truePositive.put(tag, new AtomicInteger(0));
                }
                if (!tag2falsePositive.containsKey(tag)) {
                    tag2falsePositive.put(tag, new AtomicInteger(0));
                }
                if (!tag2falseNegative.containsKey(tag)) {
                    tag2falseNegative.put(tag, new AtomicInteger(0));
                }


                int tp = tag2truePositive.get(tag).intValue();
                int fp = tag2falsePositive.get(tag).intValue();
                int fn = tag2falseNegative.get(tag).intValue();
                int tn = numExamples - (tp + fp + fn);

                totalTrueNegative += tn;

                double accuracy = (double) (tp + tn) / (double) numExamples;

                double posAccuracy = (double) tp / (double)(tp+fn);
                double negAccuracy = (double) tn / (double)(fp+tn);

                double precision = (double) tp / (double) (tp + fp);
                if ((tp + fp)==0){
                    precision = 0.0;
                }
                double recall = (double) tp / (double) (tp + fn);
                if ((tp + fn)==0){
                    recall = 0.0;
                }
                double fMeasure = (2 * recall * precision) / (recall + precision);
                if ((precision == 0.0)||(recall == 0)){
                    fMeasure = 0.0;
                }
                tag2Accuracy.put(tag, accuracy);
                tag2PosAccuracy.put(tag, posAccuracy);
                tag2NegAccuracy.put(tag, negAccuracy);
                tag2Precision.put(tag, precision);
                tag2Recall.put(tag, recall);
                tag2FMeasure.put(tag, fMeasure);

                int numPositivesExamples = tp + fn;
                int numNegativeExamples = tn + fp;
                tag2numPositiveExamples.put(tag,numPositivesExamples);
                tag2numNegativeExamples.put(tag, numNegativeExamples);
            }
        }
        
        //compute total stats
        getLogger().fine("Computing per overall statistics...");
        totalAccuracy = (double) (totalTruePositive + totalTrueNegative) / (double) (totalTruePositive + totalFalsePositive + totalFalseNegative + totalTrueNegative);
        totalPosAccuracy = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalNegAccuracy = (double) totalTrueNegative / (double) (totalFalsePositive + totalTrueNegative);
        totalPrecision = (double) totalTruePositive / (double) (totalTruePositive + totalFalsePositive);
        totalRecall = (double) totalTruePositive / (double) (totalTruePositive + totalFalseNegative);
        totalFmeasure = (2 * totalRecall * totalPrecision) / (totalRecall + totalPrecision);

        outObj.setMetadata(NemaDataConstants.TAG_ACCURACY_TAG_MAP, tag2Accuracy);
        outObj.setMetadata(NemaDataConstants.TAG_POS_ACCURACY_TAG_MAP, tag2PosAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_NEG_ACCURACY_TAG_MAP, tag2NegAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_PRECISION_TAG_MAP, tag2Precision);
        outObj.setMetadata(NemaDataConstants.TAG_RECALL_TAG_MAP, tag2Recall);
        outObj.setMetadata(NemaDataConstants.TAG_FMEASURE_TAG_MAP, tag2FMeasure);
        
        outObj.setMetadata(NemaDataConstants.TAG_ACCURACY_TRACK_MAP, track2Accuracy);
        outObj.setMetadata(NemaDataConstants.TAG_POS_ACCURACY_TRACK_MAP, track2PosAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_NEG_ACCURACY_TRACK_MAP, track2NegAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_PRECISION_TRACK_MAP, track2Precision);
        outObj.setMetadata(NemaDataConstants.TAG_RECALL_TRACK_MAP, track2Recall);
        outObj.setMetadata(NemaDataConstants.TAG_FMEASURE_TRACK_MAP, track2FMeasure);
        
        outObj.setMetadata(NemaDataConstants.TAG_ACCURACY_AVERAGE, totalAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_POS_ACCURACY_AVERAGE, totalPosAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_NEG_ACCURACY_AVERAGE, totalNegAccuracy);
        outObj.setMetadata(NemaDataConstants.TAG_PRECISION_AVERAGE, totalPrecision);
        outObj.setMetadata(NemaDataConstants.TAG_RECALL_AVERAGE, totalRecall);
        outObj.setMetadata(NemaDataConstants.TAG_FMEASURE_AVERAGE, totalFmeasure);
        
        outObj.setMetadata(NemaDataConstants.TAG_NUM_POSITIVE_EXAMPLES_MAP, tag2numPositiveExamples);
        outObj.setMetadata(NemaDataConstants.TAG_NUM_NEGATIVE_EXAMPLES_MAP, tag2numNegativeExamples);
        
        return outObj;
    }

}
