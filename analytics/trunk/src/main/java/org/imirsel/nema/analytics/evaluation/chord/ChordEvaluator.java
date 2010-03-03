/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.chord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.analytics.evaluation.util.resultpages.*;
import org.imirsel.nema.analytics.evaluation.util.*;
import org.imirsel.nema.analytics.util.io.*;
import org.imirsel.nema.model.*;

/**
 *
 * @author kris.west@gmail.com
 */
public class ChordEvaluator extends EvaluatorImpl{

//	/** Command line harness usage statement. */
//    public static final String USAGE = "args: taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription subjectMetadata /path/to/GT/file /path/to/output/dir [-h /path/to/hierarchy/file] /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    
    protected boolean performMatlabStatSigTests = true;
	protected File matlabPath = new File("matlab");
    
    private static final String BIG_DIVIDER =    "================================================================================\n";
    private static final String SMALL_DIVIDER = "--------------------------------------------------------------------------------\n";
    private static final int COL_WIDTH = 7;
    private static final DecimalFormat dec = new DecimalFormat("0.00");
    private static final String PLOT_EXT = ".chords.png";

    
    /**
     * Constructs and instance of the ClassificationEvaluator. 
     * 
     * 
     * @param task_ A description of the task being evaluated. The task must at least contain 
     * the metadata class to be predicted (N.B. the default class is 'genre'). The description 
     * will be used on the HTML evaluation report and textual evaluation reports output.
     * @param outputDir_ The directory to output results into.
     * @param workingDir_ The working directory to use for any temp files.
     * @param performMatlabStatSigTests_ A flag that determines whether the significance tests
     * are performed (N.B. this is ignored if there is only one result to evaluate).
     * @param matlabPath_ The path to the matlab executable or command. To be used to perform 
     * the significance tests.
     * @param hierarchyFile_ If non-null the specified genre hierarchy will be used to discount 
     * confusions and produce an extra evaluation metric that takes into account near misses.
     * @throws FileNotFoundException Thrown if a non-null hierarchy file is passed, but cannot be 
     * found.
     * @throws IOException Thrown if there is a problem reading the hierarchy file.
     */
    public ChordEvaluator(
    		NemaTask task_,
            NemaDataset dataset_,
            File outputDir_,
            File workingDir_,
            boolean performMatlabStatSigTests_,
            File matlabPath_,
            File hierarchyFile_) 
    		throws FileNotFoundException, IOException{
        super(workingDir_, outputDir_, task_, dataset_);
        performMatlabStatSigTests = performMatlabStatSigTests_;
        matlabPath = matlabPath_;
    }
    
//    /** Parse commandline arguments for the main method harness.
//     * 
//     * @param args Full commandline arguments received by the JVM.
//     * @return An instantiated ClassificationEvaluator, based on the arguments, that is ready to run.
//     * @throws IllegalArgumentException Thrown if a results or ground-truth file is not in the expected format.
//     * @throws FileNotFoundException Thrown if a non-null hierarchy file is passed, but cannot be 
//     * found.
//     * @throws IOException Thrown if there is a problem reading a results or ground-truth file, unrelated to 
//     * format.
//     */
//    public static ChordEvaluator parseCommandLineArgs(String[] args) throws IllegalArgumentException, FileNotFoundException, IOException{
//        if (args.length < 10 ){
//            System.err.println("ERROR: Insufficient arguments!\n" + USAGE);
//            System.exit(1);
//        }
//        
//        //args: taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription subjectMetadataID subjectMetadataName /path/to/GT/file /path/to/output/dir [-h /path/to/hierarchy/file] /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName
//        NemaTask task = new NemaTask();
//        task.setId(Integer.parseInt(args[0]));
//        task.setName(args[1]);
//        task.setDescription(args[2]);
//        task.setDatasetId(Integer.parseInt(args[3]));
//        task.setSubjectTrackMetadataId(Integer.parseInt(args[6]));
//        task.setSubjectTrackMetadataName(args[7]);
//        
//        NemaDataset dataset = new NemaDataset();
//        dataset.setId(task.getDatasetId());
//        dataset.setName(args[4]);
//        dataset.setDescription(args[5]);
//        
//        File gtFile = new File(args[8]);
//        File workingDir = new File(args[9]);
//        File hierarchyFile = null;
//        String msg = "\n" + 
//        		"Task description:  \n" + task.toString() + 
//    			"Ground-truth file: " + gtFile.getAbsolutePath() + "\n" + 
//    			"Working directory: " + workingDir.getAbsolutePath() + "\n" + 
//    			"OutputDirectory:   " + workingDir.getAbsolutePath() + "\n";
//
//        if (args.length % 2 != 1){
//            System.err.println("WARNING: an even number of arguments was specified, one may have been ignored!\n" + USAGE);
//        }
//        
//        int startIdx = -1;
//        if (args[9].equalsIgnoreCase("-h")){
//            hierarchyFile = new File(args[10]);
//            msg += "Hierarchy file:    " + hierarchyFile.getAbsolutePath() + "\n";
//            startIdx = 11;
//        }else{startIdx = 9;
//            msg += "Hierarchy file:    null\n";
//        }
//        
//        ChordEvaluator eval = new ChordEvaluator(task, dataset, workingDir, workingDir, true, new File("matlab"), hierarchyFile);
//        eval.getLogger().info(msg);
//        
//        //reading ground-truth data
//        ChordIntervalTextFile reader = new ChordIntervalTextFile(task.getSubjectTrackMetadataName());
//        List<NemaData> gt = reader.readFile(gtFile);
//        eval.setGroundTruth(gt);
//        
//        msg = "Results to evaluate:\n";
//        for (int i = startIdx; i < args.length; i+=2) {
//            String systemName = args[i+1];
//            File resultsPath = new File(args[i]);
//            
//            List<NemaData> results = reader.readDirectory(resultsPath,null);
//            for(Iterator<List<NemaData>> it = results.iterator();it.hasNext();){
//            	eval.addResults(systemName, systemName, it.next());
//            }
//            msg += systemName + ": " + resultsPath.getAbsolutePath() + ", read " + results.size() + " result files\n";
//        }
//        eval.getLogger().info(msg);
//        
//        return eval;
//    }
//    
//    /**
//     * Main method harness.
//     * 
//     * @param args Command line arguments that will be parsed by {@link #parseCommandLineArgs(String[] args)}.
//     */
//    public static void main(String[] args) {
//        
//        System.err.println("MIREX Classification evaluator\n" +
//                "\t\tby Kris West (kris.west@gmail.com");
//        System.err.println("");
//        
//        ChordEvaluator eval = null;
//		try {
//			eval = parseCommandLineArgs(args);
//			try{
//				eval.evaluate();
//			}catch(Exception e){
//				eval.getLogger().log(Level.SEVERE, "Exception occured while executing evaluate!",e);
//			}
//		} catch (Exception e) {
//			if (eval != null){
//				eval.getLogger().log(Level.SEVERE, "Exception occured while parsing command line arguments!",e);
//			}else{
//				Logger.getLogger(ChordEvaluator.class.getName()).log(Level.SEVERE, "Exception occured while parsing command line arguments!",e);
//			}
//		}
//        
//        System.err.println("---exit---");
//    }
    
    /**
     * Perform the evaluation and block until the results are fully written to the output directory.
     * Also return a map encoding the evaluation results for each job in case they are needed for further processing.
     * 
     * @return a map encoding the evaluation results for each job and other data. 
     * @throws IllegalArgumentException Thrown if required metadata is not found, either in the ground-truth
     * data or in one of the system's results.
     */
    public Map<String,NemaData> evaluate() throws IllegalArgumentException, IOException{
    	
        boolean performStatSigTests = true;
        int numJobs = jobIDToFoldResults.size();
        if(numJobs < 2){
            performStatSigTests = false;
        }
        
        String jobID;
		List<List<NemaData>> sysResults;
		
		//check num folds and tracks per fold
		int numFolds = checkFolds();

        //evaluate each fold for each system
        Map<String,List<NemaData>> jobIDTofoldEvaluations = new HashMap<String,List<NemaData>>(numJobs); 
        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	getLogger().info("Evaluating experiment folds for jobID: " + jobID);
        	sysResults = jobIDToFoldResults.get(jobID);
        	List<NemaData> foldResultList = new ArrayList<NemaData>(numFolds);
        	for(Iterator<List<NemaData>> it2 = sysResults.iterator();it2.hasNext();){
        		foldResultList.add(evaluateResultFold(jobID, it2.next()));
        	}
        	jobIDTofoldEvaluations.put(jobID, foldResultList);
        }
        

		// Make per system result dirs
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		Map<String, List<File>> jobIDToFoldResultDirs = new HashMap<String, List<File>>();
		
		for (Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			// make a sub-dir for the systems results
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobID);
			sysDir.mkdirs();
			
			//make a sub-dir for each fold
			List<File> foldDirs = new ArrayList<File>(numFolds);
			for (int i = 0; i < numFolds; i++) {
				File foldDir = new File(sysDir.getAbsolutePath() + File.separator + "fold_" + i);
				foldDir.mkdirs();
				foldDirs.add(foldDir);
			}
			
			jobIDToResultDir.put(jobID, sysDir);
			jobIDToFoldResultDirs.put(jobID, foldDirs);
		}

		
        //plot chords for each track in each fold
		Map<String, List<File[]>> jobIDToResultPlotFileList = new HashMap<String, List<File[]>>();
        //iterate over systems
        for(Iterator<String> it_systems = jobIDToFoldResults.keySet().iterator(); it_systems.hasNext();){
        	jobID = it_systems.next();
        	getLogger().info("Plotting Chord transcriptions for: " + jobID);
        	sysResults = jobIDToFoldResults.get(jobID);
        	
        	//iterate over folds
        	List<File> foldDirs = jobIDToFoldResultDirs.get(jobID);
        	List<File[]> plotFolds = new ArrayList<File[]>();
        	Iterator<File> it_foldResDir = foldDirs.iterator();
        	
        	for (Iterator<List<NemaData>> it_folds = sysResults.iterator(); it_folds.hasNext();) {
				List<NemaData> list = it_folds.next();
				File[] plots = new File[list.size()];
				File foldDir = it_foldResDir.next();
				
				//iterate over tracks
				int plotCount = 0;
				for (Iterator<NemaData> it_tracks = list.iterator(); it_tracks.hasNext();) {
					NemaData nemaData = it_tracks.next();
					
					File plotFile = new File(foldDir.getAbsolutePath() + File.separator + nemaData.getId() + PLOT_EXT);
					plots[plotCount++] = plotFile;
					
					//TODO: actually plot the chords
					
					
				}
				plotFolds.add(plots);
			}
        	jobIDToResultPlotFileList.put(jobID, plotFolds);
        }
//        
//        
//        
//        
//        //aggregate results to produce overall evaluation
//        _logger.info("Producing aggregate evaluations over all folds");
//        Map<String,NemaData> jobIDToAggregateEvaluations = new HashMap<String,NemaData>(numJobs); 
//        Map<String,File> jobIDToOverallConfFile = new HashMap<String,File>(numJobs);
//        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it.hasNext();){
//        	jobID = it.next();
//        	evalList = jobIDTofoldEvaluations.get(jobID);
//        	NemaData aggregateEval = new NemaData(jobID);
//        	int[][][] confFolds = new int[numFolds][][];
//        	for(int f=0;f<numFolds;f++){
//        		confFolds[f] = evalList.get(f).get2dIntArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW);
//        	}
//        	int[][] confusionRaw = new int[numClasses][numClasses];
//        	double[][] confusionPercent = new double[numClasses][numClasses];
//        	
//        	int[] sums = new int[numClasses];
//    		for(int i=0;i<numClasses;i++){
//    			sums[i] = 0;
//    			for(int j=0;j<numClasses;j++){
//    				for(int f=0;f<numFolds;f++){
//        				confusionRaw[j][i] += confFolds[f][j][i];
//    				}
//    				sums[i] += confusionRaw[j][i];
//    			}
//    			if(sums[i] > 0){
//	    			for(int j=0;j<numClasses;j++){
//	    				confusionPercent[j][i] = (double)confusionRaw[j][i] / sums[i];
//	    			}
//    			}
//    		}
//        	
//    		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW, confusionRaw);
//    		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT, confusionPercent);
//        	
//    		//plot aggregate confusion
//    		File overallConfFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + "overall" + CONF_MAT_PLOT_EXTENSION);
//    		jobIDToOverallConfFile.put(jobID, overallConfFile);
//    		ConfusionMatrixPlot plot = new ConfusionMatrixPlot(getTask().getName() + " - " + jobIDToName.get(jobID) + " - overall", (String[])classNames.toArray(new String[numClasses]), confusionPercent);
//            plot.writeChartToFile(overallConfFile, CONF_MAT_WIDTH, CONF_MAT_HEIGHT);
//    		
//    		//Calculate final accuracy as diagonal sum of confusion matrix divided by total number of examples
//	        double finalAccuracy = 0.0;
//	        double finalDiscountedAccuracy = 0.0;
//	        int finalSum = 0;
//	        for (int i=0;i<numClasses; i++) {
//	            finalSum += sums[i];
//	        	finalAccuracy += confusionRaw[i][i];
//	        }
//	        finalAccuracy /= (double)finalSum;
//	        
//	        aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY, finalAccuracy);
//	        
//        	//Calculate Normalised accuracy as mean of percentage confusion matrix diagonal
//	        double finalNormalisedAccuracy = 0.0;
//	        for (int i=0;i<numClasses; i++) {
//	            finalNormalisedAccuracy += confusionPercent[i][i];
//	        }
//	        finalNormalisedAccuracy /= (double)numClasses;
//	        aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY, finalNormalisedAccuracy);
//	        
//	        //repeat for discounted stuff
//        	if(usingAHierarchy){
//        		double[][] discountFoldAccs = new double[numFolds][];
//        		for(int f=0;f<numFolds;f++){
//        			discountFoldAccs[f] = evalList.get(f).getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW);
//        		}
//	        	double[] discountConfusionRaw = new double[numClasses];
//	        	double[] discountConfusionPercent = new double[numClasses];
//	        	for(int i=0;i<numClasses;i++){
//	        		for(int f=0;f<numFolds;f++){
//	        			discountConfusionRaw[i] += discountFoldAccs[f][i];
//	        		}
//	        		if(sums[i] > 0){
//	        			discountConfusionPercent[i] = discountConfusionRaw[i] / sums[i];
//	        		}
//	        	}
//	        	
//	        	aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW, discountConfusionRaw);
//	    		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, discountConfusionPercent);
//	    		
//	    		for (int i=0;i<numClasses; i++) {
//	                finalDiscountedAccuracy += discountConfusionRaw[i];
//	            }
//	            finalDiscountedAccuracy /= finalSum;
//	            aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY, finalDiscountedAccuracy);
//	            
//	            //Calculate Normalised accuracy as mean of percentage discounted confusion matrix diagonal
//	            double finalNormalisedDiscountedAccuracy = 0.0;
//	            for (int i=0;i<numClasses; i++) {
//	                finalNormalisedDiscountedAccuracy += discountConfusionPercent[i];
//	            }
//	            finalNormalisedDiscountedAccuracy /= (double)numClasses;
//	            aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY, finalNormalisedDiscountedAccuracy);    
//        	}
//        	
//        	jobIDToAggregateEvaluations.put(jobID, aggregateEval);
//        }
//        
//        //write out CSV results files
//        _logger.info("Writing out CSV result files over whole task...");
//        String msg = "Job ID to name IDs: ";
//        for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
//        	msg += it.next();
//        	if (it.hasNext()){
//        		msg += ", ";
//        	}
//        }
//        msg += "\n";
//        msg += "Job ID to fold evaluation IDs: ";
//        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator();it.hasNext();){
//        	msg += it.next();
//        	if (it.hasNext()){
//        		msg += ", ";
//        	}
//        }
//        msg += "\n";
//        msg += "Job ID to aggregate evaluation IDs: ";
//        for(Iterator<String> it = jobIDToAggregateEvaluations.keySet().iterator();it.hasNext();){
//        	msg += it.next();
//        	if (it.hasNext()){
//        		msg += ", ";
//        	}
//        }
//        msg += "\n";
//        _logger.fine(msg);
//        
//        File perClassCSV = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassResults.csv");
//        WriteChordResultFiles.prepFriedmanTestDataCSVOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT,perClassCSV);
//        
//        File perFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "PerFoldResults.csv");
//        WriteChordResultFiles.prepFriedmanTestDataCSVOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICAT)ION_ACCURACY,perFoldCSV);
//        
//        //write out results summary CSV
//        File summaryCSV = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
//        WriteChordResultFiles.prepSummaryResultDataCSV(jobIDToAggregateEvaluations,jobIDToName,classNames,summaryCSV,usingAHierarchy);
//        
//        //write out discounted results summary CSVs
//        File discountedPerClassCSV = null;
//        File discountedPerFoldCSV = null;
//        if (hierarchyFile != null){
//            discountedPerClassCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerClassResults.csv");
//            WriteChordResultFiles.prepFriedmanTestDataCSVOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, discountedPerClassCSV);
//            discountedPerFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerFoldResults.csv");
//            WriteChordResultFiles.prepFriedmanTestDataCSVOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY,discountedPerFoldCSV);
//        }
//        
//        //perform statistical tests
//        File friedmanClassTablePNG = null;
//        File friedmanClassTable = null;
//        File friedmanFoldTablePNG = null;
//        File friedmanFoldTable = null;
//        File friedmanDiscountClassTablePNG = null;
//        File friedmanDiscountClassTable = null;
//        File friedmanDiscountFoldTablePNG = null;
//        File friedmanDiscountFoldTable = null;
//        if (getPerformMatlabStatSigTests() && performStatSigTests){
//            _logger.info("Performing Friedman's tests in Matlab...");
////            String[] systemNamesArr = jobIDToName.values().toArray(new String[jobIDToName.size()]);
//            
////            File[] tmp = performFriedmanTestWithClassAccuracy(outputDir, perClassCSV, systemNamesArr);
//            File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perClassCSV, 0, 1, 1, numJobs, getMatlabPath());
//            friedmanClassTablePNG = tmp[0];
//            friedmanClassTable = tmp[1];
//
//            //tmp = performFriedmanTestWithFoldAccuracy(outputDir, perFoldCSV, systemNamesArr);
//            tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
//            friedmanFoldTablePNG = tmp[0];
//            friedmanFoldTable = tmp[1];
//
//            if (hierarchyFile != null){
//                tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerClassCSV, 0, 1, 1, numJobs, getMatlabPath());
//                friedmanDiscountClassTablePNG = tmp[0];
//                friedmanDiscountClassTable = tmp[1];
//                
//                tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
//                friedmanDiscountFoldTablePNG = tmp[0];
//                friedmanDiscountFoldTable = tmp[1];
//            }
//        }
//        
//        
//        //write text reports
//        _logger.info("Writing text evaluation reports...");
//        Map<String,File> jobIDToReportFile = new HashMap<String,File>(numJobs);
//        for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
//        	jobID = it.next();
//        	File reportFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + "report.txt");
//        	writeSystemTextReport(jobIDToAggregateEvaluations.get(jobID), jobIDTofoldEvaluations.get(jobID), classNames, jobID, jobIDToName.get(jobID), usingAHierarchy, reportFile);
//        	jobIDToReportFile.put(jobID, reportFile);
//        }
//        
//        
//        //create tarballs of individual result dirs
//        _logger.info("Preparing evaluation data tarballs...");
//        Map<String,File> jobIDToTgz = new HashMap<String,File>(jobIDToName.size());
//        for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
//        	jobID = it.next();
//        	jobIDToTgz.put(jobID, IOUtil.tarAndGzip(new File(outputDir.getAbsolutePath() + File.separator + jobID)));
//        }
//        
//        
//        //write result HTML pages
//        _logger.info("Creating result HTML files...");
//
//        List<Page> resultPages = new ArrayList<Page>();
//        List<PageItem> items;
//        Page aPage;
//
//        //do intro page to describe task
//        {
//        	items = new ArrayList<PageItem>();
//	        Table descriptionTable = WriteChordResultFiles.prepTaskTable(task,dataset);
//	        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
//	        aPage = new Page("intro", "Introduction", items, false);
//	        resultPages.add(aPage);
//        }
//        
//        //do summary page
//        {
//	        items = new ArrayList<PageItem>();
//	        Table summaryTable = WriteChordResultFiles.prepSummaryTable(jobIDToAggregateEvaluations,jobIDToName,classNames,usingAHierarchy);
//	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
//	        aPage = new Page("summary", "Summary", items, false);
//	        resultPages.add(aPage);
//        }
//
//        //do per class page
//        {
//            items = new ArrayList<PageItem>();
//            Table perClassTable = WriteChordResultFiles.prepTableDataOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
//            items.add(new TableItem("acc_class", "Accuracy per Class", perClassTable.getColHeaders(), perClassTable.getRows()));
//            if (hierarchyFile != null){
//                Table perDiscClassTable = WriteChordResultFiles.prepTableDataOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT);
//                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Class", perDiscClassTable.getColHeaders(), perDiscClassTable.getRows()));
//            }
//            aPage = new Page("acc_per_class", "Accuracy per Class", items, false);
//            resultPages.add(aPage);
//        }
//
//        //do per fold page
//        {
//            items = new ArrayList<PageItem>();
//            Table perFoldTable = WriteChordResultFiles.prepTableDataOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_ACCURACY);
//            items.add(new TableItem("acc_class", "Accuracy per Fold", perFoldTable.getColHeaders(), perFoldTable.getRows()));
//            if (hierarchyFile != null){
//                Table perDiscFoldTable = WriteChordResultFiles.prepTableDataOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
//                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Fold", perDiscFoldTable.getColHeaders(), perDiscFoldTable.getRows()));
//            }
//            aPage = new Page("acc_per_fold", "Accuracy per Fold", items, false);
//            resultPages.add(aPage);
//        }
//        
//        //do significance tests
//        if (getPerformMatlabStatSigTests() && performStatSigTests){
//            items = new ArrayList<PageItem>();
//            items.add(new ImageItem("friedmanClassTablePNG", "Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanClassTablePNG, outputDir)));
//            items.add(new ImageItem("friedmanFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanFoldTablePNG, outputDir)));
//            if(friedmanDiscountClassTable != null){
//                items.add(new ImageItem("friedmanDiscountClassTablePNG", "Discounted Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountClassTablePNG, outputDir)));
//            }
//            if(friedmanDiscountFoldTable != null){
//                items.add(new ImageItem("friedmanDiscountFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountFoldTablePNG, outputDir)));
//            }
//            aPage = new Page("sig_tests", "Significance Tests", items, true);
//            resultPages.add(aPage);
//        }
//
//        //do confusion matrices
//        List<String> sortedJobIDs = new ArrayList<String>(jobIDToName.keySet());
//        Collections.sort(sortedJobIDs);
//        {
//            items = new ArrayList<PageItem>();
//            
//            for (int i = 0; i < numJobs; i++){
//                items.add(new ImageItem("confusion_" + i, sortedJobIDs.get(i), IOUtil.makeRelative(jobIDToOverallConfFile.get(sortedJobIDs.get(i)), outputDir)));
//            }
//            aPage = new Page("confusion", "Confusion Matrices", items, true);
//            resultPages.add(aPage);
//        }
//
//        //do files page
//        {
//            items = new ArrayList<PageItem>();
//
//            //CSVs
//            List<String> CSVPaths = new ArrayList<String>(4);
//            CSVPaths.add(IOUtil.makeRelative(perClassCSV,outputDir));
//            CSVPaths.add(IOUtil.makeRelative(perFoldCSV,outputDir));
//            if (hierarchyFile != null){
//                CSVPaths.add(IOUtil.makeRelative(discountedPerClassCSV,outputDir));
//                CSVPaths.add(IOUtil.makeRelative(discountedPerFoldCSV,outputDir));
//            }
//            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));
//
//            //Friedman's tables and plots
//            if (getPerformMatlabStatSigTests() && performStatSigTests){
//                //Friedmans tables
//                List<String> sigCSVPaths = new ArrayList<String>(4);
//                sigCSVPaths.add(IOUtil.makeRelative(friedmanClassTable, outputDir));
//                sigCSVPaths.add(IOUtil.makeRelative(friedmanFoldTable, outputDir));
//                if(friedmanDiscountClassTable != null){
//                    sigCSVPaths.add(IOUtil.makeRelative(friedmanDiscountClassTable, outputDir));
//                }
//                if(friedmanDiscountFoldTable != null){
//                    sigCSVPaths.add(IOUtil.makeRelative(friedmanDiscountFoldTable, outputDir));
//                }
//                items.add(new FileListItem("sigCSVs", "Significance test CSVs", sigCSVPaths));
//
//                //Friedmans plots
//                List<String> sigPNGPaths = new ArrayList<String>(4);
//                sigPNGPaths.add(IOUtil.makeRelative(friedmanClassTablePNG, outputDir));
//                sigPNGPaths.add(IOUtil.makeRelative(friedmanFoldTablePNG, outputDir));
//                if(friedmanDiscountClassTable != null){
//                    sigPNGPaths.add(IOUtil.makeRelative(friedmanDiscountClassTablePNG, outputDir));
//                }
//                if(friedmanDiscountFoldTable != null){
//                    sigPNGPaths.add(IOUtil.makeRelative(friedmanDiscountFoldTablePNG, outputDir));
//                }
//                items.add(new FileListItem("sigPNGs", "Significance test plots", sigPNGPaths));
//            }
//
//            //System Tarballs
//            List<String> tarballPaths = new ArrayList<String>(numJobs);
//            for (int i = 0; i < numJobs; i++){
//                tarballPaths.add(IOUtil.makeRelative(jobIDToTgz.get(sortedJobIDs.get(i)),outputDir));
//            }
//            items.add(new FileListItem("tarballs", "Per algorithm evaluation tarball", tarballPaths));
//            aPage = new Page("files", "Raw data files", items, true);
//            resultPages.add(aPage);
//        }
//
//        Page.writeResultPages(task.getName(), outputDir, resultPages);
//        
//        return jobIDToAggregateEvaluations;
    }

	private int checkFolds() {
		int numFolds = -1;
        String jobID;
        
        List<List<NemaData>> sysResults;
        List<Integer> tracksPerFold = null;
        //check that all systems have the same number of folds
        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	sysResults = jobIDToFoldResults.get(jobID);
            if (numFolds == -1) {
                numFolds = sysResults.size();
                tracksPerFold = new ArrayList<Integer>(numFolds);
                for (Iterator<List<NemaData>> it2 = sysResults.iterator(); it2.hasNext();) {
					tracksPerFold.add(it2.next().size());
				}
            } else if (numFolds != sysResults.size()) {
                throw new IllegalArgumentException("The number of folds (" + sysResults.size() + ") detected for system ID: " + jobID + 
                		", name: " + jobIDToName.get(jobID) + " is not equal to the number detected " + 
                		"for the preceeding systems (" + numFolds + ")!");
            } else{
            	Iterator<Integer> it3 = tracksPerFold.iterator();
            	int foldCount = 0;
            	for (Iterator<List<NemaData>> it2 = sysResults.iterator(); it2.hasNext();) {
            		int num = it2.next().size();
            		int expected = it3.next().intValue();
					if(num != expected){
						throw new IllegalArgumentException("The number of tracks (" + num + ") for fold " + foldCount + " detected for system ID: " + jobID + 
		                		", name: " + jobIDToName.get(jobID) + " is not equal to the number detected " + 
		                		"for the preceeding systems (" + expected + ")!");
					}
					foldCount++;
				}
            }
        }
		return numFolds;
	}
    
    /**
     * Evaluates a single iteration/fold of the experiment and returns an Object representing the 
     * evaluation results.
     * 
     * @param jobID The jobID by which the results will be referred to.
     * @param theData The list of data Objects each representing a prediction about a track to be
     * evaluated.
     * @return an Object representing the evaluation results.
     */
    private NemaData evaluateResultFold(String jobID, List<NemaData> theData) {
        int numExamples = theData.size();
        
        NemaData outObj = new NemaData(jobID);
        
        NemaData data;
        NemaData gtData;
        
        List<NemaChord> systemChords;
        List<NemaChord> gtChords;
        
        double overlapAccum = 0.0;
        double weightedAverageOverlapAccum = 0.0;
        double lengthAccum = 0.0;
        //iterate through tracks
        for(int x=0; x<numExamples; x++) {
            //Do simple evaluation
        	data = theData.get(x);
        	gtData = trackIDToGT.get(data.getId());

        	systemChords = (List<NemaChord>)data.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
        	gtChords = (List<NemaChord>)gtData.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
        	
        	
        	//evaluate here
        	int res = 1000; //The grid resolution. 
        	//Create grid for the ground-truth
        	int lnGT = (int)(res*gtChords.get(gtChords.size()-1).getOffset()) ;
        	if (lnGT == 0 ){
        		throw new IllegalArgumentException("Length of GT is 0!");
        	}        		
        	int[][] gridGT = new int[lnGT][];
        	for (int i = 0; i < gtChords.size(); i++) {
        		NemaChord currentChord = gtChords.get(i);
        		int onset_index = (int)(currentChord.getOnset()*res);
        		int offset_index = (int)(currentChord.getOffset()*res);
        		for (int j= onset_index; j<offset_index; j++){
        			gridGT[j]=currentChord.getNotes();
        		}
			}
        
        	// Create grid for the system
        	int lnSys = (int)(res*systemChords.get(systemChords.size()-1).getOffset());
        	if (lnSys == 0 ){
        		throw new IllegalArgumentException("Length of DT is 0!");
        	}
        	int[][] gridSys = new int[lnSys][];        	
        	for (int i = 0; i < systemChords.size(); i++) {
        		NemaChord currentChord = systemChords.get(i);
        		int onset_index = (int)(currentChord.getOnset()*res);
        		int offset_index = (int)(currentChord.getOffset()*res);
        		for (int j= onset_index; j<offset_index; j++){
        			gridSys[j]=currentChord.getNotes();
        		}
			}
        	
        	int[] overlaps = new int[lnGT]; 
        	int  overlap_total =0;
        	//Calculate the overlap score 
        	for (int i = 0; i <lnGT; i++ ){
        		int[] gtFrame = gridGT[i]; 
        		int[] sysFrame = gridSys[i];
        		overlap_total = overlap_total +  calcOverlap(gtFrame,sysFrame);        		
        	}
        	
        	//set eval metrics on input obj for track
        	double overlap_score = overlap_total / lnGT;
        	
        	weightedAverageOverlapAccum += overlap_score*lnGT;
        	lengthAccum +=lnGT; 
        	overlapAccum += overlap_score;
        	
        	data.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, overlap_score);
        }
        
        //produce avg chord ratio
        double avg = overlapAccum / numExamples;
        double weightedAverageOverlap = weightedAverageOverlapAccum / lengthAccum;
        
        //produce weighted average chord ratio
        
        
        //set eval metrics on eval object for fold
        outObj.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, avg);
        outObj.setMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO, weightedAverageOverlap);
        
        return outObj;
    }
    
    /**
     * Writes a textual evaluation report on the results of one system to an UTF-8 text file. Includes 
     * the confusion matrices, accuracy, discounted accuracy and normalised versions of each for each 
     * iteration of the experiment and overall.
     * 
     * @param aggregateEval An Object representing the combined evaluation of all iterations.
     * @param foldEvals A list of objects representing the evaluation of each fold/iteration of the 
     * experiment.
     * @param classNames An ordered list of the classnames used in the experiment.
     * @param jobID The jobID of the system being evaluated.
     * @param jobName The name of the job being evaluated.
     * @param usingAHierarchy Flag indicating whether the evaluation used a hierarchy to discount confusions
     * (meaning we need to retrieve and report on the extra discounted results).
     * @param outputFile The File to write the report to.
     * @throws IOException Thrown if there is a problem writing to the report file.
     * @throws FileNotFoundException Thrown if the report file cannot be created.
     */
    public void writeSystemTextReport(NemaData aggregateEval, List<NemaData> foldEvals, List<String> classNames, String jobID, String jobName, boolean usingAHierarchy, File outputFile) throws IOException, FileNotFoundException{
        
    	//Write output for each fold
    	String bufferString = BIG_DIVIDER + "Classification Evaluation Report\n";
    	bufferString += "Job ID:                  " + jobID + "\n";
    	bufferString += "Job Name:                " + jobName + "\n";
    	bufferString += "Number of iterations:    " + foldEvals.size() + "\n";
    	bufferString += "Task ID:                 " + task.getId() + "\n";
    	bufferString += "Task Name:               " + task.getName() + "\n";
    	bufferString += "Task Description:        " + task.getDescription() + "\n";
    	bufferString += "Metadata predicted id:   " + task.getSubjectTrackMetadataId() + "\n";
    	bufferString += "Metadata predicted name: " + task.getSubjectTrackMetadataName() + "\n";
    	bufferString += "Dataset ID:              " + dataset.getId() + "\n";
    	bufferString += "Dataset Name:            " + dataset.getName() + "\n";
    	bufferString += "Dataset Description:     " + dataset.getDescription() + "\n\n";
    	bufferString += SMALL_DIVIDER;
    	
	    for(int i=0;i<foldEvals.size();i++){
	    	NemaData foldData = foldEvals.get(i);
	    	
	    	bufferString += "Iteration " + i + "\n";
		    bufferString += "Accuracy: " + dec.format(foldData.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY) * 100) + "%\n";
		    bufferString += "Accuracy (normalised for class sizes): " + dec.format(foldData.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY) * 100) + "%\n";
	    	
		    if(usingAHierarchy) {
		        bufferString += "Hierachically Discounted Accuracy: " + dec.format(foldData.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100) + "%\n";
		        bufferString += "Hierachically Discounted Accuracy (normalised for class sizes): " + dec.format(foldData.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100) + "%\n";
		    }
		    
		    bufferString += "Raw Confusion Matrix:\n";
		    bufferString += writeIntConfusionMatrix(foldData.get2dIntArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW), classNames);
		    bufferString += "\nConfusion Matrix percentage:\n";
		    bufferString += writePercentageConfusionMatrix(foldData.get2dDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT), classNames);
		    bufferString += writeMatrixKey(classNames);
		    
		    if (usingAHierarchy)
		    {
		        bufferString += "\nHierachically Discounted Confusion Vector:\n";
		        bufferString += writeDoubleConfusionVector(foldData.getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW), classNames);
		        bufferString += "\nHierachically Discounted Confusion Matrix percentage:\n";
		        bufferString += writePercentageConfusionVector(foldData.getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT), classNames);
		    }
		    if(i<foldEvals.size()-1){
		    	bufferString += SMALL_DIVIDER;
		    }
	    }
	    
	    bufferString += "\n" + BIG_DIVIDER;
	    bufferString += "Overall Evaluation\n";
	    bufferString += "Accuracy: " + dec.format(aggregateEval.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY) * 100) + "%\n";
	    bufferString += "Accuracy (normalised for class sizes): " + dec.format(aggregateEval.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY) * 100) + "%\n";
    	
	    if(usingAHierarchy) {
	        bufferString += "Hierachically Discounted Accuracy: " + dec.format(aggregateEval.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100) + "%\n";
	        bufferString += "Hierachically Discounted Accuracy (normalised for class sizes): " + dec.format(aggregateEval.getDoubleMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100) + "%\n";
	    }
	    
	    bufferString += "Raw Confusion Matrix:\n";
	    bufferString += writeIntConfusionMatrix(aggregateEval.get2dIntArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW), classNames);
	    bufferString += "\nConfusion Matrix percentage:\n";
	    bufferString += writePercentageConfusionMatrix(aggregateEval.get2dDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT), classNames);
	    bufferString += writeMatrixKey(this.classNames);
	        
	    if (usingAHierarchy)
	    {
	        bufferString += "\nHierachically Discounted Confusion Vector:\n";
	        bufferString += writeDoubleConfusionVector(aggregateEval.getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW), classNames);
	        bufferString += "\nHierachically Discounted Confusion Matrix percentage:\n";
	        bufferString += writePercentageConfusionVector(aggregateEval.getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT), classNames);
	    }
	    
	    bufferString += BIG_DIVIDER;
	    
	    IOUtil.writeStringToFile(outputFile, bufferString, "UTF-8");
	    
    }

    /**
     * Writes an integer confusion matrix to a file.
     * @param matrix The matrix to be written.
     * @param classNames The class names.
     * @return
     */
    public String writeIntConfusionMatrix(int[][] matrix, List<String> classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        bufferString += "\nClassification\n";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t\t";
            for(int y=0; y<classNames.size(); y++) {
                bufferString += fmtInt(matrix[x][y]) + "\t";
            }
            bufferString += "\n";
        }
        return bufferString;
    }
    
    /**
     * Writes a double confusion matrix to a file.
     * @param vector The matrix to be written.
     * @param classNames The class names.
     * @return
     */
    public String writeDoubleConfusionVector(double[] vector, List<String> classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        
        for(int x=0; x<classNames.size(); x++) {
            bufferString += fmtDec(vector[x]) + "\t";
        }
        bufferString += "\n";
        return bufferString;
    }
    
    /**
     * Writes a double confusion matrix to a file.
     * @param vector The matrix to be written.
     * @param classNames The class names.
     * @return
     */
    public String writePercentageConfusionVector(double[] vector, List<String> classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        
        for(int x=0; x<classNames.size(); x++) {
            bufferString += fmtPercent(vector[x] * 100.0) + "\t";
        }
        bufferString += "\n";
        return bufferString;
    }
    
    /**
     * Writes a double confusion matrix to a file.
     * @param matrix The matrix to be written.
     * @param classNames The class names.
     * @return 
     */
    public String writePercentageConfusionMatrix(double[][] matrix, List<String> classNames) {
        String bufferString = "Truth\t\t";
        for(int x=0; x<classNames.size(); x++) {
            bufferString += getKey(x) + "\t";
        }
        bufferString += "\nClassification\n";
        for(int x=0; x<classNames.size(); x++) {
            //bufferString += (String)classNames.get(x) + "\t\t";
            bufferString += getKey(x) + "\t\t";
            for(int y=0; y<classNames.size(); y++) {
                bufferString += fmtPercent(matrix[x][y] * 100.0) + "\t";
            }
            bufferString += "\n";
        }
        return bufferString;
    }


    /** 
     * Outputs the confusion matrix key
     * @param classNames
     * @return the key
     */
    public String writeMatrixKey(List<String> classNames) {
        StringBuffer sb = new StringBuffer();
        sb.append("Matrix Key:\n");
        for(int x=0; x<classNames.size(); x++) {
            sb.append("   ");
            sb.append(getKey(x));
            sb.append(": "); 
            sb.append(classNames.get(x));
            sb.append("\n");
        }
        return sb.toString();
    }
    
        /** 
     *  Returns a two character key for a classname based upon
     * its index
     * @param keyIndex  the class name index
     * @return  a two character key
     */
    private String getKey(int keyIndex) {
        StringBuffer label = new StringBuffer(); 
        if (keyIndex >= 26) {
            label.append((char) ('A' + (keyIndex / 26) - 1));
            keyIndex = keyIndex % 26;
        } else {
            label.append(' ');
        }
        label.append((char) ('A' + keyIndex));
        return pad(label.toString() + "  ", COL_WIDTH);
    }
 

    /** 
     * Format a decimal number for column output
     * @param val the value to format
     * @return the formatted value
     */
    private String fmtDec(double val) {
        return pad(dec.format(val), COL_WIDTH);
    }

    /** 
     * Format an int for column output
     * @param val the value to format
     * @return the formatted output
     */
    private String fmtInt(int val) {
        return pad(Integer.toString(val), COL_WIDTH);
    }

    /** 
     * Format a percentage value for output
     * @param val the value to format
     * @return the formatted value
     */
    private String fmtPercent(double val) {
        return pad(dec.format(val) + "%", COL_WIDTH);
    }

    /** 
     *  Pad the given string to the given length
     * @param v  the string to pad
     * @param padLength  the length to pad
     * @return the padded string
     */
    private String pad(String v, int padLength) {
        String paddedString = "                                                    " + v;
        return paddedString.substring(
            paddedString.length() - padLength);
    }

    private int calcOverlap(int[] gt, int[] sys) {
    	
    	if (gt.length!=sys.length){
    		return 0;
    	}
    	else {
    		for (int i = 0; i < gt.length; i++) {
				if(!(gt[i]==sys[i])){
					return 0;
				}
			}
    		return 1;
    	}

    	
    }    
    
    
    public boolean getPerformMatlabStatSigTests() {
        return performMatlabStatSigTests;
    }

    public void setPerformMatlabStatSigTests(boolean performMatlabStatSigTests) {
        this.performMatlabStatSigTests = performMatlabStatSigTests;
    }

	public void setHierarchyFile(File hierarchyFile) {
		this.hierarchyFile = hierarchyFile;
	}

	public File getHierarchyFile() {
		return hierarchyFile;
	}

	public File getMatlabPath() {
        return matlabPath;
    }

    public void setMatlabPath(File matlabPath) {
        this.matlabPath = matlabPath;
    }
}
