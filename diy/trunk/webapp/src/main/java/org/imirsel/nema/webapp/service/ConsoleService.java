package org.imirsel.nema.webapp.service;

import java.io.FileNotFoundException;

import org.imirsel.nema.model.Job;
import org.imirsel.nema.webapp.dao.MeandreConsoleDao;

/** Console Service provides interface to JobController to display the console output and error stream
 * 
 * @author guojun
 * @author Amit kumar
 * 
 */
public interface ConsoleService {

	/**Set the MeandreConsoleDao
	 * 
	 * @param dao
	 */
	public abstract void setMeandreConsoleDao(MeandreConsoleDao dao);

	/**Set the file system path where the  logs are dumped
	 * 
	 * @param folder
	 */
	public abstract void setFolder(String folder);

	/**
	 * 
	 * @return location of the file system path
	 */
	public abstract String getFolder();

	/**Dumps the console to the file system file
	 * 
	 * @param job
	 */
	public abstract void dumpConsoleToFile(Job job);

	/**Returns the job's console as String.
	 * 
	 * @param job
	 * @return
	 * @throws FileNotFoundException
	 */
	public abstract String findConsole(Job job) throws FileNotFoundException;

	/**Removes the job console from the file system dump.
	 * 
	 * @param job
	 */
	public abstract void deleteConsole(Job job);

}