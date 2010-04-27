package org.imirsel.nema.contentrepository.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.version.VersionException;
import javax.jcr.LoginException;

import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.value.BinaryValue;
import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.ExecutableBundle.ExecutableType;

import sun.net.www.MimeTable;

/**Implementation of the RepositoryService -provides abstraction over the JCR
 * 
 * @author kumaramit01
 * @since 0.0.1
 * 
 */
public class ContentRepositoryService implements ArtifactService {

	private Logger logger = Logger.getLogger(ContentRepositoryService.class.getName());

	private Repository repository;
	private String USERS_DIR ="users";
	private String FLOWS_DIR ="flows";
	private String EXECUTOR_BUNDLE_DIR ="executables";
	
	
	/**Validates the content repository -checks various content types are present
	 * 
	 * @param credentials
	 * @return
	 * @throws ContentRepositoryServiceException
	 */
	public boolean validateNodeTypes(SimpleCredentials credentials) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;
		logger.info("Logging in with credentials ");
		boolean exists=false;
		try{
			session = repository.login(credentials);
			
			NamespaceHelper namespaceHelper = new NamespaceHelper(session);
			namespaceHelper.registerNamespace("imirsel","http://www.imirsel.org/jcr");
			namespaceHelper.registerNamespace("exec","http://www.imirsel.org/jcr/exec");
			namespaceHelper.registerNamespace("flow","http://www.imirsel.org/jcr/flow");
			
			NodeTypeManager nodeTypeManager=session.getWorkspace().getNodeTypeManager();
			NodeType nodeType=nodeTypeManager.getNodeType("exec:file");
			if(nodeType==null){
				logger.severe("exec:file node type is missing");
				exists=false;
			}else{
				exists=true;
			}
			
		if(exists){
			nodeType = nodeTypeManager.getNodeType("flow:file");
			if(nodeType==null){
				logger.severe("flow:file node type is missing");
				exists=false;
			}else{
				exists=true;
			}
		}
			
			
		}catch (LoginException e){
			throw new ContentRepositoryServiceException(e);
		}catch(NoSuchNodeTypeException nsn){
			exists=false;
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.logout();
			}
		}
		return exists;
	}
	

	/**Checks if a resource node exists. 
	 * 
	 *  @param credentials
	 *  @param resourcePath
	 *  @return returns boolean true/false
	 *  @throws ContentRepositoryServiceException
	 */
	public boolean exists(SimpleCredentials credentials,RepositoryResourcePath resourcePath)
			throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;
		logger.info("Logging in with credentials ");
		try{
			session = repository.login(credentials);
			logger.info("Success Logging in");
			boolean exists=session.itemExists(resourcePath.getPath());
			return exists;
		}catch (LoginException e){
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				session.logout();
			}
		}
		
	}

	/** Removes an executable bundle
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return
	 * @throws ContentRepositoryServiceException
	 */
	public boolean removeExecutableBundle(SimpleCredentials credentials,
			RepositoryResourcePath resourcePath) throws ContentRepositoryServiceException{
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;
		logger.info("Logging in with credentials ");
		try{
			session = repository.login(credentials);
			logger.info("Success Logging in");
			boolean exists=session.itemExists(resourcePath.getPath());
		if(!exists){
			throw new ContentRepositoryServiceException("Path: " + resourcePath.getPath() + " does not exist.");
		}
		session.removeItem(resourcePath.getPath());
		session.save();
		}catch (LoginException e){
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				session.logout();
			}
		}
		return true;
	}


	/** 
	 * Saves the executable bundle into the content repository and returns a
	 * a ResourcePath
	 * 
	 * @param credentials
	 * @param flowInstanceId
	 * @param bundle 
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveExecutableBundle(SimpleCredentials credentials, String flowInstanceId,
			ExecutableBundle bundle) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		String resourcePath = null;
		Session session = null;
		try {

			logger.info("Logging in with credentials ");
			session = repository.login(credentials);
			logger.info("Success Logging in");
			Node root = session.getRootNode();
			Node executableDirNode = null;
			Node userNode=null;
			Node flowNode=null;
			Node flowInstanceDirNode=null;
			String userDirPath ="/"+USERS_DIR+"/"+credentials.getUserID();
			String flowDirPath = userDirPath +"/"+FLOWS_DIR;
			String executableDirPath=flowDirPath+"/"+EXECUTOR_BUNDLE_DIR;
			String flowInstanceDirPath = executableDirPath +"/"+ flowInstanceId;

			logger.info("checking if the executableDirPath exists: " + executableDirPath);
			boolean exists=session.itemExists(executableDirPath);
			if(!session.itemExists("/"+USERS_DIR)){
				root.addNode(USERS_DIR,"nt:folder");
				session.save();
			}

			if(!exists){
				logger.info("dir path does not exist -creating the directory path " + executableDirPath);

				if(session.itemExists(userDirPath)==false){
					Node usersNode=root.getNode(USERS_DIR);
					userNode=usersNode.addNode(credentials.getUserID(), "nt:folder");
					session.save();
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					flowInstanceDirNode=executableDirNode.addNode(flowInstanceId, "nt:folder");
					session.save();

				}else if(session.itemExists(flowDirPath)==false){
					userNode= session.getNode(userDirPath);
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					flowInstanceDirNode=executableDirNode.addNode(flowInstanceId, "nt:folder");
					session.save();
				}else if(session.itemExists(executableDirPath)==false){
					flowNode=session.getNode(flowDirPath);
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					flowInstanceDirNode=executableDirNode.addNode(flowInstanceId, "nt:folder");
					session.save();
				}

			}else{
				logger.info("dir path exists using the existing directory");
				executableDirNode=session.getNode(executableDirPath);
				if(session.itemExists(flowInstanceDirPath)){
					flowInstanceDirNode=root.getNode(flowInstanceDirPath);
				}else{
					flowInstanceDirNode=executableDirNode.addNode(flowInstanceId, "nt:folder");
				}
			}
			MimeTable mt = MimeTable.getDefaultTable();
			String mimeType = mt.getContentTypeFor(bundle.getFileName());

			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			logger.info("mimetype of the bundle: " + mimeType);


			Node fileNode = flowInstanceDirNode.addNode (bundle.getFileName(), "exec:file");
			fileNode.setProperty("executableName", bundle.getExecutableName());
			fileNode.setProperty("typeName", bundle.getTypeName());
			fileNode.setProperty("execId", bundle.getId());
			fileNode.setProperty("commandLineFlags", bundle.getCommandLineFlags());
			fileNode.setProperty("mainClass", bundle.getMainClass());
			
			if(bundle.getEnvironmentVariables()!=null){
				fileNode.setProperty("environmentVariables", getKeyValuePairs(bundle.getEnvironmentVariables()));
			}
			
			
			logger.info("creating new bundle node: " + bundle.getFileName() );
			Node resNode = fileNode.addNode ("jcr:content", "nt:resource");
			resNode.setProperty ("jcr:mimeType", mimeType);
			resNode.setProperty ("jcr:encoding", "");
			resNode.setProperty ("jcr:data", new BinaryValue(bundle.getBundleContent()));
			resNode.setProperty ("jcr:lastModified", System.currentTimeMillis());
			
		
			resourcePath = fileNode.getPath();

			logger.info("saving session: " + resNode.getPath());
			session.save();
		}catch (LoginException e){
			throw new ContentRepositoryServiceException(e);
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				logger.info("logging out of the session");
				session.logout();
			}
		}
		return new RepositoryResourcePath(resourcePath);
	}

	/**Save the flow to a content repository
	 * 
	 * @param credentials
	 * @param flow the Flow object
	 * @param flowContent in bytes
	 * @return resource path 
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveFlow(SimpleCredentials credentials, Flow flow,
			String flowInstanceId, byte[] flowContent) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;
		String resourcePath;
		try {
			logger.info("Logging in with credentials ");
			session = repository.login(credentials);
			logger.info("Success Logging in");
			Node root = session.getRootNode();
			Node userNode=null;
			Node flowNode=null;
			Node flowInstanceDirNode=null;
			String userDirPath ="/"+USERS_DIR+"/"+credentials.getUserID();
			String flowDirPath = userDirPath +"/"+FLOWS_DIR;
			String flowInstanceDirPath = flowDirPath +"/"+ flowInstanceId;

			logger.info("checking if the flowInstanceDirPath exists: " + flowInstanceDirPath);
			boolean exists=session.itemExists(flowInstanceDirPath);
			if(!session.itemExists("/"+USERS_DIR)){
				root.addNode(USERS_DIR,"nt:folder");
				session.save();
			}

			if(!exists){
				logger.info("dir path does not exist -creating the directory path " + flowInstanceDirPath);

				if(session.itemExists(userDirPath)==false){
					Node usersNode=root.getNode(USERS_DIR);
					userNode=usersNode.addNode(credentials.getUserID(), "nt:folder");
					session.save();
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					flowInstanceDirNode=flowNode.addNode(flowInstanceId, "nt:folder");
					session.save();

				}else if(session.itemExists(flowDirPath)==false){
					userNode= session.getNode(userDirPath);
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					flowInstanceDirNode=flowNode.addNode(flowInstanceId, "nt:folder");
					session.save();
				}else{
					flowNode= session.getNode(flowDirPath);
					flowInstanceDirNode= flowNode.addNode(flowInstanceId, "nt:folder");
				}

			}else{
				logger.info("dir path exists using the existing directory");
				flowNode=session.getNode(flowDirPath);
				if(session.itemExists(flowInstanceDirPath)){
					flowInstanceDirNode=root.getNode(flowInstanceDirPath);
				}else{
					flowInstanceDirNode=flowNode.addNode(flowInstanceId, "nt:folder");
				}
				session.save();
			}
			
		
			
			Node fileNode = flowInstanceDirNode.addNode (flowInstanceId, "flow:file");
			fileNode.setProperty("flowName",flow.getName());
			fileNode.setProperty("typeName",flow.getTypeName());
			fileNode.setProperty("description",flow.getDescription());
			fileNode.setProperty("keyWords", flow.getKeyWords());
			fileNode.setProperty("template", flow.isTemplate());
			
			
			
			
			logger.info("creating new flow node: " + flowInstanceId );
			Node resNode = fileNode.addNode ("jcr:content", "nt:resource");
			resNode.setProperty ("jcr:data", new BinaryValue(flowContent));
			resNode.setProperty ("jcr:lastModified", System.currentTimeMillis());
			
		
			resourcePath = fileNode.getPath();

			logger.info("saving session: " + resNode.getPath());
			session.save();
			
			
			return new RepositoryResourcePath(resourcePath);
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
	}

	/**Returns the executable bundle
	 * 
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return ExecutableBundle
	 * @throws ContentRepositoryServiceException
	 */
	public ExecutableBundle getExecutableBundle(SimpleCredentials credentials,
			ResourcePath resourcePath) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;

		try {
			session = repository.login(credentials);
			boolean exists=session.itemExists(resourcePath.getPath());
			if(!exists){
				throw new ContentRepositoryServiceException("Path: " + resourcePath.getPath() + " does not exist.");
			}
			Node node=session.getNode(resourcePath.getPath());
			ExecutableBundle bundle = retrieveExecutableBundleFromNode(node);
			return bundle;
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}



	}

	/**Get bundle metadata
	 * @param credentials
	 * @param resourcePath
	 * @return metadata for the executable bundle
	 * @throws ContentRepositoryServiceException
	 */
	public ExecutableMetadata getBundleMetadata(SimpleCredentials credentials,
			ResourcePath resourcePath) throws ContentRepositoryServiceException{
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session=null;
		try {
			session = repository.login(credentials);
			boolean exists=session.itemExists(resourcePath.getPath());
			if(!exists){
				throw new ContentRepositoryServiceException("Path: " + resourcePath.getPath() + " does not exist.");
			}
			Node node=session.getNode(resourcePath.getPath());
			ExecutableMetadata metadata = retrieveExecutableMetadataFromNode(node);
			return metadata;

		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	private ExecutableBundle retrieveExecutableBundleFromNode(Node fileNode) 
	throws PathNotFoundException, RepositoryException, ContentRepositoryServiceException {
		
		Property executableNameProperty=fileNode.getProperty("executableName");
		Property typeNameProperty=fileNode.getProperty("typeName");
		Property execIdProperty=fileNode.getProperty("execId");
		Property commandLineFlagsProperty=fileNode.getProperty("commandLineFlags");
		Property mainClassProperty=fileNode.getProperty("mainClass");
		Property envProperty=fileNode.getProperty("environmentVariables");
		
		ExecutableBundle bundle = new ExecutableBundle();
		
		String  executableName = executableNameProperty.getString();
		String typeName = typeNameProperty.getString();
		String execId =  execIdProperty.getString();
		String commandLineFlags = commandLineFlagsProperty.getString();
		String mainClass = mainClassProperty.getString();
		Value[] values = envProperty.getValues();
		Map env = getMapfromKeyValuePairs(values);
		
		bundle.setTypeName(ExecutableType.valueOf(typeName));
		bundle.setId(execId);
		bundle.setCommandLineFlags(commandLineFlags);
		bundle.setMainClass(mainClass);
		bundle.setEnvironmentVariables(env);
		bundle.setExecutableName(executableName);
		
		
		Node resNode = fileNode.getNode ("jcr:content");
		Property mimeTypeProperty = resNode.getProperty("jcr:mimeType");
		Property encodingProperty = resNode.getProperty("jcr:encoding");
		Property dataProperty = resNode.getProperty("jcr:data");
		String fileName = fileNode.getName();

		String mimeType=mimeTypeProperty.getString();
		String encodingType = encodingProperty.getString();
		InputStream is = dataProperty.getBinary().getStream();
		long length =dataProperty.getBinary().getSize();
		byte[] data;
		try {
			data = readByteDataFromStream(is,length);
		} catch (IOException e) {
			throw new ContentRepositoryServiceException(e);
		}

		
		bundle.setBundleContent(data);
		bundle.setFileName(fileName);
		bundle.setTypeName(ExecutableBundle.ExecutableType.C);
		bundle.setId(fileNode.getIdentifier());



		return bundle;
	}
		
	private ExecutableMetadata retrieveExecutableMetadataFromNode(Node fileNode) 
	throws PathNotFoundException, RepositoryException, ContentRepositoryServiceException {
		
		Property executableNameProperty=fileNode.getProperty("executableName");
		Property typeNameProperty=fileNode.getProperty("typeName");
		Property execIdProperty=fileNode.getProperty("execId");
		Property commandLineFlagsProperty=fileNode.getProperty("commandLineFlags");
		Property mainClassProperty=fileNode.getProperty("mainClass");
		Property envProperty=fileNode.getProperty("environmentVariables");
		
		ExecutableBundle bundle = new ExecutableBundle();
		
		String  executableName = executableNameProperty.getString();
		String typeName = typeNameProperty.getString();
		String execId =  execIdProperty.getString();
		String commandLineFlags = commandLineFlagsProperty.getString();
		String mainClass = mainClassProperty.getString();
		Value[] values = envProperty.getValues();
		Map env = getMapfromKeyValuePairs(values);
		
		bundle.setTypeName(ExecutableType.valueOf(typeName));
		bundle.setId(execId);
		bundle.setCommandLineFlags(commandLineFlags);
		bundle.setMainClass(mainClass);
		bundle.setEnvironmentVariables(env);
		bundle.setExecutableName(executableName);
		
		
		Node resNode = fileNode.getNode ("jcr:content");
		Property mimeTypeProperty = resNode.getProperty("jcr:mimeType");
		Property encodingProperty = resNode.getProperty("jcr:encoding");
		String fileName = fileNode.getName();

		String mimeType=mimeTypeProperty.getString();
		String encodingType = encodingProperty.getString();
		bundle.setFileName(fileName);
		bundle.setTypeName(ExecutableBundle.ExecutableType.C);
		bundle.setId(fileNode.getIdentifier());

		return bundle;
	}

	private Map<String,String> getMapfromKeyValuePairs(Value[] values) 
	throws ValueFormatException, IllegalStateException, RepositoryException {
		Map<String,String> hmap = new HashMap<String,String>();
		for(Value value:values){
			String keyValue = value.getString();
			String[] kv=keyValue.split("=");
			if(kv.length==2){
				hmap.put(kv[0], kv[1]);
			}
		}
		return hmap;
	}

	private byte[] readByteDataFromStream(InputStream is, long length) throws IOException {
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read the node data");
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}





	
	private String[] getKeyValuePairs(Map<String, String> environmentVariables) {
		String[] values = new String[environmentVariables.size()];
		int count=0;
		for(String key:environmentVariables.keySet()){
			values[count] = key+"="+environmentVariables.get(key);
		}
		return values;
	}


}
