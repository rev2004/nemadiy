/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.ContributorDao;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.ParticipantProfile;

/**
 * @author gzhu1
 *
 */
public class ContributorDaoHibernate extends GenericDaoHibernate<ParticipantProfile, Long>
		implements ContributorDao {
	

	public ContributorDaoHibernate() {
		super(ParticipantProfile.class);
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.dao.ContributorDao#getContributors(org.imirsel.nema.model.MirexSubmission)
	 */
	@Override
	public List<ParticipantProfile> getContributors(MirexSubmission submission) {
		 List list = getHibernateTemplate().find("from ParticipantProfile where Id=?", submission.getId());
		  return list;
	}

	

}
