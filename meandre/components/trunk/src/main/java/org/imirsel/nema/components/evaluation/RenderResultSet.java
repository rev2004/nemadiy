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
import java.io.IOException;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.analytics.evaluation.ResultRendererFactory;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaEvaluationResultSet;


/** ResultRendering component that converts results sets into evaluation
 * reports, including the performance of any required statistical tests or
 * plotting operations.
 * 
 * @author kris.west@gmail.com
 */
@Component(creator="Kris West", description="Converts results sets into evaluation reports, including the performance of any required statistical tests or plotting operations.", 
		name="RenderResultSet",
		tags="evaluation comparison reporting")
	public class RenderResultSet extends NemaComponent {

	//INPUTS	
	@ComponentInput(description = "The evaluation results set to render.", name = "Eval result set")
	public final static String DATA_INPUT_EVAL_RESULT_SET = "Eval result set";
	
	//OUTPUTS
	@ComponentOutput(description="Evaluation results model encoding all the data evaluated and the evaluation metrics. " +
			"The results will have already been rendered to the output directory.", name="Evaluation results")
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
	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
		NemaEvaluationResultSet results = (NemaEvaluationResultSet)cc.getDataComponentFromInput(DATA_INPUT_EVAL_RESULT_SET);

		//TODO: get the matlab path used by some evaluators from somewhere? Perhaps environment variable or props file until we can remove the need for it 
		File matlabPath = new File("/usr/local/bin/matlab");
		File procResDir = new File(getAbsoluteResultLocationForJob());
	    File procWorkingDir = new File(getProcessWorkingDirectory());
	    String processResultsDirName;
		try {
			processResultsDirName = procResDir.getCanonicalPath();
		} catch (IOException e) {
			ComponentExecutionException ex = new ComponentExecutionException("Failed to get canonical path for File: " + procResDir.toString(),e);
			throw ex;
		}
	
	    try{
		    File rootEvaluationDir = new File(processResultsDirName + File.separator + "evaluation");
	        //init renderer
		    ResultRenderer renderer = ResultRendererFactory.getRenderer(results.getTask().getSubjectTrackMetadataName(), rootEvaluationDir, procWorkingDir, true, matlabPath);
		    renderer.addLogDestination(getLogDestination());
			//perform rendering
		    renderer.renderResults(results);
			// output the raw results for reprocessing or storage in the repository
			cc.pushDataComponentToOutput(DATA_OUTPUT_EVAL_RESULTS, results);
	        this.getLogger().info("Rendering Complete\n" +
	        		"Results written to: " + rootEvaluationDir.getAbsolutePath());
	    } catch (Exception e) {
			ComponentExecutionException ex = new ComponentExecutionException("Exception occured when rendering the results!",e);
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
