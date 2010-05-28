package org.imirsel.nema.model;

import java.io.Serializable;
/**
 *  //TODO missing equals and hashCode
 * @author kumaramit01
 *
 */
public class FileDataType  implements Serializable{
	/** Version of this class
	 * 
	 */
	private static final long serialVersionUID = -4037324168549003152L;
	private String name;
	private String value;

	public FileDataType(String name, String value) {
		this.name= name;
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
