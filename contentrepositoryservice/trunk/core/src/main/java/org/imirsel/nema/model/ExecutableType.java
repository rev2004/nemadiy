package org.imirsel.nema.model;

import java.io.Serializable;

/**
 * @author kumaramit01
 * @since 0.4.0 -extracted from the class -shared with the executor service
 * @version 0.4.0 -Added versionString for the the supported executables
 */
public enum ExecutableType implements Serializable {
	JAVA("Java","-version",0), MATLAB("Matlab","-nosplash -nodesktop  -nojvm -nodisplay -r \"version,exit\"",1),
	BIN("Bin","-version",2), SHELL("Shell","-version",3), PYTHON("Python","-V",4), 
	PERL("Perl","-v",5), WINE("Wine","--version",6), RUBY("Ruby","--version",7);

  private String name;
  private String versionString;
  private int code;
  private String versionCmdLine;
  
	private ExecutableType(String name, String versionCmdLine,int code) {
		this.name = name;
		this.code = code;
		this.versionCmdLine = versionCmdLine;
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
	
	
	public void setVersionString(String version){
		this.versionString = version;
	}
	
	public String getVersionString(){
		return versionString;
	}
	
	
	public String getVersionCmdLine(String typeName){
		return versionCmdLine;
	}

	public static ExecutableType toExecutableType(String typeName) {
		if (typeName.equals(JAVA.getName())) {
			return JAVA;
		} else if (typeName.equals(BIN.getName())) {
			return BIN;
		} else if (typeName.equals(SHELL.getName())) {
			return SHELL;
		} else if (typeName.equals(MATLAB.getName())) {
			return MATLAB;
		} else if (typeName.equals(PYTHON.getName())) {
			return PYTHON;
		} else if (typeName.equals(PERL.getName())) {
			return PERL;
		} else if (typeName.equals(WINE.getName())) {
			return WINE;
		} else if (typeName.equals(RUBY.getName())) {
			return RUBY;
		}else {
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
        return ExecutableType.BIN;
     case 3:
        return ExecutableType.SHELL;
     case 4:
    	 return ExecutableType.PYTHON;
     case 5: 
    	 return ExecutableType.PERL;
     case 6:
    	 return ExecutableType.WINE;
     case 7:
    	 return ExecutableType.RUBY;
     default:
        throw new IllegalArgumentException("Unknown ExecutableType code: "
              + code);
     }
  }
}
