package org.imirsel.nema.meandre.util;

import java.util.List;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;

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
