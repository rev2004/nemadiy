package org.imirsel.nema.annotations.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.imirsel.nema.annotations.*;
import org.imirsel.nema.annotations.parser.beans.BooleanDataTypeBean;
import org.imirsel.nema.annotations.parser.beans.DataTypeBean;
import org.imirsel.nema.annotations.parser.beans.DoubleDataTypeBean;
import org.imirsel.nema.annotations.parser.beans.IntegerDataTypeBean;
import org.imirsel.nema.annotations.parser.beans.StringDataTypeBean;



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
			String visibilityRole = ((BooleanDataType)annotation).editRole().getName();
			boolean hidden=((BooleanDataType)annotation).hide();
			bd.setEditRole(visibilityRole);
			bd.setHidden(hidden);
			return bd;
		}else if(annotation instanceof DoubleDataType){
			DoubleDataTypeBean dd = new DoubleDataTypeBean();
			double max=((DoubleDataType)annotation).max();
			double min=((DoubleDataType)annotation).min();
			String rendererClass=((DoubleDataType)annotation).renderer().getName();
			String[] labelList = ((DoubleDataType)annotation).labelList();
			double[] list=((DoubleDataType)annotation).valueList();
			String visibilityRole = ((DoubleDataType)annotation).editRole().getName();
			boolean hidden=((DoubleDataType)annotation).hide();
			
			dd.setMax(max);
			dd.setMin(min);
			dd.setRenderer(rendererClass);
			dd.setValueList(list);
			dd.setEditRole(visibilityRole);
			dd.setHidden(hidden);
			dd.setLabelList(labelList);
			return dd;
			
		}else if(annotation instanceof IntegerDataType){
			IntegerDataTypeBean idt = new IntegerDataTypeBean();
			
			int max=((IntegerDataType)annotation).max();
			int min=((IntegerDataType)annotation).min();
			String className=((IntegerDataType)annotation).renderer().getName();
			int valueList[] = ((IntegerDataType)annotation).valueList();
			String[] labelList = ((IntegerDataType)annotation).labelList();
			String visibilityRole = ((IntegerDataType)annotation).editRole().getName();
			boolean hidden=((IntegerDataType)annotation).hide();
			idt.setMax(max);
			idt.setMin(min);
			idt.setRenderer(className);
			idt.setValueList(valueList);
			idt.setEditRole(visibilityRole);
			idt.setHidden(hidden);
			idt.setLabelList(labelList);
			return idt;
			
		}else if(annotation instanceof StringDataType){
			StringDataTypeBean sdt = new StringDataTypeBean();
			String[]list=((StringDataType)annotation).valueList();
			String[] labelList = ((StringDataType)annotation).labelList();
			String visibilityRole = ((StringDataType)annotation).editRole().getName();
			String className=((StringDataType)annotation).renderer().getName();
			boolean hidden=((StringDataType)annotation).hide();
			sdt.setRenderer(className);
			sdt.setValueList(list);
			sdt.setEditRole(visibilityRole);
			sdt.setHidden(hidden);
			sdt.setLabelList(labelList);
			return sdt;
		}
		return null;
	}


	
	

}
