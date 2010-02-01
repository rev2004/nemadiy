package org.imirsel.nema.artifactservice;

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
	 * @return ArtifactManager instance
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
	public synchronized String getProcessWorkingDirectory(String jobId) throws IOException {
		jobId=jobId.replaceAll(":", "");
		jobId = jobId.replaceAll("/","_");
		String directoryLocation =coreConfiguration.getPublicResourcesDirectory() + File.separator+"nema"+File.separator+jobId;
		File dir=new File(directoryLocation);
		boolean success = false;
		if(dir.exists()){
			return dir.getPath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory "+dir.getCanonicalPath()+"\nfor the job "+ jobId);
		}
		return dir.getPath();
	}
/**Returns the process result directory
 * 
 */
	public synchronized String getResultLocationForJob(String jobId) throws IOException {
		jobId=jobId.replaceAll(":", "");
		jobId = jobId.replaceAll("/","_");
		String directoryLocation =coreConfiguration.getPublicResourcesDirectory() + File.separator+"nema"+File.separator+jobId+File.separator+"results";
		File dir=new File(directoryLocation);
		boolean success = false;
		if(dir.exists()){
			return dir.getPath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory "+ dir.getCanonicalPath()+"\nfor the job "+ jobId);
		}
		return dir.getPath();
	}

/**Returns absolute directory path
 * 
 */
public synchronized String getCommonStorageLocation() throws IOException {
	String directoryLocation=coreConfiguration.getPublicResourcesDirectory() + File.separator+"common";
	File dir=new File(directoryLocation);
	boolean success = false;
	if(dir.exists()){
		return dir.getCanonicalPath();
	}else{
		success=dir.mkdirs();
	}
	if(!success){
		throw new IOException("could not create common Storage Location Directory") ;
	}
	return dir.getCanonicalPath();
}

/**Returns the absolute path for the working directory.
 * 
 */
public synchronized String getAbsoluteProcessWorkingDirectory(String jobId)
		throws IOException {
	jobId=jobId.replaceAll(":", "");
	jobId = jobId.replaceAll("/","_");
	String directoryLocation =coreConfiguration.getPublicResourcesDirectory() + File.separator+"nema"+File.separator+jobId;
	File dir=new File(directoryLocation);
	boolean success = false;
	if(dir.exists()){
		return dir.getCanonicalPath();
	}else{
		success=dir.mkdirs();
	}
	if(!success){
		throw new IOException("could not create directory "+dir.getCanonicalPath()+"\nfor the job "+ jobId);
	}
	return dir.getCanonicalPath();
}

}
