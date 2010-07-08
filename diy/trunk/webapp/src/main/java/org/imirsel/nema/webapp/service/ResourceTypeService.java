package org.imirsel.nema.webapp.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.imirsel.nema.model.FileDataType;
import org.imirsel.nema.model.GroupDataType;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.OsDataType;

/** Returns various standardized data types supported by NEMA
 * 
 * @author kumaramit01
 * @since 0.3.0
 * @version 0.8.0 -added get supported output file data types and input data types for a task id.
 */
public interface ResourceTypeService extends Serializable{
	
	
	/** Return the supported tasks
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<NemaTask> getSupportedTasks() throws SQLException;
	
	
	/**
	 * 
	 * @param taskId
	 * @return return list of FileDataTypes
	 * @throws SQLException
	 */
	public List<FileDataType> getSupportedInputFileDataTypes(int taskId) throws SQLException;
	
	
	/**
	 * 
	 * @param taskId
	 * @return the list of file data types for the corresponding task
	 * @throws SQLException
	 */
	public List<FileDataType> getSupportedOutputFileDataTypes(int taskId) throws SQLException;
	
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
	
	
	/** Returns the os type
	 * 
	 * @param value
	 * @return the Os informtion {@link OsDataType}
	 */
	public OsDataType getOsDataType(String value);
}
