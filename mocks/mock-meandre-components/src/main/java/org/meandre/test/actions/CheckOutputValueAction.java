/*
 * @(#) CheckOutputValueAction.java @VERSION@
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
import org.meandre.test.datatypes.MultipleUnorderedPushDataOutput;

public class CheckOutputValueAction implements Action {
	
	private Hashtable<String,Object>outputTable;
	
	
	public CheckOutputValueAction(Hashtable<String,Object> outputTable){
		this.outputTable=outputTable;
	}



	public Object invoke(Invocation invocation) throws Throwable {
		Object object=this.outputTable.get(invocation.getParameter(0));
		if(object.equals(invocation.getParameter(1))){
			return null;
		}else if(object instanceof MultipleUnorderedPushDataOutput){
			
			for(Object element: ((MultipleUnorderedPushDataOutput) object).getObjectList()){
				if(invocation.getParameter(1).equals(element)){
					return null;
				}
			}
			
		}
			throw new  org.junit.ComparisonFailure(invocation.getParameter(0).toString(),
					object.toString() , invocation.getParameter(1).toString() );
		
	}

	
	public void describeTo(Description description) {
		description.appendText(" validates output value ");
	}
	

	
}
