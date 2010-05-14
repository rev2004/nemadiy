package org.imirsel.nema.contentrepository.client;

import java.io.Serializable;
import java.util.List;

import org.imirsel.nema.model.FileDataType;
import org.imirsel.nema.model.GroupDataType;
import org.imirsel.nema.model.OsDataType;

/** Returns various standardized data types supported by NEMA
 * 
 * @author kumaramit01
 * @since 0.3.0
 */
public interface ResourceTypeService extends Serializable{
	/**
	 * 
	 * @return list of FileDataTypes
	 */
	public List<FileDataType> getSupportedFileDataTypes();
	/**
	 * 
	 * @return list of supported operating systems
	 */
	public List<OsDataType> getSupportedOperatingSystems();
	/**
	 * 
	 * @return supported groups
	 */
	public List<GroupDataType> getSupportedGroups();
	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public OsDataType getOsDataType(String value);
}
