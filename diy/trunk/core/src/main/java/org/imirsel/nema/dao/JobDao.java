package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.Job;

/**
 * Job Data Access Object (DAO) interface.
 *
 * @author shirk
 * @since 1.0
 */
public interface JobDao extends GenericDao<Job, Long> {
    /**
     * Return the {@link Job}s that the specified user owns.
     *
     * @param userId The unique ID of the user for whom jobs will be fetched.
     * @return A list of {@link Job}s.
     */
    List<Job> getJobsByOwnerId(Long userId);

}
