package org.imirsel.nema.annotations.parser.beans;

import java.io.Serializable;


public class StringDataTypeBean implements DataTypeBean, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6853867485499302567L;
	String editRole;
	String[] valueList;
	String[] labelList;
	String renderer;
	String className = "org.imirsel.nema.annotations.parser.beans.StringDataTypeBean";
	boolean hidden;
	
	public String[] getValueList() {
		return valueList;
	}
	public void setValueList(String[] valueList) {
		this.valueList = valueList;
	}
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String class1) {
		this.renderer = class1;
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
	public void setLabelList(String[] labelList2) {
		this.labelList= labelList2;
	}
	
	public String[] getLabelList(){
		return this.labelList;
	}

	
}
