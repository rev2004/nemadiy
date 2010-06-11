package org.imirsel.nema.model;

import java.util.Map;

/**This interface is implemented by ExecutableBundle
 * 
 * @author kumaramit01
 * @since 0.0.1 
 * 
 */
public interface ExecutableMetadata {
	
	/**
	 * @return id of the executable bundle
	 */
	public String getId();
	
	/**
	 * @return file name
	 */
	public String getFileName();
	/**
	 * @return type name
	 */
	public String getTypeName();
	/**
	 * @return executable shell script name
	 */
	public String getExecutableName();
	/**
	 * @return the command line flags
	 */
	public String getCommandLineFlags();
	/**
	 * @return the environment variables
	 */
	public Map<String, String> getEnvironmentVariables();
}
