package org.imirsel.nema.annotatons.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.imirsel.nema.annotations.*;
import org.imirsel.nema.annotatons.parser.beans.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class DataTypeAnnotationParser {
	
	
	public HashMap<String, List<DataTypeBean>> getComponentDataType(Class<?> componentClass) {
		HashMap<String,List<DataTypeBean>>  hashMap = new HashMap<String, List<DataTypeBean>>();
		if(componentClass.isInterface()){
			return  hashMap;
		}
		Field[] fields = componentClass.getDeclaredFields();
		for(Field field: fields){
		Annotation annot = field.getAnnotation(org.meandre.annotations.ComponentProperty.class);
		{
			if(annot!=null){
				Method method= null;
				String value=null;
				try {
					method = annot.getClass().getDeclaredMethod("name",(Class[])null);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}catch (NoSuchMethodException e1) {
				}
				try {
					if(method!=null){
						value = (String)(method.invoke(annot,(Object[])null));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Annotation[] dataTypeAnnotation =  field.getAnnotations();
				ArrayList<DataTypeBean> list = new ArrayList<DataTypeBean>();
				
				for(int i=0; i < dataTypeAnnotation.length;i++){
					if(!(dataTypeAnnotation[i] instanceof org.meandre.annotations.ComponentProperty)){
					list.add(getDataTypeBean(dataTypeAnnotation[i]));
					}
				}
				hashMap.put(value,list );
			}
		
		}
		}
		return hashMap;
	}
	
	
	private DataTypeBean getDataTypeBean(Annotation annotation) {
		if(annotation instanceof BooleanDataType){
			BooleanDataTypeBean bd = new BooleanDataTypeBean();
			bd.setRenderer(((BooleanDataType)annotation).renderer().getName());
			return bd;
		}else if(annotation instanceof DoubleDataType){
			DoubleDataTypeBean dd = new DoubleDataTypeBean();
			double max=((DoubleDataType)annotation).max();
			double min=((DoubleDataType)annotation).min();
			String rendererClass=((DoubleDataType)annotation).renderer().getName();
			double[] list=((DoubleDataType)annotation).valueList();
			
			dd.setMax(max);
			dd.setMin(min);
			dd.setRenderer(rendererClass);
			dd.setValueList(list);
			return dd;
			
		}else if(annotation instanceof IntegerDataType){
			IntegerDataTypeBean idt = new IntegerDataTypeBean();
			
			int max=((IntegerDataType)annotation).max();
			int min=((IntegerDataType)annotation).min();
			String className=((IntegerDataType)annotation).renderer().getName();
			int valueList[] = ((IntegerDataType)annotation).valueList();
			
			idt.setMax(max);
			idt.setMin(min);
			idt.setRenderer(className);
			idt.setValueList(valueList);
			return idt;
			
		}else if(annotation instanceof StringDataType){
			StringDataTypeBean sdt = new StringDataTypeBean();
			String[]list=((StringDataType)annotation).valueList();
			String className=((StringDataType)annotation).renderer().getName();
			sdt.setRenderer(className);
			sdt.setValueList(list);
			return sdt;
		}
		return null;
	}


	
	

}
