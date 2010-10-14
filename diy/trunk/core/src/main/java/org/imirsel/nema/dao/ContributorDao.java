package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Contributor;

/**
 * Data Access Object for Contributors of Mirex Submission
 * @author gzhu1
 */
public interface ContributorDao extends GenericDao<Contributor,Long>{

        /**
         * Find all contributors for a particular mirex submission
         */
	public  List<Contributor> getContributors(MirexSubmission submission);

        /**
         * Find all contributors that has keyword
         * @param str keyword to search
         */
	public List<Contributor> findSimilar(String str);

}