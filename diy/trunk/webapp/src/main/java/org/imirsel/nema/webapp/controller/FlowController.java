package org.imirsel.nema.webapp.controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Flow;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class FlowController extends MultiActionController{


	private final Logger log = Logger.getLogger(FlowController.class.getName());
	private FlowService flowService = null;

	public FlowService getFlowService() {
		return flowService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}


	/**Returns all the template flows
	 * 
	 * @param req
	 * @param res 
	 * @return flow/flowType.jsp
	 * @deprecated for getTemplateFlows 
	 */
	@Deprecated
	public ModelAndView flowlist(HttpServletRequest req, HttpServletResponse res){
		Set<Flow> flowSet=this.flowService.getFlowTemplates();
		log.info("done loading for flowlist");
		
		ModelAndView mav;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav =new ModelAndView("flow/flowType");
		}
		mav.addObject(Constants.FLOW_LIST, flowSet);
		mav.addObject(Constants.FLOW_TYPE, "all");
		return mav;
	} 

	
	/**Returns a view that displays Template Flows
	 * 
	 * @param req
	 * @param res
	 * @return flow/flowType.jsp
	 */
	public ModelAndView getTemplateFlows(HttpServletRequest req, HttpServletResponse res){
		String type = req.getParameter("type");
		if(type==null){
			type="all";
		}
		Set<Flow> flowSet=this.flowService.getFlowTemplates();
		ModelAndView mav;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav =new ModelAndView("flow/flowType");
		}
		
		if(!type.equalsIgnoreCase("all")){
		ArrayList<Flow> list = new ArrayList<Flow>();
		for(Flow flow:flowSet){
			if(flow.getTypeName().toUpperCase().indexOf(type.toUpperCase())!=-1){
				System.out.println("adding: " + flow.getName() + "     "+flow.getType());
				list.add(flow);
			}
		}
		log.info("done loading for flowlist");
		mav.addObject(Constants.FLOW_LIST, list);
		}else{
		log.info("done loading for flowlist");
		mav.addObject(Constants.FLOW_LIST, flowSet);
		}
	
		mav.addObject(Constants.FLOW_TYPE, type);
		return mav;
	} 
	


}
