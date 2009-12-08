package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.SubmissionDao;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;




public class SubmissionDaoHibernate extends GenericDaoHibernate<Submission, Long> implements SubmissionDao {
		
	public SubmissionDaoHibernate(){
		super(Submission.class);
	}

	public List<Submission> getSubmissions(User user) {
		  List submissionList = getHibernateTemplate().find("from Submission where User", user.getId());
		  return submissionList;
	}

	public void saveSubmission(Submission Submission) {
		save(Submission);
	}
	
	public Submission getSubmission(Long jobId) {
		  List submissionList = getHibernateTemplate().find("from Submission where jobId", jobId);
		    if (submissionList.isEmpty()) {
	            return null;
	        } else {
	            return (Submission) submissionList.get(0);
	        }
	}

	public void remove(Submission submission) {
		 Object pvalue = getSubmission(submission.getJobId());
	     getHibernateTemplate().delete(pvalue);
	}

	

}
