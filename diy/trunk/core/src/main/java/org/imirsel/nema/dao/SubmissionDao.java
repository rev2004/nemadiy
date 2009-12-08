package org.imirsel.nema.dao;


import java.util.List;

import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;


public interface SubmissionDao extends GenericDao<Submission, Long> {

	 List<Submission> getSubmissions(User user);


}
