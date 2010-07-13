package org.imirsel.nema.webapp.webflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.dao.MirexSubmissionDao;
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

	private MirexTaskDictionary mirexTaskDictionary;
	private MirexContributorDictionary mirexContributorDictionary;
	private MirexSubmissionDao mirexSubmissionDao;
	
	public void setMirexTaskDictionary(MirexTaskDictionary mirexTaskDictionary) {
		this.mirexTaskDictionary = mirexTaskDictionary;
	}

	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}

	public void setMirexSubmissionDao(MirexSubmissionDao mirexSubmissionDao) {
		this.mirexSubmissionDao = mirexSubmissionDao;
	}
	
	public List<MirexTask> mirexTaskSet() {
		return mirexTaskDictionary.findAllActive();
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
	
	public MirexSubmission save(MirexSubmission submission){
		return mirexSubmissionDao.save(submission);
	}
}
