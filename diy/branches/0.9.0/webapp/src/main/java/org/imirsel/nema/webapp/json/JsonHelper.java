/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.Map;

/**
 * Simple helper for json model map generating.
 * @author gzhu1
 *
 */
public class JsonHelper {
	Map<String,String>	jsonModelMap;
	
	public JsonHelper(Map<String, String> jsonModelMap) {
		super();
		this.jsonModelMap = jsonModelMap;
	}

	public void setJsonModelMap(Map<String, String> jsonModelMap) {
		this.jsonModelMap = jsonModelMap;
	}

	/**
	 * Add the (name,obj) pair to map only if obj is not empty
	 * @param obj
	 * @param name
	 */
	public void addCheckNull(Object obj,String name){
		if ((obj!=null)&&(!obj.toString().isEmpty())){
			jsonModelMap.put(name,obj.toString());
		}
	}
}
