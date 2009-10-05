package org.meandre.demo.components;

import java.io.PrintStream;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/** This executable component just prints the inputed object to the 
 * standard output.
 *
 * @author Xavier Llor&agrave;
 *
 */
public class PrintObjectComponentHack implements ExecutableComponent {

	/** The number of objects printed */
	long lObjectsPrinted;
	
	/** The should the count be printed */
	boolean bPrintCount;
	
	/** The print stream to use */
	PrintStream ps = null;
	
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentExecutionException If a fatal condition arises during
	 *         the execution of a component, a ComponentExecutionException
	 *         should be thrown to signal termination of execution required.
	 * @throws ComponentContextException A violation of the component context
	 *         access was detected
	 */
	public void initialize ( ComponentContextProperties ccp ) 
	throws ComponentExecutionException, ComponentContextException {
		this.lObjectsPrinted = 0;
		this.bPrintCount = false;
		
		String sCount = ""+ccp.getProperty("count");
		sCount = sCount.trim();
		if ( sCount.equalsIgnoreCase("true") )
			this.bPrintCount = true;
		ps = ccp.getOutputConsole();
	}

	/** This method just pushes a concatenated version of the entry to the
	 * output.
	 *
	 * @throws ComponentExecutionException If a fatal condition arises during
	 *         the execution of a component, a ComponentExecutionException
	 *         should be thrown to signal termination of execution required.
	 * @throws ComponentContextException A violation of the component context
	 *         access was detected

	 */
	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException {
		
		String sObject = cc.getDataComponentFromInput("object").toString();
		++lObjectsPrinted;
	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( bPrintCount )
			ps.print("P"+lObjectsPrinted+"\t");
		ps.println(sObject);
		//ps.flush();		
	}

	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @throws ComponentExecutionException If a fatal condition arises during
	 *         the execution of a component, a ComponentExecutionException
	 *         should be thrown to signal termination of execution required.
	 * @throws ComponentContextException A violation of the component context
	 *         access was detected
	 * @param ccp The properties associated to a component context
	 */
	public void dispose ( ComponentContextProperties ccp ) 
	throws ComponentExecutionException, ComponentContextException {
		this.lObjectsPrinted = 0;
		this.bPrintCount = false;
		this.ps = null;
	}

}
