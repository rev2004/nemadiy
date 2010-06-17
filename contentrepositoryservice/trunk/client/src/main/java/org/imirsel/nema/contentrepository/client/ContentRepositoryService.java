package org.imirsel.nema.contentrepository.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.LoginException;

import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.value.BinaryValue;
import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.InvalidBundleException;
import org.imirsel.nema.model.NemaResult;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.NemaResult.ResultType;

import sun.net.www.MimeTable;

/**Implementation of the RepositoryService -provides abstraction over the JCR
 * 
 * @author kumaramit01
 * @since 0.0.1
 * @version 0.0.2 -added check for the presence of result:file node
 * 
 */
final public class ContentRepositoryService implements ArtifactService, ResultStorageService{

	private final Logger logger = Logger.getLogger(ContentRepositoryService.class.getName());
	private Repository repository;
	private final String USERS_DIR ="users";
	private final String FLOWS_DIR ="flows";
	private final String EXECUTOR_BUNDLE_DIR ="executables";
	private final String RESULT_DIR ="results";
	private final String DEFAULT_WORKSPACE="default";
	private final String DEFAULT_PROTOCOL ="jcr";


	/**Validates the content repository -checks various content types are present
	 * 
	 * @param credentials
	 * @return true/false
	 * @throws ContentRepositoryServiceException
	 */
	public final boolean validateNodeTypes(final SimpleCredentials credentials) throws ContentRepositoryServiceException {
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
			namespaceHelper.registerNamespace("result","http://www.imirsel.org/jcr/result");
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
			if(exists){
			nodeType=nodeTypeManager.getNodeType("result:file");
			if(nodeType==null){
				logger.severe("result:file node type is missing");
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
	 * TODO:// ignores workspace...
	 *  @param credentials
	 *  @param resourcePath
	 *  @return returns boolean true/false
	 *  @throws ContentRepositoryServiceException
	 */
	public final boolean exists(final SimpleCredentials credentials,final ResourcePath resourcePath)
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
	 * TODO:// ignores workspace...
	 * @param credentials
	 * @param resourcePath
	 * @return true/false
	 * @throws ContentRepositoryServiceException
	 */
	public final boolean removeExecutableBundle(final SimpleCredentials credentials,
			final ResourcePath resourcePath) throws ContentRepositoryServiceException{
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

			// now remove the properties file if it exists
			String propertyFilePath = resourcePath.getPath()+".properties";
			exists=session.itemExists(propertyFilePath);
			if(exists){
				session.removeItem(propertyFilePath);
			}

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
	 * @param credentials
	 * @param name result file {@link NemaResult}
	 */
	public ResourcePath saveResultFile(final SimpleCredentials credentials,
			NemaResult nemaResult)
			throws ContentRepositoryServiceException {
		System.out.println("0");
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		System.out.println("1");
		logger.info("Validating results: ");
		BundleUtils.validateResult(nemaResult);
		System.out.println("2");
		if(nemaResult.getName().contains(":")){
			throw new ContentRepositoryServiceException("Illegal Character in the Filename ':' ");	
		}
		String resourcePath = null;
		Session session = null;
		System.out.println("3");
		try {
			logger.info("Logging in with credentials ");
			session = repository.login(credentials);
			logger.info("Success Logging in");
			Node root = session.getRootNode();
			Node executableDirNode = null;
			Node userNode=null;
			Node flowNode=null;
			Node executionInstanceDirNode=null;
			Node resultDirNode=null;
			String userDirPath ="/"+USERS_DIR+"/"+credentials.getUserID();
			String flowDirPath = userDirPath +"/"+FLOWS_DIR;
			String executableDirPath=flowDirPath+"/"+EXECUTOR_BUNDLE_DIR;
			String executionInstanceDirPath = executableDirPath +"/"+ nemaResult.getExecutionId();
			String dirName =nemaResult.getExecutionId();
			String resultDir = executionInstanceDirPath +"/"+ RESULT_DIR;
			System.out.println("4");
			boolean exists=session.itemExists(executableDirPath);
			if(!session.itemExists("/"+USERS_DIR)){
				root.addNode(USERS_DIR,"nt:folder");
				session.save();
			}
			System.out.println("5");

			if(!exists){
				logger.info("dir path does not exist -creating the directory path " + executableDirPath);
				System.out.println("6");

				if(session.itemExists(userDirPath)==false){
					Node usersNode=root.getNode(USERS_DIR);
					userNode=usersNode.addNode(credentials.getUserID(), "nt:folder");
					session.save();
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					executionInstanceDirNode=executableDirNode.addNode(dirName, "nt:folder");
					session.save();
					System.out.println("7");
				}else if(session.itemExists(flowDirPath)==false){
					userNode= session.getNode(userDirPath);
					flowNode=userNode.addNode(FLOWS_DIR,"nt:folder");
					session.save();
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					executionInstanceDirNode=executableDirNode.addNode(dirName, "nt:folder");
					session.save();
					System.out.println("8");
				}else if(session.itemExists(executableDirPath)==false){
					flowNode=session.getNode(flowDirPath);
					executableDirNode= flowNode.addNode(EXECUTOR_BUNDLE_DIR,"nt:folder");
					session.save();
					executionInstanceDirNode=executableDirNode.addNode(dirName, "nt:folder");
					session.save();
					System.out.println("9");
				}
				
				
			}else{
				logger.info("dir path exists using the existing directory");
				executableDirNode=session.getNode(executableDirPath);
				if(session.itemExists(executionInstanceDirPath)){
					executionInstanceDirNode=session.getNode(executionInstanceDirPath);
				}else{
					executionInstanceDirNode=executableDirNode.addNode(dirName, "nt:folder");
				}
				System.out.println("10");
			}
			
			if(session.itemExists(resultDir)){
				resultDirNode=session.getNode(resultDir);
			}else{
				resultDirNode=executionInstanceDirNode.addNode(RESULT_DIR,"nt:folder");
			}
			System.out.println("11-0");
			
			MimeTable mt = MimeTable.getDefaultTable();
			String mimeType = mt.getContentTypeFor(nemaResult.getName());
			System.out.println("11-1");
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			System.out.println("11-2");
			Node fileNode = resultDirNode.addNode (nemaResult.getName(), "result:file");
			System.out.println("11-3");
			fileNode.setProperty("fileName", nemaResult.getFileName());
			System.out.println("11-4");
			fileNode.setProperty("typeName", nemaResult.getResultType().toString());
			System.out.println("11-5");
			fileNode.setProperty("execId", nemaResult.getExecutionId());
			System.out.println("11-6");
			fileNode.setProperty("modelClass", nemaResult.getModelClass());
			System.out.println("12");
			long currentTime= System.currentTimeMillis();
			logger.info("creating new result file node: " + nemaResult.getName() );
			Node resNode = fileNode.addNode ("jcr:content", "nt:resource");
			resNode.setProperty ("jcr:mimeType", mimeType);
			resNode.setProperty ("jcr:encoding", "");
			resNode.setProperty ("jcr:data", new BinaryValue(nemaResult.getFileContent()));
			resNode.setProperty ("jcr:lastModified", currentTime);
			System.out.println("13-0");
			Node resultPropertiesNode =  resultDirNode.addNode(nemaResult.getName()+".properties","nt:file");
			System.out.println("13-1");
			mimeType = mt.getContentTypeFor(nemaResult.getName()+".properties");
			System.out.println("13-2");
			Node eresNode = resultPropertiesNode.addNode ("jcr:content", "nt:resource");
			System.out.println("13-3");
			eresNode.setProperty ("jcr:mimeType", mimeType);
			System.out.println("13-4");
			eresNode.setProperty ("jcr:encoding", "");
			System.out.println("13-5");
			eresNode.setProperty ("jcr:data", new BinaryValue(BundleUtils.getPropertyFileAsBytes(nemaResult)));
			System.out.println("13-6");
			eresNode.setProperty ("jcr:lastModified", currentTime);
			System.out.println("13-7");
			resourcePath = fileNode.getPath();
			System.out.println("15");
			logger.info("saving session: " + resNode.getPath());
			session.save();
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				logger.info("logging out of the session");
				session.logout();
			}
		}
		System.out.println("16");
		return new RepositoryResourcePath(DEFAULT_PROTOCOL,DEFAULT_WORKSPACE,resourcePath);
		
		
	}


	/** 
	 * Saves the executable bundle into the content repository and returns a
	 * a ResourcePath
	 * TODO:// ignores workspace...
	 * @param credentials
	 * @param flowInstanceId
	 * @param bundle 
	 * @throws ContentRepositoryServiceException
	 */
	public final ResourcePath saveExecutableBundle(final SimpleCredentials credentials, final String flowInstanceId,
			final ExecutableBundle bundle) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		// make sure that the bundle is good
		BundleUtils.validateBundle(bundle);

		if(bundle.getFileName().contains(":")){
			throw new ContentRepositoryServiceException("Illegal Character in the Filename ':' ");
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
					flowInstanceDirNode=session.getNode(flowInstanceDirPath);
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
			
			if(bundle.getExecutableName()!=null){
				fileNode.setProperty("executableName", bundle.getExecutableName());
			}
			fileNode.setProperty("typeName", bundle.getTypeName());
			fileNode.setProperty("execId", bundle.getId());
			
			if(bundle.getCommandLineFlags()!=null){
				fileNode.setProperty("commandLineFlags", bundle.getCommandLineFlags());
			}

			if(bundle.getEnvironmentVariables()!=null){
				fileNode.setProperty("environmentVariables", BundleUtils.getKeyValuePairs(bundle.getEnvironmentVariables()));
			}

			long currentTime= System.currentTimeMillis();
			logger.info("creating new bundle node: " + bundle.getFileName() );
			Node resNode = fileNode.addNode ("jcr:content", "nt:resource");
			resNode.setProperty ("jcr:mimeType", mimeType);
			resNode.setProperty ("jcr:encoding", "");
			resNode.setProperty ("jcr:data", new BinaryValue(bundle.getBundleContent()));
			resNode.setProperty ("jcr:lastModified", currentTime);

			Node executionPropertiesNode =  flowInstanceDirNode.addNode(bundle.getFileName()+".properties","nt:file");
			mimeType = mt.getContentTypeFor(bundle.getFileName()+".properties");
			Node eresNode = executionPropertiesNode.addNode ("jcr:content", "nt:resource");
			eresNode.setProperty ("jcr:mimeType", mimeType);
			eresNode.setProperty ("jcr:encoding", "");
			try {
				eresNode.setProperty ("jcr:data", new BinaryValue(BundleUtils.getPropertyFileAsBytes(bundle)));
			} catch (InvalidBundleException e) {
				throw new RepositoryException(e.getMessage());
			}
			eresNode.setProperty ("jcr:lastModified", currentTime);




			resourcePath = fileNode.getPath();

			logger.info("saving session: " + resNode.getPath());
			session.save();
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				logger.info("logging out of the session");
				session.logout();
			}
		}
		return new RepositoryResourcePath(DEFAULT_PROTOCOL,DEFAULT_WORKSPACE,resourcePath);
	}

	/**Save the flow to a content repository
	 * TODO:// ignores workspace...
	 * @param credentials
	 * @param flow the Flow object
	 * @param flowContent in bytes
	 * @return resource path 
	 * @throws ContentRepositoryServiceException
	 */
	public final ResourcePath saveFlow(final SimpleCredentials credentials, final Flow flow,
			final String flowInstanceId,final byte[] flowContent) throws ContentRepositoryServiceException {
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
					flowInstanceDirNode=session.getNode(flowInstanceDirPath);
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
			return new RepositoryResourcePath(DEFAULT_PROTOCOL,DEFAULT_WORKSPACE,resourcePath);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
	}
	/**Returns the executable bundle
	 * TODO:// ignores workspace...
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return ExecutableBundle
	 * @throws ContentRepositoryServiceException
	 */
	public final ExecutableBundle getExecutableBundle(final SimpleCredentials credentials,
			final ResourcePath resourcePath) throws ContentRepositoryServiceException {
		if(repository==null){
			throw new ContentRepositoryServiceException("Repository not set");
		}
		Session session = null;

		try {
			session = repository.login(credentials);
			System.out.println("HERE 1" + resourcePath.getPath());
			boolean exists=session.itemExists(resourcePath.getPath());
			if(!exists){
				throw new ContentRepositoryServiceException("Path: " + resourcePath.getPath() + " does not exist.");
			}
			Node node=session.getNode(resourcePath.getPath());
			ExecutableBundle bundle = retrieveExecutableBundleFromNode(node,true);
			return bundle;
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
	}
	
	/** Returns the nema result from the content repository
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return NemaResult 
	 * @throws ContentRepositoryServiceException
	 */
	public final NemaResult getNemaResult(final SimpleCredentials credentials, final ResourcePath resourcePath) throws ContentRepositoryServiceException{
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
			NemaResult nemaResult = retrieveNemaResultFromNode(node, true);
			return nemaResult;
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
		
		
		
	}

	/**
	 * Get bundle metadata
	 * @param credentials
	 * @param resourcePath
	 * @return metadata for the executable bundle
	 * @throws ContentRepositoryServiceException
	 */
	public final ExecutableMetadata getBundleMetadata(final SimpleCredentials credentials,
			final ResourcePath resourcePath) throws ContentRepositoryServiceException{
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
			ExecutableMetadata metadata = retrieveExecutableBundleFromNode(node,false);
			return metadata;
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}
	}

	/**
	 * Returns the filesystem filepath
	 * TODO:// ignores workspace...
	 * @param credentials
	 * @param resourcePath
	 * @return the filesystem filepath for a resource.
	 * @throws ContentRepositoryServiceException
	 */
	public final String getExecutableBundleFSPath(final SimpleCredentials credentials,final ResourcePath resourcePath) throws ContentRepositoryServiceException {
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
			Node execFile=node.getNode("jcr:content");

			String id=execFile.getIdentifier();
			String fsPath = FileSystemPathUtil.getFSPathFromPropertyId(id, "http://www.jcp.org/jcr/1.0/"+execFile.getName(), 0);
			return fsPath;
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null){
				session.logout();
			}
		}

	}
	
	/**
	 *  Return the flow data as bytes.
	 *  @param credentials User's credential to connect to the server
	 *  @param resourcepath ResourcePath of the flow resource
	 *  @return byte data of the flow
	 */
	public byte[] retrieveFlow(SimpleCredentials credentials,
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
			
			Node resNode = node.getNode ("jcr:content");
			String fileName = node.getName();
			Property dataProperty = resNode.getProperty("jcr:data");

			InputStream is = dataProperty.getBinary().getStream();
				long length =dataProperty.getBinary().getSize();
				byte[] data;
				try {
					data =BundleUtils.readByteDataFromStream(is,length);
				} catch (IOException e) {
					throw new ContentRepositoryServiceException(e);
				}
			return data;
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}finally{
			if(session!=null)
				session.logout();
		}

	}


	/**
	 * Set the repository to be used by the service
	 * @param repository
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	

	private NemaResult retrieveNemaResultFromNode(final Node fileNode, boolean copyContent) throws RepositoryException, ContentRepositoryServiceException {
		NemaResult nemaResult = new NemaResult();
		Property fileNameProperty=null;
		Property nameProperty=null;
		Property execIdProperty = null;
		Property typeNameProperty = null;
		Property modelClassProperty = null;
		
		boolean propExists = fileNode.hasProperty("typeName");
		if(propExists){
			typeNameProperty = fileNode.getProperty("typeName");
			String typeVal = typeNameProperty.getString();
			if(typeVal.equalsIgnoreCase("dir"))
				nemaResult.setResultType(ResultType.DIR);
			else if(typeVal.equalsIgnoreCase("file"))
				nemaResult.setResultType(ResultType.FILE);
			else
				throw new ContentRepositoryServiceException("Error invalid Result type: " + typeVal);
		}
		
		propExists = fileNode.hasProperty("fileName");
		if(propExists){
			fileNameProperty = fileNode.getProperty("fileName");
			nemaResult.setFileName(fileNameProperty.getString());
		}
		
		propExists = fileNode.hasProperty("execId");
		if(propExists){
			execIdProperty = fileNode.getProperty("execId");
			nemaResult.setExecutionId(execIdProperty.getString());
		}
		
		propExists = fileNode.hasProperty("name");
		if(propExists){
			nameProperty = fileNode.getProperty("name");
			nemaResult.setName(nameProperty.getString());
		}
		
		propExists = fileNode.hasProperty("modelClass");
		if(propExists){
			modelClassProperty = fileNode.getProperty("modelClass");
			nemaResult.setModelClass(modelClassProperty.getString());
		}
		
		Node resNode = fileNode.getNode ("jcr:content");
		Property mimeTypeProperty = resNode.getProperty("jcr:mimeType");
		Property encodingProperty = resNode.getProperty("jcr:encoding");
		Property dataProperty = resNode.getProperty("jcr:data");
		String fileName = fileNode.getName();
		nemaResult.setName(fileName);
		
		String mimeType=mimeTypeProperty.getString();
		String encodingType = encodingProperty.getString();
		if(copyContent){
			InputStream is = dataProperty.getBinary().getStream();
			long length =dataProperty.getBinary().getSize();
			byte[] data;
			try {
				data =BundleUtils.readByteDataFromStream(is,length);
			} catch (IOException e) {
				throw new ContentRepositoryServiceException(e);
			}
			nemaResult.setFileContent(data);
		}
		
		
		return nemaResult;
	}

	private ExecutableBundle retrieveExecutableBundleFromNode(final Node fileNode, boolean copyContent) 
	throws PathNotFoundException, RepositoryException, ContentRepositoryServiceException {

		ExecutableBundle bundle = new ExecutableBundle();
		Property typeNameProperty=null;
		Property execIdProperty=null;
		Property commandLineFlagsProperty=null;
		Property executableNameProperty=null;
		Property mainClassProperty=null;
		Property envProperty=null;
		String  executableName =null;
		String typeName =null;
		String execId = null;
		String commandLineFlags =null;
		Value[] values=null;
		Map<String,String> env =null;

		boolean propExists=fileNode.hasProperty("executableName");
		if(propExists){
			executableNameProperty=fileNode.getProperty("executableName");
			executableName = executableNameProperty.getString();
		}

		propExists=fileNode.hasProperty("typeName");
		System.out.println("filenode has the property typeName: " + propExists);
		if(propExists){
			typeNameProperty=fileNode.getProperty("typeName");
			typeName = typeNameProperty.getString();
			System.out.println("typeName is: " + typeName);
		}

		propExists=fileNode.hasProperty("execId");
		if(propExists){
			execIdProperty=fileNode.getProperty("execId");
			execId =  execIdProperty.getString();
		}


		propExists=fileNode.hasProperty("commandLineFlags");
		if(propExists){
			commandLineFlagsProperty=fileNode.getProperty("commandLineFlags");
			commandLineFlags = commandLineFlagsProperty.getString();
		}


		propExists=fileNode.hasProperty("environmentVariables");
		if(propExists){
			envProperty=fileNode.getProperty("environmentVariables");
			values = envProperty.getValues();
			env = BundleUtils.getMapfromKeyValuePairs(values);
		}




		bundle.setTypeName(typeName);
		bundle.setId(execId);
		bundle.setCommandLineFlags(commandLineFlags);
		bundle.setEnvironmentVariables(env);
		bundle.setExecutableName(executableName);


		Node resNode = fileNode.getNode ("jcr:content");
		Property mimeTypeProperty = resNode.getProperty("jcr:mimeType");
		Property encodingProperty = resNode.getProperty("jcr:encoding");
		Property dataProperty = resNode.getProperty("jcr:data");
		String fileName = fileNode.getName();

		String mimeType=mimeTypeProperty.getString();
		String encodingType = encodingProperty.getString();
		if(copyContent){
			InputStream is = dataProperty.getBinary().getStream();
			long length =dataProperty.getBinary().getSize();
			byte[] data;
			try {
				data =BundleUtils.readByteDataFromStream(is,length);
			} catch (IOException e) {
				throw new ContentRepositoryServiceException(e);
			}
			bundle.setBundleContent(data);
		}
		bundle.setFileName(fileName);
		bundle.setId(fileNode.getIdentifier());
		return bundle;
	}


}
