package org.imirsel.nema.webapp.webflow;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.apache.derby.impl.sql.compile.GetCurrentConnectionNode;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.contentrepository.client.CommandLineFormatter;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.contentrepository.client.ResourceTypeService;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.MatlabPredefinedCommandTemplate;
import org.imirsel.nema.model.OsDataType;
import org.imirsel.nema.model.Param;
import org.imirsel.nema.model.ParamAlreadyExistsException;
import org.imirsel.nema.model.PredefinedCommandTemplate;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.model.VanillaPredefinedCommandTemplate;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.imirsel.nema.webapp.model.ExecutableFile;
import org.imirsel.nema.webapp.model.JavaExecutableFile;
import org.imirsel.nema.webapp.model.MatlabExecutableFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.RequestContext;

/**
 * Action class for the task template flow generation web flow.
 * 
 * @author gzhu1
 * @since 0.5.1
 * 
 */
public class TasksServiceImpl {

	static private Log logger = LogFactory.getLog(TasksServiceImpl.class);
	private FlowService flowService;
	private UserManager userManager;
	private String uploadDirectory;
	private CommandLineFormatter commandLineFormatter;
	private ResourceTypeService resourceServiceType;
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
	 * Add the executable url into the parameter map
	 * 
	 * @param component
	 * @param parameters
	 * @param url
	 */
	public void addExecutable(final Component component,
			final Map<String, String> parameters, final String url,
			final OsDataType os, final String group,
			final ExecutableBundle bundle)
			throws ContentRepositoryServiceException {
		logger.debug("add executable url into parameter");
		parameters
				.put(getName(component.getInstanceUri(), EXECUTABLE_URL), url);
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		String credentialString = credential.getUserID() + ":"
				+ new String(credential.getPassword());
		parameters.put(getName(component.getInstanceUri(), CREDENTIALS),
				credentialString);
		parameters.put(getName(component.getInstanceUri(), REMOTE_COMPONENT),
				"true");
		parameters.put(getName(component.getInstanceUri(), OS), os.getValue());
		parameters.put(getName(component.getInstanceUri(), GROUP), group);
		artifactService.saveExecutableBundle(credential, component
				.getInstanceUri(), bundle);

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

	public ExecutableBundle generateExecutableBundle(
			VanillaPredefinedCommandTemplate template, ExecutableFile executable) {
		ExecutableBundle bundle = new ExecutableBundle();

		bundle.setFileName(executable.getFileName());
	//TODO	bundle.setExecutableName(executable.getExecutableInZip());
		bundle.setBundleContent(executable.getFileBytes());
		bundle.setId(UUID.randomUUID().toString());
		try {
			String command = commandLineFormatter.getCommandLineString(
					template, resourceServiceType.getOsDataType(executable
							.getOs()), false);
			bundle.setCommandLineFlags(command);
			logger.debug("generated command flag:" + command);
		} catch (InvalidCommandLineFlagException e) {
			logger.error(e, e);
		}
		bundle.setEnvironmentVariables(template.getEnvironmentMap());
		logger.debug("set the environment variables (size:"
				+ bundle.getEnvironmentVariables().size() + ")");
		// bundle.setTypeName(executable.getFileType());
		return bundle;
	}

	public void setJavaParameters(JavaPredefinedCommandTemplate template,
			JavaExecutableFile executable) {
		template.addClasspath(null);// TODO
		template.addProperty(null);// TODO
		template.setDisableAssertionPackages(executable
				.getDisableAssertionPackages());
		template.setEnableAssertionPackages(executable
				.getEnableAssertionPackages());
		template.setEnableSystemAssertions(executable
				.isEnableSystemAssertions());
		template.setJarExecutable(executable.isJarExecutable());
		template.setJarFile(executable.getJarFile());
		template.setMainClass(executable.getMainClass());
		template.setMaxMemory(executable.getMaxMemory());
		template.setMinMemory(executable.getMinMemory());
		template.setVerboseExecutionClass(executable.isVerboseExecutionClass());
		template.setVerboseExecutionGC(executable.isVerboseExecutionGC());
		template.setVerboseExecutionJNI(executable.isVerboseExecutionJNI());
	}

	public void setMatlabParameters(MatlabPredefinedCommandTemplate template,
			MatlabExecutableFile executable) {
		template.setDebug(executable.isDebug());
		template.setDisplay(executable.isDisplay());
		template.setJvm(executable.isJvm());
		template.setLogfile(executable.isLogfile());
		template.setLog(executable.getLog());
		template.setSplash(executable.isSplash());
		template.setTiming(executable.isTiming());
	}

	/**
	 * @deprecated generate a map from two string arrays (key[] and values[])
	 * @param variables
	 * @param values
	 * @return
	 */
	public Map<String, String> generateMap(String[] keys, String[] values) {
		Map<String, String> map = new HashMap<String, String>();
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			if ((keys[i] != null) && (keys[i].length() != 0))
				map.put(keys[i], values[i]);
		}
		logger.debug("generate map of size " + length);
		return map;
	}

	
	public Map<String, String> generateMap(ParameterMap parameters ) {
		String[] keys=parameters.getArray("variable");
		String[] values=parameters.getArray("value");
		Map<String, String> map = new HashMap<String, String>();
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			if ((keys[i] != null) && (keys[i].length() != 0))
				map.put(keys[i], values[i]);
		}
		logger.debug("generate map of size " + length);
		return map;
	}
	
	public CommandLineFormatter getCommandLineFormatter() {
		return commandLineFormatter;
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

	public ResourceTypeService getResourceServiceType() {
		return resourceServiceType;
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

	private String output(String[] array){
		return ((array==null)?"null":String.valueOf(array.length));
	}
	/**
	 * 
	 * @param keys
	 * @param values
	 * @param inputs
	 * @param outputs
	 * @param others
	 * @return
	 */
	private VanillaPredefinedCommandTemplate getTemplate(String[] keys,
			String[] values, String[] inputs, String[] outputs, String[] others) {
		VanillaPredefinedCommandTemplate template = new VanillaPredefinedCommandTemplate();
		logger.debug("start to populate command template " + output(keys)
				+ " variable, " + output(inputs) + " inputs, " + output(outputs)
				+ " outputs, " + output(others) + " other");
		try {
			int i = 0;
			for (String entry : inputs) {
				if ((entry != null) && (entry.length() != 0))
					template.addParam(new Param(entry, true,
							Param.ParamType.INPUT.getCode(), i++));
			}
			i = 0;
			for (String entry : outputs) {
				if ((entry != null) && (entry.length() != 0))
					template.addParam(new Param(entry, true,
							Param.ParamType.OUTPUT.getCode(), i++));
			}
			i = 0;
			for (String entry : inputs) {
				if ((entry != null) && (entry.length() != 0))
					template.addParam(new Param(entry, false,
							Param.ParamType.OTHER.getCode(), i++));
			}
		} catch (ParamAlreadyExistsException e) {
			logger.error(e, e);
		}
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			if ((keys[i] != null) && (keys[i].length() > 0))
				template.addEnvironmentVariable(keys[i], values[i]);
		}
		logger.debug("generate map of size " + length);

		return template;
	}

	public VanillaPredefinedCommandTemplate getTemplate(ParameterMap httpParam){
		String[] keys=getArray(httpParam,"variable");
		String[] values=getArray(httpParam,"value");
		String[] inputs=getArray(httpParam,"input");
		String[] others=getArray(httpParam,"other");
		String[] outputs=getArray(httpParam,"output");
		return getTemplate(keys,values,inputs,outputs,others);
		
	}
	private String[] getArray(ParameterMap httpParam,String key){
		if (httpParam.contains(key)){
			String[] values=httpParam.getArray(key);
			logger.debug("parameter "+key+" map size "+values.length);
			if (values.length==0){				
			 values=new String[1];
			 values[0]=httpParam.get(key);	
			 logger.debug("find an 1 value parameter "+values[0]);
			}
			return values;
		}else return new String[0];
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
	public void hideExecutableProperties(Map<String, Property> datatypeMap) {
		datatypeMap.remove(REMOTE_COMPONENT);
		datatypeMap.remove(CREDENTIALS);
		datatypeMap.remove(EXECUTABLE_URL);
		datatypeMap.remove(GROUP);
		datatypeMap.remove(OS);
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
	 * generate running job from all the parameters
	 * 
	 * @param flow
	 *            template flow
	 * @param parameters
	 *            all the parameters except the name/description
	 * @param name
	 *            new flow name
	 * @param description
	 *            new flow description
	 * @return testing {@link Job}
	 */
	public Job run(Flow flow, Map<String, String> parameters, String name,
			String description) throws MeandreServerException {
		return this.testRun(flow, parameters, name, description);
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

	public void setCommandLineFormatter(
			CommandLineFormatter commandLineFormatter) {
		this.commandLineFormatter = commandLineFormatter;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	public void setResourceServiceType(ResourceTypeService resourceServiceType) {
		this.resourceServiceType = resourceServiceType;
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
	 * generate testing job from all the parameters
	 * 
	 * @param flow
	 *            template flow
	 * @param parameters
	 *            all the parameters except the name/description
	 * @param name
	 *            new flow name
	 * @param description
	 *            new flow description
	 * @return testing {@link Job}
	 */
	public Job testRun(Flow flow, Map<String, String> parameters, String name,
			String description) throws MeandreServerException {
		String token = System.currentTimeMillis() + "-token";
		HashMap<String, String> paramMap = new HashMap<String, String>(
				parameters);
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

	public void setArtifactService(ArtifactService artifactService) {
		this.artifactService = artifactService;
	}

	public ArtifactService getArtifactService() {
		return artifactService;
	}

}
