package org.imirsel.nema.webapp.webflow;

import java.util.Map;

import org.imirsel.nema.model.Flow;

/**
 * 
 * @author gzhu1
 * 
 */
public interface TasksService {

	boolean testRun(Flow flow);

	boolean fillFlow();

	boolean run(Flow flow);

	public String[] getRoles();

	Map<String, String> fillDefaultParameter(Flow flow);
}
