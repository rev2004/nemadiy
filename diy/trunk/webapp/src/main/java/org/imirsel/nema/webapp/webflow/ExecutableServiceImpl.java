package org.imirsel.nema.webapp.webflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.contentrepository.client.CommandLineFormatter;
import org.imirsel.nema.contentrepository.client.ResourceTypeService;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.service.UserManager;

public class ExecutableServiceImpl {
	static private Log logger = LogFactory.getLog(TasksServiceImpl.class);
	private FlowService flowService;
	private UserManager userManager;
	private String uploadDirectory;
	private CommandLineFormatter commandLineFormatter;
	private ResourceTypeService resourceServiceType;
	private ArtifactService artifactService;

}
