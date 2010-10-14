/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.MirexTaskDao;
import org.imirsel.nema.model.MirexTask;

/**
 * Hibernate implementation of {@link MirexTaskDao}
 * @author gzhu1
 *
 */
public class MirexTaskDaoHibernate extends GenericDaoHibernate<MirexTask,Long> implements
		MirexTaskDao {

	public MirexTaskDaoHibernate(){
		super(MirexTask.class);
	}
	/**
         * {@inheritDoc }
         */
	public void addMirexTask(MirexTask task) {
			save(task);
	}

	/**
         * {@inheritDoc }
         */
	public List<MirexTask> findAllActive() {
		return getHibernateTemplate().find("find MirexTask where active = ?",true);
	}

}
