package org.imirsel.nema.meandre.util;

import java.util.List;

import org.imirsel.nema.model.Job;
import org.springframework.jdbc.core.RowCallbackHandler;
//Document This
public interface MeandreConsoleDao {
    void clearConsole(Job job);
    List<ConsoleEntry> getEntry(Job job);
    List<ConsoleEntry> getEntry(Job job,int maxCount);
    void handleEntry(Job job,RowCallbackHandler handler);
}
