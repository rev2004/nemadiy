package org.imirsel.nema.webapp.webflow;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.controller.JobController;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 
 * @author gzhu1
 * 
 */
public class TasksServiceImpl implements TasksService {

	static private Log logger = LogFactory.getLog(JobController.class);
	private FlowService flowService;
	private UserManager userManager;
	private String uploadDirectory;

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public boolean fillFlow() {
		// TODO Auto-generated method stub
		return true;
	}



	public boolean testRun(Flow flow, Map<String, String> parameters,
			String name, String description) throws MeandreServerException {
		String token = System.currentTimeMillis() + "-token";
		HashMap<String, String> paramMap = new HashMap<String, String>(
				parameters);
		String flowId = paramMap.get("flowTemplateId");
		String flowUri = paramMap.get("flowTemplateUri");

		if (flowId == null || flowUri == null) {
			logger
					.error("flowId or flowUri is null -some severe error happened...");
			throw new MeandreServerException("flowId or flowUri is null");
		}
		User user = userManager.getCurrentUser();
		if (user == null) {
			logger.error("user is null");
			throw new MeandreServerException("Could not get the user");
			// user = userManager.getUserByUsername("admin");
		}

		String newFlowUri = this.flowService.createNewFlow(paramMap, flowUri,
				user.getId());

		Long longFlowId = Long.parseLong(flowId);
		Flow templateFlow = this.flowService.getFlow(longFlowId);

		if (name == null) {
			name = paramMap.get("name");
			if (name == null) {
				name = templateFlow.getName() + File.separator + token;
			}
		}
		if (description == null) {
			description = paramMap.get("description");
			if (description == null) {
				description = templateFlow.getDescription() + " for flow: "
						+ token;
			}
		}

		long userId = user.getId();
		Flow instance = new Flow();
		instance.setCreatorId(userId);
		instance.setDateCreated(new Date());
		instance.setInstanceOf(templateFlow);
		instance.setKeyWords(templateFlow.getKeyWords());
		instance.setName(name);
		instance.setTemplate(false);
		instance.setUri(newFlowUri);
		instance.setDescription(description);
		instance.setType(templateFlow.getType());
		instance.setTypeName(templateFlow.getTypeName());

		long instanceId = this.flowService.storeFlowInstance(instance);
		Job job = this.flowService.executeJob(token, name, description,
				instanceId, user.getId(), user.getEmail());

		return true;
	}

	public String[] getRoles() {
		Set<Role> roleList = this.userManager.getCurrentUser().getRoles();
		int size = roleList.size();

		String[] roles = new String[size];
		int i = 0;
		for (Role role : roleList) {
			roles[i] = role.getName();
			i++;
		}
		return roles;
	}

	public int test(String input) {
		logger.debug(input);
		return 1;
	}

	public Map<String, String> fillDefaultParameter(Flow flow) {

		Map<String, String> parameters = new HashMap<String, String>();
		List<Component> componentList = flowService
				.getComponents(flow.getUri());
		Collections.sort(componentList);
		logger.info("componentList: " + componentList.size());
		for (int i = 0; i < componentList.size(); i++) {
			Map<String, Property> m = flowService.getComponentPropertyDataType(
					componentList.get(i), flow.getUri());
			for (Entry<String, Property> entry : m.entrySet()) {
				parameters.put(entry.getKey(), entry.getValue()
						.getDefaultValue().toString());
			}
		}
		return parameters;
	}

	@SuppressWarnings("unchecked")
	public void saveParameter(RequestContext context)
			throws MeandreServerException {
		String token = System.currentTimeMillis() + "-token";

		System.out.print("start saveParameter");
		logger.debug("start to save parameter");
		HttpServletRequest req = (HttpServletRequest) context
				.getExternalContext().getNativeRequest();
		ServletContext servletContext = (ServletContext) context
				.getExternalContext().getNativeContext();

		Map<String, String> paramMap = (Map<String, String>) context
				.getFlowScope().get("parameterMap");

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (!isMultipart) {
			logger.error("Error -this should be multipart");
			throw new MeandreServerException(
					"the call to saveFlow should be multipart");
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// upload.setSizeMax(yourMaxRequestSize);
		String uploadDir = (servletContext).getRealPath(getUploadDirectory())
				+ "/" + req.getRemoteUser() + "/" + token + "/";
		// Create the directory if it doesn't exist
		File dirPath = new File(uploadDir);

		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			logger.debug("start to save parameters #"+items.size());
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					paramMap.put(name, value);

				} else {
					String fieldName = item.getFieldName();
					String fileName = item.getName();
					String contentType = item.getContentType();
					boolean isInMemory = item.isInMemory();
					long sizeInBytes = item.getSize();
					if (fileName != null && sizeInBytes > 0
							&& fileName.length() > 0) {
						File uploadedFile = new File(uploadDir + File.separator
								+ fileName);
						item.write(uploadedFile);
						logger.debug("file uploaded: " + fileName
								+ uploadedFile.getAbsolutePath());
						String webDir = uploadDir.substring(servletContext
								.getRealPath("/").length());
						paramMap.put(fieldName, "http://" + req.getServerName()
								+ ":" + req.getServerPort()
								+ req.getContextPath() + webDir + fileName);
					}
				}
			}

		} catch (FileUploadException e) {
			logger.error(e, e);
			throw new MeandreServerException(e);
		} catch (Exception e) {
			logger.error(e, e);
			throw new MeandreServerException(e);
		}

	}

	/**
	 * Returns upload directory
	 * 
	 * @return upload directory
	 */
	public String getUploadDirectory() {
		return uploadDirectory;
	}

	/**
	 * Set the upload directory
	 * 
	 * @param uploadDirectory
	 */
	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	public boolean run(Flow flow, Map<String, String> parameters, String name,
			String description) throws MeandreServerException {
		return this.testRun(flow,parameters,name,description);
	}

}
