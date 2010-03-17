/**
 * 
 */
package org.imirsel.nema.components;

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.imirsel.nema.analytics.logging.AnalyticsLogFormatter;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**
 * This abstract class is extended by all the Nema Components.
 * @author kriswest
 * @since 0.5.0
 */
public abstract class NemaComponent implements ExecutableComponent {

	// log messages are here
	private Logger _logger;
	private PrintStream logDestination = null;
	
	
	/**
	 * Marks the cout as null after flushing and releases the resources.
	 * This method is called by the components extending this class.
	 * 
	 * @param componentContextProperties The component context properties of
	 * component.
	 * @throws ComponentContextException {@link ComponentContextException}
	 */
	public void dispose(ComponentContextProperties componentContextProperties) 
			throws ComponentContextException {
		getLogger().fine("Disposing of " + this.getClass().getName());
		componentContextProperties.getOutputConsole().flush();
	}

	/**
	 * All classes extending the NemaComponent class must implement this method.
	 */
	public abstract void execute(ComponentContext componentContext) 
			throws ComponentExecutionException, ComponentContextException;

	/**
	 * Initializes the the cout and sets up the logger.
	 * 
	 * @param componentContextProperties The Component Context Property of the component.
	 * @throws ComponentExecutionException {@link ComponentContextException}
	 * @throws ComponentContextException {@link ComponentContextException}
	 */
	public void initialize(ComponentContextProperties componentContextProperties)
			throws ComponentExecutionException, ComponentContextException {
		getLogger().setLevel(Level.FINEST);
		synchronized(NemaComponent.class){
			addLogDestination(componentContextProperties.getOutputConsole());
			getLogger().info("Initialized logging for " + this.getClass().getName());
		}
	}
	
	public Logger getLogger() {
		if (_logger == null){
			_logger = Logger.getLogger(this.getClass().getName());
		}
		return _logger;
	}

	public void addLogDestination(PrintStream stream) {
		logDestination = stream;
		Handler handler = new StreamHandler(logDestination, new AnalyticsLogFormatter());
		getLogger().addHandler(handler);
	}

	public PrintStream getLogDestination(){
		return logDestination;
	}
}
