/**
 * 
 */
package org.imirsel.nema.webapp.xstream;

import org.imirsel.nema.model.Job;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A convert for {@link Job}, that covers more fields for a job detail page  
 * @author gzhu1
 *
 */
public class LongJobConverter extends ShortJobConverter {

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		super.marshal(value, writer, context);
		
		Job job=(Job) value;
		WritingHelper helper=new WritingHelper(context,writer);
		
		helper.addCheckNull(job.getDescription(), "description");
		helper.addCheckNull(job.getFlow().getName(), "flowName");
	}
}
