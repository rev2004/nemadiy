package org.imirsel.nema.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.Constants;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.service.UserManager;
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
	private ArtifactService artifactService;
	private UserManager userManager;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public FlowService getFlowService() {
		return flowService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	/**Returns Component Data Property
	 * 
	 * @param request reads componentUri/flowUri parameters
	 * @param response
	 * @return
	 * @throws MeandreServerException
	 */
	public ModelAndView getComponentDataType(HttpServletRequest request, HttpServletResponse response)
	throws  MeandreServerException{
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		String componentUri=request.getParameter("componentUri");
		String flowUri = request.getParameter("flowUri");
		if(componentUri==null || flowUri==null){
			try {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Missing componentUri or flowUri");
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Component> componentList=flowService.getComponents(credential,flowUri);
		Component component = null;
		for(Component thisComponent:componentList){
			if(thisComponent.getUri().equalsIgnoreCase(componentUri)){
				component = thisComponent;
			}
		}
		if(component!=null){
			HashMap<String, Property> map=(HashMap<String, Property>)flowService.getComponentPropertyDataType(credential,component, flowUri);
			ModelAndView mav=new ModelAndView("jsonView");
			mav.addObject(Constants.COMPONENTPROPERTYMAP, map);
			return mav;
		}else{
			try {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"component with uri "+
						componentUri + " in flow "+ flowUri + " was not found." );
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**Returns the component list as JSON string
	 * 
	 * @param request reads flowUri parameter
	 * @param response
	 * @return
	 * @throws MeandreServerException
	 */
	public ModelAndView getComponentList(HttpServletRequest request, HttpServletResponse response)
	throws  MeandreServerException{
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		String _id=request.getParameter("flowUri");
		if(_id==null){
			try {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Missing flowId");
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int id = Integer.parseInt(_id);
		Flow flow=this.flowService.getFlow(id);
		List<Component> componentList=flowService.getComponents(credential,flow.getUri());
		ModelAndView mav=new ModelAndView("jsonView");
		mav.addObject(Constants.COMPONENTLIST, componentList);
		return mav;
	}

	
	/**Returns the status for all the meandre servers
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getAllMeandreServerStatus(HttpServletRequest request, HttpServletResponse response){
		Map<MeandreServerProxyConfig, MeandreServerProxyStatus> meadreServerProxyStatusMap=this.flowService.getWorkerStatus();
		meadreServerProxyStatusMap.put(this.flowService.getHeadConfig(), this.flowService.getHeadStatus());
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
				return null;
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
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		MeandreServerProxyStatus meadreServerProxyStatus=this.flowService.getWorkerStatus(host, port);
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
		Map<MeandreServerProxyConfig, MeandreServerProxyStatus> map=this.flowService.getWorkerStatus();
		List<MeandreServerProxyConfig> list = new ArrayList<MeandreServerProxyConfig>();
		for(MeandreServerProxyConfig mp: map.keySet()){
			list.add(mp);
		}
		MeandreServerProxyConfig mpc=this.flowService.getHeadConfig();
		list.add(mpc);
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


	public void setArtifactService(ArtifactService artifactService) {
		this.artifactService = artifactService;
	}


	public ArtifactService getArtifactService() {
		return artifactService;
	} 
	


}
