import java.util.Date;


/**
 * @stereotype entity
 */

public class Job {
	private Long id;
	private String description;
	private String serverAddress;
	private Date submitTimestamp;
	private Date startTimestamp;
	private Date finishTimestamp;
	private Integer jobStatus;
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
	public Integer getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
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
