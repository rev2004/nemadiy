package org.imirsel.nema.webapp.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.webapp.controller.JobController;
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
	final String ROOT_TOKEN = "result path";
	final String CHILD_TOKEN = "item";
	static protected Log logger = LogFactory.getLog(DisplayResultSet.class);

	public DisplayResultSet(Set<JobResult> results) {
		if ((results != null) && (results.size() > 0)) {
			JobResult[] resultArray = results.toArray(new JobResult[0]);
			int jroot = findRoot(resultArray);
			this.root = new DisplayResult(resultArray[jroot].getUrl(),
					ROOT_TOKEN, "path");
			this.children = new ArrayList<DisplayResult>();
			int count = 1;
			String url, displayString, type;
			for (int i = 0; i < resultArray.length; i++) {
				if (i != jroot) {

					url = processUrl(resultArray[i].getUrl());
					if (displayable(url)) {
						type = "img";
						displayString = "<img width='150' src='" + url
								+ "' alt='" + CHILD_TOKEN + count + "'/>";
					} else {
						type = "file";
						displayString = CHILD_TOKEN + count;
					}
					children.add(new DisplayResult(url, displayString, type));
					count++;
				}
			}
		}
	}

	public List<DisplayResult> getChildren() {
		return children;
	}

	public DisplayResult getRoot() {
		return root;
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
}
