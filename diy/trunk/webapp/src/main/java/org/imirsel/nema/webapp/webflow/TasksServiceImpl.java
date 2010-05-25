package org.imirsel.nema.webapp.webflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.jcr.SimpleCredentials;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.annotations.parser.beans.DataTypeBean;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.OsDataType;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.core.collection.ParameterMap;

import org.springframework.webflow.execution.RequestContext;

/**
 * Action class for the task template flow generation webflow.
 * 
 * @author gzhu1
 * @since 0.6.0
 * 
 */
public class TasksServiceImpl {

	public void setArtifactService(ArtifactService artifactService) {
		this.artifactService = artifactService;
	}

	static private Log logger = LogFactory.getLog(TasksServiceImpl.class);
	private FlowService flowService;
	private UserManager userManager;
	private String uploadDirectory;
	private ArtifactService artifactService;
	/**
	 * properties' name in the component datatype map
	 */
	final static String REMOTE_COMPONENT = "_remoteComponent";
	final static String CREDENTIALS = "_credentials";
	final static String EXECUTABLE_URL = "profileName";
	final static String OS = "_os";
	final static String GROUP = "_group";

	/**
	 * change after migrate to datamap back tag upload ExecutableBundle to
	 * content repository, remove the old executableBundle if there is one;
	 * replace/add the new ResourcePath of executablebundle into the
	 * executableMap;
	 * 
	 * @param component
	 * @param parameters
	 *            modified, the new path replace the old/is added
	 * @param os
	 * @param group
	 * @param bundle
	 * @param uuid
	 * @param executableMap
	 *            Note that this map is going to be modified, old path is
	 *            replaced by new path.
	 * @throws ContentRepositoryServiceException
	 */
	public void addExecutable(final Component component,
			final Map<String, Property> datatypeMap,
			final UploadedExecutableBundle bundle, final UUID uuid,
			Map<Component, ResourcePath> executableMap,
			final MessageContext messageContext)
			throws ContentRepositoryServiceException {
		logger.debug("add executable url into parameter for "
				+ bundle.getFileName());
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		logger.debug("with credential " + credential.getUserID() + " string: "
				+ new String(credential.getPassword()));
		String credentialString = credential.getUserID() + ":"
				+ new String(credential.getPassword());
		datatypeMap.get(CREDENTIALS).setValue(credentialString);
		datatypeMap.get(REMOTE_COMPONENT).setValue("true");
		datatypeMap.get(OS).setValue(bundle.getPreferredOs());
		datatypeMap.get(GROUP).setValue(bundle.getGroup());
		if (executableMap.containsKey(component)) {
			ResourcePath oldPath = executableMap.get(component);
			if (artifactService.exists(credential, oldPath)) {
				artifactService.removeExecutableBundle(credential, oldPath);
			}
		}
		ResourcePath path = artifactService.saveExecutableBundle(credential,
				uuid.toString(), bundle);		
		executableMap.put(component, path);
		if (path != null) {
			datatypeMap.get(EXECUTABLE_URL).setValue(path.getPath());
			messageContext.addMessage(new MessageBuilder().error().defaultText(
					"success uploaded executable bundle" + bundle.getFileName())
					.build());
			logger.debug("resource path is " + path);
		} else {
			throw new ContentRepositoryServiceException(
					"error in saving the executable bundle");
		}

	}

	
	/**
	 * return the list of flow templates belong to type, all templates are returned if type is not valid.  
	 * used a very lenient criteria, check both flowtype and keywords
	 * @param type controlled by {@link Flow.FlowType}, first letter needs capitalize. 
	 * @return
	 */
	public List<Flow> getFlowTemplates(String type) {
		Flow.FlowType flowType = (type==null?null:Flow.FlowType.toFlowType(type));

		Set<Flow> flowSet = this.flowService.getFlowTemplates();
		List<Flow> list = new ArrayList<Flow>();
		if (flowType != null) {
			for (Flow flow : flowSet) {
				if ((flow.getType().equals(flowType)))
					list.add(flow);

			}
		} else {
			list.addAll(flowSet);
		}
		return list;
	}

	/**
	 * Retrieve the Executable bundle with resource path {@link path}, 
	 * and populated the extra fields for UploadedExecutableBundle 
	 * @param path
	 * @param datatypeMap
	 * @return
	 */
	public UploadedExecutableBundle findBundle(ResourcePath path,
			Map<String, Property> datatypeMap) {
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		UploadedExecutableBundle bundle = null;
		try {
			if ((path != null) && (artifactService.exists(credential, path))) {
				ExecutableBundle oldBundle = artifactService
						.getExecutableBundle(credential, path);
				bundle = new UploadedExecutableBundle(oldBundle);
				if (bundle == null)
					bundle = new UploadedExecutableBundle();
				bundle.setPreferredOs(datatypeMap.get(CREDENTIALS).getValue());
				bundle.setGroup(datatypeMap.get(GROUP).getValue());
			}
		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}
		return bundle;
	}

	/**
	 * @param flow
	 *            template flow
	 * @return parameter map for the template flow fill the paramater map from
	 *         the template flow's default value.
	 */
	public Map<String, String> fillDefaultParameter(Flow flow) {

		Map<String, String> parameters = new HashMap<String, String>();
		List<Component> componentList = flowService
				.getComponents(flow.getUri());
		Collections.sort(componentList);
		logger.info("componentList: " + componentList.size());
		for (int i = 0; i < componentList.size(); i++) {
			Component component = componentList.get(i);
			Map<String, Property> m = flowService.getComponentPropertyDataType(
					component, flow.getUri());
			for (Entry<String, Property> entry : m.entrySet()) {
				parameters.put(getName(component.getInstanceUri(), entry
						.getKey()), entry.getValue().getDefaultValue()
						.toString());
			}
		}
		logger.debug("done populating default parameters now.");

		return parameters;
	}

	/**
	 * Set the datatypeMaps from the flow.  
	 * @param flow
	 * @return
	 */
	public Map<Component, Map<String, Property>> setDatatypeMaps(Flow flow) {

		Map<Component, Map<String, Property>> datatypeMaps = new TreeMap<Component, Map<String, Property>>();
		List<Component> componentList = flowService
				.getComponents(flow.getUri());
		logger.info("componentList: " + componentList.size());
		for (int i = 0; i < componentList.size(); i++) {
			Component component = componentList.get(i);
			datatypeMaps.put(component, flowService
					.getComponentPropertyDataType(component, flow.getUri()));

		}
		logger.debug("done populating default parameters now.");

		return datatypeMaps;
	}
	
	public List<Component> setComponentList(Map<Component, Map<String, Property>> datatypeMaps){
		List<Component> list=new ArrayList<Component>(datatypeMaps.keySet());
		Collections.sort(list);
		return list;
	}

	/**
	 * 
	 * @param job
	 * @return result set of job
	 */
	public DisplayResultSet getJobResult(Job job) {
		Set<JobResult> results = job.getResults();
		if (results == null)
			return null;
		else {
			for (JobResult result : results) {
				logger.debug("RESULT: " + result.getUrl() + "  "
						+ result.getId());
				result.setUrl(processUrl(result.getUrl()));

			}
			DisplayResultSet resultSet = new DisplayResultSet(results);
			return resultSet;
		}
	}

	/**
	 * @return roles from the default user manager
	 */
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

	/**
	 * Returns upload directory
	 * 
	 * @return upload directory
	 */
	public String getUploadDirectory() {
		return uploadDirectory;
	}

	/**
	 * hide some fields that needs special processing for remote service
	 * component
	 * 
	 * @param datatypeMap
	 */
	public List<Property> hideExecutableProperties(
			Map<String, Property> datatypeMap) {
		List<Property> hiddenProperties = new ArrayList<Property>();
		hiddenProperties.add(datatypeMap.remove(REMOTE_COMPONENT));
		hiddenProperties.add(datatypeMap.remove(CREDENTIALS));
		hiddenProperties.add(datatypeMap.remove(EXECUTABLE_URL));
		hiddenProperties.add(datatypeMap.remove(GROUP));
		hiddenProperties.add(datatypeMap.remove(OS));
		return hiddenProperties;
	}

	/**
	 * hide some properties for remote executable components that set in the task/executable subflow
	 * @param datatypeMap
	 * @return  the datatype map fields that should be shown
	 */
	public Map<String, Property> shownMap(Map<String, Property> datatypeMap) {
		Map<String, Property> shown = new HashMap<String, Property>();
		shown.putAll(datatypeMap);
		shown.remove(REMOTE_COMPONENT);
		shown.remove(CREDENTIALS);
		shown.remove(EXECUTABLE_URL);
		shown.remove(GROUP);
		shown.remove(OS);
		return shown;
	}

	/**
	 * return Boolean (not boolean) value for webflow mapping.
	 * 
	 * @param datatypeMap
	 * @return
	 */
	public Boolean isRemoteServiceComponent(Map<String, Property> datatypeMap) {

		return datatypeMap.keySet().contains(REMOTE_COMPONENT)
				&& (datatypeMap.get(REMOTE_COMPONENT).getDefaultValue()
						.toString().equalsIgnoreCase("true"));
	}

	/**
	 * Update the dataMap with submitted data for one component
	 * 
	 * @param parameters
	 *            http request parameters
	 * @param dataMap
	 *            dataMap for updated
	 */
	public void updateDataMap(ParameterMap parameters,
			Map<String, Property> dataMap) {
		for (String key : dataMap.keySet()) {
			Property property = dataMap.get(key);
			List<DataTypeBean> ltb = property.getDataTypeBeanList();
			if ((ltb != null) && (!ltb.isEmpty())
					&& (ltb.get(0).getRenderer() != null)
					&& (ltb.get(0).getRenderer().endsWith("FileRenderer"))) {
				MultipartFile file = parameters.getMultipartFile(property
						.getName());

				// TODO Save the file
				// String address=null;
				// File uploadedFile = new File(uploadDir + File.separator
				// + fileName);
				// item.write(uploadedFile);
				// logger.debug("file uploaded: " + fileName
				// + uploadedFile.getAbsolutePath());
				// String webDir =
				// uploadDir.substring(servletContext.getRealPath(
				// "/").length());
				// paramMap.put(fieldName, "http://" + req.getServerName() + ":"
				// + req.getServerPort() + req.getContextPath() + webDir
				// + fileName);
				// property.setValue(address);

			} else {
				property.setValue(parameters.get(property.getName()));
			}
			dataMap.put(key, property);
		}
	}

	/**
	 * 
	 * @param context
	 *            request context from the web flow. All the request parameters
	 *            are encoded in the http request parameters.
	 * @return parameter map
	 * @throws MeandreServerException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> saveParameter(RequestContext context)
			throws MeandreServerException {

		logger.debug("start to save parameter");
		Map<String, String> paramMap = (Map<String, String>) context
				.getFlowScope().get("parameterMap");
		Map<String, String> map = context.getRequestParameters().asMap();
		try {
			logger.debug("start to save parameters #" + map.size());
			for (String name : map.keySet()) {
				if (paramMap.containsKey(name)) {
					logger.debug("replace parameter  (" + name + ":"
							+ map.get(name) + ")");
					paramMap.put(name, map.get(name));
				}
			}
			return paramMap;
		} catch (Exception e) {
			logger.error(e, e);
			throw new MeandreServerException(e);
		}
	}

	/**
	 * @deprecated No longer used. Problemetic.
	 * @param context
	 * @throws MeandreServerException
	 */
	@SuppressWarnings("unchecked")
	public void saveParameterOld(RequestContext context)
			throws MeandreServerException {
		String token = System.currentTimeMillis() + "-token";

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
			logger.debug("start to save parameters #" + items.size());
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

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	/**
	 * Set the upload directory
	 * 
	 * @param uploadDirectory
	 */
	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * Only for testing purpose
	 * 
	 * @param input
	 * @return
	 */
	public int test(String input) {
		logger.debug(input);
		return 1;
	}

	/**
	 *
	 * 
	 * @param flow
	 * @param datatypeMaps
	 * @param name
	 * @param description
	 * @return
	 * @throws MeandreServerException
	 */
	public Job testRun(Flow flow,
			Map<Component, Map<String, Property>> datatypeMaps, String name,
			String description) throws MeandreServerException {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		for (Component component : datatypeMaps.keySet()) {
			Map<String, Property> m = flowService.getComponentPropertyDataType(
					component, flow.getUri());
			for (Entry<String, Property> entry : m.entrySet()) {
				paramMap.put(
						getName(component.getInstanceUri(), entry.getKey()),
						entry.getValue().getDefaultValue().toString());
			}
		}
		String token = System.currentTimeMillis() + "-token";
		String flowId = flow.getId().toString();
		String flowUri = flow.getUri();

		logger.debug("start to test run");
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
		// instance.setUri(newFlowUri);
		instance.setDescription(description);
		instance.setType(templateFlow.getType());
		instance.setTypeName(templateFlow.getTypeName());

		instance = this.flowService.createNewFlow(userManager
				.getCurrentUserCredentials(), instance, paramMap, flowUri, user
				.getId());
		long instanceId = instance.getId();

		Job job = this.flowService.executeJob(token, name, description,
				instanceId, user.getId(), user.getEmail());
		return job;

	}
	
	public Job run(Flow flow,
			Map<Component, Map<String, Property>> datatypeMaps, String name,
			String description) throws MeandreServerException {
		return this.testRun(flow, datatypeMaps, name, description);
	}

	// TODO this method is the same as the one in ComponentPropertyTag, might
	// need some
	// refaction to get rid of one
	private String getName(String component, String propertyName) {
		if (component == null) {
			return propertyName;
		}
		int index = component.lastIndexOf("/");
		if (index == -1) {
			return component + "_" + propertyName;
		}
		int second = component.substring(0, index).lastIndexOf("/");
		String cname = component.substring(second + 1, index);
		String count = component.substring(index + 1);
		return cname + "_" + count + "_" + propertyName;
	}

	private String processUrl(String url) {
		String identifier = "published_resources/nema";
		int index = url.indexOf(identifier);
		String resultFolder = url.substring(index + identifier.length());
		return "http://nema.lis.uiuc.edu/nema_out" + resultFolder;

	}

}
