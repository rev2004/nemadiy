package org.imirsel.nema.webapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.VanillaPredefinedCommandTemplate;

/**
 * Model object for the webflow for template task, a UUIC is assigned for every task.
 * 
 * @author gzhu1
 * @since 0.6.0
 * 
 */
public class JobForm implements Serializable {
	static private Log logger = LogFactory.getLog(JobForm.class);
	
	private static final long serialVersionUID = 1L;
	private String flowId;
	private String name;
	private String description;

	private String mirexSubmissionCode;

	private UUID uuid;
	private int taskId;

	public final static String IMPOSSIBLE="NON-SUBMISSION";
	/**
	 * Map of remote executable {@link Component}s to the address of the
	 * executable profile stored in the repository service.
	 */
	private Map<Component,ResourcePath> executableMap;
	private Map<Component,VanillaPredefinedCommandTemplate> templateMap;
	
	public JobForm() {
		uuid=UUID.randomUUID();
		logger.debug("set a new flowId "+uuid.toString());
		executableMap=new HashMap<Component,ResourcePath>();
		templateMap=new HashMap<Component,VanillaPredefinedCommandTemplate>();
		mirexSubmissionCode=IMPOSSIBLE;
	}

	public UUID getUuid(){
		return uuid;
	}
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String id) {
		this.flowId = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<Component,ResourcePath> getExecutableMap() {
		return executableMap;
	}

	public Map<Component,VanillaPredefinedCommandTemplate> getTemplateMap() {
		return templateMap;
	}

	
	public void setMirexSubmissionCode(String mirexSubmissionCode) {
		this.mirexSubmissionCode = mirexSubmissionCode;
	}

	public String getMirexSubmissionCode() {
		return mirexSubmissionCode;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskId() {
		return taskId;
	}

}
