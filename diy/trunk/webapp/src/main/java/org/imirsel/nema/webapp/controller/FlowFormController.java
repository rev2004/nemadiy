package org.imirsel.nema.webapp.controller;

import java.io.File;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.model.NEMADataset;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.service.ComponentMetadataService;
import org.imirsel.nema.service.FlowMetadataService;
import org.imirsel.nema.service.UserManager;
import org.imirsel.util.FlowTypeUtils;
import org.meandre.webapp.CorruptedFlowException;
import org.meandre.webapp.MeandreCommunicationException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

public class FlowFormController extends MultiActionController{


	private Logger log = Logger.getLogger(FlowFormController.class.getName());
	private FlowService flowService = null;
	private ComponentMetadataService componentMetadataService;
	private FlowMetadataService flowMetadataService;
	private UserManager userManager = null;
	private RepositoryClientInterface repositoryClientInterface;
	
	private String uploadDirectory;



	public RepositoryClientInterface getRepositoryClientInterface() {
		return repositoryClientInterface;
	}


	public void setRepositoryClientInterface(
			RepositoryClientInterface repositoryClientInterface) {
		this.repositoryClientInterface = repositoryClientInterface;
	}


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


	public ModelAndView flowtemplate(HttpServletRequest req, HttpServletResponse res) throws TransmissionException, SQLException{
		String _id=req.getParameter("id");
		int id = Integer.parseInt(_id);
		Flow flow=this.flowService.getFlow(id);
		ModelAndView mav= new ModelAndView("flow/flowTemplate");
		List<Component> componentList=flowMetadataService.getComponents(flow.getUrl());
		if(componentList==null){
			throw new TransmissionException("Error could not get any " +
					"template flows from the Meandre Server.");
		}
		Collections.sort(componentList);
		log.info("componentList: " + componentList.size());
		TreeMap<Component,Map<String, Property>> map = new TreeMap<Component,Map<String, Property>>();
		for(int i=0;i<componentList.size();i++){
			HashMap<String, Property> m=null;
				m = (HashMap<String, Property>)componentMetadataService.getComponentPropertyDataType(componentList.get(i), flow.getUrl());
				map.put(componentList.get(i), new TreeMap(m));
		}
		Set<Role> roleList=this.userManager.getCurrentUser().getRoles();
		int size=roleList.size();
		
		String[] roles = new String[size];
		int i=0;
		for(Role role:roleList){
			roles[i]= role.getName();
			i++;
		}
 	   
		log.info(Constants.COMPONENTPROPERTYMAP + " : " + map.size());
		mav.addObject(Constants.FLOW, flow);
		mav.addObject(Constants.COMPONENTLIST,componentList);
	    mav.addObject(Constants.COMPONENTPROPERTYMAP,map);
	    mav.addObject(Constants.USER_ROLES,roles);

		return mav;
	}
	
	public ModelAndView saveflow(HttpServletRequest req, HttpServletResponse res) throws TransmissionException, MeandreCommunicationException, CorruptedFlowException{
		String token=System.currentTimeMillis()+"-token";
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if(!isMultipart){
			log.severe("Error -this should be multipart");
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
			    FileItem item = (FileItem) iter.next();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(flowId==null || flowUri==null){
			log.severe("flowId or flowUri is null -some severe error happened...");
		}
		
		
		String newFlowUri=flowMetadataService.createNewFlow(paramMap,flowUri);
		
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
		Flow instance = new Flow();
		User user = userManager.getCurrentUser();
		logger.debug("USER IS ====> " + user);

		if (user == null) {
			user = userManager.getUserByUsername("admin");
		}
		long userId = user.getId();
		instance.setCreatorId(userId);
		instance.setDateCreated(new Date());
		instance.setInstanceOf(templateFlow);
		instance.setKeyWords(templateFlow.getKeyWords());
		instance.setName(name);
		instance.setTemplate(false);
		instance.setUrl(newFlowUri);
		instance.setDescription(description);
		instance.setType(FlowTypeUtils.DEFAULT_FLOW);
		
		System.out.println("The new flow uri is: " + newFlowUri);
		long instanceId=this.getFlowService().storeFlowInstance(instance);
		Job job=this.getFlowService().executeJob(token, name,description, instanceId, user.getId(), user.getEmail());
		
		
		ModelAndView mav= new ModelAndView(new RedirectView("JobManager.getUserJobs",true));
		
		//ModelAndView mav = new ModelAndView("job/job");
		//mav.addObject(Constants.JOB, job);
		return mav;
	}



	public String getUploadDirectory() {
		return uploadDirectory;
	}


	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}


	public UserManager getUserManager() {
		return userManager;
	}


	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



}
