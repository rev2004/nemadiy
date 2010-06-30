package org.imirsel.nema.webapp.request;


public class ExecuteJobRequest {
	private String token;
	private String name;
	private String description;
	private long flowInstanceId;
	

	public String getToken() {
		return token;
	}
	public void setToken(final String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}
	public long getFlowInstanceId() {
		return flowInstanceId;
	}
	public void setFlowInstanceId(final long flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

}
