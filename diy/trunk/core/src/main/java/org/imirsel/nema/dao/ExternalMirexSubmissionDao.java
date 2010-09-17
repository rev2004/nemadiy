/**
 * 
 */
package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;

/**
 * DAO to access the mirex submissions
 * Note: strictly Read-Only 
 * @author gzhu1
 *
 */
public interface ExternalMirexSubmissionDao {
	
	List<MirexSubmission> getAllSubmissions();
	
	MirexSubmission getSubmissionById(long id);
	MirexSubmission getSubmissionByHashcode(String hashcode);

}
