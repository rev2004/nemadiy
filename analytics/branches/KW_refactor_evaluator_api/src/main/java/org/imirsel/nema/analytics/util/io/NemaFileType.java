package org.imirsel.nema.analytics.util.io;

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Logger;

public interface NemaFileType {

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
	
	/**
	 * Returns the name of the file type operated on by this class.
	 * @return The file type name.
	 */
	public String getTypeName();
	
	/**
	 * Returns the default file name extension that files of this type have.
	 * Primarily used to create file names.
	 * @return String extension, e.g. ".txt".
	 */
	public String getFilenameExtension();
}