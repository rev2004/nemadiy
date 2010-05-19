package org.imirsel.nema.webapp.model;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.webapp.webflow.TasksServiceImpl;
/**
 * Model object for the webflow for template task
 * 
 * @author gzhu1
 * @since 0.5
 * 
 */
public class TaskFlowModel implements Serializable {
	static private Log logger = LogFactory.getLog(TaskFlowModel.class);
	
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;

	private UUID uuid;
	
	public UUID getUuid(){
		return uuid;
	}
	
	
	public TaskFlowModel() {
		uuid=UUID.randomUUID();
		logger.debug("set a new id "+uuid.toString());
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
