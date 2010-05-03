package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Java Predefined Command Template
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class JavaPredefinedCommandTemplate extends VanillaPredefinedCommandTemplate implements Serializable {
	/** Version of this class
	 * 
	 */
	private static final long serialVersionUID = 6405184554728991221L;
	
	
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


}
