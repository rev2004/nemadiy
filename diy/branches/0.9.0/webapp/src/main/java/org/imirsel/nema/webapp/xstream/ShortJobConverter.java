package org.imirsel.nema.webapp.xstream;

import org.imirsel.nema.model.Job;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A convert for {@link Job}, that only covers enough fields for a job list table  
 * @author gzhu1
 *
 */
public class ShortJobConverter implements Converter {

	public boolean canConvert(Class clazz) {
		return Job.class == clazz;
	}

	
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Job job=(Job) value;
		WritingHelper helper=new WritingHelper(context,writer);
		
		helper.addCheckNull(job.getId(), "id");
		
	
		helper.addCheckNull(job.getSubmitTimestamp(),"submitTimestamp");
		helper.addCheckNull(job.getScheduleTimestamp(),"scheduleTimestamp");
		helper.addCheckNull(job.getEndTimestamp(),"endTimestamp");
		helper.addCheckNull(job.getUpdateTimestamp(),"updateTimestamp");
		helper.addCheckNull(job.getHost(), "host");
		helper.addCheckNull(job.getName(), "name");
		helper.addCheckNull(job.getPort(), "port");
		helper.addCheckNull(job.getJobStatus().toString(), "status");
	}

	
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return null;
	}

}
