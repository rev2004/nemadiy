/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.Profile;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;

/**
 * Implementation of {@link MirexSubmissionDao} with Hibernate
 * @author gzhu1
 *
 */
public class MirexSubmissionDaoHibernate extends GenericDaoHibernate<MirexSubmission, Long> implements MirexSubmissionDao {

	public MirexSubmissionDaoHibernate() {
		super(MirexSubmission.class);
	}

        /**
         * {@inheritDoc }
         */
	public List<MirexSubmission> getSubmissions(User user){
		 List<MirexSubmission> submissionList = getHibernateTemplate().find("from MirexSubmission sub where sub.user=?", user);
		  return submissionList;
	}

        /**
         * {@inheritDoc }
         */
	public List<MirexSubmission> getSubmissions(Profile contributor){
		return getHibernateTemplate().find("find MirexSubmission submission where ? member of submission.contributors",contributor);
	}

	/**
         * {@inheritDoc }
         */
	public List<MirexSubmission> getSubmissions(MirexTask task) {
		List<MirexSubmission> submissionList = getHibernateTemplate().find("from MirexSubmission sub where sub.mirexTask=?", task);
		  return submissionList;
	}

        /**
         * {@inheritDoc }
         */
	public List<MirexSubmission> findByHashcodeBeginning(String code) {
		List<MirexSubmission> submissionList = getHibernateTemplate().find("from MirexSubmission  where hashcode like ?", code+"%");
		  return submissionList;
	}

        /**
         * Save a submission into database, with correct setting for createTime(first time), or updateTime
         * @param submission
         * @return
         */
	@Override
	public MirexSubmission save(MirexSubmission submission){
		
		if (submission.getId()==null) {
			submission.setCreateTime(new Date());
		}
		submission.setUpdateTime(new Date());
		return super.save(submission);
		
	}
}
