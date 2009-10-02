import java.util.Date;


/**
 * @stereotype entity
 */

public class Job {
	
	public enum JobStatus {UNKNOWN,SUBMITTED,RUNNING,FINISHED};
	
	private Long id;
	private String description;
	private String serverAddress;
	private Date submitTimestamp;
	private Date startTimestamp;
	private Date finishTimestamp;
	private Integer statusCode;
	private Long ownerId;
	private String ownerEmail;
	private Flow flow;
	private String jobUri;
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
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
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
	public Date getFinishTimestamp() {
		return finishTimestamp;
	}
	public void setFinishTimestamp(Date finishTimestamp) {
		this.finishTimestamp = finishTimestamp;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public JobStatus getJobStatus() {
		switch (statusCode) {
		case 1:
		return JobStatus.SUBMITTED;
		case 2:
		return JobStatus.RUNNING;
		case 3:
		return JobStatus.FINISHED;
		default:
			return JobStatus.UNKNOWN;
		}
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
	public Flow getFlow() {
		return flow;
	}
	public void setFlow(Flow flow) {
		this.flow = flow;
	}
	public String getJobUri() {
		return jobUri;
	}
	public void setJobUri(String jobUri) {
		this.jobUri = jobUri;
	}
	
}
