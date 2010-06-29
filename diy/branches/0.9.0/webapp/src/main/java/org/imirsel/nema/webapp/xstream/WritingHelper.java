/**
 * 
 */
package org.imirsel.nema.webapp.xstream;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**  A simple helper for {@link com.thoughtworks.xstream.converters.Converter}
 * 
 * @author gzhu1
 *
 */
public class WritingHelper {
	MarshallingContext context;
	HierarchicalStreamWriter writer;
	
	public WritingHelper(MarshallingContext context,
			HierarchicalStreamWriter writer) {
		super();
		this.context = context;
		this.writer = writer;
	}

	public void setContext(MarshallingContext context) {
		this.context = context;
	}
	
	public void setWriter(HierarchicalStreamWriter writer) {
		this.writer = writer;
	}
	
	/**
	 * Write a node unless obj is not null
	 * @param obj
	 * @param name
	 */
	public void addCheckNull(Object obj,String name){
		if (obj != null) {
			writer.startNode(name);
			context.convertAnother(obj);
			writer.endNode();
		}
	}
}
