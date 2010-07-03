/**
 * 
 */
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
 * @author gzhu1
 * 
 */
@Entity
@Table(name = "mirexTask")
public class MirexTask implements Serializable {


	private static final long serialVersionUID = 7267580817801096741L;
	private String name;
	private String fullname;
	private long id;

	@Column(name="name")
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

	@Override
	public	boolean equals(Object o){
		if (this==o) {return true;}
		else if ((o==null) || (!(o instanceof MirexTask))) {return false;}
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
	

}
