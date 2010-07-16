/**
 * 
 */
package org.imirsel.nema.webapp.service;

import java.util.List;

import org.imirsel.nema.model.MirexTask;

/**
 * @author gzhu1
 *
 */
public interface MirexTaskDictionary extends Dictionary<MirexTask,Long>{
  
   List<MirexTask> getAll();
   List<MirexTask> findAllActive();
}
