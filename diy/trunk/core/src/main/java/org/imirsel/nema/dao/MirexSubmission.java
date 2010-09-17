/**
 * 
 */
package org.imirsel.nema.dao;

import java.io.Serializable;

/**
 * Submission record from the Mirex 2010 submission system
 * 
 * @author gzhu1
 * 
 */
public class MirexSubmission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -979140837106061694L;
	private long id;
	// unique code for submission, usually initials of programmers + number
	private String hashcode;
	private String name;

	public MirexSubmission() {
		super();
	}
	public MirexSubmission(long id, String hashcode, String name) {
		super();
		this.id = id;
		this.hashcode = hashcode;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public String getHashcode() {
		return hashcode;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
