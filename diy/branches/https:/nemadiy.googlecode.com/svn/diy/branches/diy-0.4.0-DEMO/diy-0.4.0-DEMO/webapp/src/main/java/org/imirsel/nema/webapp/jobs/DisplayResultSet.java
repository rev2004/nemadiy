package org.imirsel.nema.webapp.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.JobResult;

/**
 * 
 * @author Guojun Zhu
 * 
 * Class to convert JobResult set into something better looking (root path+ set of items) in Html.
 *
 */
public class DisplayResultSet {
	private List<DisplayResult> children;
	private DisplayResult root;
	final String ROOT_TOKEN = "result directory";
	final String INDEX_TOKEN="results";
	final String CHILD_TOKEN = "item";
	final String NONE_TOKEN="no result";
	static protected Log logger = LogFactory.getLog(DisplayResultSet.class);

	public DisplayResultSet(Set<JobResult> results) {
		this.children = new ArrayList<DisplayResult>();

		if ((results != null) && (results.size() > 0)) {
			String index=testIndexType(results);
			if (index==null){
			JobResult[] resultArray = results.toArray(new JobResult[0]);
			int jroot = findRoot(resultArray);
			this.root = new DisplayResult(resultArray[jroot].getUrl(),
					ROOT_TOKEN, "path");
			int count = 1;
			String url, displayString, type;
			for (int i = 0; i < resultArray.length; i++) {
				if (i != jroot) {

					url = processUrl(resultArray[i].getUrl());
					if (displayable(url)) {
						type = "img";
						displayString = "<img width='150' src='" + url
								+ "' alt='" + shorten(url) + "'/>";
					} else {
						type = resultArray[i].getResultType();
						displayString = shorten(url);
					}
					children.add(new DisplayResult(url, displayString, type));
					count++;
				}
			}
			}
			else {
				this.root=new DisplayResult(index,INDEX_TOKEN,"index");
			}
		}else{
			this.root=new DisplayResult("",NONE_TOKEN,"none");
		}
	}

	public List<DisplayResult> getChildren() {
		return children;
	}

	public DisplayResult getRoot() {
		return root;
	}
	
	
	//test whether the result is the type with one index.html file linked to everything
	private String testIndexType(Set<JobResult> results){
		for (JobResult s:results){
			String url=s.getUrl();
			if (strip(url).equalsIgnoreCase("index.htm")){
				return url;
			}else if(s.getResultType().equalsIgnoreCase("dir") && 
					((strip(url).equalsIgnoreCase("evaluation")))    ||  
					((strip(url).equalsIgnoreCase("overall")))		
			){
				if (url.charAt(url.length()-1)=='/') return url+"index.htm";
				else return url+"/index.htm";
			}
		}
		return null;
	}

	// note that the root node is also removed from the results.
	private int findRoot(JobResult[] results) {
		int root = 0;
		for (int i = 1; i < results.length; i++) {
			if (results[root].getUrl().length() > results[i].getUrl().length()) {
				root = i;
			}
		}
		// logger.debug("I have found the root :"+results.contains(root));
		// boolean delete=results.remove(root);
		// logger.debug("I have delete the root :"+delete);
		return root;
	}

	private String processUrl(String url) {
		// String identifier = "published_resources/nema";
		// int index = url.indexOf(identifier);
		// String resultFolder = url.substring(index + identifier.length());
		// return "http://nema.lis.uiuc.edu/nema_out" + resultFolder;
		return url;
	}

	// strip everything except file name
	private String strip(String shortUrl) {
		String[] parts = shortUrl.trim().split("[\\/]");
		if (parts != null)
			return parts[parts.length - 1];
		else
			return "";
	}

	private boolean displayable(String url) {
		if (url == null)
			return false;
		String filename = strip(url);
		// logger.debug("striped url is "+filename);
		String[] parts = filename.trim().split("\\.");
		String ext = parts[parts.length - 1];
		if (ext != null)
			ext = ext.toLowerCase();
		else
			return false;
		if ((ext.equals("png")) || (ext.equals("jpg")) || (ext.equals("bmp"))
				|| (ext.equals("gif")) || (ext.equals("tif"))
				|| (ext.equals("jpeg"))) {
			return true;
		} else
			return false;
	}
	
	private String shorten(String url){
		String shortUrl=strip(url);
		if (shortUrl.length()<20) return shortUrl;
		else {
			String s=shortUrl.substring(0,5)+"..."+shortUrl.substring(shortUrl.length()-6);
			return s;
		}
	}
	public static void main(String[] arg){
		DisplayResultSet ds=new DisplayResultSet(null);
		System.out.print(ds.strip("fjdsakjf;asd/fasdjf;dasjf/"));
	}
}
