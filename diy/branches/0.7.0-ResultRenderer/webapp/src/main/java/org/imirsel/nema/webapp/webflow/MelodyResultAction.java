package org.imirsel.nema.webapp.webflow;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.NemaEvaluationResultSet;

/**
 * Action class for the result webflow for Melody task 
 * @author gzhu1
 * @since 0.7.0
 *
 */
public class MelodyResultAction {

	FlowService flowService;

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	public NemaEvaluationResultSet retrieveNemaResult(int jobId){
		return null;
	}
}
