/**
 * 
 */
package org.imirsel.nema.webapp.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.model.Job;

/**
 *  Converter for a  {@link MeandreServerProxyConfig} to (key, value) pairs for Json rendering. 
 * @author gzhu1
 * 
 */
public class ConverterToMapServerConfig
		implements
		ConverterToMap<MeandreServerProxyConfig> {

	@Override
	public Map<String, String> convertToMap(
			MeandreServerProxyConfig config) {
		Map<String, String> map = new HashMap<String, String>();

		JsonHelper helper = new JsonHelper(map);

		helper.addCheckNull(config.getHost(), "host");
		helper.addCheckNull(config.getPort(), "port");
		helper.addCheckNull(config.getMaxConcurrentJobs(),
				"maxConcurrentJobs");
		

		return map;
	}

}
