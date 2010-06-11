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
	public int hashCode(){
		int hash = 31;
		hash=hash+ this.name.hashCode() + this.value.hashCode();
		return hash;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof GroupDataType)){
			return false;
		}
		GroupDataType fdt = (GroupDataType) object;
		return this.getName().equals(fdt.name) && this.getValue().equals(fdt.value);
	}
}
