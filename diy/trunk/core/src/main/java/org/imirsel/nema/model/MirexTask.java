package org.imirsel.nema.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.imirsel.nema.util.StringUtil;

/**
 * Model for Mirex Task.  Name is for the short abbreviation such as "QBT".  
 * @author gzhu1
 * 
 */
@Entity
@Table(name = "mirex_task")
public class MirexTask implements Serializable,Comparable<MirexTask>{


	private static final long serialVersionUID = 7267580817801096741L;
	private String name;
	private String fullname;
	private String description;
	
	private String url;
	private long id;
	private boolean active;

	@Column(name="name",nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return fullname;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public	boolean equals(Object o){
		if (this==o) {return true;}
		else if (!(o instanceof MirexTask)) {return false;}
		else {
			final MirexTask that=(MirexTask)o;
			return (
				StringUtil.same(name,that.getName()));
		}
	}
	
	static int cachedHashCode=0;
	@Override 
	public int hashCode(){
		if (cachedHashCode==0) {
			String total=StringUtil.nonNull(name);
			cachedHashCode=total.hashCode();
		}
		return cachedHashCode;
	}
	
	@Override
	public String toString(){
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
        	.append("Name:", name);
		return sb.toString();
	}

	public int compareTo(MirexTask o) {
		
		return (name+fullname).compareTo(o.getName()+o.getFullname());
	}
	

}
