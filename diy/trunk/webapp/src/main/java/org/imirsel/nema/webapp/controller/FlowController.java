package org.imirsel.nema.webapp.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.User;
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

	
	
	public ModelAndView flowlist(HttpServletRequest req, HttpServletResponse res){
		Set<Flow> flowSet=this.flowService.getFlowTemplates();
		return new ModelAndView("flow/flowList", Constants.FLOW_LIST, flowSet);
	} 
	
	public ModelAndView storeFlowInstance(HttpServletRequest req, HttpServletResponse res){
		Flow instance= null;
		this.flowService.storeFlowInstance(instance);
		return null;
	} 
	
	public ModelAndView flowform(HttpServletRequest req, HttpServletResponse res){
		String _id=req.getParameter("id");
		int id = Integer.parseInt(_id);
		Flow flow=this.flowService.getFlow(id);
		
		return new ModelAndView("flow/flowForm", Constants.FLOW, flow);
	}
	
	
	
}
