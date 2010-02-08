/**
 * 
 */
package org.imirsel.nema;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.meandre.annotations.Component;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/**
 * @author kriswest
 *
 */
public abstract class NEMAComponent implements ExecutableComponent {

	// log messages are here
	protected Logger _logger;
	private java.io.PrintStream cout;
	
	public void dispose(ComponentContextProperties ccp) 
			throws ComponentContextException {
		cout = null;
	}

	public abstract void execute(ComponentContext cc) 
			throws ComponentExecutionException, ComponentContextException;

	public void initialize(ComponentContextProperties ccp, Class componentClass)
			throws ComponentExecutionException, ComponentContextException {
		cout = ccp.getOutputConsole();
		_logger = Logger.getLogger(componentClass.getName());
		_logger.addHandler(new StreamHandler(cout, new SimpleFormatter()));
	}

}
