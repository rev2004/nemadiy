package org.imirsel.nema.analytics.util.process;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;

public interface ProcessExecutorInterface {

	/**
	 * Returns the configured Logger for this instance.
	 * @return logger 
	 */
	public Logger getLogger();

	/**
	 * Adds a handler to the logger for this instance.
	 * @param handler The handler to add to the logger.
	 */
	public void addLogHandler(Handler handler);

	/**
	 * Kills the process. The runCommand method should them exit gracefully, immeadiately.
	 */
	public void killProcess();

	/**
	 * Sets up and executes the process. Blocks until the process completes.
	 * Output from the process is sent to the Logger.
	 *  
	 * @param input
	 * @return exit value returned by process
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public int runCommand(final Object[] input)
			throws IllegalArgumentException, IOException;

	/**
	 * @return the outpath
	 */
	public File getOutpath();

	/**
	 * @param outpath the outpath to set
	 */
	public void setOutpath(File outpath);

	/**
	 * @return the processWorkingDir
	 */
	public File getProcessWorkingDir();

	/**
	 * @return the processResultsDir
	 */
	public File getProcessResultsDir();

	/**
	 * @return the outputIsDirectory
	 */
	public boolean isOutputIsDirectory();

	/**
	 * @return the isRunning
	 */
	public boolean isRunning();

	/**
	 * @return the commandFormattingStr
	 */
	public String getCommandFormattingStr();

	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName();

	/**
	 * @return the inputToExtend
	 */
	public int getInputToExtend();

	/**
	 * @return the extension
	 */
	public String getExtension();

	/**
	 * @return the envVar
	 */
	public String getEnvVar();

}