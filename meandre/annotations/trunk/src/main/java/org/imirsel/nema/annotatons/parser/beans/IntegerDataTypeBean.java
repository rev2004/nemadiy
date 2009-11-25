package org.imirsel.nema.annotatons.parser.beans;


public class IntegerDataTypeBean implements DataTypeBean{
	int[] valueList;
	String renderer;
	int max;
	int min;
	String className = this.getClass().getName();
	
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

	

}
