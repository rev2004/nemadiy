/**
 * 
 */
package org.imirsel.nema.dao;

import java.util.List;

/**
 * DAO to access the mirex submissions
 * Note: strictly Read-Only 
 * @author gzhu1
 *
 */
public interface MirexSubmissionDao {
	
	List<MirexSubmission> getAllSubmissions();
	
	MirexSubmission getSubmissionById(long id);
	MirexSubmission getSubmissionByHashcode(String hashcode);

}
