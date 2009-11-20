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

import org.imirsel.service.*;
/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Runs external code " +
		"using the process builder. This module accepts one File input.", 
		name="RunBinaryOneInput",
		tags="test ft please hello")
		public class RunBinaryOneInput implements ExecutableComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="Input file list of audio files, String[][]", name="FileList")
	final static String DATA_INPUT_1= "FileList";

	@ComponentOutput(description="Java File Object Out", name="fileObjectOut")
	final static String DATA_OUTPUT_1= "fileObjectOut";

	@ComponentProperty(defaultValue="/path/to/workingDir",
			description="The Working Directory of the Executeable",
			name="Working Directory")
			final static String DATA_PROPERTY_WORKINGDIR = "Working Directory";
	private String workingDir = "/path/to/workingDir";

	@ComponentProperty(defaultValue="$m -anOption $i $o",
			description="Command format string. $m is the binary/script name. $i represents the " +
			"input file. $o represents the output file. Any number of command line options" +
			"can also be specified. Example: if from the command line you run: myProgramName " +
			"-t 0.1 -fl 1024 -i <inputfile> -o <outputfile>, the proper format string would be: " +
			"$m -t 0.1 -fl 1024 -i $i -o $o.",
			name="Command Format String")
			final static String DATA_PROPERTY_FORMATSTRING = "Command Format String";
	private String commandFormattingStr = "$m -anOption $i $o";

	@ComponentProperty(defaultValue="myExecutableName",
			description="The name of the executable, e.g. bextract, extractFeatures, runtempo, etc.",
			name="Executeable Name")
			final static String DATA_PROPERTY_EXECNAME = "Executeable Name";
	private String execName = "myExecutableName";

	@ComponentProperty(defaultValue="outputFileName.txt",
			description="The name of the output file (passed into the command format string to the $o field)." +
			" This option is overriden if AddExtentionToInput is set to TRUE",
			name="Output File Name")
			final static String DATA_PROPERTY_OUPUTFILENAME = "Output File Name";
	private String outputFileName = "outputFileName.txt";

	@ComponentProperty(defaultValue="true",
			description="Generate the output file name by adding the extention specified " +
			"in the 'Output File Extension to Append' field to the input file name. (true/false). E.g. " +
			"if the input file name is input.txt, this value is true, and the 'Output File Extension to Append' is " +
			".result, the output file name will be input.txt.result",
			name="Add Extension to Input File Name to Generate Output File Name")
			final static String DATA_PROPERTY_ADDEXTENSION = "Add Extension to Input File Name to Generate Output File Name";
	private boolean addExtension = true;

	@ComponentProperty(defaultValue=".result",
			description="The output file extension to add to the input file name to " +
			"generate the output file name. Only used if 'Add Extension to Input File Name to Generate Output File Name' " +
			"is set to true.",
			name="Output File Name Extension to Append")
			final static String DATA_PROPERTY_EXTENSION = "Output File Name Extension to Append";
	private String extension= ".result";

	private String outfile;
	private String processWorkingDir;
	private String processResultsDir;

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
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance()
					.getProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance()
			.getResultLocationForJob(ccp.getFlowExecutionInstanceID());
		} catch (IOException e1) {
			try {
				throw new ComponentExecutionException(e1);
			} catch (ComponentExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

      
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
		String[][] fileLists = (String[][])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[][] outLists = new String[fileLists.length][2];
		cout.println("Starting Execution of External Binaries");
		cout.flush();
		File[] names = new File[fileLists.length];
		for (int i = 0; i < fileLists.length; i++) {

			File inFile = new File(fileLists[i][0]);

			try {
				runCommand(inFile.getCanonicalPath(),cout);
				outLists[i][0] = outfile;
				outLists[i][1] = fileLists[i][1];
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cout.println();
		cout.println("Execution of External Binaries Complete");
		cout.flush();
		cc.pushDataComponentToOutput(DATA_OUTPUT_1, outLists);
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void dispose ( ComponentContextProperties ccp ) {
		cout.close();
	}

	private void runCommand(final String inputFilename, java.io.PrintStream cout) throws IOException, RuntimeException {
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
				cout.println("External Command NOT FOUND!");
				cout.flush();
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

		cout.print("Running command: ");
		for (int i=0;i<cmdArray.length;i++) {
			cout.print(cmdArray[i] + " ");
		}
		cout.println("");
		cout.println("in directory: " + dir.getCanonicalPath());
		cout.flush();
		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		// Map<String, String> env = pb.environment();
		// env.put("VAR1", "myValue");
		// env.remove("OTHERVAR");
		// env.put("VAR2", env.get("VAR1") + "suffix");
		pb.directory(dir);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
          cout.println("\t" + line);
        }
        int exitStatus;
		try {
			exitStatus = process.waitFor();
			cout.println("Exit status: " + exitStatus);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
