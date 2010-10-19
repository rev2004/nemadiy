package org.imirsel.nema.webapp.model;

import java.util.Date;


/**
 * Entry of Meandre Console, (corresponding one record in DB)
 * @author gzhu1
 */
public class ConsoleEntry {
	private String line;
	private Date timeStamp;
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
