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
   Contributor find(Long id);
   void add(Contributor contributor);
   void refresh();
   List<Contributor> findSimilar(String str);
}
