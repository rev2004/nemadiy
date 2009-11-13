package org.imirsel.model;

import java.io.Serializable;

import org.imirsel.annotations.SqlPersistence;

/** Probe side model of the job result
 * 
 * @author Amit Kumar
 *
 */

@SqlPersistence(
		select = "select * from jobResult where jobId=?",	
		create =  "create table IF NOT EXISTS jobResult" +
			" (id bigint(20) NOT NULL auto_increment PRIMARY KEY," +
			" resultType varchar(255) NOT NULL,"+
			" url mediumtext NOT NULL,"+
			" jobId bigint(20) NOT NULL" +
			") engine=innodb",
		store="insert into jobResult(resultType,url,jobId) " +
				"values(?,?,?)"
		)
public class JobResult implements Serializable {

	/**
	 * The version of this class.
	 */
	private static final long serialVersionUID = -5142973545301953290L;
	private Long id;
	private Job job;
	private String url;
	private String resultType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result
				+ ((resultType == null) ? 0 : resultType.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		JobResult other = (JobResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (resultType == null) {
			if (other.resultType != null)
				return false;
		} else if (!resultType.equals(other.resultType))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
}
