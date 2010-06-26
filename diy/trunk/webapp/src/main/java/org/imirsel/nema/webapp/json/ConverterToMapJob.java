/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;

import org.imirsel.nema.model.Job;

/**
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
		helper.addCheckNull(job.getEndTimestamp(), "endTimestamp");
		helper.addCheckNull(job.getUpdateTimestamp(), "updateTimestamp");
		helper.addCheckNull(job.getHost(), "host");
		helper.addCheckNull(job.getName(), "name");
		helper.addCheckNull(job.getPort(), "port");
		helper.addCheckNull(job.getJobStatus().toString(), "status");
		return map;
	}

}
