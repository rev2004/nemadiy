package org.imirsel.nema.components.jcr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.contentrepository.client.CompressionUtils;
import org.imirsel.nema.contentrepository.client.ContentRepositoryService;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.contentrepository.client.ResultStorageService;

import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResultType;
import org.imirsel.nema.model.NemaContentRepositoryFile;


import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

@Component(creator = "Amit Kumar", description = "Reads result file from the content repository <br/> and writes the file to the result location",
		name = "ContentRepositoryToFileSystemComponent", tags = "content_repository")
public class ContentRepositoryToFileSystemComponent extends NemaComponent {

	@ComponentProperty(defaultValue = "rmi://nema.lis.uiuc.edu:2099/jackrabbit.repository", description = "Component Repository RMI", name = "contentrepositoryUri")
	private static final String PROPERTY_1 = "contentrepositoryUri";
	
	@ComponentProperty(defaultValue = "test:test", description = "Content Repository Credential", name = "_credentials")
	private static final String PROPERTY_2 = "_credentials";
	
	@ComponentInput(description = "The process artifact list with the jcr uri.", name = "listProcessArtifactIn")
	private static final String DATA_IN_1 ="listProcessArtifactIn";
	
	@ComponentOutput(description = "The process artifact list with the file uri.", name = "listProcessArtifactOut")
	private static final String DATA_OUT_1 ="listProcessArtifactOut";
	
	
	private SimpleCredentials credentials;
	private ResultStorageService resultStorageService;
	
	
	@Override
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {		
		super.initialize(ccp);
		String RMI_URL=ccp.getProperty(PROPERTY_1);
		String credentialString=ccp.getProperty(PROPERTY_2);
		String[] splits = credentialString.split(":");
		
		if(splits.length!=2){
			throw new ComponentExecutionException("Invalid credentials: " + credentialString);
		}
		
		String username = splits[0];
		String password = splits[1];
		credentials = new SimpleCredentials(username,password.toCharArray());
		
		ClientRepositoryFactory factory = new ClientRepositoryFactory();
		Repository repository = null;
		try {
			repository = factory.getRepository(RMI_URL);
		} catch (MalformedURLException e) {
			throw new ComponentExecutionException(e.getMessage());
		} catch (ClassCastException e) {
			throw new ComponentExecutionException(e.getMessage());
		} catch (RemoteException e) {
			throw new ComponentExecutionException(e.getMessage());
		} catch (NotBoundException e) {
			throw new ComponentExecutionException(e.getMessage());
		}
		
		if(repository==null){
			throw new ComponentExecutionException("Could not connect to the content repository: " + RMI_URL);
		}
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		resultStorageService = crs;
	}

	@Override
	public void execute(ComponentContext componentContext)
			throws ComponentExecutionException, ComponentContextException {
		List<ProcessArtifact> processArtifactList=(List<ProcessArtifact>)componentContext.getDataComponentFromInput(DATA_IN_1);
		List<ProcessArtifact> processArtifactListOutput = new ArrayList<ProcessArtifact>(10);
		for(ProcessArtifact processArtifact:processArtifactList){
			componentContext.getLogger().info("=====> IN Process Artifact: " + processArtifact.getResourcePath());
		if(!processArtifact.getResourcePath().startsWith("jcr:")){
			processArtifactListOutput.add(processArtifact);
		}else{
			try {
				ProcessArtifact processArtifactModified = provisionArtifact(processArtifact);
			
				componentContext.getLogger().info("OUT Process Artifact: " + processArtifactModified.getResourcePath());
				processArtifactListOutput.add(processArtifactModified);
			} catch (ContentRepositoryServiceException e) {
				throw new ComponentExecutionException(e);
			}
		
		}
		}
		componentContext.getLogger().info("SIZE OF THE LIST BEFORE PUSH: "+processArtifactListOutput.size() );
		componentContext.getLogger().info("pushing: " + processArtifactListOutput.get(0).getResourcePath()+"\n\n");
		componentContext.pushDataComponentToOutput(DATA_OUT_1, processArtifactListOutput);
		
	}
	
	
	
	private ProcessArtifact provisionArtifact(ProcessArtifact processArtifact) throws ContentRepositoryServiceException {
		System.out.println("CAME HERE TO PROVISION");
		String resultLoc=this.getAbsoluteResultLocationForJob();
		
		System.out.println("RESULT LOCATION: "+resultLoc);
		String path=processArtifact.getResourcePath();
		int loc=path.indexOf("://");
		path = path.substring(loc+"://".length());
		String workspaceName="default";
		String pcol ="jcr";
		
		System.out.println("PROTOCOL: " + pcol + " workspaceName: " + workspaceName + " path: " + path);
		RepositoryResourcePath rrp = new RepositoryResourcePath(pcol, workspaceName, path);
		System.out.println("ProcessArtifact0");
		CompressionUtils cutils= CompressionUtils.getInstanceOf();
		System.out.println("ProcessArtifact1");
		if(this.resultStorageService==null){
			System.out.println("RESULT STORAGE SERVICE IS NULL");
		}else{
			System.out.println("RESULT STORAGE SERVICE IS NOT NULL");
		}
		NemaContentRepositoryFile result=this.resultStorageService.getNemaContentRepositoryFile(credentials, rrp);
		System.out.println("ProcessArtifact2");

		byte[] content=result.getFileContent();
		//String modelClass=result.getModelClass();
		//String fileName=result.getFileName();
		System.out.println("3");
		String name=result.getName();
		System.out.println("DECOMPRESSING: " + name + " size: " + content.length);
		List<String> fileList=null;
		try {
			fileList=cutils.decompress(content, resultLoc, name);
		} catch (ZipException e1) {
			throw new ContentRepositoryServiceException(e1);
		} catch (IOException e1) {
			throw new ContentRepositoryServiceException(e1);
		}
		
		System.out.println("number of files after decompressing: " + fileList.size());
		
		for(String fname:fileList){
			System.out.println("FILE NAME: "+fname);
		}

		String jcrPath = processArtifact.getResourcePath();
		int resultIndex = jcrPath.indexOf("results");
		String resultPath = jcrPath;
		if(resultIndex!=-1){
			resultPath = jcrPath.substring(resultIndex+"results".length());
		}
		File localResult = new File(resultLoc,resultPath);
		System.out.println("LOCAL RESULT LOCATION: " + localResult.getAbsolutePath());
		String type="file";
		if(result.getResultType() == ResultType.DIR){
			type= "dir";	
		}else if(result.getResultType() == ResultType.FILE){
			type="file";
		}
		
		ProcessArtifact pa = new ProcessArtifact(localResult.getAbsolutePath(), type, result.getModelClass());
		return pa;
	}

	@Override
	public void dispose(ComponentContextProperties ccp)
	throws ComponentContextException {
		//super.dispose(ccp);
	}
	
	

}
