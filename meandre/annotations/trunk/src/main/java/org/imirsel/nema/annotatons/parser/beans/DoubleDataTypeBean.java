package org.imirsel.nema.annotatons.parser.beans;


public class DoubleDataTypeBean implements DataTypeBean{
	double[] valueList;
	String renderer;
	double max;
	double min;
	String className = "org.imirsel.nema.annotatons.parser.beans.DoubleDataTypeBean";
	
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
	


}
