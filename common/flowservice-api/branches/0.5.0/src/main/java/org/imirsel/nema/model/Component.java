package org.imirsel.nema.model;

import java.io.Serializable;

/** Component is represented using this bean
 * 
 * @author kumaramit01
 * @since 0.5.0
 */
public class Component implements Comparable<Component>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2197472386343940677L;
	private String name;
	private String description;
	private String uri;
	private String instanceUri;
	private boolean hidden;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getInstanceUri() {
		return instanceUri;
	}
	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription(){
		return description;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public int compareTo(Component o) {
		if(o==null){
			return 0;
		}
		return o.getInstanceUri().compareTo(this.getInstanceUri());
	}

}
