package org.imirsel.nema.model;

import java.util.List;

import org.imirsel.nema.annotatons.parser.beans.DataTypeBean;

/**Component Property is represented using this bean
 * 
 * @author Amit Kumar
 *
 */
public class Property {
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

}
