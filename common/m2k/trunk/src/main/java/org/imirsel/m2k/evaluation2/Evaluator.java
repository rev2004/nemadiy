/*
 * Evaluator.java
 *
 * Created on 23 October 2006, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.imirsel.m2k.util.noMetadataException;

/**
 * Interface defining the methods of a evaluation utility for an MIR task.
 * 
 * @author kriswest
 */
public interface Evaluator { 
    
	/**
	 * Set the working directory for the utility, to be used for creation of an temp files
	 * required.
	 * 
	 * @param workingDir File representing the path to the working directory.
	 * @throws FileNotFoundException Thrown if the specified path can't be found or created.
	 */
    public void setWorkingDir(File workingDir) throws FileNotFoundException;
    
    /**
     * Sets the output directory for the utility. Outputs may include encoded data files,
     * reports, an HTML mini-site providing an interface to the results etc.
     * 
     * @param outputDir File representing the output directory.
     * @throws FileNotFoundException Thrown if the specified path can't be found or created.
     */
    public void setOutputDir(File outputDir) throws FileNotFoundException;
    
    /**
     * Set task description so that the task name, description and dataset details
     * may be encoded in the results and used on any generated result pages. The subject 
     * metadata is also set and maybe required to perform evaluation (for example the
     * ClassificationEvaluator can only work if it knows the field being classified, e.g. 
     * genre or mood).
     * 
     * Under the NEMA service all this data will be made available by the task description. 
     * 
     * This method will create a new task description Object.
     * 
     * @param taskName The name of the task (e.g. MIREX2010 Genre classification).
     * @param taskDescription  A description of the task. E.g. classify tracks into a single 
     * genre per class.
     * @param metadataPredicted The metadata predicted, e.g. onset times, genre class labels.
     * @param datasetId The ID of the dataset used.
     * @param datasetName The name of the dataset used.
     * @param datasetDescription The dataset description.
     */
    public void setTask(int taskID, String taskName, String taskDescription, String metadataPredicted, int datasetId, String datasetName, String datasetDescription);
    
    /**
     * Set the task description so that the task name, description and dataset details
     * may be encoded in the results and used on any generated result pages. The subject 
     * metadata is also set and maybe required to perform evaluation (for example the
     * ClassificationEvaluator can only work if it knows the field being classified, e.g. 
     * genre or mood).
     * 
     * Under the NEMA service all this data will be made available by the task description. 
     * 
     * This method uses the TaskDescription Object passed and does not create a new one.
     * 
     * @param taskName The name of the task (e.g. MIREX2010 Genre classification).
     * @param taskDescription  A description of the task. E.g. classify tracks into a single 
     * genre per class.
     * @param metadataPredicted The metadata predicted, e.g. onset times, genre class labels.
     * @param datasetId The ID of the dataset used.
     * @param datasetName The name of the dataset used.
     * @param datasetDescription The dataset description.
     */
    public void setTask(TaskDescription task);
    
    /**
     * Sets the <code>List</code> of groundtruth data to be used to evaluate results. In a 
     * multi-iteration experiment this ground-truth should provide a superset of ground-truth 
     * data to cover all iterations.
     * 
     * The <code>DataObj</code> may be handled internally as desired, including
     * the retention and use of the order in which they were supplied (brittle) or indexed in
     * a one or more map structures (e.g. trackID -> GT data).
     * 
     * Where there are multiple groundtruths for a track these should be combined into a single 
     * <code>DataObj</code> with multiple values for the specified field. E.g.
     * there might be 4 different annotations of the onset times from different users.
     *  
     * @param groundtruth A list of groundtruth Objects to use in evaluation.
     */
    public void setGroundTruth(List<DataObj> groundtruth);

    /**
     * Adds a set of results to the evaluation. For a single iteration experiment add a single
     * result per jobID. For a multi-iteration experiment add results per iteration (in order) for 
     * each system (it doesn't matter if results from different systems are shuffled together but 
     * the results for each system must be added in order).
     * 
     * @param systemName The name of the system (for printing on result output).
     * @param jobID Primary key for systems, can be a unique system name or URI for flow/config of
     * job that produced results. Used in encoded results.
     * @param results A <code>List</code> of <code>EvaluationDataObjects</code>
     */
    public void addResults(String systemName, String jobID, List<DataObj> results);

    /**
     * Set the logger to use for console output. If not set the default logger should be used, i.e.
     * the fully qualified name of the implementing class given by <code>this.getClass().getName()</code>.
     * @param logger The logger to use for output.
     */
    public void setLogger(Logger logger);
    
    /**
     * Returns the logger in use. Can be used to change the logging verbosity 
     * level with:
     * getLogger.setLevel(Level.WARNING).
     * @return the Logger that will be used for console output.
     */
    public Logger getLogger();
    
    /**
     * Perform the evaluation and block until the results are fully written to the output directory.
     * Also return a map encoding the evaluation results for each job in case they are needed for further processing.
     * 
     * @return a map encoding the evaluation results for each job and other data. 
     * @throws IllegalArgumentException Thrown if required metadata is not found, either in the ground-truth
     * data or in one of the system's results.
     */
    public Map<String,DataObj> evaluate() throws IllegalArgumentException, IOException;
    
}
