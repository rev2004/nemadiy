package org.imirsel.model;


import java.util.Date;

import org.imirsel.annotations.SqlPersistence;



@SqlPersistence(
	select = "select * from job where token=?",	
	create =  "create table IF NOT EXISTS job" +
		" (id bigint(20) NOT NULL auto_increment PRIMARY KEY," +
		" name text NOT NULL,"+
		" description text NOT NULL,"+
		" token text NOT NULL,"+
		" host text NOT NULL,"+
		" port int NOT NULL,"+
		" executionInstanceId text NOT NULL," +
		" statusCode text NOT NULL," +
		" ownerId bigint(20) NOT NULL,"+
		" ownerEmail text,"+
		" submitTimeStamp TIMESTAMP," +
		" endTimeStamp TIMESTAMP," +
		" startTimeStamp TIMESTAMP," +
		" updateTimeStamp TIMESTAMP," +
		" execPort int NOT NULL," +
		" numTries int NOT NULL," +
		" flowInstanceId int NOT NULL"+
		") engine=innodb",
	store="insert into job(name,description,token,host,port,executionInstanceId,statusCode,ownerId,ownerEmail,submitTimeStamp,execPort,numTries,flowInstanceId ) " +
			"values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
	start="update job set statusCode=?, executionInstanceId=?,updateTimeStamp = now(), startTimeStamp = now() where token=?",
	finish="update job set statusCode=?,updateTimeStamp = now(), endTimeStamp=now() wh" +
			"ere executionInstanceId=?",
	updateHostAndPort="update job set host=?, execPort=? where executionInstanceId=?",
	queryByName="select id from job where executionInstanceId=?")
public class Job {
	
	static public enum JobStatus {
	      UNKNOWN(-1), SUBMITTED(0), STARTED(1), FINISHED(2), FAILED(3), ABORTED(4);

	    private final int code;

	    private JobStatus(int code) { this.code = code; }

	    public int getCode() { return code; }

	    @Override
		public String toString() {
	         String name = null;
	         switch (code) {

	            case -1: {
	               name = "Unknown";
	               break;
	            }

	            case 0: {
	               name = "Submitted";
	               break;
	            }

	            case 1: {
	               name = "Started";
	               break;
	            }

	            case 2: {
	               name = "Ended";
	               break;
	            }
	            
	            case 3: {
	                name = "Ended";
	                break;
	             }

	            case 4: {
	                name = "Aborted";
	                break;
	             }
	            
	         }
	         return name;
	      }

	      static public JobStatus toJobStatus(int code) {
	         JobStatus status = null;
	         switch (code) {

	            case -1: {
	               status = JobStatus.UNKNOWN;
	               break;
	            }

	            case 0: {
	               status = JobStatus.SUBMITTED;
	               break;
	            }

	            case 1: {
	               status = JobStatus.STARTED;
	               break;
	            }

	            case 2: {
	               status = JobStatus.FINISHED;
	               break;
	            }
	            
	            case 3: {
	                status = JobStatus.FAILED;
	                break;
	             }
	            
	            case 4: {
	                status = JobStatus.ABORTED;
	                break;
	             }
	            
	         }
	         return status;
	      }

		public static Integer toJobStatus(JobStatus status) {
			return status.code;
		}

	
		
	   }
	private Long id;
	
	private String name;
	private String description;
	private String token;
	private String host;
	private Integer port;
	private Date submitTimestamp;
	private Date startTimestamp;
	private Date endTimestamp;
	private Date updateTimestamp;
	private Integer statusCode;
	private Long ownerId;
	private String ownerEmail;
	//private Flow flow;
	private String executionInstanceId;
	
	private int numTries;
	private  String execPort;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Date getSubmitTimestamp() {
		return submitTimestamp;
	}
	public void setSubmitTimestamp(Date submitTimestamp) {
		this.submitTimestamp = submitTimestamp;
	}
	public Date getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public Date getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public String getExecutionInstanceId() {
		return executionInstanceId;
	}
	public void setExecutionInstanceId(String executionInstanceId) {
		this.executionInstanceId = executionInstanceId;
	}
	public int getNumTries() {
		return numTries;
	}
	public void setNumTries(int numTries) {
		this.numTries = numTries;
	}
	public String getExecPort() {
		return execPort;
	}
	public void setExecPort(String execPort) {
		this.execPort = execPort;
	}

	

	
}
