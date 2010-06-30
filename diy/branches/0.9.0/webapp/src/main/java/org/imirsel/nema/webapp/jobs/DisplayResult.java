package org.imirsel.nema.webapp.jobs;

/**
 * 
 * @author Guojun Zhu
 * 
 * Class to convert JobResult into something better looking in Html.
 *
 */
public class DisplayResult {
	private String url;


	private String displayString;
	private String type;
	
	public DisplayResult(){
		
	}



	public DisplayResult(String url, String displayString, String type) {
		super();
		this.url = url;
		this.displayString = displayString;
		this.type = type;
	}

	public DisplayResult(String url) {
		super();
		this.url = url;
	}

	public String getDisplayString() {
		return displayString;
	}
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



}
