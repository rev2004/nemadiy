/*
 * @(#) GetDefaultPropertyValueAction.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.actions;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Description;

import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class GetDefaultPropertyValueAction implements Action {
	
	private HashMap<String,String> properties;
	
	
	public GetDefaultPropertyValueAction(Map<String,String> properties){
		this.properties=(HashMap<String, String>) properties;
	}



	public Object invoke(Invocation invocation) throws Throwable {
		return this.properties.get(invocation.getParameter(0));
	}

	
	public void describeTo(Description description) {
		description.appendText(" returns property value ");
	}
	

	
}
