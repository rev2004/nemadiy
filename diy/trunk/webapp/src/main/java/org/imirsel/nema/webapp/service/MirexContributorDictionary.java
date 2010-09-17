/**
 * 
 */
package org.imirsel.nema.webapp.service;

import java.util.List;

import org.imirsel.nema.model.Contributor;

/**
 * @author gzhu1
 *
 */
public interface MirexContributorDictionary extends Dictionary<Contributor,Long>{
  
   List<Contributor> findSimilar(String str);
}
