/*
 * @(#) PropertyListMatcher.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;



public class PropertyListMatcher<T> extends BaseMatcher<T> {
	
	private String[] propertyNames;
	
	public PropertyListMatcher(String[] propertyNames){
		this.propertyNames = propertyNames;
	}


	public void describeTo(Description description) {
		description.appendText("a valid entry from the list ");
	}
	


	public boolean matches(Object item) {
		if(propertyNames==null){
			return false;
		}
		for(String property:propertyNames){
			if(item.equals(property)){
				return true;
			}
		}
		return false;
	}
	

}
