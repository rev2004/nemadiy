/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.core.security.Role;
import org.imirsel.service.*;
import org.imirsel.nema.annotations.*;
import org.imirsel.nema.role.RoleAdmin;

import org.imirsel.m2k.evaluation2.classification.*;

/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Evaluates Multi-fold Classification Results", 
		name="ClassificationEvaluator",
		tags="test ft please hello",
		dependency={"jfreechart-1.0.9.jar", "swing-layout-1.0.jar", "jcommon-1.0.12.jar", "commons-compress-1.0.jar"})
		public class ClassificationEvaluator implements ExecutableComponent {


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
			name="Hierarchy File Name")
			final static String DATA_PROPERTY_HIERARCHYFILENAME = "Hierarchy File Name";
	private String hierarchyFileName = "";
	
	@StringDataType()
	@ComponentProperty(defaultValue="MyEvaluation",
			description="Name of the evaluation",
			name="Evaluation Name" )
			final static String DATA_PROPERTY_EVALNAME = "Evaluation Name";
	private String evalName= "MyEvaluation";

	private String processWorkingDir;
	private String processResultsDir;
	private Logger logger;
	java.io.PrintStream cout;
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void initialize ( ComponentContextProperties ccp ) {
		this.logger = ccp.getLogger();
		cout = ccp.getOutputConsole();
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance()
					.getProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance()
			.getResultLocationForJob(ccp.getFlowExecutionInstanceID());
		} catch (IOException e1) {
			try {
				throw new ComponentExecutionException(e1);
			} catch (ComponentExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		hierarchyFileName = String.valueOf(cc.getProperty(DATA_PROPERTY_HIERARCHYFILENAME));
		evalName = String.valueOf(cc.getProperty(DATA_PROPERTY_EVALNAME));
		String[] fileLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] gtFileName = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		
		// initialize variables for MIREXClassificationEvalMain Constructor
		String matlabPath = "/usr/local/bin/matlab";
	    String evaluationName = evalName;
	    File gtFile = new File(gtFileName[0]);
	    File rootEvaluationDir = new File(processResultsDir + File.separator + "evaluation");
	    
	    // create a directory to move the raw results to
	    File classificationResultsDir = new File(processResultsDir);
	    File hierarchyFile;
	    if (!hierarchyFileName.contentEquals("")) {
	    	hierarchyFile = new File(hierarchyFileName);
		} else {
			hierarchyFile = null;
		}
	    // move the classification files (e.g. fold.1.txt) to their own dir
	    // so MIREXClassifier has them all alone
	    
	    List<String> systemNames = new ArrayList<String>();
        List<File> resultsDir = new ArrayList<File>();
	    systemNames.add(evalName);
        resultsDir.add(classificationResultsDir);
        cout.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        cout.println("Evaluating...");
        cout.flush();
        // call constructor and evaluation method
        MIREXClassificationEvalMain evaluator = new MIREXClassificationEvalMain(false,
                matlabPath,
                evaluationName,
                gtFile,
                rootEvaluationDir,
                hierarchyFile,
                systemNames,
                resultsDir);
        
        evaluator.performEvaluation();
        
        // output the raw results dir for reprocessing by the summarizer component
        String[] outLists = new String[fileLists.length];
        outLists[0] = processResultsDir;
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, outLists);
		
        cout.println("Evaluation Complete");
        cout.flush();
        cout.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void dispose ( ComponentContextProperties ccp ) {
		cout.close();

	}

}
