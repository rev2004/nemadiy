/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.core.security.Role;

import org.imirsel.service.*;
import org.imirsel.nema.annotations.*;
import org.imirsel.nema.role.RoleAdmin;
import org.imirsel.nema.util.ProcessOutputReceiver;
/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Holds Train and Test file lists " +
		"until something is sent to the Trigger input to notify the component and flow " +
		"that feature extraction has completed.", 
		name="TrainTestGateKeeper",
		tags="test ft please hello")
		public class TrainTestGateKeeper implements ExecutableComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="Trigger Input (usually from a RunBinaryOneInput component that is responsible " +
			"for executing external feature extraction code prior to a train test run", name="Trigger")
	final static String DATA_INPUT_1= "Trigger";
	
	@ComponentInput(description="An Array of the fully qualified names of each of the Training Files (String[])", 
			name="Training List Files In")
	final static String DATA_INPUT_2= "Training List Files In";
	
	@ComponentInput(description="An Array of the fully qualified names of each of the Training Files (String[])", 
			name="Testing List Files In")
	final static String DATA_INPUT_3= "Testing List Files In";

	@ComponentOutput(description="An Array of the fully qualified names of each of the Training Files (String[])", name="Training List Files Out")
	final static String DATA_OUTPUT_1= "Training List Files Out";
	
	@ComponentOutput(description="An Array of the fully qualified names of each of the Training Files (String[])", name="Testing List Files Out")
	final static String DATA_OUTPUT_2= "Testing List Files Out";

    
	// log messages are here
	private Logger _logger;
	java.io.PrintStream cout;
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void initialize ( ComponentContextProperties ccp ) {
		this._logger = ccp.getLogger();
		cout = ccp.getOutputConsole();
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
		//File inFile = (File)cc.getDataComponentFromInput(DATA_INPUT_1);
		
		Object Trigger = cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] TrainLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] TestLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_3);
		cout.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		cout.println("Pushing Training and Testing Lists off for Training and Classification");
		cout.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		cout.flush();
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, TrainLists);
		cc.pushDataComponentToOutput(DATA_OUTPUT_2, TestLists);
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void dispose ( ComponentContextProperties ccp ) {
		cout.close();

	}

}
