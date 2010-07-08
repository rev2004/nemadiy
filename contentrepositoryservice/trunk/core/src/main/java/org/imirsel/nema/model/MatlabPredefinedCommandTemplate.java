package org.imirsel.nema.model;

import java.io.Serializable;

/** Predefined template for matlab codebase
 * 
 * @author kumaramit01
 * @since 0.0.1
 * @version 0.0.4 -Added functionCall to support matlab function
 */
public class MatlabPredefinedCommandTemplate extends VanillaPredefinedCommandTemplate implements
		Serializable {
	
	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = -9056319373768134321L;
	private boolean jvm = false;
	private boolean timing = false;
	private boolean splash= false;
	private boolean display=false;
	private boolean debug=false;
	private boolean logfile=false;
	private String log;
	private String functionCall;
	
	public boolean isJvm() {
		return jvm;
	}
	public boolean isTiming() {
		return timing;
	}
	public boolean isSplash() {
		return splash;
	}
	public boolean isDisplay() {
		return display;
	}
	public boolean isDebug() {
		return debug;
	}
	public boolean isLogfile() {
		return logfile;
	}
	public String getLog() {
		return log;
	}
	public void setJvm(boolean jvm) {
		this.jvm = jvm;
	}
	public void setTiming(boolean timing) {
		this.timing = timing;
	}
	public void setSplash(boolean splash) {
		this.splash = splash;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public void setLogfile(boolean logfile) {
		this.logfile = logfile;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getFunctionCall() {
		return functionCall;
	}
	public void setFunctionCall(String functionCall) {
		this.functionCall = functionCall;
	}
	

}
