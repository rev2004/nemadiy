package org.imirsel.nema.meandre.util;

import org.imirsel.nema.dao.MeandreConsoleDao;
import java.util.List;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;

/**
 * A command line tool for console dumping. 
 * @author gzhu1
 */
public class ConsoleDumpCommand{

	FlowService flowService;
	MeandreConsoleDao meandreConsoleDao;
	void manualDump(){
		ConsoleUtil consoleUtil=new ConsoleUtil();
		consoleUtil.setDao(meandreConsoleDao);
		consoleUtil.setFolder("");
		List<Job> jobs=flowService.getUserJobs(-2L);//Admin_user's id
		for (Job job:jobs){
			if (job.isDone()) {
				consoleUtil.dumpConsoleToFile(job);
			}
		}
	}
}
