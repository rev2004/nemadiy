/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.ContributorDao;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Contributor;

/**
 * @author gzhu1
 *
 */
public class ContributorDaoHibernate extends GenericDaoHibernate<Contributor, Long>
		implements ContributorDao {
	

	public ContributorDaoHibernate() {
		super(Contributor.class);
	}

	
	public List<Contributor> getContributors(MirexSubmission submission) {
		List list = getHibernateTemplate().find("from Contributor where Id=?", submission.getId());
		  return list;
	}


	public List<Contributor> findSimilar(String str) {
		List list = 
			getHibernateTemplate().find(
					"from Contributor where (firstname like ?) or (lastname like ?) or (orgnization like ?)",
					fuzzy(str),fuzzy(str),fuzzy(str));
		return list;
	}

	private String fuzzy(String str){
		return "%"+str+"%";
	}
	

	

}
