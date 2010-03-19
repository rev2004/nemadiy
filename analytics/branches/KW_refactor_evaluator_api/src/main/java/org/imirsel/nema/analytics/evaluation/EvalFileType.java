package org.imirsel.nema.analytics.evaluation;

import java.io.PrintStream;
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
	 * Ensures that the log output is also sent to the specified PrintStream.
	 * @param stream The PrintStream to send the log output to.
	 */
	public void addLogDestination(PrintStream stream);
	
}