package org.imirsel.nema.dao.hibernate;

import java.util.HashMap;
import java.util.List;

import org.imirsel.nema.dao.SubmissionDao;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;




public class SubmissionDaoHibernate extends GenericDaoHibernate<Submission, Long> implements SubmissionDao {
		
	public SubmissionDaoHibernate(){
		super(Submission.class);
	}

	public List<Submission> getSubmissions(User user) {
		  List submissionList = getHibernateTemplate().find("from Submission where userId=?", user.getId());
		  return submissionList;
	}

	public void saveSubmission(Submission Submission) {
		save(Submission);
	}
	
	public Submission getSubmission(Long jobId) {
		  List<Submission> submissionList = getHibernateTemplate().find("from Submission where jobId= ?", jobId);
		    if (submissionList.isEmpty()) {
	            return null;
	        } else {
	            return  submissionList.get(0);
	        }
	}

	public void remove(Submission submission) {
		 Object pvalue = getSubmission(submission.getJobId());
	     getHibernateTemplate().delete(pvalue);
	}

	
	public Submission getSubmission(User user, String type) {
		List<Submission> submissionList = getHibernateTemplate().find("from Submission where type=? and userId= ?", new Object[] {type,user.getId()});
		if(submissionList.isEmpty()){
			  return null;
		  }
		  return submissionList.get(0);
	}


}
