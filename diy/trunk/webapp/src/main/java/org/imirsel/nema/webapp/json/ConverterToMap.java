/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

/**
 * Converter for a type T object to (key, value) pairs for Json rendering.  
 * @author gzhu1
 * 
 */

public interface ConverterToMap<T> {

	/**
         * Convert {@link T} object into a map of (key, value) pairs
         * @param t
         * @return
         */
	public Map<String,String> convertToMap(T t);
	
}
