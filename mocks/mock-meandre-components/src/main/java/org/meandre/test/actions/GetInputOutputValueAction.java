/*
 * @(#) GetInputOutputValueAction.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.actions;

import java.util.Hashtable;


import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class GetInputOutputValueAction implements Action {
	
	private Hashtable<String,Object>inputTable;
	
	
	public GetInputOutputValueAction(Hashtable<String,Object> inputTable){
		this.inputTable=inputTable;
	}



	public Object invoke(Invocation invocation) throws Throwable {
		return this.inputTable.get(invocation.getParameter(0));
	}

	
	public void describeTo(Description description) {
		description.appendText(" returns input value ");
	}
	

	
}
