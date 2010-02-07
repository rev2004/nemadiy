package org.imirsel.nema.annotations.parser.beans;

import java.io.Serializable;


public class IntegerDataTypeBean implements DataTypeBean, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6034628837167685767L;
	String editRole;
	int[] valueList;
	String[] labelList;
	String renderer;
	int max;
	int min;
	String className = "org.imirsel.nema.annotations.parser.beans.IntegerDataTypeBean";
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
		this.labelList = labelList2;
	}
	
	public String[] getLabelList(){
		return this.labelList;
	}

}
