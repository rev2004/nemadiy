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
	public String getDuration(){
		/*static final PeriodFormatter formatter = new PeriodFormatterBuilder()
	     .printZeroAlways()
	     .appendHours()
	     .appendSeparator(":")
	     .printZeroRarely()
	     .appendMi()
	     .appendSuffix(" month", " months")
	     .toFormatter();
*/
		Job job=(Job)this.getCurrentRowObject();
		if (job.getEndTimestamp()!=null){
	//		Period period=new Period(job.getEndTimestamp().getTime(),job.getScheduleTimestamp().getTime());
	//		return period.toString();
			Long duration=(job.getEndTimestamp().getTime()-job.getScheduleTimestamp().getTime())/1000;
			long hr=duration/3600; duration-=hr*3600;
			long min=(duration/60); duration-=min*60;
			return String.format("%02d:%02d:%02d",hr,min,duration);
		}else {
			return "";
		}
	}
}
