/*
 * @(#) MultiplePushDataElements.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.datatypes;

/**Output DataType: Use this output type
 * when the component spits out multiple values
 * for the same output
 * 
 * @author Amit Kumar
 * Created on Apr 7, 2009 8:26:02 AM
 *
 */
public class MultipleUnorderedPushDataOutput<T> {
	
	private T[] objectList;
	
	public MultipleUnorderedPushDataOutput(T[] objectList){
		this.objectList= objectList;
	}


	public T[] getObjectList() {
		return objectList;
	}
	
	
	
	
	

}
