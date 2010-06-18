package org.imirsel.model;


import java.io.Serializable;
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
		" statusCode int (11) NOT NULL," + 
		" ownerId bigint(20) NOT NULL,"+  
		" ownerEmail text,"+ 
		" scheduleTimestamp TIMESTAMP,"+
		" submitTimestamp TIMESTAMP," + 
		" endTimestamp TIMESTAMP," + 
		" startTimestamp TIMESTAMP," +
		" updateTimestamp TIMESTAMP," +
		" execPort int(11) NOT NULL," +
		" numTries int(11) NOT NULL," +
		" flowInstanceId bigint(20) NOT NULL"+
		") engine=innodb",
	store="insert into job(name,description,token,host,port,executionInstanceId,statusCode,ownerId,ownerEmail,scheduleTimestamp,submitTimestamp,execPort,numTries,flowInstanceId ) " +
			"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
	start="update job set statusCode=?, executionInstanceId=?,updateTimestamp = now(), startTimestamp = now() where token=?",
	finish="update job set statusCode=?,updateTimestamp = now(), endTimestamp=now() wh" +
			"ere executionInstanceId=?",
	updateHostAndPort="update job set host=?, execPort=? where executionInstanceId=?",
	queryByName="select id from job where executionInstanceId=?")
public class Job implements Serializable, Cloneable{
 	/**
	 * Version of this class.
	 */
	private static final long serialVersionUID = 3383935885803288343L;
	
	static public enum JobStatus {
      UNKNOWN(-1), SCHEDULED(0), SUBMITTED(1), STARTED(2), FINISHED(3), FAILED(4), ABORTED(5);

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
               name = "Scheduled";
               break;
            }

            case 1: {
               name = "Submitted";
               break;
            }

            case 2: {
               name = "Started";
               break;
            }

            case 3: {
               name = "Finished";
               break;
            }
            
            case 4: {
                name = "Failed";
                break;
             }

            case 5: {
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
               status = JobStatus.SCHEDULED;
               break;
             }
            
            case 1: {
               status = JobStatus.SUBMITTED;
               break;
            }

            case 2: {
               status = JobStatus.STARTED;
               break;
            }

            case 3: {
               status = JobStatus.FINISHED;
               break;
            }
            
            case 4: {
               status = JobStatus.FAILED;
               break;
             }
            
            case 5: {
               status = JobStatus.ABORTED;
               break;
             }
            
         }
         return status;
      }
   }
	
	private Long id;
	private String token;
	private String name;
	private String description;
	private String host;
	private Integer port;
	private Integer execPort;
	private Date scheduleTimestamp;
	private Date submitTimestamp;
	private Date startTimestamp;
	private Date endTimestamp;
	private Date updateTimestamp;
	private Integer statusCode = -1;
	private Long ownerId;
	private Long flowInstanceId;
	private String ownerEmail;
	private Integer numTries = 0;
	private String executionInstanceId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Date getScheduleTimestamp() {
		return scheduleTimestamp;
	}
	public void setScheduleTimestamp(Date scheduleTimestamp) {
		this.scheduleTimestamp = scheduleTimestamp;
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
	public Integer getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(Integer statusCode) {
		if(!this.statusCode.equals(statusCode)) {
		  setUpdateTimestamp(new Date());
		  this.statusCode = statusCode;
		}
	}
	
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
	
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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
	public JobStatus getJobStatus() {
	      return JobStatus.toJobStatus(statusCode);
	}
	public void setJobStatus(JobStatus status) {
		this.statusCode = status.getCode();
	}
	
	
	public Integer getNumTries() {
		return numTries;
	}
	public void setNumTries(Integer numTries) {
		this.numTries = numTries;
	}
	public void incrementNumTries() {
		numTries++;
	}
	
	public Integer getExecPort() {
		return execPort;
	}
	public void setExecPort(Integer execPort) {
		this.execPort = execPort;
	}
	public void setFlowInstanceId(Long flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}
	public Long getFlowInstanceId() {
		return flowInstanceId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public Job clone() {
		Job clone = new Job();
		clone.setId(this.getId());
		clone.setName(this.getName());
		clone.setDescription(this.getDescription());
		clone.setScheduleTimestamp(this.getScheduleTimestamp());
		clone.setSubmitTimestamp(this.getSubmitTimestamp());
		clone.setStartTimestamp(this.getStartTimestamp());
		clone.setEndTimestamp(this.getEndTimestamp());
		clone.setUpdateTimestamp(this.getUpdateTimestamp());
		clone.setHost(this.getHost());
		clone.setPort(this.getPort());
	    clone.setExecPort(this.getExecPort());
        clone.setExecutionInstanceId(this.getExecutionInstanceId());
        clone.setStatusCode(this.getStatusCode());
        clone.setNumTries(this.getNumTries());
        clone.setOwnerEmail(this.getOwnerEmail());
        clone.setOwnerId(this.getOwnerId());
        clone.setToken(this.getToken());
        return clone;
	}
}