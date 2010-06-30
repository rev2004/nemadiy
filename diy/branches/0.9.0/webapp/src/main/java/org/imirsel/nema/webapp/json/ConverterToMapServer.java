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
 *  Converter for a  {@link Entry} of <{@link MeandreServerProxyConfig},{@link MeandreServerProxyStatus}> 
 *  to (key, value) pairs for Json rendering. 
 * @author gzhu1
 * 
 */
public class ConverterToMapServer
		implements
		ConverterToMap<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>> {

	@Override
	public Map<String, String> convertToMap(
			Entry<MeandreServerProxyConfig, MeandreServerProxyStatus> entry) {
		
		ConverterToMapServerConfig converter=new ConverterToMapServerConfig();
		Map<String, String> map = converter.convertToMap(entry.getKey());

		JsonHelper helper = new JsonHelper(map);

		
		helper.addCheckNull(entry.getValue().getNumAborting(), "numAborting");
		helper.addCheckNull(entry.getValue().getNumRunning(), "numRunning");

		return map;
	}

}
