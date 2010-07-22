/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.components.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.EvaluatorFactory;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;


/** Evaluation component for single result sets (i.e. a number of folds of 
 * results from a single algorithm).
 *
 * @author kris.west@gmail.com
 */
@Component(creator="Kris West", description="Evaluates multi-fold results from multiple systems", 
		name="EvaluateMultipleResultSets",
		tags="evaluation",
		dependency={"commons-compress-1.0.jar","jfreechart-1.0.9.jar","jcommon-1.0.12.jar"})
		public class EvaluateMultipleResultSets extends NemaComponent {

	//INPUTS
	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	public final static String DATA_INPUT_NEMATASK = "NemaTask";

	@ComponentInput(description = "NemaDataset Object defining the task.", name = "NemaDataset")
	public final static String DATA_INPUT_DATASET = "NemaDataset";

	@ComponentInput(description = "List of NemaData Objects defining the ground-truth list.", name = "GroundTruthList")
	public final static String DATA_INPUT_GROUNDTRUTH_LIST = "GroundTruthList";
//
//	@ComponentInput(description = "Map of NemaTrackList to List of NemaData Objects defining each training set (with annotated ground-truth).", name = "TrainSets")
//	public final static String DATA_INPUT_TRAIN_SETS = "TrainSets";

	@ComponentInput(description = "Map of String jobID to Map of NemaTrackList to List of NemaData Objects defining each test set (with predictions from the algorithm).", name = "TestSets")
	public final static String DATA_INPUT_TEST_SETS_MAP = "TestSets";

	@ComponentInput(description = "Map of String jobID to the submission details of the systems being evaluated (used in rendering results). ", name = "SubmissionDetails")
	public final static String DATA_INPUT_SUBMISSION_DETAILS = "SubmissionDetails";
	
	//OUTPUTS
	@ComponentOutput(description="Evaluation results model encoding all the data evaluated and the evaluation metrics, suitable for use with the RenderResultSet component.", name="Evaluation results")
	final static String DATA_OUTPUT_EVAL_RESULTS= "Evaluation results";


	
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 * @throws ComponentExecutionException 
	 */
	@Override
	public void initialize (ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException{
		super.initialize(ccp);
	}

	/** 
	 *
	 * @throws ComponentExecutionException If a fatal condition arises during
	 *         the execution of a component, a ComponentExecutionException
	 *         should be thrown to signal termination of execution required.
	 * @throws ComponentContextException A violation of the component context
	 *         access was detected

	 */
	@Override
	@SuppressWarnings("unchecked")
	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
		NemaTask task = (NemaTask)cc.getDataComponentFromInput(DATA_INPUT_NEMATASK);
		NemaDataset dataset = (NemaDataset)cc.getDataComponentFromInput(DATA_INPUT_DATASET);
		List<NemaData> gtList = (List<NemaData>)cc.getDataComponentFromInput(DATA_INPUT_GROUNDTRUTH_LIST);
//		Map<NemaTrackList,List<NemaData>> trainSets = (Map<NemaTrackList,List<NemaData>>)cc.getDataComponentFromInput(DATA_INPUT_TRAIN_SETS);
		Map<String,Map<NemaTrackList,List<NemaData>>> jobIdToTestSets = (Map<String,Map<NemaTrackList,List<NemaData>>>)cc.getDataComponentFromInput(DATA_INPUT_TEST_SETS_MAP);
		
		Map<String,NemaSubmission> systemIDToSubmissionDetails =  (Map<String,NemaSubmission>)cc.getDataComponentFromInput(DATA_INPUT_SUBMISSION_DETAILS);
		
		try{
			
	        //init evaluator
		    List<NemaTrackList> trainSetList = null;
		    List<NemaTrackList> testSetList = null;
		    
		    {
			    Comparator<NemaTrackList> trackListComp = new Comparator<NemaTrackList>() {
			    	public int compare(NemaTrackList a, NemaTrackList b){
			    		return a.getFoldNumber() - b.getFoldNumber();
			    	}
				};
//			    if(trainSets != null){
//			    	trainSetList = new ArrayList<NemaTrackList>(trainSets.keySet());
//			    	Collections.sort(trainSetList,trackListComp);
//			    }
			    if (jobIdToTestSets == null){
			    	throw new ComponentExecutionException("Received null list of map of job ID to test results to evaluate");
			    }
			    
			    testSetList = new ArrayList<NemaTrackList>(jobIdToTestSets.values().iterator().next().keySet());
			    if (testSetList.isEmpty()){
			    	throw new ComponentExecutionException("Received empty list of test results to evaluate");
			    }
			    if (testSetList.size() != dataset.getNumFolds()){
			    	throw new ComponentExecutionException("The number of folds in the results for at least one system does not match the number of fols declared in the dataset!");
			    }
		    	Collections.sort(testSetList,trackListComp);   
		    }
		    this.getLogger().info("Initializing evaluation toolset for metadata type: " + task.getSubjectTrackMetadataName());
	        Evaluator eval = EvaluatorFactory.getEvaluator(task.getSubjectTrackMetadataName(), task, dataset, trainSetList, testSetList);
	        eval.addLogDestination(getLogDestination());
	        
	        //add the ground-truth
	        eval.setGroundTruth(gtList);
	        
	        //add the result to the evaluator
	        for (Iterator<String> jobIt = jobIdToTestSets.keySet().iterator(); jobIt.hasNext();){
	        	String jobId = jobIt.next();
	        	Map<NemaTrackList,List<NemaData>> sysResults = jobIdToTestSets.get(jobId);
		        for (Iterator<NemaTrackList> iterator = testSetList.iterator(); iterator.hasNext();) {
		        	NemaTrackList testSet = iterator.next();
		        	List<NemaData> results = sysResults.get(testSet);
		        	if (results == null){
		        		throw new ComponentExecutionException("Did not receive results for fold " + 
		        				testSet.getFoldNumber() + "(" + testSet.getId() + ") of dataset " + 
		        				dataset.getName() + "(" + dataset.getId() + ") for system " + 
		        				systemIDToSubmissionDetails.get(jobId) + "(" + jobId + ")");
		        	}
					eval.addResults(systemIDToSubmissionDetails.get(jobId), jobId, testSet, results);
				}
	        }
	        
			//perform evaluation
			NemaEvaluationResultSet evalOutput = eval.evaluate();
			
			// output the raw results dir for reprocessing or storage in the repository
			cc.pushDataComponentToOutput(DATA_OUTPUT_EVAL_RESULTS, evalOutput);
			
			this.getLogger().info("Evaluation Complete");
	        
	    } catch (Exception e) {
			ComponentExecutionException ex = new ComponentExecutionException("Exception occured when performing the evaluation!",e);
			throw ex;
		}
        
	}

	/** This method is called when the Meandre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	@Override
	public void dispose (ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}

}
