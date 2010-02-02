/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.evaluation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.imirsel.m2k.evaluation2.resultPages.WriteResultPagePerFile;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.util.ProcessOutputReceiver;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;

/** This executable component executes an external binary using the process builder.
 *
 * @author Andreas F. Ehmann;
 *
 */
@Component(creator="Andreas F. Ehmann", description="Evaluates Structural Segmentors", 
		name="StructuralSegmentationEvaluator",
		tags="test ft please hello")
		public class StructuralSegmentationEvaluator implements ExecutableComponent {


	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
	//final static String DATA_INPUT_1= "fileObjectIn";

	@ComponentInput(description="Algorithm Result File List, String[]", name="FileList1")
	final static String DATA_INPUT_1= "FileList1";

	@ComponentInput(description="Ground-truth Result File List, String[]", name="FileList2")
	final static String DATA_INPUT_2= "FileList2";

	@StringDataType(hide=true)
	@ComponentProperty(defaultValue="/path/to/workingDir",
			description="The Working Directory of the Evaluation Script",
			name="Working Directory")
			final static String DATA_PROPERTY_WORKINGDIR = "Working Directory";
	private String workingDir = "/path/to/workingDir";

	@StringDataType()
	@ComponentProperty(defaultValue="evalstruct.sh",
			description="The name of the evalscript, called as: evalscript gtfile algofile pngoutfile csvoutfile",
			name="Executeable Name")
			final static String DATA_PROPERTY_EXECNAME = "Executeable Name";
	private String execName = "evalstruct.sh";

	private String outfilePNG;
	private String outfileCSV;
	private String processWorkingDir;
	private String processResultsDir;
	private boolean isAborted = false;
	Process process;

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
		if(process != null) {
			process.destroy();
		}
		isAborted = false;
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance().
			getAbsoluteProcessWorkingDirectory(ccp.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance().
			getResultLocationForJob(ccp.getFlowExecutionInstanceID());
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
	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException{
		//File inFile = (File)cc.getDataComponentFromInput(DATA_INPUT_1);
		workingDir = String.valueOf(cc.getProperty(DATA_PROPERTY_WORKINGDIR));
		execName = String.valueOf(cc.getProperty(DATA_PROPERTY_EXECNAME));

		String[] fileLists1 = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] fileLists2 = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] outLists = new String[fileLists1.length];


		// sanity check that both filelists are same length
		if (fileLists1.length != fileLists2.length) {
			cout.println("ERROR: File lists for input1 and input2 are different lengths!");
			return;
		}
		cout.println("");
		cout.println("=============================================================");
		cout.println("Starting evaluation of structural segmentation");
		cout.println("=============================================================");
		cout.println("Number of files to process: " + fileLists1.length);
		cout.flush();
		
		File[] csvFiles = new File[fileLists1.length];
		File[] pngFiles = new File[fileLists1.length];
		String[] pageNames = new String[fileLists1.length];
		File resultsDirT = new File(processResultsDir);
		String processResultsDirName = processResultsDir;
		try {
			processResultsDirName = resultsDirT.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File resultsDir = new File(processResultsDirName);
		for (int i = 0; i < fileLists1.length; i++) {
			cout.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			cout.println("FILE:  " + (i+1) +"/" + fileLists1.length);
			cout.flush();
			if(isAborted) {
				cout.println("");
				cout.println("Execution of external binaries aborted");
				cout.flush();
				break;
			}

			File inFile1 = new File(fileLists1[i]);
			File inFile2 = new File(fileLists2[i]);

			try {
				runCommand(inFile2.getCanonicalPath(), inFile1.getCanonicalPath(),cout);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				cout.println("IOException");
				cout.flush();
				e.printStackTrace();
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				cout.println("RuntimeException");
				cout.flush();
				e.printStackTrace();
			}
			
			csvFiles[i] = new File(outfileCSV);
			pngFiles[i] = new File(outfilePNG);
			pageNames[i] = inFile1.getName().split(".wav")[0];
		}
		try {
			WriteResultPagePerFile.writeResultsHTML("Structural Segmentation Evaluation", pageNames, csvFiles, pngFiles, resultsDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cout.println("=============================================================");
		cout.println("Evaluation of structural segmentation complete");
		cout.println("=============================================================");
		cout.flush();
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 */
	public void dispose ( ComponentContextProperties ccp ) {
		cout.close();
		if(process != null) {
			process.destroy();
		}
		isAborted = true;

	}

	private void runCommand(final String inputFilename2, final String inputFilename1, java.io.PrintStream cout) throws RuntimeException, IOException {


		// Create File to represent working directory
		File dir = new File(workingDir);	
		File resdir = new File(processResultsDir);
		outfilePNG = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.png";            
		outfileCSV = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.csv";    

		File command = new File(execName);
		if (!command.exists()) {
			File command2 = new File(dir.getCanonicalPath() + File.separator + execName);
			if (!command2.exists()) {
				throw new RuntimeException("External Integration module was unable to locate your command!\n" +
						"File names tried:\n\t" + command.getCanonicalPath() + "\n\t" + command2.getCanonicalPath() + "\n" +
						"Please ensure that your binaries are in the working directory set in the ExternalInteration " +
				"modules's properties panel.");
			} else {
				command = command2;
			}
		}

		String[] cmdArray = new String[5];
		cmdArray[0] = command.getCanonicalPath();
		cmdArray[1] = inputFilename2;
		cmdArray[2] = inputFilename1;
		cmdArray[3] = outfilePNG;
		cmdArray[4] = outfileCSV;
		

		cout.println("");
		cout.println("Running command:");
		for (int i=0;i<cmdArray.length;i++) {
			cout.print(cmdArray[i] + " ");
		}
		cout.println("");
		cout.println("");
		cout.println("In directory:");
		cout.println(dir.getCanonicalPath());
		cout.println("");
		cout.println("Sending results to:");
		cout.println(resdir.getCanonicalPath());
		cout.println("");
		cout.flush();
		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		Map<String, String> env = pb.environment();
		pb.directory(dir);
		pb.redirectErrorStream(true);
		process = pb.start();
		InputStream is = process.getInputStream();
		cout.println("*******************************************");
		cout.println("EXTERNAL PROCESS STDOUT AND STDERR:");
		cout.println("");
		cout.flush();
		new Thread( new ProcessOutputReceiver( is, cout ) ).start();
		/*
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
          cout.println("\t" + line);
        }
		 */
		int exitStatus;
		try {
			exitStatus = process.waitFor();
			cout.println("");
			cout.println("EXTERNAL PROCESS EXIT STATUS: " + exitStatus);
			cout.println("*******************************************");
			cout.flush();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.getErrorStream().close();
		is.close();

	}
}
