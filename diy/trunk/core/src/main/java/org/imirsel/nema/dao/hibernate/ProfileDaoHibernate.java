/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.ProfileDao;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Profile;

/**
 * Hibernate implementation of {@link ProfileDao}
 * @author gzhu1
 *
 */
public class ProfileDaoHibernate extends GenericDaoHibernate<Profile, Long>
		implements ProfileDao {
	

	public ProfileDaoHibernate() {
		super(Profile.class);
	}

	/**
         * {@inheritDoc }
         */
	public List<Profile> getContributors(MirexSubmission submission) {
		List<Profile> list = getHibernateTemplate().find("from Contributor where Id=?", submission.getId());
		return list;
	}

        /**
         * {@inheritDoc }
         */
	public List<Profile> findSimilar(String str) {
		List<Profile> list =
			getHibernateTemplate().find(
					"from Contributor where (firstname like ?) or (lastname like ?) or (orgnization like ?)",
					fuzzy(str),fuzzy(str),fuzzy(str));
		return list;
	}

	private String fuzzy(String str){
		return "%"+str+"%";
	}
	

	

}
