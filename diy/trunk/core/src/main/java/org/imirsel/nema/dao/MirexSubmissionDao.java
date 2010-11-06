package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Profile;
import org.imirsel.nema.model.MirexTask;
import org.imirsel.nema.model.User;

/**
 * Data Access Object of Mirex Submissions
 * @author gzhu1
 */
public interface MirexSubmissionDao extends GenericDao<MirexSubmission,Long>{

        /**
         * Get all submissions from a particular user
         */
	public List<MirexSubmission> getSubmissions(User user);

        /**
         * Get all submissions from a particular user
         */
	public List<MirexSubmission> getSubmissions(Profile contributor);

        /**
         * Get all submissions of a particular task
         */
	public List<MirexSubmission> getSubmissions(MirexTask task);

        /**
         * Get all submissions with certain submission hashcode
         */
	public List<MirexSubmission> findByHashcodeBeginning(String string);

}