package org.imirsel.nema.analytics.evaluation;

import java.util.logging.Handler;
import java.util.logging.Logger;

public interface EvalFileType {

	/**
	 * Returns the logger in use. Can be used to change the logging verbosity 
	 * level with:
	 * getLogger.setLevel(Level.WARNING).
	 * @return the Logger that will be used for console output.
	 */
	public abstract Logger getLogger();

	/**
	 * Adds a handler to the logger (for getting log messages to alternate printstreams).
	 * 
	 * @param logHandler
	 */
	public abstract void addLogHandler(Handler logHandler);

}