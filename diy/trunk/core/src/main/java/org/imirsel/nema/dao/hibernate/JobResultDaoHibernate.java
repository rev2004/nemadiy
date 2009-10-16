package org.imirsel.nema.dao.hibernate;

import org.imirsel.nema.dao.JobResultDao;
import org.imirsel.nema.model.JobResult;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Job objects.
 */
public class JobResultDaoHibernate extends GenericDaoHibernate<JobResult, Long> implements JobResultDao {

    public JobResultDaoHibernate() {
        super(JobResult.class);
    }

}
