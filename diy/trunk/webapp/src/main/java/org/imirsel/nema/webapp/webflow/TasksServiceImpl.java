package org.imirsel.nema.webapp.webflow;

import org.imirsel.nema.flowservice.FlowService;

public class TasksServiceImpl implements TasksService {

	
	FlowService flowService;
	
	public void setFlowService(FlowService flowService){
		this.flowService=flowService;
	}
	public boolean testRun() {
		
		return true;
	}

}
