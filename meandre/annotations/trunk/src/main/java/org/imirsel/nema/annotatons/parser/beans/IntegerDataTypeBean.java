package org.imirsel.nema.annotatons.parser.beans;


public class IntegerDataTypeBean implements DataTypeBean{
	String visibilityRole;
	int[] valueList;
	String renderer;
	int max;
	int min;
	String className = "org.imirsel.nema.annotatons.parser.beans.IntegerDataTypeBean";
	boolean hidden;
	
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	
	public int[] getValueList() {
		return valueList;
	}
	public void setValueList(int[] valueList) {
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
