package org.imirsel.nema.webapp.request;


public class ExecuteJobRequest {
	String token;
	String name;
	String description;
	long flowInstanceId;
	

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(long flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

}
