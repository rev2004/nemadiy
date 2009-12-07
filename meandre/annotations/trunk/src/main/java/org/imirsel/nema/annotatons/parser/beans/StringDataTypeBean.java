package org.imirsel.nema.annotatons.parser.beans;


public class StringDataTypeBean implements DataTypeBean{
	String editRole;
	String[] valueList;
	String renderer;
	String className = "org.imirsel.nema.annotatons.parser.beans.StringDataTypeBean";
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
	

	
}
