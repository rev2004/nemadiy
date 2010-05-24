package org.imirsel.nema.webapp.model;

import java.io.Serializable;

/**
 * help class for JavaExecutable that bundles the max/min memory args 
 * for java executable with fixed options
 * 
 * @author gzhu1
 *
 * TODO should be put back into JavaExecutable
 */
public class MemoryOption implements Serializable{

	private static final long serialVersionUID = 1L;
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
