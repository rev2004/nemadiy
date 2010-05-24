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
public class TaskFlowModel implements Serializable {
	static private Log logger = LogFactory.getLog(TaskFlowModel.class);
	
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;

	private UUID uuid;
	
	private Map<Component,ResourcePath> executableMap;
	private Map<Component,VanillaPredefinedCommandTemplate> templateMap;
	public UUID getUuid(){
		return uuid;
	}
	
	
	public TaskFlowModel() {
		uuid=UUID.randomUUID();
		logger.debug("set a new id "+uuid.toString());
		executableMap=new HashMap<Component,ResourcePath>();
		templateMap=new HashMap<Component,VanillaPredefinedCommandTemplate>();
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





	public Map<Component,ResourcePath> getExecutableMap() {
		return executableMap;
	}


	


	public Map<Component,VanillaPredefinedCommandTemplate> getTemplateMap() {
		return templateMap;
	}

}
