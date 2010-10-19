package org.imirsel.nema.webapp.dao;

import org.imirsel.nema.webapp.model.ConsoleEntry;
import java.util.List;

import org.imirsel.nema.model.Job;
import org.springframework.jdbc.core.RowCallbackHandler;
//Document This
public interface MeandreConsoleDao {
    /**
     * Wipe all records of console in Meandre DB for a job
     */
    void clearConsole(Job job);
    /**
     * Get all entries in console from Meandre DB for a job
     */
    List<ConsoleEntry> getEntry(Job job);

    /**
     * Get entries in console from Meandre DB for a job.
     * Only get the last maxCount entries if there are more than that.
     */
    List<ConsoleEntry> getEntry(Job job,int maxCount);

    /**
     * Handle the Meandre DB console records of a job one by one with handler,
     * Use this method to avoid out-of-memory when a job has huge amount of console entries
     * @param handler {@link RowCallbackHandler} for one result set
     * @param job job
     */
    void handleEntry(Job job,RowCallbackHandler handler);
}
