/**
 * 
 */
package org.imirsel.nema.webapp.xstream;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author gzhu1
 *
 */
public class ModelMapConverter implements Converter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		WritingHelper helper=new WritingHelper(context,writer);
		Map<String,Object> modelMap=(Map<String,Object>)(source);
		for (Entry<String,Object> model:modelMap.entrySet()){
			helper.addCheckNull(model.getValue(), model.getKey());
		}
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		return null;
	}

	@Override
	public boolean canConvert(Class type) {
		 return Map.class.isAssignableFrom(type);
	}

	
}
