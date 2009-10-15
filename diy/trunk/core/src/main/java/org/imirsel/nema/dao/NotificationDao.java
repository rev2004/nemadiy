package org.imirsel.nema.dao;

import java.util.List;

import org.imirsel.nema.model.Job;

/**
 * Notification Data Access Object (DAO) interface.
 *
 * @author shirk
 * @since 1.0
 */
public interface NotificationDao extends GenericDao<Job, Long> {
    /**
     * Return the {@link Notifications}s that the specified user was the
     * recipient.
     *
     * @param userId The unique ID of the user for whom 
     * {@link Notification}s will be fetched.
     * @return A list of {@link Notifications}s.
     */
    List<Job> getJobsByOwnerId(Long userId);

}
