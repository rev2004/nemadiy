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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

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
			
			if(obj instanceof ArrayList){
				ArrayList alist1 = (ArrayList)obj;
				if(item instanceof ArrayList){
					ArrayList alist2 = (ArrayList)item;
				
					if(alist1.size()==alist2.size()){
						
						Iterator i1 = alist1.iterator();
						Iterator i2 = alist2.iterator();
						boolean comparison = true;
						while (i1.hasNext() && i2.hasNext()) {
							Object o1 = i1.next();
							Object o2 = i2.next();
							if(!o1.equals(o2)){
								comparison = false;
							}
						}
					
					
						if(comparison){ return true;}
					}
				}
				
				
				
				
				
			}else if(!item.getClass().isArray()){
				// item is not an array
				if(item.equals(obj)){
					return true;
				}
			}else{
				// item is an array
				// check if the obj is an array
				if(obj.getClass().isArray()){
					return Arrays.equals((Object []) item, (Object[]) obj);
				}
			}
			
			
			
			
			
		}
		
		
		return false;
	}

	public void describeTo(Description description) {
		description.appendText("a valid output value ");

	}

}
