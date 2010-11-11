package org.imirsel.nema.dao;

import java.util.List;
import java.util.UUID;

import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Profile;

/**
 * Data Access Object for Contributors of Mirex Submission
 * @author gzhu1
 */
public interface ProfileDao extends GenericDao<Profile,Long>{

        /**
         * Find all contributors for a particular mirex submission
         */
	public  List<Profile> getContributors(MirexSubmission submission);

        /**
         * Find all contributors that has keyword
         * @param str keyword to search
         */
	public List<Profile> findSimilar(String str);

        /**
         * 
         * @param uuid
         * @return null if cannot find the profile
         */
        public Profile findByUuid(UUID uuid);

        
}