package org.imirsel.nema.webapp.webflow;

import org.imirsel.nema.model.Flow;

public interface TasksService {
 
	boolean testRun(Flow flow);
	boolean fillFlow();
	boolean run(Flow flow);
}
