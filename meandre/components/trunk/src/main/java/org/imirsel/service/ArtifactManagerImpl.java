package org.imirsel.service;

import java.io.File;
import java.io.IOException;

import org.meandre.configuration.CoreConfiguration;

public class ArtifactManagerImpl implements ArtifactManager {
	
	
	private static CoreConfiguration coreConfiguration;
	private static ArtifactManagerImpl instance;
	private static boolean inited = false;
	
	// disable the default constructor
	private ArtifactManagerImpl(){}
	
	/**This method must be called before calling the get methods
	 * 
	 * @param config
	 */
	public static void init(CoreConfiguration config){
		if(instance ==null){
			instance = new ArtifactManagerImpl(config);
			inited = true;
		}
	}
	
	/**Returns the instance of the artifact manager impl
	 * 
	 * @return
	 */
	public static ArtifactManager getInstance(){
		if(!inited){
			throw new RuntimeException("Call ArtifactManagerImpl.init before calling getInstance");
		}
		return instance;
	}
	
	
	private ArtifactManagerImpl(CoreConfiguration config){
		coreConfiguration = config;
	}

/**Returns the process working directory
 * 
 */
	public String getProcessWorkingDirectory(String jobId) throws IOException {
		jobId=jobId.replaceAll(":", "");
		jobId = jobId.replaceAll("/","_");
		String directoryLocation =coreConfiguration.getPublicResourcesDirectory() + File.separator+"nema"+File.separator+jobId;
		File dir=new File(directoryLocation);
		boolean success = Boolean.FALSE;
		if(dir.exists()){
			return dir.getPath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory for the job "+ jobId);
		}
		return dir.getPath();
	}
/**Returns the process result directory
 * 
 */
	public String getResultLocationForJob(String jobId) throws IOException {
		jobId=jobId.replaceAll(":", "");
		jobId = jobId.replaceAll("/","_");
		String directoryLocation =coreConfiguration.getPublicResourcesDirectory() + File.separator+"nema"+File.separator+jobId+File.separator+"results";
		File dir=new File(directoryLocation);
		boolean success = Boolean.FALSE;
		if(dir.exists()){
			return dir.getPath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory for the job "+ jobId);
		}
		return dir.getPath();
	}

}
