/**
 * 
 */
package org.imirsel.nema.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.imirsel.nema.dao.MirexTaskDao;
import org.imirsel.nema.model.MirexTask;

/**
 * @author gzhu1
 *
 */
public class MirexTaskDaoHibernate extends GenericDaoHibernate<MirexTask,Long> implements
		MirexTaskDao {

	public MirexTaskDaoHibernate(){
		super(MirexTask.class);
	}
	/* (non-Javadoc)
	 * @see org.imirsel.nema.dao.MirexTaskDao#addMirexTask(org.imirsel.nema.model.MirexTask)
	 */
	@Override
	public void addMirexTask(MirexTask task) {
			save(task);
	}

}
