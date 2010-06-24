package org.imirsel.nema.components;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/** Remote components implement the RemoteExecutable
 * 
 * @author kumaramit01
 * @since 0.6.0
 */
public interface RemoteExecutableComponent {
	
	public void initializeNema(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException;
	
	
	public  void executeNema(ComponentContext componentContext) 
	throws ComponentExecutionException, ComponentContextException;
	
	
	public void disposeNema(ComponentContextProperties componentContextProperties) 
	throws ComponentContextException;
	
}
