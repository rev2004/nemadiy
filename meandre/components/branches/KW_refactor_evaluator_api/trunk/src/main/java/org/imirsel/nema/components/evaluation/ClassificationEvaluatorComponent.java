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
import java.util.logging.Logger;


import org.imirsel.nema.analytics.evaluation.classification.ClassificationEvaluator;
import org.imirsel.nema.analytics.evaluation.classification.ClassificationTextFile;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;


/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann and Kris West;
 * @deprecated Use EvaluateSingleResultSet instead as it supercedes this.
 */
@Deprecated
@Component(creator="Andreas F. Ehmann", description="Evaluates Multi-fold Classification Results", 
		name="ClassificationEvaluatorComponent",
		tags="classification evaluation comparison",
		dependency={"commons-compress-1.0.jar","jfreechart-1.0.9.jar","jcommon-1.0.12.jar"})
		public class ClassificationEvaluatorComponent extends NemaComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="List of the algorithm classification files, one for each fold", name="Results List")
	final static String DATA_INPUT_1= "Results List";
	
	@ComponentInput(description="Ground-truth File", name="Ground-truth File")
	final static String DATA_INPUT_2= "Ground-truth File";

	@ComponentOutput(description="Results Directory Ouput", name="Results Directory")
	final static String DATA_OUTPUT_1= "Results Directory";

	
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
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="Short name of the system being evaluated.",
			name="System name")
	final static String DATA_SYSTEM_NAME = "System name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="Description of the system being evaluated.",
			name="System Description")
	final static String DATA_SYSTEM_DESC = "System Description";
			

			
	private String taskName;
	private String taskDesc;
	private String datasetName;
	private String datasetDesc;
	private String systemName;
	private String systemID;
	private String metadata;
	private File hierarchyFile;
	
	
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 * @throws ComponentExecutionException 
	 */
	public void initialize (ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException{
		super.initialize(ccp);
		System.out.println("Init " + this.getClass().getName());

		taskName = ccp.getProperty(DATA_TASK_NAME);
		taskDesc = ccp.getProperty(DATA_TASK_DESC);
		datasetName = ccp.getProperty(DATA_DATASET_NAME);
		datasetDesc = ccp.getProperty(DATA_DATASET_DESC);
		systemName = ccp.getProperty(DATA_SYSTEM_NAME);
		systemID = ccp.getFlowID();
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
		String[] fileLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] gtFileName = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		
		
		// initialize variables for MIREXClassificationEvalMain Constructor
		File matlabPath = new File("/usr/local/bin/matlab");
	    File gtFile = new File(gtFileName[0]);
	    File procResDir = new File(getAbsoluteResultLocationForJob());
	    String processResultsDirName;
	    try{
			try {
				processResultsDirName = procResDir.getCanonicalPath();
			} catch (IOException e) {
				ComponentExecutionException ex = new ComponentExecutionException("Failed to get canonical path for File: " + procResDir.toString(),e);
				throw ex;
			}
		    File rootEvaluationDir = new File(processResultsDirName + File.separator + "evaluation");
		    
		    // create a directory to move the raw results to
		    File classificationResultsDir = new File(fileLists[0]);
		    
	        NemaTask task = new NemaTask(-1, taskName, taskDesc, -1, metadata, -1);
	        NemaDataset dataset = new NemaDataset(-1,datasetName,datasetDesc,-1,-1,-1,null,null,-1,metadata,-1,null);
	        
	        //init evaluator
	        this.getLogger().info("Initializing evaluation toolset");
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
			
			//read results
			try{
				List<List<NemaData>> results = reader.readDirectory(classificationResultsDir, null);
				for(Iterator<List<NemaData>> it= results.iterator();it.hasNext();){
		        	eval.addResults(systemName, systemID, it.next());
		        }
			} catch (Exception e) {
				ComponentExecutionException ex = new ComponentExecutionException("Exception occured when reading up results!",e);
				throw ex;
			}
	        
			//perform evaluation
	        try {
				Map<String,NemaData> evalOutput = eval.evaluate();
				// output the raw results dir for reprocessing by the summarizer component
		        String[] outLists = new String[1];
		        outLists[0] = procResDir.getAbsolutePath();
				cc.pushDataComponentToOutput(DATA_OUTPUT_1, outLists);
				
		        this.getLogger().info("Evaluation Complete\n" +
		        		"Results written to: " + rootEvaluationDir.getAbsolutePath());
		    } catch (Exception e) {
				ComponentExecutionException ex = new ComponentExecutionException("Exception occured when performing the evaluation!",e);
				throw ex;
			}
			
	    } catch (ComponentExecutionException e) {
			getLogger().log(Level.SEVERE, "Terminating execution", e);
			throw e;
		}
        
	}


	/** This method is called when the Meandre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	public void dispose (ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
	}

}