package org.imirsel.nema.webapp.webflow;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.MirexSubmission.SubmissionStatus;

import edu.emory.mathcs.backport.java.util.Arrays;


public class MirexSubmissionServiceImpl {

	static private Log logger = LogFactory.getLog(MirexSubmissionServiceImpl.class);
	
	static public List<SubmissionStatus> statusList(){
		List<SubmissionStatus> list=Arrays.asList(SubmissionStatus.values());
		logger.trace("set status list"+list);
		return list;
	}
	
	private List<MirexTask> mirexTasks;

	public List<MirexTask> mirexTaskSet() {
		return mirexTasks;
	}

	public void setMirexTasks(List<MirexTask> mirexTasks) {
		this.mirexTasks = mirexTasks;
	}
}
