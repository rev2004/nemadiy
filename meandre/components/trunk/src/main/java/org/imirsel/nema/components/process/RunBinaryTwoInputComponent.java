/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.components.process;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.imirsel.nema.analytics.util.process.CommandLineExecutorImpl;
import org.imirsel.nema.annotations.BooleanDataType;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.role.RoleAdmin;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;



/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann and Kris West
 *
 */
@Component(creator="Andreas F. Ehmann", description="Runs external code " +
		"using the process builder. This module accepts one File input.", 
		name="RunBinaryTwoInputComponent",
		tags="process")
		public class RunBinaryTwoInputComponent extends NemaComponent {

	@ComponentInput(description="Input file list of audio files for input1, String[][]", name="FileList1")
	final static String DATA_INPUT_1= "FileList1";
	
	@ComponentInput(description="Input file list of audio files for input2, String[][]", name="FileList2")
	final static String DATA_INPUT_2= "FileList2";

	@ComponentOutput(description="List of files output by the external binary processes run", name="fileListOut")
	final static String DATA_OUTPUT_1= "fileListOut";
	
	@StringDataType()
	@ComponentProperty(defaultValue="$m -anOption $s $1 $2 $o",
			description="Command format string. $m is the binary/script name. $1 represents the " +
			"input file 1. $2 represents input file2. $o represents the output file. $s is a scratch directory (if needed). Any number of command line options" +
			"can also be specified. Example: if from the command line you run: myProgramName " +
			"-t 0.1 -fl 1024 <inputfile1> <inputfile2> -o <outputfile>, the proper format string would be: " +
			"$m -t 0.1 -fl 1024 $1 $2 -o $o.",
			name="Command Format String")
			final static String DATA_PROPERTY_FORMATSTRING = "Command Format String";
	private String commandFormattingStr = "$m -anOption $1 $2 $o";

	@StringDataType(editRole=RoleAdmin.class)
	@ComponentProperty(defaultValue="/path/to/executable",
			description="Path to the executable or shell script",
			name="Executeable Path")
			final static String DATA_PROPERTY_EXECPATH = "Executeable Path";
	private String execPath = "/path/to/executable";

	@StringDataType()
	@ComponentProperty(defaultValue="outputFileName.txt",
			description="The name of the output file (passed into the command format string to the $o field)." +
			" This option is overriden if AddExtentionToInput is set to TRUE",
			name="Output File Name")
			final static String DATA_PROPERTY_OUPUTFILENAME = "Output File Name";
	private String outputFileName = "outputFileName.txt";

	@BooleanDataType()
	@ComponentProperty(defaultValue="false",
			description="Flags whether the output of the process is a directory rather than a file.",
			name="Output is directory")
			final static String DATA_PROPERTY_OUPUTISDIRECTORY = "Output is directory";
	private boolean outputIsDir = false;

	@BooleanDataType()
	@ComponentProperty(defaultValue="true",
			description="Generate the output file name by adding the extention specified " +
			"in the 'Output File Extension to Append' field to the input file name. (true/false). E.g. " +
			"if the input file name is input.txt, this value is true, and the 'Output File Extension to Append' is " +
			".result, the output file name will be input.txt.result",
			name="Add Extension to Input File Name to Generate Output File Name")
			final static String DATA_PROPERTY_ADDEXTENSION = "Add Extension to Input File Name to Generate Output File Name";
	private boolean addExtension = true;

	
	@StringDataType(hide=true)
	@ComponentProperty(defaultValue="VAR_NAME,VAR_VAL",
			description="The environment variable`s name and value separated by \",\"",
			name="Environment Variable" )
			final static String DATA_PROPERTY_ENV_VAR = "Environment Variable";
	private String env_var= "VAR_NAME,VAR_VAL";

	@StringDataType()
	@ComponentProperty(defaultValue=".result",
			description="The output file extension to add to the input file name to " +
			"generate the output file name. Only used if 'Add Extension to Input File Name to Generate Output File Name' " +
			"is set to true.",
			name="Output File Name Extension to Append")
			final static String DATA_PROPERTY_EXTENSION = "Output File Name Extension to Append";
	private String extension= ".result";

	
	private boolean isAborted = false;
	CommandLineExecutorImpl executor;
	
	// logger is setup to got cout in superclass NemaComponent
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void initialize (ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException{
		super.initialize(ccp);
		
		executor = null;
		isAborted = false;
		
		getLogger().info("RUNBINARY: PROCESS WORKING DIR: " + getAbsoluteProcessWorkingDirectory());

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
		commandFormattingStr = String.valueOf(cc.getProperty(DATA_PROPERTY_FORMATSTRING));
		execPath = String.valueOf(cc.getProperty(DATA_PROPERTY_EXECPATH));
		addExtension = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ADDEXTENSION));
		extension = String.valueOf(cc.getProperty(DATA_PROPERTY_EXTENSION));
		env_var = String.valueOf(cc.getProperty(DATA_PROPERTY_ENV_VAR));
		outputFileName = String.valueOf(cc.getProperty(DATA_PROPERTY_OUPUTFILENAME)); 
		outputIsDir = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_OUPUTISDIRECTORY));
		String[] fileLists1 = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] fileLists2 = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] outLists = new String[fileLists1.length];
		

		// sanity check that both filelists are same length
		if (fileLists1.length != fileLists2.length) {
			getLogger().severe("ERROR: File lists for input1 and input2 are different lengths!");
			
			return;
		}
		getLogger().info("\n" +
				"=============================================================\n" +
				"Starting execution of external binaries\n" +
				"=============================================================\n" +
				"Number of files to process: " + fileLists1.length + "\n");

		for (int i = 0; i < fileLists1.length; i++) {
			getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
					"FILE:  " + (i+1) +"/" + fileLists1.length);

			if(isAborted) {
				getLogger().info("Execution of external binaries aborted");
				break;
			}

			File inFile1 = new File(fileLists1[i]);
			File inFile2 = new File(fileLists2[i]);

			try {
				if(addExtension){
					executor = new CommandLineExecutorImpl(
						new File(execPath).getParentFile(), new File(getAbsoluteResultLocationForJob()), 
						new File(getAbsoluteProcessWorkingDirectory()), commandFormattingStr, 
						new File(execPath), 0, extension, env_var);
				}else{
					executor = new CommandLineExecutorImpl(new File(new File(getAbsoluteResultLocationForJob()).getAbsolutePath() + File.separator + outputFileName), outputIsDir, 
							new File(execPath).getParentFile(), new File(getAbsoluteResultLocationForJob()), new File(getAbsoluteProcessWorkingDirectory()), commandFormattingStr, 
							new File(execPath), env_var);
				}
				executor.addLogDestination(getLogDestination());
				int exitVal = executor.runCommand(new Object[]{inFile1,inFile2});
				getLogger().info("Process exited with code " + exitVal);
				
				outLists[i] = executor.getOutpath().getCanonicalPath(); 

			} catch (IOException e) {
				getLogger().log(Level.SEVERE,"IOException occured while working with input file1: '" + inFile1.getAbsolutePath() + "' and input file2: '" + inFile2.getAbsolutePath() + "'",e); 
			} catch (RuntimeException e) {
				getLogger().log(Level.SEVERE,"Runtime occured while working with input file1: '" + inFile1.getAbsolutePath() + "' and input file2: '" + inFile2.getAbsolutePath() + "'",e); 
			}
		}
		getLogger().info("=============================================================\n" +
				"Execution of external binaries complete\n" +
				"=============================================================\n");
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, outLists);
	}

	/** This method is called when the Meandre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	public void dispose (ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
		if(executor != null) {
			executor.killProcess();
       }
		isAborted = true;
	}	

}
