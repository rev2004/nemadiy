package org.imirsel.nema.webapp.controller;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This controller provides functions that create a new flow and forwards to
 * jsp pages that render the flow templates.
 * @author kumaramit01
 * @since 0.4.0
 */
public class FlowFormController extends MultiActionController{


	private static final Logger LOGGER = Logger.getLogger(FlowFormController.class.getName());
	private FlowService flowService = null;
	/**UserManager**/
	private UserManager userManager = null;
	/**Upload directory**/
	private String uploadDirectory;

	private FlowService getFlowService() {
		return flowService;
	}


	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	
	/**
	 * This function renders the flow Templates. It forwards to a jsp page that
	 * with the flow, the component list, the role of the user and the jsp pages does the
	 * actual rendering.
	 * @param req
	 * @param res
	 * @return flow/flowTemplate
	 * @throws MeandreServerException
	 */
	public ModelAndView flowtemplate(HttpServletRequest req, HttpServletResponse res) throws  MeandreServerException{
		String _id=req.getParameter("id");
		int id = Integer.parseInt(_id);
		Flow flow=this.flowService.getFlow(id);
		LOGGER.info("get flow"+(flow==null?"null":flow.getName()));
		ModelAndView mav= new ModelAndView("flow/flowTemplate");
		
		List<Component> componentList=flowService.getComponents(flow.getUri());
		Collections.sort(componentList);
		LOGGER.info("componentList: " + componentList.size());
		TreeMap<Component,Map<String, Property>> map = new TreeMap<Component,Map<String, Property>>();
		for(int i=0;i<componentList.size();i++){
			HashMap<String, Property> m=(HashMap<String, Property>)flowService.getComponentPropertyDataType(componentList.get(i), flow.getUri());
			map.put(componentList.get(i), new TreeMap<String, Property>(m));
		}
		Set<Role> roleList=this.userManager.getCurrentUser().getRoles();
		int size=roleList.size();
		
		String[] roles = new String[size];
		int i=0;
		for(Role role:roleList){
			roles[i]= role.getName();
			i++;
		}
 		LOGGER.info(Constants.COMPONENTPROPERTYMAP + " : " + map.size());
		mav.addObject(Constants.FLOW, flow);
		mav.addObject(Constants.COMPONENTLIST,componentList);
	    mav.addObject(Constants.COMPONENTPROPERTYMAP,map);
	    mav.addObject(Constants.USER_ROLES,roles);
		return mav;
	}
	
	
	/**Saves the flow by calling the flowservice
	 * 
	 * @param req
	 * @param res
	 * @return Redirects to JobManager.jobDetail
	 * @throws MeandreServerException
	 */
	public ModelAndView saveflow(final HttpServletRequest req, HttpServletResponse res) throws MeandreServerException{
		String token=System.currentTimeMillis()+"-token";
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if(!isMultipart){
			LOGGER.severe("Error -this should be multipart");
			throw new MeandreServerException("the call to saveFlow should be multipart");
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		//upload.setSizeMax(yourMaxRequestSize);
		String uploadDir = getServletContext().getRealPath(getUploadDirectory()) + "/" + req.getRemoteUser() + "/"+ token+"/";
	    // Create the directory if it doesn't exist
	    File dirPath = new File(uploadDir);
	     
	      if (!dirPath.exists()) {
	         dirPath.mkdirs();
	      }
	      String flowId =  null;
		  String flowUri= null;
			
		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = iter.next();
			    if (item.isFormField()) {
			    	String name = item.getFieldName();
			        String value = item.getString();
			        paramMap.put(name, value);
			        if("flowTemplateId".equals(name)){
			        	flowId = value;
			        }else if("flowTemplateUri".equals(name)){
			        	 flowUri = value;
			        }
			        
			    } else {
			    	String fieldName = item.getFieldName();
			        String fileName = item.getName();
			        String contentType = item.getContentType();
			        boolean isInMemory = item.isInMemory();
			        long sizeInBytes = item.getSize();
			        if(fileName!=null && sizeInBytes>0 && fileName.length()>0){
			        	File uploadedFile = new File(uploadDir+File.separator + fileName);
			        	item.write(uploadedFile);
			        	System.out.println("file uploaded: "+ fileName + uploadedFile.getAbsolutePath());
			        	String webDir = uploadDir.substring(getServletContext().getRealPath("/").length());
			        	paramMap.put(fieldName,"http://"+ req.getServerName()+":"+req.getServerPort()+ req.getContextPath()+webDir+fileName);
			        }
			    }
			}
			
		} catch (FileUploadException e) {
			e.printStackTrace();
			throw new MeandreServerException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MeandreServerException(e);
		}
		
		if(flowId==null || flowUri==null){
			LOGGER.severe("flowId or flowUri is null -some severe error happened...");
			throw new MeandreServerException("flowId or flowUri is null");
		}
		User user = userManager.getCurrentUser();
		if (user == null) {
			LOGGER.severe("user is null");
			throw new MeandreServerException("Could not get the user");
			//user = userManager.getUserByUsername("admin");
		}
		
		
		
		Long longFlowId  =Long.parseLong(flowId);
		Flow templateFlow = this.getFlowService().getFlow(longFlowId );
		
		String name = paramMap.get("name");
		String description = paramMap.get("description");
		if(name ==null){
			name =templateFlow.getName()+File.separator+token;
		}
		
		if(description==null){
			description = templateFlow.getDescription()+" for flow: "+token;
		}
		
		long userId = user.getId();
		Flow instance = new Flow();
		instance.setCreatorId(userId);
		instance.setDateCreated(new Date());
		instance.setInstanceOf(templateFlow);
		instance.setKeyWords(templateFlow.getKeyWords());
		instance.setName(name);
		instance.setTemplate(false);
		//instance.setUri(newFlowUri);
		instance.setDescription(description);
		instance.setType(templateFlow.getType());
		instance.setTypeName(templateFlow.getTypeName());
		
		//long instanceId=this.getFlowService().storeFlowInstance(instance);
		
		Flow flow=flowService.createNewFlow(userManager.getCurrentUserCredentials(),instance,paramMap,flowUri,user.getId());
		long instanceId = flow.getId();
		
		Job job=this.getFlowService().executeJob(token, name,description, instanceId, user.getId(), user.getEmail());
		ModelAndView mav= new ModelAndView(new RedirectView("JobManager.jobDetail",true));
		//ModelAndView mav = new ModelAndView("job/job");
		mav.addObject("id", job.getId());
		return mav;
	}



	/**Returns upload directory
	 * 
	 * @return upload directory
	 */
	private String getUploadDirectory() {
		return uploadDirectory;
	}

	
	/**Set the upload directory
	 * 
	 * @param uploadDirectory
	 */
	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	/**sets the user manager
	 * 
	 * @param userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
