package org.imirsel.nema.webapp.taglib;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.imirsel.nema.annotatons.parser.beans.BooleanDataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.DataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.DoubleDataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.IntegerDataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.StringDataTypeBean;
import org.imirsel.nema.model.Property;

/**This tag renders the component property
 * 
 * @author Amit Kumar
 *
 */
public class ComponentPropertyTag  extends SimpleTagSupport implements DynamicAttributes{
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	private Property property;
	private String component;


	// class and other attributes are dynamic
	public void setDynamicAttribute(String uri, String localName, Object value)
	throws JspException {
		keys.add( localName );
		values.add(value.toString());
	}

	// setter for the value attribute
	public void setValue(Property property){
		this.property=property;
	}

	
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		StringWriter writer = new StringWriter();
		StringWriter cssWriter = new StringWriter();
		for( int i = 0; i < keys.size(); i++ ) {
			String key = (String)keys.get( i );
			Object value = values.get(i);
			cssWriter.append(key+"='"+value+"' ");
		}
		List<DataTypeBean> ltb =this.property.getDataTypeBeanList();
		String htmlWidget=null;
		if(ltb.isEmpty()){
			htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
		}else{
			DataTypeBean dataTypeBean = ltb.get(0);
			if(dataTypeBean instanceof StringDataTypeBean){
				StringDataTypeBean sd = (StringDataTypeBean) dataTypeBean;
				//if(sd.getRenderer()==null){
					if(sd.getValueList()!=null){
						if(sd.getValueList().length>0){
							htmlWidget=createSelectBox(property.getName(),cssWriter.toString(),property.getDefaultValue(),sd.getValueList(),property.getValue());
						}else{
							htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
						}
					}else{
						htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
					}
				//}else if(sd.getRenderer().endsWith("FileRenderer")){
				//	htmlWidget=createInputBox(property.getName(),"file",cssWriter.toString(),property.getDefaultValue(),property.getValue());
				//}


			}else if(dataTypeBean instanceof BooleanDataTypeBean){
				htmlWidget=createRadioBox(property.getName(),cssWriter.toString(),property.getDefaultValue(),property.getValue());
			}else if(dataTypeBean instanceof DoubleDataTypeBean){
				DoubleDataTypeBean sd = (DoubleDataTypeBean)dataTypeBean;
				if(sd.getValueList()!=null){
					if(sd.getValueList().length>0){
						htmlWidget=createSelectBox(property.getName(),cssWriter.toString(),property.getDefaultValue(), sd.getValueList(),property.getValue());
					}else{
						htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
					}
				}else{
				htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
				}
			}else if(dataTypeBean instanceof IntegerDataTypeBean){
				IntegerDataTypeBean sd = (IntegerDataTypeBean)dataTypeBean;
				if(sd.getValueList()!=null){
					if(sd.getValueList().length>0){
					htmlWidget=createSelectBox(property.getName(),cssWriter.toString(),property.getDefaultValue(), sd.getValueList(),property.getValue());
					}else{
					htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
					}
				}else{
					htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
				}
			}else{
				htmlWidget=createInputBox(property.getName(),"text",cssWriter.toString(),property.getDefaultValue(),property.getValue());
			}
		}
		
		writer.append(htmlWidget);


		try {
			out.println(writer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private String createSelectBox(String name, String styleString,
			Object defaultValue, String[] valueList, String value) {
		StringWriter swriter = new StringWriter();
		if(value!=null){
			if(value.length()>0){
				defaultValue = value;
			}
		}
		swriter.append("<select name='"+getName(name)+"' "+styleString+">");

		for(int i=0;i<valueList.length;i++){
			if(defaultValue==null){
				swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
			}else{
				if(defaultValue.toString().equalsIgnoreCase(valueList[i])){
					swriter.append("<option selected='selected' value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}else{
					swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}
			}
		}
		swriter.append("</select>");
		return swriter.toString();
	}
	

	private String createSelectBox(String name, String styleString,
			Object defaultValue, double[] valueList, String value) {
		StringWriter swriter = new StringWriter();
		if(value!=null){
			if(value.length()>0){
				defaultValue = value;
			}
		}
		swriter.append("<select name='"+getName(name)+"' "+styleString+">");
		for(int i=0;i<valueList.length;i++){
			if(defaultValue==null){
				swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
			}else{
				if(Double.parseDouble(defaultValue.toString()) == valueList[i]){
					swriter.append("<option selected='selected' value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}else{
					swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}
			}
				
		}
		swriter.append("</select>");
		return swriter.toString();
	}
	
	private String createSelectBox(String name, String styleString,
			Object defaultValue, int[] valueList, String value) {
		StringWriter swriter = new StringWriter();
		if(value!=null){
			if(value.length()>0){
				defaultValue = value;
			}
		}
		swriter.append("<select name='"+getName(name)+"' "+styleString+">");

		for(int i=0;i<valueList.length;i++){
			if(defaultValue==null){
				swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
			}else {
				if(Integer.parseInt(defaultValue.toString()) == valueList[i]){
					swriter.append("<option selected='selected' value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}else{
					swriter.append("<option value='"+valueList[i]+"'>"+valueList[i]+"</option>");
				}
			}
		}
		swriter.append("</select>");
		return swriter.toString();
	}
	

	private String createRadioBox(String name, String styleString,	Object defaultValue, String value) {
		StringWriter swriter = new StringWriter();
		if(value!=null){
			if(value.length()>0){
				defaultValue = value;
			}
		}
		swriter.append("<input type='radio' name='"+getName(name)+"' "+ styleString);
		if(defaultValue!=null){
			if(defaultValue.toString().equals("true")){
				swriter.append(" checked='checked' ");
			}
		}
		swriter.append(" value='true'>true</input>");
		swriter.append("<input type='radio' name='"+getName(name)+"' "+ styleString);
		if(defaultValue!=null){
			if(defaultValue.toString().equals("false")){
				swriter.append(" checked='checked' ");
			}
		}
		swriter.append(" value='false'>false</input>");
		return swriter.toString();
	}

	private String createInputBox(String name, String type, String styleString,
			Object defaultValue, String value) {
		if(value!=null){
			if(value.length()>0){
				defaultValue = value;
			}
		}
		StringWriter swriter = new StringWriter();
		swriter.append("<input type='"+type+"' name='"+getName(name)+"' "+ styleString);
		if(defaultValue!=null){
			swriter.append(" value='"+defaultValue.toString()+"' ");
		}
		swriter.append("/>");
		return swriter.toString();
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getComponent() {
		return component;
	}
	
	public String getName(String propertyName){
		if(this.component==null){
			return propertyName;
		}
		int index = this.component.lastIndexOf("/");
		if(index==-1){
			return this.component+"_"+propertyName;
		}
		int second = this.component.substring(0, index).lastIndexOf("/");
		String cname=this.component.substring(second+1,index);
		String count = this.component.substring(index+1);
		return cname+"_"+count+"_"+propertyName;
	}


}
