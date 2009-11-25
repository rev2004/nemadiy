package org.imirsel.nema.annotatons.parser.beans;


public class StringDataTypeBean implements DataTypeBean{
	String[] valueList;
	String renderer;
	String className = this.getClass().getName();
	
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

	
}
