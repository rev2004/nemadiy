package org.imirsel.nema.webapp.webflow;

import java.util.Map;

import org.imirsel.nema.model.Job;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Flow;

/**
 * 
 * @author gzhu1
 * 
 */
public interface TasksService {

	Job testRun(Flow flow,Map<String,String> parameters,String name,String description) throws MeandreServerException;

	boolean fillFlow();

	Job run(Flow flow,Map<String,String> parameters,String name,String description)throws MeandreServerException;

	public String[] getRoles();

	Map<String, String> fillDefaultParameter(Flow flow);
}
