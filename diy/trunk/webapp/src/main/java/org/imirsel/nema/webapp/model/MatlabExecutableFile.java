/**
 * Model class for the Matlab type executable submission web flow
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
public class MatlabExecutableFile extends ExecutableFile {



	private static final long serialVersionUID = 1L;
	private boolean jvm = false;
	private boolean timing = false;
	private boolean splash= false;
	private boolean display=false;
	private boolean debug=false;
	private boolean logfile=false;
	private String log;
	
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
	
}
