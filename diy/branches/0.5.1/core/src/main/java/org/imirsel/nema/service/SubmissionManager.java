package org.imirsel.nema.service;

import java.util.List;

import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;

public interface SubmissionManager {
	public void removeSubmission(long l); 
	public Submission saveSubmission(Submission submission);
	List<Submission> getSubmissions(User user);
	List<Submission> getAllSubmissions();
	public Submission getSubmission(User user, String type);
	public Submission getSubmission(long submissionId);
}
