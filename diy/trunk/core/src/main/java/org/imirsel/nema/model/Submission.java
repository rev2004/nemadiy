package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="submission")
@NamedQueries ({
    @NamedQuery(
        name = "findSubmissionByUserAndType",
        query = "select s from Submission s where s.type = ?"
        )
})
public class Submission extends BaseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	// the user who submits this
	private User user;
	// id of the job that is considered as a submission
	private Long jobId;
	// job type there could be multiple submissions
	private String type;
	
	private Date dateCreated;
	
	// same as the job name
	private String name;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	 public Long getId() {
	    return id;
	 }


	public void setId(Long id) {
		this.id = id;
	}


	@JoinColumn(name = "userId")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	@Column(nullable=false)
	public Long getJobId() {
		return jobId;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(nullable=false)
	public String getType() {
		return type;
	}
	
	
	@Column(name="dateCreated")
	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public boolean equals(Object o) {
		  if (this == o) {
	            return true;
	        }
	        if (!(o instanceof Submission)) {
	            return false;
	        }

	        final Submission submission = (Submission) o;

	        return !(submission != null ? !(jobId==submission.getJobId()) : submission.getJobId() != -1l);

	}

	@Override
	public int hashCode() {
		return (jobId != null ? jobId.hashCode() : 0);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": jobId " + this.getId();
	}


}
