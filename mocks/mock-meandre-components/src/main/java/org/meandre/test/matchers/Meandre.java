/*
 * @(#) Meandre.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.matchers;

import java.util.Hashtable;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;


/**Allows for static imports
 * 
 * @author Amit Kumar
 * Created on Mar 7, 2009 6:07:11 AM
 *
 */
public class Meandre {

	  @Factory
	  public static <T> Matcher<String> aValidProperty(String[] propertyNames) {
	    return new PropertyListMatcher<String>(propertyNames);
	  }
	  
	  @Factory
	  public static <T> Matcher<String> aValidInput(String[] inputNames) {
	    return new PropertyListMatcher<String>(inputNames);
	  }
	  
	  @Factory
	  public static <T> Matcher<String> aValidOutput(String[] outputNames) {
	    return new PropertyListMatcher<String>(outputNames);
	  }
	  
	  
	  @Factory
	  public static <T> Matcher<Object> aValidOutputValue(Hashtable<String,Object> htOutput) {
	    return new OutputValueMatcher<Object>(htOutput);
	  }




}
