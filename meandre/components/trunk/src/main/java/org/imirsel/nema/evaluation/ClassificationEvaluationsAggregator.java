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
@Component(creator="Andreas F. Ehmann", description="Evaluates all Multi-fold Classification " +
		"Results of a single task, constructs a summary table/leader board, and performs significance tests ", 
		name="ClassificationEvaluationsAggregator",
		tags="test ft please hello",
		dependency={"jfreechart-1.0.9.jar", "swing-layout-1.0.jar", "jcommon-1.0.12.jar"})
		public class ClassificationEvaluationsAggregator implements ExecutableComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="List of the directories containing the raw classification results", name="Results Dirs")
	final static String DATA_INPUT_1= "Results Dirs";
	
	@ComponentInput(description="Algorithm names", name="Algorithm Names")
	final static String DATA_INPUT_2= "Algorithm Names";
	
	@ComponentInput(description="Ground-truth File", name="Ground-truth File")
	final static String DATA_INPUT_3= "Ground-truth File";

	@BooleanDataType(hide=true)
	@ComponentProperty(defaultValue="false",
			description="Flag whether to perform significance tests",
			name="Perform Significance Tests")
			final static String DATA_PROPERTY_PERFORMSIGTESTS = "Perform Significance Tests";
	private boolean performSigTests = false;

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
		//System.setProperty("java.awt.headless", "true");
      
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
		
		performSigTests = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_PERFORMSIGTESTS));

		String[] dirLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] nameLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] gtFileName = (String[])cc.getDataComponentFromInput(DATA_INPUT_3);
		// initialize variables for MIREXClassificationEvalMain Constructor
		String matlabPath = "matlab";
		String evaluationName = "OverallPublishedResults";
	    File gtFile = new File(gtFileName[0]);
	    File rootEvaluationDir = new File(processResultsDir + File.separator + "overallresults");

	    List<String> systemNames = new ArrayList<String>();
        List<File> resultsDirs = new ArrayList<File>();
        if (dirLists.length != nameLists.length) {
        	cout.println("ERROR: List of Directories and List of Algorithm Names are Different!");
        	cout.flush();
        	return;
        }
        for (int i=0; i<dirLists.length; i++){
        	resultsDirs.add(new File(dirLists[i]));
        	systemNames.add(nameLists[i]);
        }

        cout.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        cout.println("Performing Overall Evaluation of All Published Results...");
        cout.flush();
        // call constructor and evaluation method
        MIREXClassificationEvalMain evaluator = new MIREXClassificationEvalMain(performSigTests,
                matlabPath,
                evaluationName,
                gtFile,
                rootEvaluationDir,
                null,
                systemNames,
                resultsDirs);
        
        evaluator.performEvaluation();
		
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
