package org.imirsel.nema.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Flow;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FlowController extends MultiActionController{

	
   private FlowService flowService = null;
	   

   public FlowService getFlowService() {
		return flowService;
	}


   public void setFlowService(FlowService flowService) {
	 	this.flowService = flowService;
   }

	
	
	public ModelAndView getFlowTemplates(HttpServletRequest req, HttpServletResponse res){
		this.flowService.getFlowTemplates();
		return null;
	} 
	
	public ModelAndView storeFlowInstance(HttpServletRequest req, HttpServletResponse res){
		Flow instance= null;
		this.flowService.storeFlowInstance(instance);
		return null;
	} 
	
	
	
}
