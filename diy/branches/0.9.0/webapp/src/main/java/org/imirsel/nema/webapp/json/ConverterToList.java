/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converter for a {@link Collection} to a list of (key, value) pair for Json rendering.  
 * @author gzhu1
 *
 */
public class ConverterToList<T> {



	public List<Map<String,String>> convertToList(Collection<T> collection, ConverterToMap<T> converter){
		
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for (T t:collection){
			list.add(converter.convertToMap(t));
		}
		return list;
	}

}
