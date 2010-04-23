package org.imirsel.nema.webapp.webflow;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Flow;

public class TasksServiceImpl implements TasksService {

	
	FlowService flowService;
	
	public void setFlowService(FlowService flowService){
		this.flowService=flowService;
	}

	public boolean fillFlow() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean run(Flow flow) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean testRun(Flow flow) {
		// TODO Auto-generated method stub
		return true;
	}
}
