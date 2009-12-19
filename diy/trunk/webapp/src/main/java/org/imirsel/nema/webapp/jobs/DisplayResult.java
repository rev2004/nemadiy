package org.imirsel.nema.webapp.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.webapp.controller.JobController;
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
	static protected Log logger=LogFactory.getLog(JobController.class);
	public DisplayResult(){
		
	}

	public DisplayResult(JobResult jobResult) {
		String shortUrl=jobResult.getUrl();
		url=processUrl(shortUrl);
		if (displayable(url)){
			type="img";
			displayString="<img src='"+url+"' alt='"+strip(shortUrl)+"'/>";
		}else{
			type="file";
		displayString=strip(shortUrl);
		}
	}

	public DisplayResult(String url) {
		super();
		this.url = url;
	}

	public String getDisplayString() {
		return displayString;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//put the full context path
	private String processUrl(String url) {
//		String identifier = "published_resources/nema";
//		int index = url.indexOf(identifier);
//		String resultFolder = url.substring(index + identifier.length());
//		return "http://nema.lis.uiuc.edu/nema_out" + resultFolder;
		return url;
	}
	
	//strip everything except file name
	private String strip(String shortUrl){
		String[] parts=shortUrl.trim().split("[\\/]"); 
		if (parts!=null) return parts[parts.length-1];
		else return "";
	}
	
	private boolean displayable(String url){
		if (url==null) return false;
		String filename=strip(url);
		logger.debug("striped url is "+filename);
		String[] parts=filename.trim().split("\\.");
		String ext=parts[parts.length-1];
		if (ext!=null) ext=ext.toLowerCase();
		else return false;
		if ((ext.equals("png"))||(ext.equals("jpg"))||
				(ext.equals("bmp"))||(ext.equals("gif"))||(ext.equals("tif"))||(ext.equals("jpeg"))){
			return true;
		}else return false;
	}
	public static void main(String[] args){
		DisplayResult dr=new DisplayResult();
		System.out.print(dr.displayable("fdsjafjdsa/dfskjsd;af"));
	}
}
