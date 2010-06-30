/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

import org.imirsel.nema.model.Job;

/**
 * Converter for a  {@link Job} to (key, value) pairs for Json rendering. 
 * @author gzhu1
 * 
 */
public class ConverterToMapJob implements ConverterToMap<Job> {

	@Override
	public Map<String, String> convertToMap(Job job) {
		Map<String, String> map = new HashMap<String, String>();

		JsonHelper helper = new JsonHelper(map);

		helper.addCheckNull(job.getId(), "id");

		helper.addCheckNull(job.getSubmitTimestamp(), "submitTimestamp");
		helper.addCheckNull(job.getScheduleTimestamp(), "scheduleTimestamp");
		helper.addCheckNull(job.getStartTimestamp(), "startTimestamp");
		helper.addCheckNull(job.getEndTimestamp(), "endTimestamp");
		helper.addCheckNull(job.getUpdateTimestamp(), "updateTimestamp");
		helper.addCheckNull(job.getHost(), "host");
		helper.addCheckNull(job.getName(), "name");
		helper.addCheckNull(job.getPort(), "port");
		//now I am free to put any bean. Even not a bean. 
		helper.addCheckNull(job.getJobStatus(), "status");
		return map;
	}

}
