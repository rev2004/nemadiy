/**
 * 
 */
package org.imirsel.nema.dao;

import org.imirsel.nema.model.MirexTask;

/**
 * @author gzhu1
 *
 */
public interface MirexTaskDao extends GenericDao<MirexTask, Long> {

	void addMirexTask(MirexTask task);
	
}
