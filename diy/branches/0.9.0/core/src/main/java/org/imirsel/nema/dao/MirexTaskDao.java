/**
 * 
 */
package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexTask;

/**
 * @author gzhu1
 *
 */
public interface MirexTaskDao extends GenericDao<MirexTask, Long> {

	void addMirexTask(MirexTask task);
	
	List<MirexTask> findAllActive();
	
}
