/**
 * 
 */
package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexTask;

/**
 * Data Access Object for Mirex Task
 * @author gzhu1
 *
 */
public interface MirexTaskDao extends GenericDao<MirexTask, Long> {



	/**
         * Add task into database
         */
        void addMirexTask(MirexTask task);

        /**
         * Find all active tasks
         */
	List<MirexTask> findAllActive();
	
}
