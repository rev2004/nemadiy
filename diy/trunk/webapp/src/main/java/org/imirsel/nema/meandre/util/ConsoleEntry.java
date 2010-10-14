package org.imirsel.nema.meandre.util;

import java.util.Date;

import org.imirsel.nema.model.Job;

class ConsoleEntry {
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
