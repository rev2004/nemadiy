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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationTextFile;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann and Kris West;
 * @deprecated Use EvaluateSingleResultSet instead as it supercedes this.
 */
@Deprecated
@Component(creator="Andreas F. Ehmann", description="Evaluates all Multi-fold Classification " +
		"Results of a single task, constructs a summary table/leader board, and performs significance tests ", 
		name="ClassificationEvaluationsAggregatorComponent",
		tags="classification evaluation comparison",
		dependency={"commons-compress-1.0.jar","jfreechart-1.0.9.jar","jcommon-1.0.12.jar"}
		)
		public class ClassificationEvaluationsAggregatorComponent extends NemaComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="List of the directories containing the raw classification results", name="Results Dirs")
	final static String DATA_INPUT_1= "Results Dirs";
	
	@ComponentInput(description="Algorithm names", name="Algorithm Names")
	final static String DATA_INPUT_2= "Algorithm Names";
	
	@ComponentInput(description="Ground-truth File", name="Ground-truth File")
	final static String DATA_INPUT_3= "Ground-truth File";

	@StringDataType(hide=true)
	@ComponentProperty(defaultValue="",
			description="Path and name of the hierarchy file",
			name="Hierarchy file path")
	final static String DATA_HEIRARCY_FILE_PATH = "Hierarchy file path";
	
	@StringDataType()
	@ComponentProperty(defaultValue="genre",
			description="Metadata type to be trained on and predicted.",
			name="Metadata to classify")
	final static String DATA_METADATA_PREDICTED = "Metadata to classify";
	
	@StringDataType()
	@ComponentProperty(defaultValue="MIREX Genre Classification",
			description="The name of the evaluation to be used on the result pages output.",
			name="Task Name")
	final static String DATA_TASK_NAME = "Task Name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="Classification of popular music by genre",
			description="Task Description",
			name="Task Description")
	final static String DATA_TASK_DESC = "Task Description";
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="The name of the dataset being evaluated.",
			name="Dataset Name")
	final static String DATA_DATASET_NAME = "Dataset Name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="Description of the dataset used.",
			name="Dataset Description")
	final static String DATA_DATASET_DESC = "Dataset Description";
	

	String taskName;
	String taskDesc;
	String datasetName;
	String datasetDesc;
	String metadata;
	File hierarchyFile;
	
	private String processWorkingDir;
	private String processResultsDir;

	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 * @throws ComponentExecutionException 
	 */
	public void initialize ( ComponentContextProperties ccp ) throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory())
					.getProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory())
			.getResultLocationForJob(ccp.getFlowExecutionInstanceID());
		} catch (IOException e1) {
			ComponentExecutionException ex = new ComponentExecutionException("IOException occured when getting working and result directories!",e1);
			getLogger().log(Level.SEVERE, "Terminating execution",ex);
			throw ex;
		}
		
		taskName = ccp.getProperty(DATA_TASK_NAME);
		taskDesc = ccp.getProperty(DATA_TASK_DESC);
		datasetName = ccp.getProperty(DATA_DATASET_NAME);
		datasetDesc = ccp.getProperty(DATA_DATASET_DESC);
		metadata = ccp.getProperty(DATA_METADATA_PREDICTED);
		String hierarchyFilePath = ccp.getProperty(DATA_HEIRARCY_FILE_PATH);
		if (!hierarchyFilePath.trim().equals("")){
			hierarchyFile = new File(hierarchyFilePath);
		}else{
			hierarchyFile = null;
		}
      
	}

	/** This method just pushes a concatenated version of the entry to the
	 * output.
	 *
	 * @throws ComponentExecutionException If a fatal condition arises during
	 *         the execution of a component, a ComponentExecutionException
	 *         should be thrown to signal termination of execution required.
	 * @throws ComponentContextException A violation of the component context
	 *         access was detected

	 */
	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
		//File inFile = (File)cc.getDataComponentFromInput(DATA_INPUT_1);
		
		String[] dirLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] nameLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] gtFileName = (String[])cc.getDataComponentFromInput(DATA_INPUT_3);
		
		// initialize variables for MIREXClassificationEvalMain Constructor
		File matlabPath = new File("/usr/local/bin/matlab");
		File gtFile = new File(gtFileName[0]);
	    File procResDir = new File(processResultsDir);
	    String processResultsDirName = processResultsDir;
	    
	    try {
			try {
				processResultsDirName = procResDir.getCanonicalPath();
			} catch (IOException e) {
				ComponentExecutionException ex = new ComponentExecutionException("Failed to get canonical path for File: " + processResultsDirName.toString(),e);
				throw ex;
			}
		    File rootEvaluationDir = new File(processResultsDirName + File.separator + "overall");
	
		    // call constructor and evaluation method
	        NemaTask task = new NemaTask(-1, taskName, taskDesc, -1, metadata, -1);
	        NemaDataset dataset = new NemaDataset(-1,datasetName,datasetDesc,-1,-1,-1,null,null,-1,metadata,-1,null);
	        
	        //init evaluator
	        ClassificationEvaluator eval;
			try {
				eval = new ClassificationEvaluator(task,dataset,rootEvaluationDir,rootEvaluationDir,false,matlabPath,hierarchyFile);
				eval.addLogDestination(getLogDestination());
			} catch (FileNotFoundException e) {
				ComponentExecutionException ex = new ComponentExecutionException("FileNotFoundException occured when setting up evaluator!",e);
				throw ex;
			} catch (IOException e) {
				ComponentExecutionException ex = new ComponentExecutionException("IOException occured when setting up evaluator!",e);
				throw ex;
			}
		    
			//read Ground-truth
	        ClassificationTextFile reader = new ClassificationTextFile(metadata);
	        reader.addLogDestination(getLogDestination());

			try {
				List<NemaData> gt = reader.readFile(gtFile);
				eval.setGroundTruth(gt);
			} catch (Exception e) {
				ComponentExecutionException ex = new ComponentExecutionException("Exception occured when reading up ground-truth from: " + gtFile.getAbsolutePath(),e);
				throw ex;
			}
		    
		    
	        if (dirLists.length != nameLists.length) {
	        	ComponentExecutionException ex = new ComponentExecutionException("List of directories and list of algorithm names are different lengths!");
				throw ex;
	        }
	        for (int i=0; i<dirLists.length; i++){
	        	try{
		        	List<List<NemaData>> results = reader.readDirectory(new File(dirLists[i]),null);
					for(Iterator<List<NemaData>> it= results.iterator();it.hasNext();){
			        	eval.addResults(nameLists[i], ""+i, it.next());
			        }
	        	} catch (Exception e) {
	    			ComponentExecutionException ex = new ComponentExecutionException("Exception occured when reading up results for system '" + nameLists[i] + " from file: " + dirLists[i],e);
	    			throw ex;
	    		}
	        }
	
	      //perform evaluation
	        try {
				Map<String,NemaData> evalResults = eval.evaluate();
			} catch (Exception e) {
				ComponentExecutionException ex = new ComponentExecutionException("Exception occured when performing the evaluation!",e);
				throw ex;
			}
			
			getLogger().info("Evaluation Complete\n" +
	        		"Results written to: " + rootEvaluationDir.getAbsolutePath());
			
			
		} catch (ComponentExecutionException e) {
			getLogger().log(Level.SEVERE, "Terminating execution", e);
			throw e;
		}
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	public void dispose (ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}

}
