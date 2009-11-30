package org.imirsel.nema.webapp.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.service.ComponentMetadataService;
import org.imirsel.nema.service.FlowMetadataService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FlowFormController extends MultiActionController{


	private Logger log = Logger.getLogger(FlowFormController.class.getName());
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

	public ModelAndView storeFlowInstance(HttpServletRequest req, HttpServletResponse res){
		Flow instance= null;
		this.flowService.storeFlowInstance(instance);
		return null;
	} 


	public ModelAndView flowtemplate(HttpServletRequest req, HttpServletResponse res){
		String _id=req.getParameter("id");
		int id = Integer.parseInt(_id);
		Flow flow=this.flowService.getFlow(id);
		ModelAndView mav= new ModelAndView("flow/flowTemplate");
		List<Component> componentList=flowMetadataService.getComponents(flow.getUrl());
		log.info("componentList: " + componentList.size());
		HashMap<Component,HashMap<String, Property>> map = new HashMap<Component,HashMap<String, Property>>();
		for(int i=0;i<componentList.size();i++){
			HashMap<String, Property> m=componentMetadataService.getComponentPropertyDataType(componentList.get(i), flow.getUrl());
			map.put(componentList.get(i), m);
		}
		log.info(Constants.COMPONENTPROPERTYMAP + " : " + map.size());
		mav.addObject(Constants.FLOW, flow);
		mav.addObject(Constants.COMPONENTLIST,componentList);
	    mav.addObject(Constants.COMPONENTPROPERTYMAP,map);
		return mav;
	}
	
	public ModelAndView saveflow(HttpServletRequest req, HttpServletResponse res){
		String flowId = req.getParameter("flowTemplateId");
		String flowUri= req.getParameter("flowTemplateUri");
		String[] modifiedComponentsUri = req.getParameterValues("modifiedComponents");
		System.out.println("Number of modified Components Uri: " + modifiedComponentsUri.length);
		Enumeration<String> paramNames=req.getParameterNames();
		HashMap<String,String> paramMap = new HashMap<String,String>();
		while(paramNames.hasMoreElements()){
			String paramName= paramNames.nextElement();
			paramMap.put(paramName, req.getParameter(paramName));
		}

		flowMetadataService.createNewFlow(paramMap,modifiedComponentsUri,flowUri);
	
		return null;
	}



}
