/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.MirexSubmission;
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
		return getHibernateTemplate().find("find MirexSubmission");
	}
}
