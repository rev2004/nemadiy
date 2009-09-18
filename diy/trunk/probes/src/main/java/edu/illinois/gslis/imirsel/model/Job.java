package edu.illinois.gslis.imirsel.model;

import java.sql.Timestamp;

public class Job {
	// object id
	private long id;
	private Timestamp finishTimeStamp;
	private String name;
	private String serverAddress;
	private Timestamp startTimeStamp;
	private int status;
	private Timestamp submitTimeStamp;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getFinishTimeStamp() {
		return finishTimeStamp;
	}
	public void setFinishTimeStamp(Timestamp finishTimeStamp) {
		this.finishTimeStamp = finishTimeStamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public Timestamp getStartTimeStamp() {
		return startTimeStamp;
	}
	public void setStartTimeStamp(Timestamp startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getSubmitTimeStamp() {
		return submitTimeStamp;
	}
	public void setSubmitTimeStamp(Timestamp submitTimeStamp) {
		this.submitTimeStamp = submitTimeStamp;
	}
	
}
