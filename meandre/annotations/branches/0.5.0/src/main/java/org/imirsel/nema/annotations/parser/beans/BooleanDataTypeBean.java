package org.imirsel.nema.annotations.parser.beans;

import java.io.Serializable;


public class BooleanDataTypeBean implements DataTypeBean, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288953812372603319L;
	String renderer;
	String editRole;
	boolean hidden;
	String className = "org.imirsel.nema.annotations.parser.beans.BooleanDataTypeBean";
	
	boolean[] valueList = {true,false};
	
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String class1) {
		this.renderer = class1;
	}
	
	public boolean[] getValueList() {
		return valueList;
	}
	
	public String getClassName(){
		return className;
	}
	public String getEditRole() {
		return editRole;
	}
	public void setEditRole(String editRole) {
		this.editRole = editRole;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hide) {
		this.hidden = hide;
	}
	

}
