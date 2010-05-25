package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Map;

public class ExecutableBundle implements Serializable, ExecutableMetadata {

   /**
	 * Version of this class.
	 */
	private static final long serialVersionUID = -6672878925392305298L;
	
   private static final ExecutableType DEFAULT_EXECUTABLE_TYPE = ExecutableType.JAVA;
   private static final String DEFAULT_OS = "Unix Like";
   
	public enum ExecutableType {
		JAVA("Java",0), MATLAB("MATLAB",1), C("C",2), SHELL("Shell",3);

		private String name;
      private int code;
      
		private ExecutableType(String name,int code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public int getCode() {
		   return code;
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
		
      public static ExecutableType valueOf(int code)
            throws IllegalArgumentException {
         switch (code) {
         case 0:
            return ExecutableType.JAVA;
         case 1:
            return ExecutableType.MATLAB;
         case 2:
            return ExecutableType.C;
         case 3:
            return ExecutableType.SHELL;
         default:
            throw new IllegalArgumentException("Unknown ExecutableType code: "
                  + code);
         }
      }
	}

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
   }

}
