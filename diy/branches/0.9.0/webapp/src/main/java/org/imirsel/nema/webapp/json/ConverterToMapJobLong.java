/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

import org.imirsel.nema.model.Job;



/**
 * Converter for a  {@link Job} to (key, value) pairs for Json rendering. 
 * This converter include more fields from Job. 
 * @author gzhu1
 *
 */
public class ConverterToMapJobLong extends ConverterToMapJob {
	
	@Override
	public Map<String, String> convertToMap(Job job) {
		Map<String, String> map = super.convertToMap(job);

		JsonHelper helper = new JsonHelper(map);

		helper.addCheckNull(job.getDescription(), "description");

		helper.addCheckNull(job.getFlow().getName(),"flowName");
		return map;
	}
}
