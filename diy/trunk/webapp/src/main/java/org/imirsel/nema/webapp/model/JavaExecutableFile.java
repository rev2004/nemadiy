/**
 * Model class for the java type executable submission web flow
 * 
 */
package org.imirsel.nema.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.SysProperty;

/**
 * @author gzhu1
 * 
 */
public class JavaExecutableFile extends ExecutableFile {

	final public static MemoryOption[] memoryOptions = {
			new MemoryOption("max:1024m,min:512m", 0, "-Xmx1024m", "-Xms512m"),
			new MemoryOption("max:512m,min:256m", 1, "-Xmx512m", "-Xms256m"),
			new MemoryOption("max:256m,min:128m", 2, "-Xmx256m", "-Xms128m"),
			new MemoryOption("max:128m,min:64m", 3, "-Xmx128m", "-Xms64m") };

	public static MemoryOption[] getMemoryoptions() {
		return memoryOptions;
	}

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

}

class MemoryOption {
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public MemoryOption(String label, int code, String max, String min) {
		super();
		this.label = label;
		this.code = code;
		this.max = max;
		this.min = min;
	}

	public String label;
	public int code;
	public String max;
	public String min;
}
