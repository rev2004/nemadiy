package org.imirsel.nema.webapp.taglib;

import org.displaytag.decorator.TableDecorator;
import org.imirsel.nema.model.Job;

/**
 * @author  Guojun 
 * 
 * For the display:table tag
 */
public class JobDecorator extends TableDecorator {

	public String getNullValue(){
		return null;
	}
	public String getAbort(){
		Job job=(Job)this.getCurrentRowObject();
		if (job.isRunning()){
			return "<a href=\"abortJob.html?id="+job.getId()+"\">Abort</a>";
		}
		return "Aborted";
	}
	public String getStatus(){
		Job job=(Job)this.getCurrentRowObject();
		
		return job.getJobStatus().toString();
	}
}
