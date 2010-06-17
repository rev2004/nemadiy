package org.imirsel.nema.artifactservice;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ArtifactManagerImpl implements ArtifactManager {
	
	
	private final static Logger LOGGER = Logger.getLogger(ArtifactManagerImpl.class.getName());
	private static String publicResourceDirectory;
	private static ArtifactManagerImpl instance;
	private static boolean inited = false;
	
	// disable the default constructor
	private ArtifactManagerImpl(){}
	
	/**This method must be called before calling the get methods
	 * @param publicResourceDirectory string
	 * 
	 * @param config
	 */
	public static void init(String publicResourceDirectory){
		LOGGER.info("Initing the ArtifactManagerImpl...");
		if(instance ==null){
			instance = new ArtifactManagerImpl();
			ArtifactManagerImpl.publicResourceDirectory= publicResourceDirectory;
			inited = true;
			
		LOGGER.info("Initialization Success: " + inited);
		}
		
	}
	
	/**Returns the instance of the artifact manager impl
	 * @param publicResourceDirectory 
	 * @param config 
	 * 
	 * @return ArtifactManager instance
	 */
	public static ArtifactManager getInstance(String publicResourceDirectory){
		if(!inited){
			//throw new RuntimeException("Call ArtifactManagerImpl.init before calling getInstance");
			instance = new ArtifactManagerImpl();
			ArtifactManagerImpl.publicResourceDirectory= publicResourceDirectory;
		}
		return instance;
	}
	
	public static ArtifactManager getInstance(){
		if(!inited){
			LOGGER.severe("Error ArtifactManagerImpl is null");
		}
		return instance;
	}
	
	
/**Returns the process working directory
 * 
 */
	public synchronized String getProcessWorkingDirectory(String jobId) throws IOException {
		jobId = getJobId(jobId);
		String directoryLocation =publicResourceDirectory+ File.separator+"nema"+File.separator+jobId;
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

private String getJobId(String jobId) {
	String[] splits = jobId.split("/");
	if(splits.length>0){
		int len = splits.length;
		jobId = splits[len-1];
	}
	return jobId;
}
/**Returns the process result directory
 * 
 */
	public synchronized String getResultLocationForJob(String jobId) throws IOException {
		jobId = getJobId(jobId);
		String directoryLocation =publicResourceDirectory + File.separator+"nema"+File.separator+jobId+File.separator+"results";
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
	String directoryLocation=publicResourceDirectory + File.separator+"common";
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
	jobId = getJobId(jobId);
	String directoryLocation =publicResourceDirectory + File.separator+"nema"+File.separator+jobId;
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

/**
 * @return the publicResourceDirectory
 */
public static String getPublicResourceDirectory() {
	return publicResourceDirectory;
}

/**
 * @param publicResourceDirectory the publicResourceDirectory to set
 */
public static void setPublicResourceDirectory(String publicResourceDirectory) {
	ArtifactManagerImpl.publicResourceDirectory = publicResourceDirectory;
}

}
