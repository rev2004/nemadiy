package org.imirsel.nema.contentrepository.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.MatlabPredefinedCommandTemplate;
import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.PredefinedCommandTemplate;
import org.imirsel.nema.model.SysProperty;

/**Returns a command line string based on the PredefinedCommandTemplate
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class CommandLineFormatter {
	
	// list of protected java properties that cannot be changed by
	// the user
	private List<String> secureProperties = new ArrayList<String>();
	
	
	
	public CommandLineFormatter(){
		secureProperties.add("java.security.manager");
		secureProperties.add("java.security.policy");
		secureProperties.add("user.dir");
		secureProperties.add("user.home");
		secureProperties.add("java.home");
		secureProperties.add("java.compiler");
		secureProperties.add("file.separator");
		secureProperties.add("java.class.path");
	}
	
	/** Returns a command line string
	 * 
	 * @param predefined command template
	 * @param filterInvalidOptions filter the invalid options or throw execeptions if one is encountered
	 * @return a command line string
	 * @throws InvalidCommandLineFlagException
	 */
	public String getCommandLineString(PredefinedCommandTemplate pct, boolean filterInvalidOptions) throws InvalidCommandLineFlagException {
		if(pct instanceof JavaPredefinedCommandTemplate){
			return getJavaCommandLineFormatter((JavaPredefinedCommandTemplate)pct,filterInvalidOptions);
		}else if(pct instanceof MatlabPredefinedCommandTemplate){
			return getMatlabCommandLineFormatter((MatlabPredefinedCommandTemplate)pct,filterInvalidOptions);
		}else{
			throw new IllegalArgumentException("Error -invalid predefined component template");
		}
		
	}

	private String getMatlabCommandLineFormatter(MatlabPredefinedCommandTemplate pct, boolean filterInvalidOptions) throws InvalidCommandLineFlagException {
		StringBuilder sbuilder = new StringBuilder();
		if(!pct.isDisplay()){
			sbuilder.append(" -nodisplay ");
		}
		if(pct.isDebug()){
			sbuilder.append(" -debug ");
		}
		
		if(!pct.isJvm()){
			sbuilder.append(" -nojvm ");
		}
		if(pct.isLogfile()){
			if(pct.getLog()!=null){
			sbuilder.append(" -logfile " + pct.getLog());
			}else{
				if(!filterInvalidOptions){
					throw new InvalidCommandLineFlagException(" logfile option selected but the log file location missing");
				}
			}
		}
		
		if(!pct.isSplash()){
			sbuilder.append(" -nosplash ");
		}
		
		if(pct.isTiming()){
			sbuilder.append(" -timing ");
		}
		
		
		return sbuilder.toString();
	}

	
	private String getJavaCommandLineFormatter(JavaPredefinedCommandTemplate pct, boolean filterInvalidOptions) throws InvalidCommandLineFlagException{
		StringBuilder sbuilder = new StringBuilder();
		
		boolean isJarExecutable = pct.isJarExecutable();
		
		if(isJarExecutable){
			if(pct.getJarFile()==null){
				throw new InvalidCommandLineFlagException("Marked as Jar File executable but jar file is missing");
			}
		}
		

		if(!isJarExecutable){
		if(pct.getClasspath().size()!=0){
			sbuilder.append(" -classpath ");
			for(Path path: pct.getClasspath()){
				sbuilder.append(path.getElement()+File.pathSeparatorChar);
			}
			// append the current directory
			sbuilder.append(". ");
		}
		}
		
		if(pct.getDisableAssertionPackages()!=null){
			
		}
		
		if(pct.getEnableAssertionPackages()!=null){
			
		}
		
		if(pct.getProperties().size()!=0){
			for(SysProperty property: pct.getProperties()){
				if(validateJavaPropery(property.toString())){
					sbuilder.append(" -D"+property.toString()+" ");
				}else{
					if(!filterInvalidOptions){
						throw new InvalidCommandLineFlagException("Invalid Property -the property " 
								+ property.toString()+" is protected. It cannot be changed by user submitted code.");
					}
				}
			}
		}
		
		if(pct.getMinMemory()!=null){
			if(pct.getMinMemory().startsWith("-Xms")){
				sbuilder.append(pct.getMinMemory());
			}
		}
		
		if(pct.getMaxMemory()!=null){
			if(pct.getMaxMemory().startsWith("-Xmx")){
				sbuilder.append(" "+pct.getMinMemory()+" ");
			}
		}
		
		
		if(pct.isVerboseExecutionGC()){
			sbuilder.append(" -verbose:gc ");
		}
		
		if(pct.isVerboseExecutionJNI()){
			sbuilder.append(" -verbose:jni ");
		}
		if(pct.isVerboseExecutionClass()){
			sbuilder.append(" -verbose:class ");
		}
		
		if(pct.isEnableSystemAssertions()){
			sbuilder.append(" -esa ");
		}
		
		if(!isJarExecutable){
			if(pct.getMainClass()!=null){
				sbuilder.append(pct.getMainClass()+" ");
			}
		}else{
			sbuilder.append(" -jar "+ pct.getJarFile());
		}
		
		return sbuilder.toString();
	}


	private boolean validateJavaPropery(String propertyName) {
		if(this.secureProperties.contains(propertyName)){
			return false;
		}
		return true;
		
	}

}
