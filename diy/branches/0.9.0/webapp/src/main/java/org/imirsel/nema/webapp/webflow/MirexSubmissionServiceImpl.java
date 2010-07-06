package org.imirsel.nema.webapp.webflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.MirexSubmission.SubmissionStatus;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.imirsel.nema.webapp.service.MirexTaskDictionary;
import org.springframework.webflow.core.collection.ParameterMap;

import edu.emory.mathcs.backport.java.util.Arrays;

public class MirexSubmissionServiceImpl {

	static private Log logger = LogFactory
			.getLog(MirexSubmissionServiceImpl.class);

	static public List<SubmissionStatus> statusList() {
		List<SubmissionStatus> list = Arrays.asList(SubmissionStatus.values());
		logger.trace("set status list" + list);
		return list;
	}

	private List<MirexTask> mirexTasks;
	private MirexTaskDictionary mirexTaskDictionary;
	private MirexContributorDictionary mirexContributorDictionary;

	public List<MirexTask> mirexTaskSet() {
		return mirexTasks;
	}

	public void setMirexTasks(List<MirexTask> mirexTasks) {
		this.mirexTasks = mirexTasks;
	}

	public void updateSubmission(MirexSubmission submission, ParameterMap params) {
		Long[] contributorIds = (Long[]) params.getArray("contributor",
				Long.class);
		Long taskId = params.getLong("mirexTask");
		submission.setMirexTask(mirexTaskDictionary.find(taskId));
		if (contributorIds != null) {
			List<Contributor> list=new ArrayList<Contributor>();
			for (long id : contributorIds) {
				list.add(mirexContributorDictionary.find(id));
			}
			submission.setContributors(list);
		}
	}
}
