package org.imirsel.nema.meandre.util;

import java.util.List;

import org.imirsel.nema.model.Job;

public interface MeandreConsoleDao {
    void clearConsole(Job job);
    List<ConsoleEntry> getEntry(Job job);
    List<ConsoleEntry> getEntry(Job job,int maxCount);
}
