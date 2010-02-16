/*
 * @(#) NonWebUIFragmentCallback.java @VERSION@
 * 
 * Copyright (c) 2008+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */

package org.imirsel.nema.components.evaluation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import org.imirsel.m2k.evaluation.TaskDescription;
import org.imirsel.m2k.evaluation.resultpages.WriteResultPagePerFile;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.annotations.StringDataType;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.components.util.ProcessOutputReceiver;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
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
@Component(creator="Andreas F. Ehmann", description="Evaluates Structural Segmentors", 
		name="StructuralSegmentationEvaluatorComponent",
		tags="evaluation segmentation",
		dependency={"commons-compress-1.0.jar","jfreechart-1.0.9.jar","jcommon-1.0.12.jar"}
		)
		public class StructuralSegmentationEvaluatorComponent extends NemaComponent {


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
	final static String DATA_PROPERTY_WORKINGDIR = "ExeWorking Directory";
	
	@StringDataType()
	@ComponentProperty(defaultValue="evalstruct.sh",
			description="The name of the evalscript, called as: evalscript gtfile algofile pngoutfile csvoutfile",
			name="Executeable Name")
	final static String DATA_PROPERTY_EXECNAME = "Executeable Name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="Structural segmentation",
			description="The name of the evaluation to be used on the result pages output.",
			name="Task Name")
	final static String DATA_TASK_NAME = "Task Name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="Automated segmentation of music audio according to structure",
			description="Task Description",
			name="Task Description")
	final static String DATA_TASK_DESC = "Task Description";
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="The name of the dataset being evaluated.",
			name="Dataset Name")
	final static String DATA_DATASET_NAME = "Dataset Name";
			
	@StringDataType()
	@ComponentProperty(defaultValue="",
			description="Description of the dataset used.",
			name="Dataset Description")
	final static String DATA_DATASET_DESC = "Dataset Description";
	
	
	//private String processWorkingDir;
	//private String processResultsDir;
	private boolean isAborted = false;
	Process process;
	ProcessOutputReceiver procReceiverThread = null;

	/** This method is invoked when the Meandre Flow is being prepared for 
	 * getting run.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 * @throws ComponentExecutionException 
	 */
	public void initialize ( ComponentContextProperties ccp ) throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
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
		String execName = "evalstruct.sh";
		//String processWorkingDir =null;
		String processResultsDir =null;
		
		String taskName;
		String taskDesc;
		String datasetName;
		String datasetDesc;
		
		
		taskName = cc.getProperty(DATA_TASK_NAME);
		taskDesc = cc.getProperty(DATA_TASK_DESC);
		datasetName = cc.getProperty(DATA_DATASET_NAME);
		datasetDesc = cc.getProperty(DATA_DATASET_DESC);
				
		
		
		String workingDir = String.valueOf(cc.getProperty(DATA_PROPERTY_WORKINGDIR));
		execName = String.valueOf(cc.getProperty(DATA_PROPERTY_EXECNAME));

		String[] fileLists1 = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
		String[] fileLists2 = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
		String[] outLists = new String[fileLists1.length];
		try {
			//processWorkingDir = ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).
			//getAbsoluteProcessWorkingDirectory(cc.getFlowExecutionInstanceID());
			processResultsDir = ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).
			getResultLocationForJob(cc.getFlowExecutionInstanceID());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// sanity check that both filelists are same length
		if (fileLists1.length != fileLists2.length) {
			throw new ComponentExecutionException("ERROR: File lists for input1 and input2 are different lengths!");
		}
		_logger.info("\n" +
				"=============================================================\n" +
				"Starting evaluation of structural segmentation\n" +
				"=============================================================\n" +
				"Number of files to process: " + fileLists1.length + "\n");
		
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
			_logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
					"FILE:  " + (i+1) +"/" + fileLists1.length + "\n");

			if(isAborted) {
				_logger.info("Execution of external binaries aborted");
				break;
			}

			File inFile1 = new File(fileLists1[i]);
			File inFile2 = new File(fileLists2[i]);
			CommandResult result = null;
				try {
				result=runCommand(inFile2.getCanonicalPath(), inFile1.getCanonicalPath(), execName,workingDir,processResultsDir,resultsDir);
			} catch (IOException e) {
				_logger.log(Level.SEVERE,"Exception occured while running eval script!",e);
				throw new ComponentExecutionException(e);
			} catch (Exception e) {
				_logger.log(Level.SEVERE,"Exception occured while running eval script!",e);
				throw new ComponentExecutionException(e);
			}
			
			csvFiles[i] = new File(result.getCsvFile());
			pngFiles[i] = new File(result.getPngFile());
			pageNames[i] = inFile1.getName().split(".wav")[0];
			
		}
		
		try {
			TaskDescription task = new TaskDescription(-1, taskName, taskDesc, "Structure", -1, datasetName, datasetDesc);
			WriteResultPagePerFile.writeResultsHTML(task, pageNames, csvFiles, pngFiles, resultsDir);
		} catch (Exception e) {
			_logger.log(Level.SEVERE,"Exception occured while writing results pages!",e);
		}
		 
		_logger.info("=============================================================\n" +
				"Evaluation of structural segmentation complete\n" +
				"=============================================================");
				
	}


	/** This method is called when the Menadre Flow execution is completed.
	 *
	 * @param ccp The properties associated to a component context
	 * @throws ComponentContextException 
	 */
	public void dispose ( ComponentContextProperties ccp ) throws ComponentContextException {
		super.dispose(ccp);
		if(process != null) {
            process.destroy();
        }
		if(procReceiverThread != null){
			procReceiverThread.kill();
		}
	}

	private CommandResult runCommand(final String inputFilename2, final String inputFilename1, final String execName, 
			final String workingDir, final String processResultsDir, final File resultsDir) throws RuntimeException, IOException {
		String outfilePNG;
		String outfileCSV;
		CommandResult commandResult = new CommandResult();
		
		// Create File to represent working directory
		File dir = new File(workingDir);	
		File resdir = new File(processResultsDir);
		outfilePNG = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.png";            
		outfileCSV = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.csv"; 
		
		commandResult.setCsvFile(outfileCSV);
		commandResult.setPngFile(outfilePNG);

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
		
		String msg = "";
		msg += "Running command:";
		for (int i=0;i<cmdArray.length;i++) {
			msg += cmdArray[i] + " ";
		}
		msg += "\n\n In directory:" + dir.getCanonicalPath() + "\n" +
				"Sending results to: " + resdir.getCanonicalPath() + "\n";

		_logger.info(msg);
		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		Map<String, String> env = pb.environment();
		pb.directory(dir);
		pb.redirectErrorStream(true);

		msg = "*******************************************\n" +
				"EXTERNAL PROCESS STDOUT AND STDERR:\n";
		_logger.info(msg);
		
		
		InputStream is = null;
		try{
			process = pb.start();
			is = process.getInputStream();
			_logger.info("*******************************************\n" +
					"EXTERNAL PROCESS STDOUT AND STDERR:");
			procReceiverThread = new ProcessOutputReceiver( is, _logger );
			procReceiverThread.start();
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
			if(procReceiverThread != null){
				procReceiverThread.kill();
			}
			if(process != null){
				process.getErrorStream().close();
			}
			if(is != null){
				is.close();
			}
		}
		
		return commandResult;
	}
}
