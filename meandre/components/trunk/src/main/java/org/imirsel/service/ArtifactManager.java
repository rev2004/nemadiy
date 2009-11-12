package org.imirsel.service;

import java.io.IOException;

/**Manages the Process's working and result directory
 * 
 * 
 * @author Amit Kumar
 *
 */
public interface ArtifactManager {
	
	
	/**Executable for the process will be uploaded to this
	 * directory. A flow can have multiple executable files.
	 * 
	 * @param jobId
	 * @return
	 */
	public String getProcessWorkingDirectory(String jobId) throws IOException;
	
	/**The results of the flow are stored in this directory
	 * 
	 * @param jobId
	 * @return
	 */
	public String getResultLocationForJob(String jobId) throws IOException;

}
