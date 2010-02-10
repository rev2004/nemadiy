package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.imirsel.nema.annotations.parser.beans.DataTypeBean;

/**Component Property is represented using this bean
 * 
 * @author kumaramit01
 * @since 0.5.0
 *
 */
public class Property implements Comparable<Property>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6537139194655021669L;
	private ArrayList<Object> valueList = new ArrayList<Object>();
	private ArrayList<String> labelList = new ArrayList<String>();
	
	private List<DataTypeBean> dataTypeBeanList;
	private Object defaultValue;
	private String name;
	private String description;
	private String value;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DataTypeBean> getDataTypeBeanList() {
		return dataTypeBeanList;
	}
	public void setDataTypeBeanList(List<DataTypeBean> dataTypeBeanList) {
		this.dataTypeBeanList = dataTypeBeanList;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int compareTo(Property o) {
		if(o==null){
			return 0;
		}
		return o.getName().compareTo(this.getName());
	}
	public void setEnumeratedValueList(ArrayList<String> labelList) {
		this.labelList=labelList;
		
	}
	public void setEnumneratedLabelList(ArrayList<Object> valueList) {
		this.valueList=valueList;
	}
	
	public Object[] getValueList() {
		return valueList.toArray(new Object[0]);
	}
	public String[] getLabelList() {
		return labelList.toArray(new String[0]);
	}

}
