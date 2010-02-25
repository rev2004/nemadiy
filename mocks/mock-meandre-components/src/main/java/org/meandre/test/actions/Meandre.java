/*
 * @(#) Meandre.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.actions;

import java.util.Hashtable;
import java.util.Map;

import org.hamcrest.Factory;
import org.jmock.api.Action;



/**Allows for static imports
 * 
 * @author Amit Kumar
 * Created on Mar 7, 2009 6:07:31 AM
 *
 */
public class Meandre {
	
	 @Factory
	  public static <T> Action checkOutputValue(Hashtable<String,Object> outputTable) {
	    return new CheckOutputValueAction(outputTable);
	  }
	 
	
	 @Factory
	  public static <T> Action returnInputValue(Hashtable<String,Object> inputTable) {
	    return new GetInputOutputValueAction(inputTable);
	  }
	 

	 @Factory
	  public static <T> Action returnPropertyValue(Map<String,String> properties) {
	    return new GetDefaultPropertyValueAction(properties);
	  }
	 

	 

}
