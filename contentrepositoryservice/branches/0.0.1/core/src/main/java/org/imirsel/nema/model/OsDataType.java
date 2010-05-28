package org.imirsel.nema.model;

import java.io.Serializable;

public class OsDataType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3914130238815939149L;
	private String name;
	private String value;

	public OsDataType(String name, String value) {
		this.name=name;
		this.value=value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
