/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gzhu1
 * 
 */

public interface ConverterToMap<T> {

	
	public Map<String,String> convertToMap(T t);
	
}
