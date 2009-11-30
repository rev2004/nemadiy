package org.imirsel.nema.annotatons.parser.beans;


public class BooleanDataTypeBean implements DataTypeBean{
	String renderer;
	String visibilityRole;
	boolean hidden;
	String className = "org.imirsel.nema.annotatons.parser.beans.BooleanDataTypeBean";
	
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
	public String getVisibilityRole() {
		return visibilityRole;
	}
	public void setVisibilityRole(String visibilityRole) {
		this.visibilityRole = visibilityRole;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hide) {
		this.hidden = hide;
	}
	

}
