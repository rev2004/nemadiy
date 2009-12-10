package org.imirsel.nema.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.ComponentMetadataService;
import org.imirsel.nema.service.FlowMetadataService;
import org.imirsel.nema.service.UserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FlowController extends MultiActionController{


	private Logger log = Logger.getLogger(FlowController.class.getName());
	private FlowService flowService = null;
	private ComponentMetadataService componentMetadataService;
	private FlowMetadataService flowMetadataService;
	
	public FlowService getFlowService() {
		return flowService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}




	public ComponentMetadataService getComponentMetadataService() {
		return componentMetadataService;
	}


	public void setComponentMetadataService(
			ComponentMetadataService componentMetadataService) {
		this.componentMetadataService = componentMetadataService;
	}


	public FlowMetadataService getFlowMetadataService() {
		return flowMetadataService;
	}


	public void setFlowMetadataService(FlowMetadataService flowMetadataService) {
		this.flowMetadataService = flowMetadataService;
	}


	public ModelAndView flowlist(HttpServletRequest req, HttpServletResponse res){
		Set<Flow> flowSet=this.flowService.getFlowTemplates();
		log.info("done loading for flowlist");
		return new ModelAndView("flow/flowType", Constants.FLOW_LIST, flowSet);
	} 

	public ModelAndView storeFlowInstance(HttpServletRequest req, HttpServletResponse res){
		Flow instance= null;
		this.flowService.storeFlowInstance(instance);
		return null;
	} 

	

	


}
