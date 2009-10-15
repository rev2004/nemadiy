package org.imirsel.nema.dao.hibernate;

import java.util.List;

import org.imirsel.nema.dao.JobDao;
import org.imirsel.nema.model.Job;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Job objects.
 */
public class JobDaoHibernate extends GenericDaoHibernate<Job, Long> implements JobDao {

    public JobDaoHibernate() {
        super(Job.class);
    }
    
    @SuppressWarnings("unchecked")
	public List<Job> getJobsByOwnerId(Long userId) {
        return getHibernateTemplate().find("from Job where ownerId=?", userId);
	}

}
