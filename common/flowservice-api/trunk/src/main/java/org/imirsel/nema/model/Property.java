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
	
	public boolean equals(Object object){
		if(!(object instanceof Property)){
			return false;
		}
		Property property = (Property)object;
		// name of the property cannot be null
		if(property.name==null){
			return false;
		}
		
		 if(this.name!= null){
			 if(this.value==null && property.value==null){
				 return this.name.equals(property.name);
			 }else if(this.value!=null && property.value !=null){ // none of the values are null
				 if(property.name.equals(this.name) &&  
							property.value.equals(this.value)){ // compare the name and the value
						return true;
					}
					
			 }else{ // one of them has a null value not both
				 return false;
			 }
			}
		 return false;
	}
	
	public int hashCode(){
		int hash=1;
		hash = hash * 31 
        + (this.name == null ? 0 : this.name.hashCode())
        + (this.value == null ? 0 : this.value.hashCode())
        +  (this.description == null ? 0 : this.description.hashCode())
        +  (this.valueList == null ? 0 : this.valueList.hashCode())
        +  (this.labelList == null ? 0 : this.labelList.hashCode())
		+  (this.defaultValue == null ? 0 : this.defaultValue.hashCode());
		return hash;
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
	
	public static void main(String[] args){
		Property p = new Property();
		p.setName("name");
		p.setValue("value");
		Property p1 = new Property();
		p1.setName("value");
		p1.setValue("name");
		System.out.println(p.hashCode() +"  " + p1.hashCode());
	}

}
