package org.imirsel.nema.artifactservice;

import java.io.IOException;

/**Manages the Process's working and result directory
 * 
 * 
 * @author kumaramit01
 * @deprecated extend NemaComponent class to get this functionality
 */
public interface ArtifactManager {
	
	
	/**Executable for the process will be uploaded to this
	 * directory. A flow can have multiple executable files.
	 * @deprecated extend NemaComponent class to get this functionality
	 * @param jobId
	 * @throws IOException 
	 * @return StringAbsolute path to the working directory
	 */
	public String getAbsoluteProcessWorkingDirectory(String jobId) throws IOException;
	
	
	
	
	/**Executable for the process will be uploaded to this
	 * directory. A flow can have multiple executable files.
	 * @deprecated extend NemaComponent class to get this functionality
	 * @param jobId
	 * @return processWorkingDirectory
	 * @throws IOException 
	 */
	public String getProcessWorkingDirectory(String jobId) throws IOException;
	
	/**The results of the flow are stored in this directory
	 * @deprecated extend NemaComponent class to get this functionality
 	 * @param jobId
	 * @return resultLocation
	 * @throws IOException 
	 */
	public String getResultLocationForJob(String jobId) throws IOException;
	
	
	/**
	 * @deprecated extend NemaComponent class to get this functionality
	 * @return commonstorage location as string
	 * @throws IOException 
	 */
	public String getCommonStorageLocation() throws IOException;

}
