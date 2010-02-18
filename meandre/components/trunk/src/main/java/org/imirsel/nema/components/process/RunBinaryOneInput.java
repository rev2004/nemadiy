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
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.annotations.BooleanDataType;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.components.util.ProcessOutputReceiver;
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
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Runs external code " +
		"using the process builder. This module accepts one File input.", 
		name="RunBinaryOneInput",
		tags="test ft please hello")
		public class RunBinaryOneInput extends NemaComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="Input file list of audio files, String[][]", name="FileList")
	final static String DATA_INPUT_1= "FileList";

	@ComponentOutput(description="Java File Object Out", name="fileObjectOut")
	final static String DATA_OUTPUT_1= "fileObjectOut";

    @StringDataType(hide=true)
	@ComponentProperty(defaultValue="/path/to/workingDir",
			description="The Working Directory of the Executeable",
			name="Working Directory")
			final static String DATA_PROPERTY_WORKINGDIR = "Working Directory";
	private String workingDir = "/path/to/workingDir";

	@StringDataType()
	@ComponentProperty(defaultValue="$m $s -anOption $i $o",
			description="Command format string. $m is the binary/script name. $i represents the " +
			"input file. $o represents the output file. $s is a scratch directory (if needed). Any number of command line options" +
			"can also be specified. Example: if from the command line you run: myProgramName " +
			"-t 0.1 -fl 1024 -i <inputfile> -o <outputfile>, the proper format string would be: " +
			"$m -t 0.1 -fl 1024 -i $i -o $o.",
			name="Command Format String")
			final static String DATA_PROPERTY_FORMATSTRING = "Command Format String";
	private String commandFormattingStr = "$m -anOption $i $o";

	@StringDataType(editRole=RoleAdmin.class)
	@ComponentProperty(defaultValue="myExecutableName",
			description="The name of the executable, e.g. bextract, extractFeatures, runtempo, etc.",
			name="Executeable Name")
			final static String DATA_PROPERTY_EXECNAME = "Executeable Name";
	private String execName = "myExecutableName";

	@StringDataType()
	@ComponentProperty(defaultValue="outputFileName.txt",
			description="The name of the output file (passed into the command format string to the $o field)." +
			" This option is overriden if AddExtentionToInput is set to TRUE",
			name="Output File Name")
			final static String DATA_PROPERTY_OUPUTFILENAME = "Output File Name";
	private final String outputFileName = "outputFileName.txt";

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

	
	private String outfile;
	private String processWorkingDir;
	private String processWorkingDirName;
	private String processResultsDir;
	private boolean isAborted = false;
	Process process = null;;
	ProcessOutputReceiver procOutputReceiverThread = null;

	// log messages are set to go to cout in the superclass NemaComponent
	
	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void initialize (ComponentContextProperties ccp) throws ComponentExecutionException, ComponentContextException{
		super.initialize(ccp);
		
		if(process != null) {
            process.destroy();
        }
		isAborted = false;
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory())
					.getAbsoluteProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance(ccp.getPublicResourcesDirectory())
			.getResultLocationForJob(ccp.getFlowExecutionInstanceID());
		} catch (IOException e1) {
			try {
				throw new ComponentExecutionException(e1);
			} catch (ComponentExecutionException e) {
				_logger.log(Level.SEVERE, "IOException in initialise!",e);
				throw e;
			}
		}
		_logger.info("RUNBINARY: PROCESS WORKING DIR: " + processWorkingDir);


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
		workingDir = String.valueOf(cc.getProperty(DATA_PROPERTY_WORKINGDIR));
		commandFormattingStr = String.valueOf(cc.getProperty(DATA_PROPERTY_FORMATSTRING));
		execName = String.valueOf(cc.getProperty(DATA_PROPERTY_EXECNAME));
		addExtension = Boolean.valueOf(cc.getProperty(DATA_PROPERTY_ADDEXTENSION));
		extension = String.valueOf(cc.getProperty(DATA_PROPERTY_EXTENSION));
		env_var = String.valueOf(cc.getProperty(DATA_PROPERTY_ENV_VAR));
		String[] fileLists = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] outLists = new String[fileLists.length];
		_logger.info("\n" +
				"=============================================================\n" +
				"Starting execution of external binaries\n" +
				"=============================================================\n" +
				"Number of files to process: " + fileLists.length + "\n");
		
		File[] names = new File[fileLists.length];
		for (int i = 0; i < fileLists.length; i++) {
			_logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
					"FILE:  " + (i+1) +"/" + fileLists.length + "\n");
			if(isAborted) {
				_logger.info("\n" +
					"Execution of external binaries aborted");
				break;
			}

			File inFile = new File(fileLists[i]);

			try {
				runCommand(inFile.getCanonicalPath());
				outLists[i] = outfile;
			} catch (IOException e) {
				_logger.log(Level.SEVERE,"IOException occured while working with input file: " + inFile.getAbsolutePath(),e); 
			} catch (RuntimeException e) {
				_logger.log(Level.SEVERE,"Runtime occured while working with input file: " + inFile.getAbsolutePath(),e);
			}
		}
		_logger.info("=============================================================\n" +
				"Execution of external binaries complete\n" +
				"=============================================================\n");
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, outLists);
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	public void dispose (ComponentContextProperties ccp) throws ComponentContextException {
		super.dispose(ccp);
		if(process != null) {
            process.destroy();
        }
		if(procOutputReceiverThread != null){
			procOutputReceiverThread.kill();
		}
		isAborted = true;
	}

	private void runCommand(final String inputFilename) throws IOException, RuntimeException {
		//System.out.println("External Code Integration Module \nby Kris West, University of East Anglia, UK, kristopher.west@uea.ac.uk");

		// Create File to represent working directory
		File dir;
		if (!workingDir.contentEquals("")) {
			dir = new File(workingDir);
		} else {
			dir = new File (processWorkingDir);
		}
		File resdir = new File(processResultsDir);
		
		// Get the output filename
		if (addExtension == false) {
			outfile = resdir.getCanonicalPath() + File.separator + outputFileName;
		} 
		else {
			outfile = resdir.getCanonicalPath() + File.separator + (new File(inputFilename)).getName() + extension;            
		}

		// Create command
		String ExternalCommand = "";
		String[] components = commandFormattingStr.split("[$]");

		File command = new File(execName);
		if (!command.exists()) {
			File command2 = new File(dir.getCanonicalPath() + File.separator + execName);
			if (!command2.exists()) {
				_logger.info("External binary (" + command2.getAbsolutePath() + ") NOT FOUND!");
				throw new RuntimeException("External Integration module was unable to locate your command!\n" +
						"File names tried:\n\t" + command.getCanonicalPath() + "\n\t" + command2.getCanonicalPath() + "\n" +
						"Please ensure that your binaries are in the working directory set in the ExternalInteration " +
						"modules's properties panel.");
			} else {
				command = command2;
			}
		}

		int commandLength = 0;
		String[] cmdArray;
		for (int i=0;i<components.length;i++) {
			if (components[i].length() >= 1) {
				char testSymbol = components[i].charAt(0);
				switch (testSymbol) {
				//case 'm': ExternalCommand += "\"" + command.getCanonicalPath() + "\"" + components[i].substring(1);
				case 'm':
					commandLength++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						commandLength += comps.length;
					}

					break;
					// case 'i': ExternalCommand += "\"" + inputFilename + "\"" + components[i].substring(1);
				case 'i':
					commandLength++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						commandLength += comps.length;
					}
					break;
					//case 'o': ExternalCommand += "\"" + outfile + "\"" + components[i].substring(1);
				case 'o':
					commandLength++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						commandLength += comps.length;
					}
					break;
					
				case 's':
					commandLength++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						commandLength += comps.length;
					}
					break;
					//default: ExternalCommand += components[i];
				default:
					if(components[i].trim().equals("")) {
						//commandLength--;
					} else {
						String[] comps = components[i].trim().split(" ");
						commandLength += comps.length;
					}
				break;
				}
			} else {
				//ExternalCommand += components[i];
				//System.out.println("short component: " + components[i]);
			}
		}
		cmdArray = new String[commandLength];

		int cmdCount = 0;
		for (int i=0;i<components.length;i++) {
			if (components[i].length() >= 1) {
				char testSymbol = components[i].charAt(0);
				switch (testSymbol) {
				//case 'm': ExternalCommand += "\"" + command.getCanonicalPath() + "\"" + components[i].substring(1);
				case 'm':
					//cmdArray[cmdCount] = "\"" + command.getCanonicalPath() + "\"";
					cmdArray[cmdCount] = command.getCanonicalPath();
					cmdCount++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						for (int j=0;j<comps.length;j++) {
							cmdArray[cmdCount] = comps[j].trim();
							cmdCount++;
						}
					}
					break;
					// case 'i': ExternalCommand += "\"" + inputFilename + "\"" + components[i].substring(1);
				case 'i':
					//cmdArray[cmdCount] = "\"" + inputFilename + "\"";
					cmdArray[cmdCount] = inputFilename;
					cmdCount++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						for (int j=0;j<comps.length;j++) {
							cmdArray[cmdCount] = comps[j].trim();
							cmdCount++;
						}
					}
					break;
					//case 'o': ExternalCommand += "\"" + outfile + "\"" + components[i].substring(1);
				case 'o':
					//cmdArray[cmdCount] = "\"" + outfile + "\"";
					cmdArray[cmdCount] = outfile;
					cmdCount++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						for (int j=0;j<comps.length;j++) {
							cmdArray[cmdCount] = comps[j].trim();
							cmdCount++;
						}
					}
					break;
				case 's':
					//cmdArray[cmdCount] = "\"" + outfile + "\"";
					cmdArray[cmdCount] = processWorkingDir;
					cmdCount++;
					if(!components[i].substring(1).trim().equals("")) {
						String[] comps = components[i].substring(1).trim().split(" ");
						for (int j=0;j<comps.length;j++) {
							cmdArray[cmdCount] = comps[j].trim();
							cmdCount++;
						}
					}
					break;
				default:
					if(components[i].trim().equals("")) {
					} else {
						String[] comps = components[i].trim().split(" ");
						for(int j=0;j<comps.length;j++) {
							cmdArray[cmdCount] = comps[j].trim();
							cmdCount++;
						}         
					}
				break;
				}
			} else {
				//ExternalCommand += components[i];
				//System.out.println("short component: " + components[i]);
			}
		}
		String msg = "Running command:    ";
		for (int i=0;i<cmdArray.length;i++) {
			msg += cmdArray[i] + " ";
		}
		msg += "\n";
		msg += "In directory:       " + dir.getCanonicalPath() + "\n";
		msg += "Sending results to: " + resdir.getCanonicalPath() + "\n";
		
		_logger.info(msg);

		ProcessBuilder pb = new ProcessBuilder(cmdArray);

		Map<String, String> env = pb.environment();
		if (!env_var.contentEquals("VAR_NAME,VAR_VAL")){
		     String[] env_pair = env_var.split(",");
		     if (env_pair.length == 2){
			     env.put(env_pair[0], env_pair[1]);
			     _logger.info("Environment variable " + env_pair[0] +"="+ env_pair[1]+ " succesfully set.");
		     }
		     else {
		    	 _logger.info("The environment variable " + env_var + " can not be parsed !!!");
		     }
		}
		// 
		// env.remove("OTHERVAR");
		// env.put("VAR2", env.get("VAR1") + "suffix");
		pb.directory(dir);
		pb.redirectErrorStream(true);
		InputStream is = null;
		try{
			process = pb.start();
			is = process.getInputStream();
			_logger.info("*******************************************\n" +
					"EXTERNAL PROCESS STDOUT AND STDERR:");
			procOutputReceiverThread = new ProcessOutputReceiver( is, _logger );
	        procOutputReceiverThread.start();
	        int exitStatus;
	        
			try {
				exitStatus = process.waitFor();
				_logger.info("EXTERNAL PROCESS EXIT STATUS: " + exitStatus + "\n" +
						"*******************************************");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally{
			if(procOutputReceiverThread != null){
				procOutputReceiverThread.kill();
			}
			if(process != null){
				process.getErrorStream().close();
			}
			if(is != null){
				is.close();
			}
		}
	}
}
