package org.imirsel.nema.service;

import java.util.HashMap;

/** Returns the component metadata
 * 
 * @author Amit Kumar
 *
 */
public interface ComponentMetadataManager{
	
	/**Returns the list of components in a flow
	 * 
	 * @param flowId
	 * @return string[] flowList
	 */
	public String[] getComponents(String flowId);
	
	/**Returns the component property data types
	 * 
	 * @param componentId
	 * @return HashMap of component properties and data type
	 */
	public HashMap<String,DataType> getComponentPropertyDataType(String componentId);
	
	/**
	 * 
	 * @param componentId
	 * @param propertyId
	 * @return Default PropertyValue
	 */
	public PropertyValue getComponentPropertyDefaultValue(String componentId, String propertyId);
}
