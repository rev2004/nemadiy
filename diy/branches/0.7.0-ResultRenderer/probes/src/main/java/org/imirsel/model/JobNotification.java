package org.imirsel.model;

import java.sql.Timestamp;

public class JobNotification {
	
	// object id
	private long id;
	private String deliveredTo;
	private String message;
	private Timestamp timeStampCreated;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeliveredTo() {
		return deliveredTo;
	}
	public void setDeliveredTo(String deliveredTo) {
		this.deliveredTo = deliveredTo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getTimeStampCreated() {
		return timeStampCreated;
	}
	public void setTimeStampCreated(Timestamp timeStampCreated) {
		this.timeStampCreated = timeStampCreated;
	}
	

}
