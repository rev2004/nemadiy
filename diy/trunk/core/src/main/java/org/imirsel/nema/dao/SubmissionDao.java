package org.imirsel.nema.dao;


import java.util.List;

import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;


public interface SubmissionDao extends GenericDao<Submission, Long> {

	 List<Submission> getSubmissions(User user);

	 Submission getSubmission(User user, String type);
	 Submission save(Submission submission);
	 Submission get(Long submissionId);
	 List<Submission> getAllDistinct();
	 void remove(Long l);


}
