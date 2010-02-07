package org.imirsel.nema.annotations.parser.beans;

import java.io.Serializable;


public class DoubleDataTypeBean implements DataTypeBean, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6144045477195475340L;
	String editRole;
	double[] valueList;
	String[] labelList;
	String renderer;
	double max;
	double min;
	String className = "org.imirsel.nema.annotations.parser.beans.DoubleDataTypeBean";
	boolean hidden;
	
	public double getMax() {
		return max;
	}
	public void setMax(double max2) {
		this.max = max2;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	
	
	public double[] getValueList() {
		return valueList;
	}
	public void setValueList(double[] valueList) {
		this.valueList = valueList;
	}
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String class1) {
		this.renderer = class1;
	}
	public String getClassName() {
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
	public void setLabelList(String[] labelList) {
		this.labelList = labelList;
		
	}
	public String[] getLabelList(){
		return this.labelList;
	}

}
