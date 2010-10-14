/**
 * 
 */
package org.imirsel.nema.webapp.service;

import java.util.List;

import org.imirsel.nema.model.MirexTask;

/**
 * A dictionary of mirex taskss.
 * @see {@link Dictionary}
 * @author gzhu1
 *
 */
public interface MirexTaskDictionary extends Dictionary<MirexTask,Long>{

   /**
    *
    * @return all tasks in dictionary
    */
   List<MirexTask> getAll();

   /**
    *
    * @return all active tasks in dictionary
    */
   List<MirexTask> findAllActive();
}
