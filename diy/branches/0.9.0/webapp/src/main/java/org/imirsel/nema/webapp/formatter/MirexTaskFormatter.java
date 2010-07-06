/**
 * 
 */
package org.imirsel.nema.webapp.formatter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.imirsel.nema.model.MirexTask;
import org.springframework.format.Formatter;

/**
 * @author gzhu1
 *
 */
public class MirexTaskFormatter implements Formatter<MirexTask> {

	
	private Map<String,MirexTask> taskMap;
	
	@Override
	public String print(MirexTask object, Locale locale) {
		
		return object.getFullname()+"("+object.getName()+")";
	}

	@Override
	public MirexTask parse(String text, Locale locale) throws ParseException {
		if (taskMap==null){
			throw new ParseException(
					"The map of mirex tasks is required to be populated in order to parse the mirex task string",0);
		}else {return taskMap.get(text);
		}
	}

	public void setTaskMap(List<MirexTask> mirexTasks) {
		taskMap=new HashMap<String,MirexTask>();
		for (MirexTask task:mirexTasks){
			taskMap.put(task.getName(),task);
		}
	}

	

}
