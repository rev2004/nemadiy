package org.imirsel.nema.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.model.Flow;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * This controller exposes list of flows.
 * @author kumaramit01
 * @since 0.4.0
 * @modified 0.5.1
 * -Added support for getMeandreServerStatus, getMeandreServerList
 * and getAllMeandreServerStatus
 */
public class FlowServiceController extends MultiActionController{

	private final static Logger log = Logger.getLogger(FlowServiceController.class.getName());
	private FlowService flowService = null;

	public FlowService getFlowService() {
		return flowService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	/**Returns the status for all the meandre servers
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getAllMeandreServerStatus(HttpServletRequest request, HttpServletResponse response){
		Map<MeandreServerProxyConfig, MeandreServerProxyStatus> meadreServerProxyStatusMap=this.flowService.getMeandreServerProxyStatus();
		ModelAndView mav=new ModelAndView("jsonView");
		mav.addObject(Constants.MEANDRE_SERVER_STATUS, meadreServerProxyStatusMap);
		return mav;
	}
	
	/**Returns the status for a meandre server
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getMeandreServerStatus(HttpServletRequest request, HttpServletResponse response){
		String host =  request.getParameter("host");
		String _port = request.getParameter("port");
		if(host ==null || _port== null){
			try {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Missing host or port");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int port = -1;
		try{
		port = 	Integer.parseInt(_port);
		}catch(Exception ex){
			logger.error("invalid port "+ _port);
		}
		if(port==-1){
			try {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Invalid port");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		MeandreServerProxyStatus meadreServerProxyStatus=this.flowService.getMeandreServerProxyStatus(host, port);
		ModelAndView mav=new ModelAndView("jsonView");
		mav.addObject(Constants.MEANDRE_SERVER_STATUS, meadreServerProxyStatus);
		return mav;
	}
	
	/**Returns the list of Meandre Servers
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getMeandreServerList(HttpServletRequest request, HttpServletResponse response){
		List<MeandreServerProxyConfig> list=this.flowService.getSchedulerConfig();
		ModelAndView mav=new ModelAndView("jsonView");
		mav.addObject(Constants.MEANDRE_SERVER_LIST, list);
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
		
		ModelAndView mav;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav =new ModelAndView("flow/flowType");
		}
		Set<Flow> flowSet=this.flowService.getFlowTemplates();
		if(!type.equalsIgnoreCase("all")){
		ArrayList<Flow> list = new ArrayList<Flow>();
		for(Flow flow:flowSet){
			log.info("Name is: "+ flow.getName());
			log.info("Flow Type is: " + flow.getTypeName());
			list.add(flow);
			
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
