package org.imirsel.nema.model;

import java.util.Map;

/**This interface is implemented by ExecutableBundle
 * 
 * @author kumaramit01
 * @since 0.0.1 
 * 
 */
public interface ExecutableMetadata {
	public String getId();
	public String getFileName();
	public String getTypeName();
	public String getMainClass();
	public String getExecutableName();
	public String getCommandLineFlags();
	public Map<String, String> getEnvironmentVariables();
}
