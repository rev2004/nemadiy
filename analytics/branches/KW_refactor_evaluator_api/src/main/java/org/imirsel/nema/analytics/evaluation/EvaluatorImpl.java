/**
 * 
 */
package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.imirsel.nema.analytics.logging.AnalyticsLogFormatter;
import org.imirsel.nema.analytics.logging.ProcessExecutorLogFormatter;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;

/**
 * @author kris.west@gmail.com
 *
 */
public abstract class EvaluatorImpl implements Evaluator {

	protected Logger _logger;
	protected File workingDir;
	protected File outputDir;
	protected Map<String,NemaData> trackIDToGT;
	protected Map<String,Map<NemaTrackList,List<NemaData>>> jobIDToFoldResults;
	protected Map<String,String> jobIDToName;
	protected NemaTask task;
	protected NemaDataset dataset;
	protected List<NemaTrackList> trainingSets;
	protected List<NemaTrackList> testSets;
	protected List<String> overallEvalMetrics;
	protected List<String> foldEvalMetrics;
	protected List<String> trackEvalMetricsAndResults;
	
	
	public EvaluatorImpl(File workingDir_, File outputDir_)  throws FileNotFoundException{
		_logger = Logger.getLogger(this.getClass().getName());
		
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		
		trackIDToGT = new HashMap<String,NemaData>();
		jobIDToFoldResults = new HashMap<String,Map<NemaTrackList,List<NemaData>>>();
		jobIDToName = new HashMap<String,String>();
		task = null;
		dataset = null;
		trainingSets = null;
		testSets = null;
		overallEvalMetrics = new ArrayList<String>();
		foldEvalMetrics = new ArrayList<String>();
		trackEvalMetricsAndResults = new ArrayList<String>();
	}
	
	public EvaluatorImpl(File workingDir_, File outputDir_, 
			NemaTask task_, NemaDataset dataset_,
			List<NemaTrackList> testSets_)  throws FileNotFoundException{
		_logger = Logger.getLogger(this.getClass().getName());
		
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		
		trackIDToGT = new HashMap<String,NemaData>();
		jobIDToFoldResults = new HashMap<String,Map<NemaTrackList,List<NemaData>>>();
		jobIDToName = new HashMap<String,String>();
		task = task_;
		dataset = dataset_;
		trainingSets = null;
		testSets = testSets_;
		overallEvalMetrics = new ArrayList<String>();
		foldEvalMetrics = new ArrayList<String>();
		trackEvalMetricsAndResults = new ArrayList<String>();
	}
	
	public EvaluatorImpl(File workingDir_, File outputDir_, 
			NemaTask task_, NemaDataset dataset_, List<NemaTrackList> trainingSets_,
			List<NemaTrackList> testSets_)  throws FileNotFoundException{
		_logger = Logger.getLogger(this.getClass().getName());
		
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		
		trackIDToGT = new HashMap<String,NemaData>();
		jobIDToFoldResults = new HashMap<String,Map<NemaTrackList,List<NemaData>>>();
		jobIDToName = new HashMap<String,String>();
		task = task_;
		dataset = dataset_;
		trainingSets = trainingSets_;
		testSets = testSets_;
		overallEvalMetrics = new ArrayList<String>();
		foldEvalMetrics = new ArrayList<String>();
		trackEvalMetricsAndResults = new ArrayList<String>();
	}
	
    public void setTrainingSets(List<NemaTrackList> trainingSets){
    	this.trainingSets = trainingSets;
    }
    
    public void setTestSets(List<NemaTrackList> testSets){
    	this.testSets = testSets;
    }
    
    public NemaEvaluationResultSet getEmptyEvaluationResultSet(){
    	return new NemaEvaluationResultSet(dataset, task, trainingSets, trainingSets, getOverallEvalMetricsKeys(), getFoldEvalMetricsKeys(), getTrackEvalMetricsAndResultsKeys());
    }
	
	public List<String> getOverallEvalMetricsKeys() {
		return overallEvalMetrics;
	}

	public List<String> getFoldEvalMetricsKeys() {
		return foldEvalMetrics;
	}

	public List<String> getTrackEvalMetricsAndResultsKeys() {
		return trackEvalMetricsAndResults;
	}

	/**
	 * Performs simple averaging of the per-fold evaluation metrics to produce an
	 * overall evaluation.
	 * @param jobId The jobId related to the results.
	 * @param perFoldEvaluations The collection of fold evaluations to average.
	 * @return A NemaData Object representing the averaged, overall evaluation.
	 */
	protected NemaData averageFoldMetrics(String jobId, Collection<NemaData> perFoldEvaluations){
		NemaData[] foldData = perFoldEvaluations.toArray(new NemaData[perFoldEvaluations.size()]);
		NemaData overall = new NemaData(jobId);
		for (Iterator<String> metricIt = foldEvalMetrics.iterator(); metricIt.hasNext();) {
			String metric = metricIt.next();
			double accum = 0.0;
			for (int i = 0; i < foldData.length; i++) {
				accum += foldData[i].getDoubleMetadata(metric);
			}
			overall.setMetadata(metric, accum / foldData.length);
		}
		return overall;
	}
	
	/**
	 * Checks that the algorithm results contain only a single fold of results. This evaluation 
	 * task is not cross-fold validated
	 */
	protected void checkFolds() {
		String jobID;
		Map<NemaTrackList,List<NemaData>> sysResults;
		for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			sysResults = jobIDToFoldResults.get(jobID);
			if (!sysResults.keySet().containsAll(testSets)) {
				throw new IllegalArgumentException(
						"The folds detected for job Id '" + jobID + "' do not match the dataset!");
			}
		}
	}

	public Logger getLogger() {
		if (_logger == null){
			_logger = Logger.getLogger(this.getClass().getName());
		}
		return _logger;
	}

	public void addLogDestination(PrintStream stream) {
		Handler handler = new StreamHandler(stream, new AnalyticsLogFormatter());
		getLogger().addHandler(handler);
	}

	public void setOutputDir(File outputDir_) throws FileNotFoundException {
		outputDir = outputDir_;
		outputDir.mkdirs();
		if (!outputDir.exists()){
			throw new FileNotFoundException("Output directory " + outputDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}

	public void setWorkingDir(File workingDir_) throws FileNotFoundException {
		workingDir = workingDir_;
		workingDir.mkdirs();
		if (!workingDir.exists()){
			throw new FileNotFoundException("Working directory " + workingDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}
	
	public void addResults(String systemName, String jobID, NemaTrackList fold, List<NemaData> results) {
		jobIDToName.put(jobID, systemName);
		Map<NemaTrackList,List<NemaData>> resultList = jobIDToFoldResults.get(jobID);
		if (resultList == null){
			resultList = new HashMap<NemaTrackList,List<NemaData>>(testSets.size());
			jobIDToFoldResults.put(jobID, resultList);
		}
		resultList.put(testSets.get(testSets.indexOf(fold)),results);
	}
	

	public void setGroundTruth(List<NemaData> groundtruth) {
		NemaData data;
		for(Iterator<NemaData> it = groundtruth.iterator(); it.hasNext();){
			data = it.next();
			trackIDToGT.put(data.getId(), data);
		}		
		_logger.info("Received groundtruth for " + groundtruth.size() + " track IDs");
	}
	
	public NemaData getGroundTruth(String trackID){
		return trackIDToGT.get(trackID);
	}

	public void setTask(NemaTask task) {
		this.task = task;
	}
	
	public void setDataset(NemaDataset dataset) {
		this.dataset = dataset;
	}
	
	public NemaTask getTask() {
		return task;
	}
	
	public NemaDataset getDataset() {
		return dataset;
	}

	public abstract NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException;

}
