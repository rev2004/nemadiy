package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Map;

public class ExecutableBundle implements Serializable, ExecutableMetadata {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6672878925392305298L;

	public enum ExecutableType {
		JAVA("Java"), C("C"), SHELL("Shell"), MATLAB("MATLAB");

		private String name;

		private ExecutableType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static ExecutableType toExecutableType(String typeName) {
			if (typeName.equals(JAVA.getName())) {
				return JAVA;
			} else if (typeName.equals(C.getName())) {
				return C;
			} else if (typeName.equals(SHELL.getName())) {
				return SHELL;
			} else if (typeName.equals(MATLAB.getName())) {
				return MATLAB;
			} else {
				return null;
			}
		}
	}

	private String id;
	private String fileName;
	private String typeName;
	private String mainClass;
	private String executableName;
	private String commandLineFlags;
	private Map<String, String> environmentVariables;
	private byte[] bundleContent;
	
	
	public String getId() {
		return id;
	}
	public String getTypeName() {
		return typeName;
	}
	public String getMainClass() {
		return mainClass;
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
	public void setTypeName(ExecutableType etype) {
		this.typeName = etype.getName();
	}
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
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

}
