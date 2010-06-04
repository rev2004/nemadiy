///*
// * @(#) NonWebUIFragmentCallback.java @VERSION@
// * 
// * Copyright (c) 2008+ Amit Kumar
// * 
// * The software is released under ASL 2.0, Please
// * read License.txt
// *
// */
//
//package org.imirsel.nema.components.evaluation;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import org.imirsel.nema.components.NemaComponent;
//import org.imirsel.nema.model.NemaDataset;
//import org.imirsel.nema.model.NemaTask;
//import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
//import org.imirsel.nema.analytics.evaluation.util.resultpages.*;
//import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
//import org.imirsel.nema.analytics.util.io.IOUtil;
//import org.imirsel.nema.analytics.util.process.ProcessOutputReceiver;
//import org.imirsel.nema.annotations.StringDataType;
//import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
//import org.meandre.annotations.Component;
//import org.meandre.annotations.ComponentInput;
//import org.meandre.annotations.ComponentProperty;
//import org.meandre.core.ComponentContext;
//import org.meandre.core.ComponentContextException;
//import org.meandre.core.ComponentContextProperties;
//import org.meandre.core.ComponentExecutionException;
//
///** This executable component executes an external binary using the process builder.
// *
// * @author Andreas F. Ehmann;
// *
// */
//@Component(creator="Andreas F. Ehmann", description="Evaluates Structural Segmentors", 
//		name="StructuralSegmentationEvaluatorComponent",
//		tags="evaluation segmentation",
//		dependency={"commons-compress-1.0.jar","jfreechart-1.0.9.jar","jcommon-1.0.12.jar"}
//		)
//		public class StructuralSegmentationEvaluatorComponent extends NemaComponent {
//
//
//	//@ComponentInput(description="Java File Object In", name="fileObjectIn")
//	//final static String DATA_INPUT_1= "fileObjectIn";
//
//	@ComponentInput(description="Algorithm Result File List, String[]", name="FileList1")
//	final static String DATA_INPUT_1= "FileList1";
//
//	@ComponentInput(description="Ground-truth Result File List, String[]", name="FileList2")
//	final static String DATA_INPUT_2= "FileList2";
//
//	@StringDataType(hide=true)
//	@ComponentProperty(defaultValue="/path/to/workingDir",
//			description="The Working Directory of the Evaluation Script",
//			name="Executable Working Directory")
//	final static String DATA_PROPERTY_EXEWORKINGDIR = "Executable Working Directory";
//	
//	@StringDataType()
//	@ComponentProperty(defaultValue="evalstruct.sh",
//			description="The name of the evalscript, called as: evalscript gtfile algofile pngoutfile csvoutfile",
//			name="Executeable Name")
//	final static String DATA_PROPERTY_EXECNAME = "Executeable Name";
//			
//	@StringDataType()
//	@ComponentProperty(defaultValue="Structural segmentation",
//			description="The name of the evaluation to be used on the result pages output.",
//			name="Task Name")
//	final static String DATA_TASK_NAME = "Task Name";
//			
//	@StringDataType()
//	@ComponentProperty(defaultValue="Automated segmentation of music audio according to structure",
//			description="Task Description",
//			name="Task Description")
//	final static String DATA_TASK_DESC = "Task Description";
//			
//	@StringDataType()
//	@ComponentProperty(defaultValue="",
//			description="The name of the dataset being evaluated.",
//			name="Dataset Name")
//	final static String DATA_DATASET_NAME = "Dataset Name";
//			
//	@StringDataType()
//	@ComponentProperty(defaultValue="",
//			description="Description of the dataset used.",
//			name="Dataset Description")
//	final static String DATA_DATASET_DESC = "Dataset Description";
//	
//	
//	//private String processWorkingDir;
//	//private String processResultsDir;
//	private boolean isAborted = false;
//	Process process;
//	ProcessOutputReceiver procReceiverThread = null;
//
//	/** This method is invoked when the Meandre Flow is being prepared for 
//	 * getting run.
//	 *
//	 * @param ccp The properties associated to a component context
//	 * @throws ComponentContextException 
//	 * @throws ComponentExecutionException 
//	 */
//	public void initialize ( ComponentContextProperties ccp ) throws ComponentExecutionException, ComponentContextException {
//		super.initialize(ccp);
//	}
//
//	/** This method just pushes a concatenated version of the entry to the
//	 * output.
//	 *
//	 * @throws ComponentExecutionException If a fatal condition arises during
//	 *         the execution of a component, a ComponentExecutionException
//	 *         should be thrown to signal termination of execution required.
//	 * @throws ComponentContextException A violation of the component context
//	 *         access was detected
//
//	 */
//	public void execute(ComponentContext cc) throws ComponentExecutionException, ComponentContextException{
//		//File inFile = (File)cc.getDataComponentFromInput(DATA_INPUT_1);
//		String execName = "evalstruct.sh";
//		//String processWorkingDir =null;
//		String processResultsDir =null;
//		
//		String taskName;
//		String taskDesc;
//		String datasetName;
//		String datasetDesc;
//		
//		
//		taskName = cc.getProperty(DATA_TASK_NAME);
//		taskDesc = cc.getProperty(DATA_TASK_DESC);
//		datasetName = cc.getProperty(DATA_DATASET_NAME);
//		datasetDesc = cc.getProperty(DATA_DATASET_DESC);
//				
//		
//		
//		String workingDir = String.valueOf(cc.getProperty(DATA_PROPERTY_EXEWORKINGDIR));
//		execName = String.valueOf(cc.getProperty(DATA_PROPERTY_EXECNAME));
//
//		String[] fileLists1 = (String[])cc.getDataComponentFromInput(DATA_INPUT_1);
//		String[] fileLists2 = (String[])cc.getDataComponentFromInput(DATA_INPUT_2);
//		String[] outLists = new String[fileLists1.length];
//		try {
//			//processWorkingDir = ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).
//			//getAbsoluteProcessWorkingDirectory(cc.getFlowExecutionInstanceID());
//			processResultsDir = ArtifactManagerImpl.getInstance(cc.getPublicResourcesDirectory()).
//			getResultLocationForJob(cc.getFlowExecutionInstanceID());
//		} catch (IOException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		
//		// sanity check that both filelists are same length
//		if (fileLists1.length != fileLists2.length) {
//			throw new ComponentExecutionException("ERROR: File lists for input1 and input2 are different lengths!");
//		}
//		getLogger().info("\n" +
//				"=============================================================\n" +
//				"Starting evaluation of structural segmentation\n" +
//				"=============================================================\n" +
//				"Number of files to process: " + fileLists1.length + "\n");
//		
//		File[] csvFiles = new File[fileLists1.length];
//		File[] pngFiles = new File[fileLists1.length];
//		String[] pageNames = new String[fileLists1.length];
//		File resultsDirT = new File(processResultsDir);
//		String processResultsDirName = processResultsDir;
//		try {
//			processResultsDirName = resultsDirT.getCanonicalPath();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	 	File resultsDir = new File(processResultsDirName);
//		for (int i = 0; i < fileLists1.length; i++) {
//			getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
//					"FILE:  " + (i+1) +"/" + fileLists1.length + "\n");
//
//			if(isAborted) {
//				getLogger().info("Execution of external binaries aborted");
//				break;
//			}
//
//			File inFile1 = new File(fileLists1[i]);
//			File inFile2 = new File(fileLists2[i]);
//			CommandResult result = null;
//				try {
//				result=runCommand(inFile2.getCanonicalPath(), inFile1.getCanonicalPath(), execName,workingDir,processResultsDir,resultsDir);
//			} catch (IOException e) {
//				getLogger().log(Level.SEVERE,"Exception occured while running eval script!",e);
//				throw new ComponentExecutionException(e);
//			} catch (Exception e) {
//				getLogger().log(Level.SEVERE,"Exception occured while running eval script!",e);
//				throw new ComponentExecutionException(e);
//			}
//			
//			csvFiles[i] = new File(result.getCsvFile());
//			pngFiles[i] = new File(result.getPngFile());
//			pageNames[i] = inFile1.getName().split(".wav")[0];
//			
//		}
//		
//		try {
//			NemaTask task = new NemaTask(-1, taskName, taskDesc, -1, "Structure", -1);
//	        NemaDataset dataset = new NemaDataset(-1,datasetName,datasetDesc,-1,-1,-1,null,null,-1,"Structure",-1,null);
//	        
//			writeResultsHTML(task, dataset, pageNames, csvFiles, pngFiles, resultsDir);
//		} catch (Exception e) {
//			getLogger().log(Level.SEVERE,"Exception occured while writing results pages!",e);
//		}
//		 
//		getLogger().info("=============================================================\n" +
//				"Evaluation of structural segmentation complete\n" +
//				"=============================================================");
//				
//	}
//
//	private static void writeResultsHTML(NemaTask task, NemaDataset dataset, String[] pageNames, File[] CSVFiles, File[] imagePaths, File outputDirectory) throws IOException{
//        //create result pages
//        System.err.println("Creating result HTML files...");
//
//        if ((pageNames.length != CSVFiles.length)||(CSVFiles.length != imagePaths.length)){
//            throw new IllegalArgumentException("The arrays of page names, CSV files and imagePaths must be the same length!\n" +
//                    "pageNames length  = " + pageNames.length + "\n" +
//                    "CSVFiles length   = " + CSVFiles.length + "\n" +
//                    "imagePaths length = " + imagePaths.length);
//        }
//        List<Page> resultPages = new ArrayList<Page>();
//        List<PageItem> items;
//        Page aPage;
//
//        //do intro page
//        items = new ArrayList<PageItem>();
//        Table descriptionTable = WriteCsvResultFiles.prepTaskTable(task,dataset);
//        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
//        aPage = new Page("intro", "Introduction", items, false);
//        resultPages.add(aPage);
//        
//        //do a page per file
//        String[][][] csvData = new String[pageNames.length][][];
//        for (int i = 0; i < pageNames.length; i++){
//            items = new ArrayList<PageItem>();
//            String cleanName = pageNames[i].replaceAll("\\s", "_");
//
//            //add table from CSV files
//            csvData[i] = DeliminatedTextFileUtilities.loadDelimTextData(CSVFiles[i], ",", -1);
//            ArrayList<String[]> rows = new ArrayList<String[]>(csvData[i].length-1);
//            for (int j = 1; j < csvData[i].length; j++){
//                rows.add(csvData[i][j]);
//            }
//            items.add(new TableItem("evalMetrics", "Evaluation Metrics", csvData[i][0], rows));
//
//            //add evaluation plot
//            items.add(new ImageItem("plot", "Plot", IOUtil.makeRelative(imagePaths[i], outputDirectory)));
//
//            //add the page
//            aPage = new Page(cleanName, pageNames[i], items, true);
//            resultPages.add(aPage);
//        }
//
//        //do mean results page
//        if(csvData.length > 1){
//            items = new ArrayList<PageItem>();
//            
//            try{
//                //average tables
//                double[] averages = new double[csvData[0].length-1];
//                for (int i = 0; i < csvData.length; i++){
//                    for (int j = 0; j < averages.length; j++){
//                        averages[j] += Double.parseDouble(csvData[i][j+1][1]);
//                    }
//                }
//                for (int i = 0; i < averages.length; i++){
//                    averages[i] /= csvData.length;
//                }
//                
//                ArrayList<String[]> rows = new ArrayList<String[]>(averages.length);
//                
//                for (int r = 0; r < averages.length; r++){
//                    rows.add(new String[]{csvData[0][r+1][0],""+averages[r]});
//                }
//                
//                items.add(new TableItem("meanEvalMetrics", "Mean Evaluation Metrics", csvData[0][0], rows));
//
//                //add the page
//                aPage = new Page("mean_scores", "Mean scores", items, false);
//                resultPages.add(aPage);
//            }catch(Exception e){
//                Logger.getLogger(WriteResultPagePerFile.class.getName()).log(Level.WARNING, "Was unable to produce mean scores from second column of CCSV tables!",e);
//            }
//            
//            
//        }
//
//
//        //do files page
//        {
//            items = new ArrayList<PageItem>();
//
//            //CSVs
//            List<String> CSVPaths = new ArrayList<String>(CSVFiles.length);
//            for (int i = 0; i < CSVFiles.length; i++){
//                 CSVPaths.add(IOUtil.makeRelative(CSVFiles[i],outputDirectory));
//            }
//            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));
//
//            //Friedman's tables and plots
//            List<String> images = new ArrayList<String>(imagePaths.length);
//            for (int i = 0; i < imagePaths.length; i++){
//                 images.add(IOUtil.makeRelative(imagePaths[i],outputDirectory));
//            }
//            items.add(new FileListItem("plots", "Evaluation plots", images));
//
//            aPage = new Page("files", "All data files", items, true);
//            resultPages.add(aPage);
//        }
//
//        Page.writeResultPages(task.getName(), outputDirectory, resultPages);
//    }
//
//	/** This method is called when the Menadre Flow execution is completed.
//	 *
//	 * @param ccp The properties associated to a component context
//	 * @throws ComponentContextException 
//	 */
//	public void dispose ( ComponentContextProperties ccp ) throws ComponentContextException {
//		super.dispose(ccp);
//		if(process != null) {
//            process.destroy();
//        }
//		if(procReceiverThread != null){
//			procReceiverThread.kill();
//		}
//	}
//
//	private CommandResult runCommand(final String inputFilename2, final String inputFilename1, final String execName, 
//			final String workingDir, final String processResultsDir, final File resultsDir) throws RuntimeException, IOException {
//		String outfilePNG;
//		String outfileCSV;
//		CommandResult commandResult = new CommandResult();
//		
//		// Create File to represent working directory
//		File dir = new File(workingDir);	
//		File resdir = new File(processResultsDir);
//		outfilePNG = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.png";            
//		outfileCSV = resdir.getCanonicalPath() + File.separator + (new File(inputFilename1)).getName() + ".eval.csv"; 
//		
//		commandResult.setCsvFile(outfileCSV);
//		commandResult.setPngFile(outfilePNG);
//
//		File command = new File(execName);
//		if (!command.exists()) {
//			File command2 = new File(dir.getCanonicalPath() + File.separator + execName);
//			if (!command2.exists()) {
//				throw new RuntimeException("External Integration module was unable to locate your command!\n" +
//						"File names tried:\n\t" + command.getCanonicalPath() + "\n\t" + command2.getCanonicalPath() + "\n" +
//						"Please ensure that your binaries are in the working directory set in the ExternalInteration " +
//				"modules's properties panel.");
//			} else {
//				command = command2;
//			}
//		}
//
//		String[] cmdArray = new String[5];
//		cmdArray[0] = command.getCanonicalPath();
//		cmdArray[1] = inputFilename2;
//		cmdArray[2] = inputFilename1;
//		cmdArray[3] = outfilePNG;
//		cmdArray[4] = outfileCSV;
//		
//		String msg = "";
//		msg += "Running command:";
//		for (int i=0;i<cmdArray.length;i++) {
//			msg += cmdArray[i] + " ";
//		}
//		msg += "\n\n In directory:" + dir.getCanonicalPath() + "\n" +
//				"Sending results to: " + resdir.getCanonicalPath() + "\n";
//
//		getLogger().info(msg);
//		ProcessBuilder pb = new ProcessBuilder(cmdArray);
//		Map<String, String> env = pb.environment();
//		pb.directory(dir);
//		pb.redirectErrorStream(true);
//
//		msg = "*******************************************\n" +
//				"EXTERNAL PROCESS STDOUT AND STDERR:\n";
//		getLogger().info(msg);
//		
//		
//		InputStream is = null;
//		try{
//			process = pb.start();
//			is = process.getInputStream();
//			getLogger().info("*******************************************\n" +
//					"EXTERNAL PROCESS STDOUT AND STDERR:");
//			procReceiverThread = new ProcessOutputReceiver( is, getLogger() );
//			procReceiverThread.start();
//	        int exitStatus;
//	        
//			try {
//				exitStatus = process.waitFor();
//				getLogger().info("EXTERNAL PROCESS EXIT STATUS: " + exitStatus + "\n" +
//						"*******************************************");
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}finally{
//			if(procReceiverThread != null){
//				procReceiverThread.kill();
//			}
//			if(process != null){
//				process.getErrorStream().close();
//			}
//			if(is != null){
//				is.close();
//			}
//		}
//		
//		return commandResult;
//	}
//}
