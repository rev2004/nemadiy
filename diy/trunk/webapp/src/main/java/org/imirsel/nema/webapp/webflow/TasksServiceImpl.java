package org.imirsel.nema.webapp.webflow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.jcr.SimpleCredentials;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.RequestContext;

/**
 * Action class for the task template flow generation webflow.
 * Notice: that for the DataTypeMap Map<String,Property>, key is strictly for display,
 * while the Property.name should be used as name sent to content repository 
 * 
 * @author gzhu1
 * @since 0.6.0
 * 
 */
public class TasksServiceImpl {

	static private Log logger = LogFactory.getLog(TasksServiceImpl.class);

	private FlowService flowService;
	private UserManager userManager;
	private ArtifactService artifactService;
	private String uploadDirectory;
	private String physicalDir;
	private String webDir;

	// Component properties that should be hidden.
	final static String REMOTE_COMPONENT = "_remoteDynamicComponent";
	final static String CREDENTIALS = "_credentials";
	final static String EXECUTABLE_URL = "profileName";
	final static String OS = "_os";
	final static String GROUP = "_group";

	final static Set<String> HIDDEN_PROPERTIES=new HashSet<String>();
	{
		HIDDEN_PROPERTIES.add(REMOTE_COMPONENT);
		HIDDEN_PROPERTIES.add(CREDENTIALS);
		HIDDEN_PROPERTIES.add(EXECUTABLE_URL);
		HIDDEN_PROPERTIES.add(OS);
		HIDDEN_PROPERTIES.add(GROUP);				
	}
	
	public String getPhysicalDir() {
		return physicalDir;
	}

	public String getWebDir() {
		return webDir;
	}

	/**
	 * Send executable bundle to content repository, replace/add the new 
	 * ResourcePath of executable bundle in the the executableMap.
	 * 
	 * @param component
	 * @param bundle
	 * @param uuid
	 * @param executableMap
	 *            Note that this map is going to be modified, old path is
	 *            replaced by new path.
	 * @throws ContentRepositoryServiceException
	 */
	public void addExecutable(final Component component,
			final Map<String, Property> propertyMap,
			final UploadedExecutableBundle bundle, final UUID uuid,
			Map<Component, ResourcePath> executableMap,
			MessageContext messageContext)
			throws ContentRepositoryServiceException {

		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		String credentialString = credential.getUserID() + ":"
				+ new String(credential.getPassword());
		propertyMap.get(CREDENTIALS).setValue(credentialString);
		propertyMap.get(REMOTE_COMPONENT).setValue("true");
		propertyMap.get(OS).setValue(bundle.getPreferredOs());
		propertyMap.get(GROUP).setValue(bundle.getGroup());
		deleteExecutableFromRepository(executableMap.get(component),
				credential);
		ResourcePath path = artifactService.saveExecutableBundle(credential,
				uuid.toString(), bundle);
		executableMap.put(component, path);
		String uri = path.getProtocol() +":"+ path.getWorkspace() +"://"+path.getPath();
		propertyMap.get(EXECUTABLE_URL).setValue(uri);
		if (path != null) {
			// MessageContext messageContext=requestContext.getMessageContext();
			messageContext.addMessage(new MessageBuilder().info().defaultText(
					"Executable profile was successfully saved.").build());
		} else {
			throw new ContentRepositoryServiceException(

			"An error occurred while saving the executable profile: "
					+ bundle.getFileName());
		}

	}

	/**
	 * Remove the executable bundle from the content repository.
	 * 
	 * @param component
	 * @param executableMap
	 * @param datatypeMap
	 */
	public void removeExecutable(Component component,
			Map<Component, ResourcePath> executableMap,
			Map<String, Property> propertyMap)
			throws ContentRepositoryServiceException {
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		if (executableMap.containsKey(component)) {
			ResourcePath oldPath = executableMap.get(component);
			deleteExecutableFromRepository(oldPath, credential);
			executableMap.remove(component);
			propertyMap.get(EXECUTABLE_URL).setValue("");
		}
	}

	/**
	 * clear all executable bundles sent over to content repository
	 * 
	 * @param executableMap
	 */
	public void clearBundles(Map<Component, ResourcePath> executableMap)
			throws ContentRepositoryServiceException {
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		for (Component component : executableMap.keySet()) {
			ResourcePath oldPath = executableMap.get(component);
			deleteExecutableFromRepository(oldPath, credential);
		}
	}

	private void deleteExecutableFromRepository(
			ResourcePath path, SimpleCredentials credential)
			throws ContentRepositoryServiceException {
		try {
			if ((path != null) && (artifactService.exists(credential, path))) {
				logger.info("remove from content repository executable bundle:"
						+ path);
				artifactService.removeExecutableBundle(credential, path);
			}
		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
			throw (e);
		}

	}

	
	/**
	 * Retrieve the Executable bundle with resource path {@link path}, and
	 * populated the extra fields for UploadedExecutableBundle
	 * 
	 * @param path
	 * @param datatypeMap
	 * @return
	 */
	public UploadedExecutableBundle findBundle(ResourcePath path,
			Map<String, Property> propertyMap) {
		SimpleCredentials credential = userManager.getCurrentUserCredentials();
		UploadedExecutableBundle bundle = null;
		try {
			if ((path != null) && (artifactService.exists(credential, path))) {
				ExecutableBundle oldBundle = artifactService
						.getExecutableBundle(credential, path);
				bundle = new UploadedExecutableBundle(oldBundle);
				if (bundle == null)
					bundle = new UploadedExecutableBundle();
				bundle.setPreferredOs(propertyMap.get(OS).getValue());
				bundle.setGroup(propertyMap.get(GROUP).getValue());
			}
		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}
		return bundle;
	}

	/**
	 * return the list of flow templates belong to type, all templates are
	 * returned if type is not valid. used a very lenient criteria, check both
	 * flowtype and keywords
	 * 
	 * @param type
	 *            controlled by {@link Flow.FlowType}, first letter needs
	 *            capitalize.
	 * @return
	 */
	public List<Flow> getFlowTemplates(String type) {
		Flow.FlowType flowType = (type == null ? null : Flow.FlowType
				.toFlowType(type));

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


	private String getFullyQualifiedPropertyName(String component, String propertyName) {
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

	/**
	 * This method is necessary for render tag
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
	 * Tests whether or not the supplied properties are from a remote component.
	 * 
	 * @param properties Map of property names to {@link Property} instances.
	 * @return True if the properties are from a remote component.
	 */
	public boolean areFromRemoteComponent(Map<String, Property> properties) {
		return properties.keySet().contains(REMOTE_COMPONENT)
				&& (properties.get(REMOTE_COMPONENT).getDefaultValue()
						.toString().equalsIgnoreCase("true"));
	}

	/**
	 * Create a job with all the properties in datatypeMaps.
	 * 
	 * @param flow
	 * @param datatypeMaps
	 * @param name
	 * @param description
	 * @return the job object created with the parameters 
	 * @throws MeandreServerException
	 */
	public Job run(Flow flow,
			Map<Component, Map<String, Property>> componentMap, String name,
			String description) throws MeandreServerException {
		HashMap<String, String> paramMap = new HashMap<String, String>();

		Component component;
		for (Entry<Component, Map<String, Property>> mapsEntry : componentMap
				.entrySet()) {
			component = mapsEntry.getKey();
			for (Entry<String, Property> entry : mapsEntry.getValue()
					.entrySet()) {
				paramMap.put(
						getFullyQualifiedPropertyName(component.getInstanceUri(), entry.getValue().getName()),
						entry.getValue().getValue());
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
		}

		if (name == null) {
			name = paramMap.get("name");
			if (name == null) {
				name = flow.getName() + File.separator + token;
			}
		}
		if (description == null) {
			description = paramMap.get("description");
			if (description == null) {
				description = flow.getDescription() + " for flow: " + token;
			}
		}
		long userId = user.getId();
		Flow instance = new Flow();
		instance.setCreatorId(userId);
		instance.setDateCreated(new Date());
		instance.setInstanceOf(flow);
		instance.setKeyWords(flow.getKeyWords());
		instance.setName(name);
		instance.setTemplate(false);
		instance.setDescription(description);
		instance.setType(flow.getType());
		instance.setTypeName(flow.getTypeName());

		SimpleCredentials credential=userManager.getCurrentUserCredentials();
		instance = this.flowService.createNewFlow(credential, instance, paramMap, flowUri, user
				.getId());
		long instanceId = instance.getId();

		Job job = this.flowService.executeJob(credential,token, name, description,
				instanceId, user.getId(), user.getEmail());
		return job;

	}

	public void test(){
		throw new org.imirsel.nema.flowservice.ServiceException("custom exception");
	}
	public void setArtifactService(ArtifactService artifactService) {
		this.artifactService = artifactService;
	}

	/**
	 * Extract a {@code List} of {@link Component}s from the given {@code Map}
	 * of {@link Components}. The order of the list should not be changed.
	 * 
	 * @param componentMap Map of {@link Component}s to component properties.
	 * @return List of just {@link Component}s extracted from the supplied map.
	 */
	public List<Component> extractComponentList(
			Map<Component, Map<String, Property>> componentMap) {
		List<Component> list = new ArrayList<Component>(componentMap.keySet());
		Collections.sort(list);
		return list;
	}

	/**
	 * Load the {@link Components} for the given {@link Flow}.
	 * 
	 * @param flow The {@link Flow} for which the properties should be loaded.
	 * @return Map containing {@link Component}s to {@link Properties}.
	 */
	public Map<Component, Map<String, Property>> loadFlowComponents(Flow flow) {
		Map<Component, List<Property>> componentsToPropertyLists = 
		   flowService.getAllComponentsAndPropertyDataTypes(flow.getUri());
		Map<Component, Map<String, Property>> componentsToPropertyMaps = 
		   new HashMap<Component, Map<String, Property>>();
		
		for (Entry<Component, List<Property>> entry:componentsToPropertyLists.entrySet()) {
			Component component=entry.getKey();
			if (!component.isHidden()) {
				Map<String,Property> properties=new TreeMap<String,Property>( );
				for (Property property:entry.getValue()){
					properties.put(property.getName(), property);
				}
				
				componentsToPropertyMaps.put(component,properties);
			}

		}

		return componentsToPropertyMaps;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	/**
	 * By default this field is set by method {@link setUploadingPaths}, and
	 * this field <B>must</B> match field webDir {@link setWebDir}. It is the
	 * physical directory used to store the uploading field of file type. This
	 * one should end with proper "/" or "\" TODO this is a bad implementation
	 * of file upload, it needs more robust implementation
	 * 
	 * @param physicalDir
	 */
	public void setPhysicalDir(String physicalDir) {
		this.physicalDir = physicalDir;
	}

	/**
	 * Set the upload directory, this one should start with "/" and end with no
	 * "/", an empty one would be "". The method should check on it and correct
	 * it if possible.
	 * 
	 * TODO this is a bad implementation of file upload, it needs more robust
	 * implementation
	 * 
	 * @param uploadDirectory
	 */
	public void setUploadDirectory(String uploadDirectory) {
		if ((!uploadDirectory.isEmpty()) && (!uploadDirectory.startsWith("/"))) {
			uploadDirectory = "/" + uploadDirectory;
		}
		if ((!uploadDirectory.isEmpty()) && (uploadDirectory.endsWith("/"))) {
			uploadDirectory = uploadDirectory.substring(0, uploadDirectory
					.length() - 1);
		}
		this.uploadDirectory = uploadDirectory;
	}

	/**
	 * set the real physical/web path from the servlet context/request for
	 * uploading, default behavior, when webDir has value (set by outside), skip
	 * this step. TODO this is a bad implementation of file upload, it needs
	 * more robust implementation
	 * 
	 * @param externalContext
	 * @param httpServletRequest
	 */
	public void buildUploadPath(ExternalContext externalContext, UUID uuid) {
		if ((webDir == null) || (webDir.isEmpty())) {
			ServletContext context = (ServletContext) externalContext
					.getNativeContext();
			HttpServletRequest req = (HttpServletRequest) externalContext
					.getNativeRequest();
			String subDir = uploadDirectory + "/" + req.getRemoteUser() + "/"
					+ uuid + "/";
			physicalDir = context.getRealPath(subDir);
			// Create the directory if it doesn't exist
			if (!physicalDir.endsWith(File.separator)) {
				physicalDir = physicalDir + File.separator;
			}

			webDir = "http://" + req.getServerName() + ":"
					+ req.getServerPort() + req.getContextPath() + subDir;

			logger.info("Built the upload path: " + physicalDir + ","
							+ webDir);
		}
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * By default this field is set by method {@link setUploadingPaths}, and
	 * this field <B>must</B> match field physicalDir {@link setPhysicalDir}. It
	 * is the web directory used to store the uploading field of file type. This
	 * one should end with "/"
	 * 
	 * @param webDir
	 */
	public void setWebDir(String webDir) {
		this.webDir = webDir;
	}

   /**
    * Format the provided properties for display to the user.
    * 
    * @param properties Map from property names to {@link Properties}.
    * @return Map of {@link Properties} that have been formatted for display
    * purposes.
    */
   public Map<String, Property> formatPropertiesForDisplay(
         Map<String, Property> properties) {
      Map<String, Property> tmpProps = new HashMap<String, Property>(properties);
      
      // For properties of remote components, remove properties that 
      // should be hidden.
      if (areFromRemoteComponent(properties)) {
        
         tmpProps.remove(REMOTE_COMPONENT);
         tmpProps.remove(CREDENTIALS);
         tmpProps.remove(EXECUTABLE_URL);
         tmpProps.remove(GROUP);
         tmpProps.remove(OS);
      }
      
      //Map<String, Property> formattedProps = new TreeMap<String, Property>();
      
      // Title case the property names.
//      for (Map.Entry<String, Property> entry : tmpProps.entrySet()) {
//         String key = entry.getKey();
//         
//         assert !key.isEmpty():"key is empty";
//         StringBuilder newkey=new StringBuilder(key.substring(0, 1).toUpperCase());
//         for (int i=1;i<key.length();i++){
//          boolean currentCharIsUpper = Character.isUpperCase(key.charAt(i));
//          boolean previousCharIsNotUpper = !Character.isUpperCase(key.charAt(i-1));
//          boolean previousCharIsNotSpace = key.charAt(i-1)!=' ';
//        	 if (currentCharIsUpper && previousCharIsNotUpper && previousCharIsNotSpace){
//        		 newkey.append(" ");
//        	 }
//        	 newkey.append(key.charAt(i));
//         }
//         formattedProps.put(newkey.toString(), entry.getValue());
//      }
      return tmpProps;
   }

	/**
	 * Update the property values using the user-submitted parameters.
	 * 
	 * @param parameters HTTP request parameters.
	 * @param properties Properties to be updated.
	 */
	public void updateProperties(ParameterMap parameters,
			Map<String, Property> properties) {
		for (String key : properties.keySet()) {
			Property property = properties.get(key);
			if (parameters.contains(property.getName())) {
				List<DataTypeBean> ltb = property.getDataTypeBeanList();
				if ((ltb != null) && (!ltb.isEmpty())
						&& (ltb.get(0).getRenderer() != null)
						&& (ltb.get(0).getRenderer().endsWith("FileRenderer"))) {
					MultipartFile file = parameters.getMultipartFile(property
							.getName());
					if ((file != null) && (!file.isEmpty())) {
						File dirPath = new File(physicalDir);

						if (!dirPath.exists()) {
							dirPath.mkdirs();
						}
						String filename = file.getOriginalFilename();
						File uploadedFile = new File(physicalDir + filename);
						try {
							file.transferTo(uploadedFile);
						} catch (IllegalStateException e) {

							logger.error(e, e);
						} catch (IOException e) {

							logger.error(e, e);
						}
						property.setValue(webDir + filename);
					}
				} else {
					property.setValue(parameters.get(property.getName()));
				}
			}
			
		}// for loop
	}
	
}
