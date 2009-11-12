package org.imirsel.service;

import java.io.File;
import java.io.IOException;

import org.meandre.configuration.CoreConfiguration;

public class ArtifactManagerImpl implements ArtifactManager {
	
	
	CoreConfiguration coreConfiguration;
	
	
	public ArtifactManagerImpl(CoreConfiguration config){
		this.coreConfiguration = config;
	}


	public String getProcessWorkingDirectory(String jobId) throws IOException {
		String directoryLocation =this.coreConfiguration.getPublicResourcesDirectory() + File.pathSeparator+"nema"+File.pathSeparator+jobId;
		File dir=new File(directoryLocation);
		boolean success = Boolean.FALSE;
		if(dir.exists()){
			return dir.getAbsolutePath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory for the job "+ jobId);
		}
		return dir.getAbsolutePath();
	}

	public String getResultLocationForJob(String jobId) throws IOException {
		String directoryLocation =this.coreConfiguration.getPublicResourcesDirectory() + File.pathSeparator+"nema"+File.pathSeparator+jobId+File.pathSeparator+"results";
		File dir=new File(directoryLocation);
		boolean success = Boolean.FALSE;
		if(dir.exists()){
			return dir.getAbsolutePath();
		}else{
			success=dir.mkdirs();
		}
		if(!success){
			throw new IOException("could not create directory for the job "+ jobId);
		}
		return null;
	}

}
