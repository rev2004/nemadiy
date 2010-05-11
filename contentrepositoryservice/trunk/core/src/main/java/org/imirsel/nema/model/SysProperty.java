package org.imirsel.nema.model;

import java.io.Serializable;


/** System properties for java cmdline
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class SysProperty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1085496238767009164L;
	private String name;
	private String value;
	public SysProperty(String name,String value){
		this.name =name;
		this.value = value;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof SysProperty)){
			return false;
		}
		SysProperty s1 = (SysProperty)object;
		if(s1.name.equals(this.name) && s1.value.equals(this.value)){
			return true;
		}
		return false;
	}
	
	
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + (null == value ? 0 :value.hashCode());;
		hash = 31 * hash + (null == name ? 0 :name.hashCode());
		return hash;
	}
	
	public String toString(){
		return name+"="+value;
	}

}
