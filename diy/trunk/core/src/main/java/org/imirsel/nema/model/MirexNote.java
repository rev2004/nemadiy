package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Model for notes in mirex submission
 * @author gzhu1
 * @since 0.9.0
 */
@Entity
@Table(name="mirex_note")
public class MirexNote implements Serializable{
	
	
	
	
	/**
         * <ul>
	 * <li>PRIVATE: left by mirexRunner for internal use <li/>
	 * <li>PUBLIC: 	left by mirexRunner for public view <li/>
	 * <li>USER:  	left by user <li/>
	 * <li>AUTO_PUBLIC:		left by system for public view<li/>
	 * <li>AUTO_PRIVATE:	left by system for internal use<li/>
	 *
	 */
	public static enum NoteType{
		PRIVATE,PUBLIC,USER,AUTO_PUBLIC,AUTO_PRIVATE
	}
	
	private static final long serialVersionUID = 868188736722883743L;
	
	private Long id;
	private NoteType type;
	private MirexSubmission submission;
	private User author;
	private Date createTime;
	private String content;
	
	
	public MirexNote(){
		super();
	}
	
	public MirexNote(Long id, NoteType type, MirexSubmission submission,
			User author, Date createTime, String content) {
		super();
		this.id = id;
		this.type = type;
		this.submission = submission;
		this.author = author;
		this.createTime = createTime;
		this.content = content;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Enumerated(EnumType.STRING)
	public NoteType getType() {
		return type;
	}
	
	public void setType(NoteType type) {
		this.type = type;
	}
	
	@ManyToOne 
	@JoinColumn(name="submission_id",insertable=false, updatable=false,nullable=false)
	public MirexSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(MirexSubmission submission) {
		this.submission = submission;
	}
	
	@ManyToOne
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

        @Column(updatable=false)
        @Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(length=3000)
	public String getContent() {
		return content;
	}
	
	
	
	
	@Transient
	public boolean isUserVisible(){
		return ((type==NoteType.AUTO_PUBLIC)||(type==NoteType.PUBLIC)||(type==NoteType.USER));
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((submission == null) ? 0 : submission.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
		return true;
		if (!(obj instanceof MirexNote))
		return false;
		
		MirexNote other = (MirexNote) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (submission == null) {
			if (other.submission != null)
				return false;
		} else if (!submission.equals(other.submission))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "MirexNote [author=" + author + ", content=" + content
				+ ", createTime=" + createTime + ", id=" + id + ", submission="
				+ submission + ", type=" + type + "]";
	}
	
	
}
