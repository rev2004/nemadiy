/**
 * Model class for the java type executable submission web flow
 * 
 */
package org.imirsel.nema.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.CommandLineFormatter;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.SysProperty;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author gzhu1
 * 
 */
public class DiyJavaTemplate extends JavaPredefinedCommandTemplate {
	static private Log logger = LogFactory.getLog(DiyJavaTemplate.class);

	 MemoryOption[] memoryOptions = {
				new MemoryOption("max:1024m,min:512m", 0, "-Xmx1024m", "-Xms512m"),
				new MemoryOption("max:512m,min:256m", 1, "-Xmx512m", "-Xms256m"),
				new MemoryOption("max:256m,min:128m", 2, "-Xmx256m", "-Xms128m"),
				new MemoryOption("max:128m,min:64m", 3, "-Xmx128m", "-Xms64m") };

	@SuppressWarnings("unchecked")
	public List<MemoryOption> getMemoryOptions() {
		
		logger.debug("getMemoryOptions");
		return (List<MemoryOption>)Arrays.asList(memoryOptions);
	}
	

	private String zipJar;
	private static final long serialVersionUID = 1L;

	private int memoryOption;

	private String disableAssertionPackages;



	public String getDisableAssertionPackages() {
		return disableAssertionPackages;
	}




	public void setDisableAssertionPackages(String disableAssertionPackages) {
		this.disableAssertionPackages = disableAssertionPackages;
	}



	public void setMemoryOption(int memoryOption) {
		this.memoryOption = memoryOption;
		MemoryOption mo=getMemoryOptions().get(memoryOption);

		setMinMemory(mo.min);
		setMaxMemory(mo.max);
	}

	public int getMemoryOption() {
		return memoryOption;
	}
	

//	public void generateCommandline(){
//		MemoryOption mo=getMemoryOptions().get(memoryOption);
//		minMemory=mo.min;
//		maxMemory=mo.max;
//		
//		JavaPredefinedCommandTemplate command=new JavaPredefinedCommandTemplate();
//		command.setDisableAssertionPackages(disableAssertionPackages);
//		command.setEnableAssertionPackages(enableAssertionPackages);
//		command.setEnableSystemAssertions(enableSystemAssertions);
//		command.setJarExecutable(jarExecutable);
//		command.setMainClass(mainClass);
//		command.setMaxMemory(maxMemory);
//		command.setMinMemory(minMemory);
//		command.setVerboseExecutionClass(verboseExecutionClass);
//		command.setVerboseExecutionGC(verboseExecutionGC);
//		command.setVerboseExecutionJNI(verboseExecutionJNI);
//		
//		CommandLineFormatter formatter=new CommandLineFormatter();
//		try {
//			setEnvironment(formatter.getCommandLineString(command, resourceTypeService.getOsDataType(os), true));
//		} catch (InvalidCommandLineFlagException e) {
//			logger.error(e,e);
//		}
//	}

	public void setZipJar(String zipJar) {
		this.zipJar = zipJar;
	}

	public String getZipJar() {
		return zipJar;
	}

}

