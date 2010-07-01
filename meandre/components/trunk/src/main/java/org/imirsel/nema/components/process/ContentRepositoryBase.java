package org.imirsel.nema.components.process;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.components.RemoteExecutableComponent;
import org.imirsel.nema.contentrepository.client.CompressionUtils;
import org.imirsel.nema.contentrepository.client.ContentRepositoryService;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.contentrepository.client.ResultStorageService;
import org.imirsel.nema.model.NemaContentRepositoryFile;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.ResultType;

import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;


/** Base Class for Content Repository based Nema Components
 * 
 *
 * @author kumaramit01
 * @since 0.6.0
 */
public abstract class ContentRepositoryBase extends NemaComponent implements RemoteExecutableComponent{
	
	@ComponentProperty(defaultValue = "test:test", description = "Credentials to Login to the content repository", name = "_credentials")
	private static final String PROPERTY_1="_credentials";

	
	@ComponentProperty(defaultValue = "rmi://nema-dev.lis.illinois.edu:/..", description = "Content Repository URI", name = "_contentRepositoryUri")
	private static final String PROPERTY_2="_contentRepositoryUri";

	
	
	private static ResultStorageService resultStorageService;
	private SimpleCredentials credentials;
	private String flowInstanceId;
	
	
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
		try{
		String _credentials = ccp.getProperty(PROPERTY_1);
		String contentRepositoryUri = ccp.getProperty(PROPERTY_2);
		this.flowInstanceId = ccp.getFlowExecutionInstanceID();
		setCredentials(parseCredentials(_credentials));
		synchronized(ContentRepositoryBase.class){
		if(resultStorageService==null){
			ClientRepositoryFactory factory = new ClientRepositoryFactory();
			Repository repository;
			repository = factory.getRepository(contentRepositoryUri);
			ContentRepositoryService crs = new ContentRepositoryService();
			crs.setRepository(repository);
			resultStorageService = crs;
		}
		}
		}catch(Exception ex){
			throw new ComponentExecutionException(ex);
		}
		this.initializeNema(ccp);	
	}
	






	public void execute(ComponentContext context) throws ComponentExecutionException,ComponentContextException{
		this.executeNema(context);
	}
	
	
	public void dispose(ComponentContextProperties componentContextProperties) 
	throws ComponentContextException{
		super.dispose(componentContextProperties);
		this.disposeNema(componentContextProperties);
	}
	
	
	public abstract void initializeNema(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException;
	
	
	public abstract void executeNema(ComponentContext componentContext) 
	throws ComponentExecutionException, ComponentContextException;
	
	
	public abstract void disposeNema(ComponentContextProperties componentContextProperties) 
	throws ComponentContextException;
	
	
	
	private SimpleCredentials parseCredentials(String credentialsString) throws ComponentExecutionException {
		String[] splits = credentialsString.split(":");
		String username = splits[0];
		String password = splits[1];
		if(splits.length!=2){
			throw new ComponentExecutionException("Invalid credentials");
		}
		return new SimpleCredentials(username,password.toCharArray());
	}
	
	
		/**Save the file to the content repository
	 * 
	 * @param file
	 * @param model
	 * @return The repository resource path to the file in the content repository
	 * @throws IOException
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveFileToContentRepository(final File file, final String model) throws IOException, ContentRepositoryServiceException{
		if(!file.exists()){
			throw new FileNotFoundException("File " + file.getAbsolutePath() + " does not exist");
		}
		if(!file.canRead()){
			throw new IOException("File " + file.getAbsolutePath() + " could not be read.");
		}
		NemaContentRepositoryFile nemaResult = createNemaContentRepositoryFile(file, this.getAbsoluteProcessWorkingDirectory(),model);
		ResourcePath rrp=this.resultStorageService.saveResultFile(this.getCredentials(), nemaResult);
		return rrp;
	}

	


	
	

	private void setCredentials(SimpleCredentials credentials) {
		this.credentials = credentials;
	}

	public SimpleCredentials getCredentials() {
		return credentials;
	}



	private NemaContentRepositoryFile createNemaContentRepositoryFile(File file, String relativeLoc, String model) throws IOException {
		NemaContentRepositoryFile nemaResult = new NemaContentRepositoryFile();
		String path = file.getCanonicalPath();
		byte[] fileContent= null;
		CompressionUtils cutils= CompressionUtils.getInstanceOf();
		fileContent = cutils.compress(path,relativeLoc);
		
		if(fileContent==null){
			throw new RuntimeException("file byte contents size is null " + path);
		}
		
		nemaResult.setExecutionId(this.flowInstanceId);
		nemaResult.setFileContent(fileContent);
		String parentPath=file.getParent();
		nemaResult.setModelClass(model);
		nemaResult.setName(file.getName());
		nemaResult.setFileName(path);
		if(file.isDirectory()){
			nemaResult.setResultType(ResultType.DIR);
		}else{
			nemaResult.setResultType(ResultType.FILE);
		}
		nemaResult.setPath(parentPath);
		return nemaResult;
	}





}
