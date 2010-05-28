package org.imirsel.nema.model;

import java.io.Serializable;

public class GroupDataType  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7883504198551206914L;
	private String name;
	private String value;

	public GroupDataType(String name, String value) {
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
