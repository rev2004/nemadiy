package org.imirsel.nema.model;

import java.io.Serializable;

/** Java classpath entry
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class Path implements Serializable{

	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = 2933147179307618325L;
	
	private String element;
	
	public Path(String element){this.element=element;}
	
	public String getElement(OsDataType targetOs) {
		String modifiedElement = element;
		if(targetOs.getValue().equals("Unix Like"))
			return element;
		else
			modifiedElement = element.replace('/', '\\');
	
		return modifiedElement;
	}
	

}
