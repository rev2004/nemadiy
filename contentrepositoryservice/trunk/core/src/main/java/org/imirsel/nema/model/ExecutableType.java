package org.imirsel.nema.model;

/**
 * @author kumaramit01
 * @since 0.5.0 -extracted from the class -shared with the executor service
 */
public enum ExecutableType {
	JAVA("Java",0), MATLAB("Matlab",1), BIN("Bin",2), SHELL("Shell",3), PYTHON("Python",4), PERL("Perl",5), WINE("Wine",6), RUBY("Ruby",7);

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
