package org.imirsel.nema.service.impl;

import java.util.List;
import org.imirsel.nema.dao.SubmissionDao;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.SubmissionManager;


public class SubmissionManagerImpl extends UniversalManagerImpl implements SubmissionManager {

	private SubmissionDao submissionDao;
	
	public void removeSubmission(long l) {
		submissionDao.remove(l);
	}
	public Submission saveSubmission(Submission submission) {
		submissionDao.save(submission);
		return submission;
	}
	
	public SubmissionDao getSubmissionDao() {
		return submissionDao;
	}

	public void setSubmissionDao(SubmissionDao submissionDao) {
		this.submissionDao = submissionDao;
	}

	
	public List<Submission> getSubmissions(User user) {
		return submissionDao.getSubmissions(user);
	}
	
	public List<Submission> getAllSubmissions() {
		return submissionDao.getAllDistinct();
	}

	public Submission getSubmission(User user, String type) {
		return submissionDao.getSubmission(user, type);
	
	}
	
	
	public Submission getSubmission(long submissionId) {
		return submissionDao.get(submissionId);
	}

	

}
