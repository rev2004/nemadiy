/*
 * @(#) OutputValueMatcher.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.matchers;

import java.util.Hashtable;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.meandre.test.datatypes.MultipleUnorderedPushDataOutput;


public class OutputValueMatcher<T> extends BaseMatcher<T> {

	private Hashtable<String,Object> htOutput;

	public OutputValueMatcher(Hashtable<String,Object> htOutput){
		this.htOutput = htOutput;
	}
	

	public boolean matches(Object item) {
		if(htOutput==null){
			return false;
		}
		
		for(Object obj:htOutput.values()){
			if(obj instanceof MultipleUnorderedPushDataOutput){
				for(Object element:((MultipleUnorderedPushDataOutput<?>) obj).getObjectList()){
					if(item.equals(element)){
						return true;
					}
				}
			}
			if(item.equals(obj)){
				return true;
			}
		}
		
		
		return false;
	}

	public void describeTo(Description description) {
		description.appendText("a valid output value ");

	}

}
