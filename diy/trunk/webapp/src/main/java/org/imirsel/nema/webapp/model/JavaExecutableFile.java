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
public class JavaExecutableFile extends ExecutableFile {
	static private Log logger = LogFactory.getLog(JavaExecutableFile.class);

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
	private List<SysProperty> properties = new ArrayList<SysProperty>();
	private List<Path> classpath = new ArrayList<Path>();
	private String enableAssertionPackages;
	private boolean verboseExecutionGC;
	private boolean verboseExecutionClass;
	private boolean verboseExecutionJNI;
	// if jarExecutable is set to true then jarFile should be valid
	private boolean jarExecutable;
	private String jarFile;
	//
	private boolean enableSystemAssertions;
	private String minMemory;
	private String maxMemory;
	private String mainClass;
	private int memoryOption;

	private String disableAssertionPackages;

	public List<SysProperty> getProperties() {
		return properties;
	}

	public List<Path> getClasspath() {
		return classpath;
	}

	public String getDisableAssertionPackages() {
		return disableAssertionPackages;
	}

	public String getEnableAssertionPackages() {
		return enableAssertionPackages;
	}

	public boolean isVerboseExecutionGC() {
		logger.debug("isVerboseE");
		return verboseExecutionGC;
	}

	public boolean isVerboseExecutionClass() {
		return verboseExecutionClass;
	}

	public boolean isVerboseExecutionJNI() {
		return verboseExecutionJNI;
	}

	public void setVerboseExecutionClass(boolean verboseExecutionClass) {
		this.verboseExecutionClass = verboseExecutionClass;
	}

	public void setVerboseExecutionJNI(boolean verboseExecutionJNI) {
		this.verboseExecutionJNI = verboseExecutionJNI;
	}

	public boolean isEnableSystemAssertions() {
		return enableSystemAssertions;
	}

	public String getMinMemory() {
		return minMemory;
	}

	public String getMaxMemory() {
		return maxMemory;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void addProperty(SysProperty property) {
		this.properties.add(property);
	}

	public void addClasspath(Path classpath) {
		this.classpath.add(classpath);
	}

	public void setDisableAssertionPackages(String disableAssertionPackages) {
		this.disableAssertionPackages = disableAssertionPackages;
	}

	public void setEnableAssertionPackages(String enableAssertionPackages) {
		this.enableAssertionPackages = enableAssertionPackages;
	}

	public void setVerboseExecutionGC(boolean verboseExecutionGC) {
		this.verboseExecutionGC = verboseExecutionGC;
	}

	public void setEnableSystemAssertions(boolean enableSystemAssertions) {
		this.enableSystemAssertions = enableSystemAssertions;
	}

	public void setMinMemory(String minMemory) {
		this.minMemory = minMemory;
	}

	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public void setJarExecutable(boolean jarExecutable) {
		this.jarExecutable = jarExecutable;
	}

	public boolean isJarExecutable() {
		return jarExecutable;
	}

	public void setJarFile(String jarFile) {
		this.jarFile = jarFile;
	}

	public String getJarFile() {
		return jarFile;
	}

	public void setMemoryOption(int memoryOption) {
		this.memoryOption = memoryOption;
	}

	public int getMemoryOption() {
		return memoryOption;
	}
	
	@Override
	public void generateCommandline(){
		MemoryOption mo=getMemoryOptions().get(memoryOption);
		minMemory=mo.min;
		maxMemory=mo.max;
		
		JavaPredefinedCommandTemplate command=new JavaPredefinedCommandTemplate();
		command.setDisableAssertionPackages(disableAssertionPackages);
		command.setEnableAssertionPackages(enableAssertionPackages);
		command.setEnableSystemAssertions(enableSystemAssertions);
		command.setJarExecutable(jarExecutable);
		command.setMainClass(mainClass);
		command.setMaxMemory(maxMemory);
		command.setMinMemory(minMemory);
		command.setVerboseExecutionClass(verboseExecutionClass);
		command.setVerboseExecutionGC(verboseExecutionGC);
		command.setVerboseExecutionJNI(verboseExecutionJNI);
		
		CommandLineFormatter formatter=new CommandLineFormatter();
		try {
			setEnvironment(formatter.getCommandLineString(command, resourceTypeService.getOsDataType(os), true));
		} catch (InvalidCommandLineFlagException e) {
			logger.error(e,e);
		}
	}

	public void setZipJar(String zipJar) {
		this.zipJar = zipJar;
	}

	public String getZipJar() {
		return zipJar;
	}

}

