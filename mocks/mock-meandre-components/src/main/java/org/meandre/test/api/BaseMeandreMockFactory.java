/*
 * @(#) BaseMeandreMockFactory.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.api;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import org.meandre.annotations.DetectDefaultComponentAnnotations;
import org.meandre.core.ExecutableComponent;

public class BaseMeandreMockFactory {
	
	private DetectDefaultComponentAnnotations detectComponentAnnotations;
	public BaseMeandreMockFactory(){
		detectComponentAnnotations= new DetectDefaultComponentAnnotations();
	}
	

	protected HashMap<String, String> getPropertyDefaultValues(
			Class<? extends ExecutableComponent> componentClass) {
		HashMap<String, Annotation> propertiesAnnotations
		=detectComponentAnnotations.getComponentFieldAnnotations(componentClass, org.meandre.annotations.ComponentProperty.class);
		if(propertiesAnnotations.size()==0){
			System.out.println("Warning: could not find any properties annotations." );
			return null;
		}
		HashMap<String,String> hashedValues= new HashMap<String,String>();
		for(String key: propertiesAnnotations.keySet()){
		String defaultValue=	((org.meandre.annotations.ComponentProperty)propertiesAnnotations.get(key)).defaultValue();
		hashedValues.put(key, defaultValue);
		}
		return hashedValues;
	}

	protected String[] getPropertyNames(Class<? extends ExecutableComponent> componentClass) {
		HashMap<String, Annotation> propertiesAnnotations
		=detectComponentAnnotations.getComponentFieldAnnotations(componentClass, org.meandre.annotations.ComponentProperty.class);
		if(propertiesAnnotations.size()==0){
			System.out.println("Warning: could not find any properties annotations." );
			return null;
		}
		
		Set<String> propertiesNameSet=propertiesAnnotations.keySet();
		String[] propertiesNames = new String[propertiesNameSet.size()];
		int count=0;
		for(String key:propertiesNameSet){
			propertiesNames[count]=key;
			count++;
		}
		return propertiesNames;
	}

	protected String[] getInputNames(Class<? extends ExecutableComponent> componentClass) {
		HashMap<String, Annotation> inputAnnotations
		=detectComponentAnnotations.getComponentFieldAnnotations(componentClass, org.meandre.annotations.ComponentInput.class);
		if(inputAnnotations.size()==0){
			System.out.println("Warning: could not find any input annotations." );
			return null;
		}
		
		Set<String> inputNameSet=inputAnnotations.keySet();
		String[] inputNames = new String[inputNameSet.size()];
		int count=0;
		for(String key:inputNameSet){
			inputNames[count]=key;
			count++;
		}
		return inputNames;
	}
	
	
	protected String[] getOutputNames(Class<? extends ExecutableComponent> componentClass) {
		HashMap<String, Annotation> outputAnnotations
		=detectComponentAnnotations.getComponentFieldAnnotations(componentClass, org.meandre.annotations.ComponentOutput.class);
		if(outputAnnotations.size()==0){
			System.out.println("Warning: could not find any output annotations." );
			return null;
		}
		
		Set<String> outputNameSet=outputAnnotations.keySet();
		String[] outputNames = new String[outputNameSet.size()];
		int count=0;
		for(String key:outputNameSet){
			outputNames[count]=key;
			count++;
		}
		return outputNames;
	}
}
