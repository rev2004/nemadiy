/**
 * 
 */
package org.imirsel.nema.webapp.service;

import java.util.List;

import org.imirsel.nema.model.Contributor;

/**
 * A dictionary of mirex contributors.
 * @see {@link Dictionary}
 * @author gzhu1
 *
 */
public interface MirexContributorDictionary extends Dictionary<Contributor,Long>{

   /**
    * Find contributors with keywords (name, ...)
    * @param str keyword
    */
   List<Contributor> findSimilar(String str);
}
