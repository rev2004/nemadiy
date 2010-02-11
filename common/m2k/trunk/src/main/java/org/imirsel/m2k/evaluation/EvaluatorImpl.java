/**
 * 
 */
package org.imirsel.m2k.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.m2k.evaluation.tagsClassification.TagClassificationAffinityEvaluator;
import org.imirsel.m2k.evaluation.tagsClassification.TagClassificationBinaryEvaluator;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.util.noMetadataException;

/**
 * @author kriswest
 *
 */
public abstract class EvaluatorImpl implements Evaluator {

	protected Logger _logger;
	protected File workingDir;
	protected File outputDir;
	protected Map<String,DataObj> trackIDToGT;
	protected Map<String,List<List<DataObj>>> jobIDToFoldResults;
	protected Map<String,String> jobIDToName;
	protected TaskDescription task;
	
	
	public EvaluatorImpl(Class loggingClass, File workingDir_, File outputDir_)  throws FileNotFoundException{
		setLogger(Logger.getLogger(loggingClass.getName()));
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		trackIDToGT = new HashMap<String,DataObj>();
		jobIDToFoldResults = new HashMap<String,List<List<DataObj>>>();
		jobIDToName = new HashMap<String,String>();
		task = new TaskDescription();
	}
	
	public EvaluatorImpl(Logger logger, File workingDir_, File outputDir_)  throws FileNotFoundException{
		setLogger(logger);
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		trackIDToGT = new HashMap<String,DataObj>();
		jobIDToFoldResults = new HashMap<String,List<List<DataObj>>>();
		jobIDToName = new HashMap<String,String>();
		task = new TaskDescription();
	}
	
	public EvaluatorImpl(Class loggingClass, File workingDir_, File outputDir_, 
			TaskDescription task_)  throws FileNotFoundException{
		setLogger(Logger.getLogger(loggingClass.getName()));
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		trackIDToGT = new HashMap<String,DataObj>();
		jobIDToFoldResults = new HashMap<String,List<List<DataObj>>>();
		jobIDToName = new HashMap<String,String>();
		task = task_;
	}
	
	public EvaluatorImpl(Logger logger, File workingDir_, File outputDir_, 
			TaskDescription task_)  throws FileNotFoundException{
		setLogger(logger);
		setWorkingDir(workingDir_);
		setOutputDir(outputDir_);
		trackIDToGT = new HashMap<String,DataObj>();
		jobIDToFoldResults = new HashMap<String,List<List<DataObj>>>();
		jobIDToName = new HashMap<String,String>();
		task = task_;
	}
	
	public Logger getLogger() {
		return _logger;
	}

	public void setLogger(Logger logger) {
		_logger = logger;
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
	
	public void addResults(String systemName, String jobID, List<DataObj> results) {
		jobIDToName.put(jobID, systemName);
		List<List<DataObj>> resultList = jobIDToFoldResults.get(jobID);
		if (resultList == null){
			resultList = new ArrayList<List<DataObj>>(5);
			jobIDToFoldResults.put(jobID, resultList);
		}
		resultList.add(results);
	}
	

	public void setGroundTruth(List<DataObj> groundtruth) {
		DataObj data;
		for(Iterator<DataObj> it = groundtruth.iterator(); it.hasNext();){
			data = it.next();
			trackIDToGT.put(data.getStringMetadata(DataObj.PROP_FILE_LOCATION), data);
		}		
		_logger.info("Received groundtruth for " + groundtruth.size() + " track IDs");
	}
	
	public DataObj getGroundTruth(String trackID){
		return trackIDToGT.get(trackID);
	}

	public void setTask(TaskDescription task) {
		this.task = task;
	}
	
	public void setTask(int taskID, String taskName, String taskDescription, String metadataPredicted, int datasetId, String datasetName, String datasetDescription){
		this.task = new TaskDescription(taskID, taskName, taskDescription, metadataPredicted, datasetId, datasetName, datasetDescription);
	}
	
	public TaskDescription getTask() {
		return task;
	}

	public abstract Map<String,DataObj> evaluate() throws IllegalArgumentException, IOException;

}
