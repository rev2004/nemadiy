package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author kumaramit01
 *
 */
public class ExecutableBundle implements Serializable, ExecutableMetadata {

   /**
	 * Version of this class.
	 */
	private static final long serialVersionUID = -6672878925392305298L;
	
	private static final ExecutableType DEFAULT_EXECUTABLE_TYPE = ExecutableType.JAVA;
	private static final String DEFAULT_OS = "Unix Like";
   
    private String id;
	private String fileName;
	private ExecutableType type = DEFAULT_EXECUTABLE_TYPE;
	private String executableName;
	private String commandLineFlags;
	private String preferredOs = DEFAULT_OS;
	private Map<String, String> environmentVariables;
	private byte[] bundleContent;
	
	public String getId() {
		return id;
	}
	public String getTypeName() {
		return type.getName();
	}
   public int getTypeCode() {
      return type.getCode();
   }
   public ExecutableType getType() {
      return type;
   }
	public String getExecutableName() {
		return executableName;
	}
	public String getCommandLineFlags() {
		return commandLineFlags;
	}
	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}
	public void setId(String id) {
		this.id = id;
	}
   public void setType(ExecutableType fileType) {
      this.type = fileType;
   }
	public void setTypeName(String typeName) {
		this.type = ExecutableType.toExecutableType(typeName);
	}
   public void setTypeCode(int typeCode)  {
      this.type = ExecutableType.valueOf(typeCode);
   }
	public void setExecutableName(String executableName) {
		this.executableName = executableName;
	}
	public void setCommandLineFlags(String commandLineFlags) {
		this.commandLineFlags = commandLineFlags;
	}
	public void setEnvironmentVariables(Map<String, String> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}
	public byte[] getBundleContent() {
		return bundleContent;
	}
	public void setBundleContent(byte[] bundleContent) {
		this.bundleContent = bundleContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
   public String getPreferredOs() {
      return preferredOs;
   }
   public void setPreferredOs(String preferredOs) {
      this.preferredOs = preferredOs;
   }
   public void clear() {
      id = null;
      fileName = null;
      executableName = null;
      type = DEFAULT_EXECUTABLE_TYPE;
      bundleContent = null;
      commandLineFlags = null;
      environmentVariables = null;
      bundleContent=null;
   }

}
