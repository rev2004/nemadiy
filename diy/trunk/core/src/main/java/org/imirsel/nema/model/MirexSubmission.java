package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenerationTime;

/**
 * Submission record from the Mirex  submission system
 * 
 * @author gzhu1
 * @since 0.7.0
 */
@Entity
@Table(name="mirex_submission")
public class MirexSubmission  implements Serializable {

	public static enum SubmissionStatus {
		UNKNOWN(-1,"unknown"),STARTED(0,"started"),READY_FOR_RUN(1,"ready for run"),RUNNING(2,"running"),ERROR(3,"error in running"),
		FINISHED(4,"finished running"),REVIEWED(5,"reviewed"),DELETED(6,"deleted");
		final private int code;
		final private String statusStr;
		private SubmissionStatus(int code,String statusStr){
			this.code=code;
			this.statusStr=statusStr;
		}
		
		public int getCode() {
			return code;
		}

		@Override
		public String toString(){
			return statusStr;
		}
		
		
	}
	
	private static final long serialVersionUID = -979140837106061694L;
	private Long id;
	// unique code for submission, usually initials of programmers + number
	private String hashcode="";
	private String name;
	private String readme;
	private SubmissionStatus status=SubmissionStatus.UNKNOWN;
	private Date updateTime;
	private Date createTime;
	private List<Contributor> contributors;
	private List<MirexNote> notes;
	private User user;
	private MirexTask mirexTask;
	private String url;
	private String resourcePath;

	public MirexSubmission() {
		super();
	}
	public MirexSubmission(long id, String hashcode, String name) {
		super();
		this.id = id;
		this.hashcode = hashcode;
		this.name = name;
	}

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Unique code for submission, usually initials of programmers + number
	 * @return
	 */
	@Column(name="hashcode",length=10,nullable=false,unique=true)
	public String getHashcode() {
		return hashcode;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Column(name="readme",length=30000)
	public String getReadme() {
		return readme;
	}
	public void setReadme(String readme) {
		this.readme = readme;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="status",nullable=false)
	public SubmissionStatus getStatus() {
		return status;
	}
	public void setStatus(SubmissionStatus status) {
		this.status = status;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}
	
	@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	@OrderColumn(name="contributor_rank")
	public List<Contributor> getContributors() {
		return contributors;
	}
	public void addContributor(Contributor contributor){
		if (contributors==null) {contributors=new ArrayList<Contributor>();}
		contributors.add(contributor);
	}
	
	public void setNotes(List<MirexNote> notes) {
		this.notes = notes;
	}
	@OneToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	@OrderColumn(name="note_order")
	@JoinColumn(name="submission_id",nullable=false)
	public List<MirexNote> getNotes() {
		return notes;
	}
	
	public void addNote(MirexNote note){
		if (this.notes==null) {
			this.notes=new ArrayList<MirexNote>();
		}
		this.notes.add(note);
		note.setSubmission(this);
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public User getUser() {
		return user;
	}
	
	
	
	public void setMirexTask(MirexTask mirexTask) {
		this.mirexTask = mirexTask;
	}
	@ManyToOne
	public MirexTask getMirexTask() {
		return mirexTask;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

        @Column(name="createTime",updatable=false)
        @Temporal(TemporalType.TIMESTAMP)
        public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public void setResourcePath(String resource) {
		this.resourcePath = resource;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	
	
	
	@Override
	public boolean equals(Object o) {
		if (this==o) {return true;}
		else if ((o==null) || (!(o instanceof MirexSubmission))) {return false;}
		else {
			final MirexSubmission that=(MirexSubmission)o;
			return this.hashcode.equals(that.getHashcode());
		}
	}
	
	@Override
	public int hashCode() {
		return this.hashcode.hashCode();
	}
	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
    		.append("Name:", name)
    		.append("Hashcode:",hashcode);
		return sb.toString();
	}
	
	
	

}
