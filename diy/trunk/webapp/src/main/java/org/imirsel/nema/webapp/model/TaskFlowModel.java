package org.imirsel.nema.webapp.model;

import java.io.Serializable;

public class TaskFlowModel implements Serializable{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;
private String name;
private String description;

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
