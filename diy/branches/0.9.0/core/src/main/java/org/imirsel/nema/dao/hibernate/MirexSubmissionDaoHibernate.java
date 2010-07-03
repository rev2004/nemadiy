/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;

/**
 * @author gzhu1
 *
 */
public class MirexSubmissionDaoHibernate extends GenericDaoHibernate<MirexSubmission, Long> implements MirexSubmissionDao {

	public MirexSubmissionDaoHibernate() {
		super(MirexSubmission.class);
	}

	public List<MirexSubmission> getSubmissions(User user){
		 List<MirexSubmission> submissionList = getHibernateTemplate().find("from MirexSubmission sub where sub.user=?", user);
		  return submissionList;
	}
	
	public List<MirexSubmission> getSubmissions(Contributor contributor){
		return getHibernateTemplate().find("find MirexSubmission submission where ? member of submission.contributors",contributor);
	}

	
	public List<MirexSubmission> getSubmissions(MirexTask task) {
		List<MirexSubmission> submissionList = getHibernateTemplate().find("from MirexSubmission sub where sub.mirexTask=?", task);
		  return submissionList;
	}
}
