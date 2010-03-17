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
import org.imirsel.nema.analytics.evaluation.melody.WriteMelodyResultFiles;
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
            File matlabPath_) 
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
        
        String jobId;
		List<List<NemaData>> sysResults;
		
		//check num folds and tracks per fold
		int numFolds = checkFolds();

        //evaluate each fold for each system
        Map<String,List<NemaData>> jobIDTofoldEvaluations = new HashMap<String,List<NemaData>>(numJobs); 
        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
        	jobId = it.next();
        	getLogger().info("Evaluating experiment folds for jobID: " + jobId);
        	sysResults = jobIDToFoldResults.get(jobId);
        	List<NemaData> foldResultList = new ArrayList<NemaData>(numFolds);
        	for(Iterator<List<NemaData>> it2 = sysResults.iterator();it2.hasNext();){
        		foldResultList.add(evaluateResultFold(jobId, it2.next()));
        	}
        	jobIDTofoldEvaluations.put(jobId, foldResultList);
        }
        

		// Make per system result dirs
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		Map<String, List<File>> jobIDToFoldResultDirs = new HashMap<String, List<File>>();
		
		for (Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it
				.hasNext();) {
			jobId = it.next();
			// make a sub-dir for the systems results
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobId);
			sysDir.mkdirs();
			
			//make a sub-dir for each fold
			List<File> foldDirs = new ArrayList<File>(numFolds);
			for (int i = 0; i < numFolds; i++) {
				File foldDir = new File(sysDir.getAbsolutePath() + File.separator + "fold_" + i);
				foldDir.mkdirs();
				foldDirs.add(foldDir);
			}
			
			jobIDToResultDir.put(jobId, sysDir);
			jobIDToFoldResultDirs.put(jobId, foldDirs);
		}

		
        //plot chords for each track in each fold
		Map<String, List<File[]>> jobIDToResultPlotFileList = new HashMap<String, List<File[]>>();
        //iterate over systems
        for(Iterator<String> it_systems = jobIDToFoldResults.keySet().iterator(); it_systems.hasNext();){
        	jobId = it_systems.next();
        	getLogger().info("Plotting Chord transcriptions for: " + jobId);
        	sysResults = jobIDToFoldResults.get(jobId);
        	
        	//iterate over folds
        	List<File> foldDirs = jobIDToFoldResultDirs.get(jobId);
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
        	jobIDToResultPlotFileList.put(jobId, plotFolds);
        }

        //aggregate results to produce overall evaluation
        Map<String, NemaData> jobIDToAggregateEvaluations = aggregateFoldEvaluations(
				numJobs, jobIDTofoldEvaluations);
    		
    		
        //write out per metric CSV results files
        getLogger().info("Writing out CSV result files over whole task...");
        File overlapCsv = new File(outputDir.getAbsolutePath()+ File.separator + "overlap.csv");
        WriteChordResultFiles.writeTableToCsv(
        		WriteChordResultFiles.prepTableDataOverTracksAndSystems(jobIDToFoldResults,jobIDToName,NemaDataConstants.CHORD_OVERLAP_RATIO),
        		overlapCsv
    		);
        
//        File weightOverlapCsv = new File(outputDir.getAbsolutePath() + File.separator + "weightedOverlap.csv");
//        WriteChordResultFiles.writeTableToCsv(
//        		WriteChordResultFiles.prepTableDataOverTracksAndSystems(jobIDToFoldResults,jobIDToName, NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO),
//        		weightOverlapCsv
//        	);
        
        //write out results summary CSV
        File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
        WriteChordResultFiles.writeTableToCsv(
        		WriteChordResultFiles.prepSummaryTable(jobIDToAggregateEvaluations,jobIDToName),
        		summaryCsv
        	);
        
        //write out per system CSVs
        getLogger().info("Writing out CSV result files for each system...");
        Map<String, File> jobIDToCSV = new HashMap<String, File>(numJobs);
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
			jobId = it.next();
			sysResults = jobIDToFoldResults.get(jobId);
			
			File sysDir = jobIDToResultDir.get(jobId);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator + "results.csv");
			WriteChordResultFiles.writeTableToCsv(
					WriteChordResultFiles.prepTableDataOverTracks(sysResults,new String[]{NemaDataConstants.CHORD_OVERLAP_RATIO, NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO}),
					trackCSV
				);
			jobIDToCSV.put(jobId, trackCSV);
		}
        
        
        
        //perform statistical tests
        File friedmanOverlapTablePNG = null;
        File friedmanOverlapTable = null;
        File friedmanWeightedOverlapTablePNG = null;
        File friedmanWeightedOverlapTable = null;
        
        if (getPerformMatlabStatSigTests() && performStatSigTests){
        	getLogger().info("Performing Friedman's tests in Matlab...");

            File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, overlapCsv,  0, 2, 1, numJobs, getMatlabPath());
            friedmanOverlapTablePNG = tmp[0];
            friedmanOverlapTable = tmp[1];

            //tmp = performFriedmanTestWithFoldAccuracy(outputDir, perFoldCSV, systemNamesArr);
            tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, overlapCsv, 0, 2, 1, numJobs, getMatlabPath());
            friedmanWeightedOverlapTablePNG = tmp[0];
            friedmanWeightedOverlapTable = tmp[1];
        }
        
        //create tarballs of individual result dirs
        getLogger().info("Preparing evaluation data tarballs...");
        Map<String,File> jobIDToTgz = new HashMap<String,File>(jobIDToName.size());
        for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
        	jobId = it.next();
        	jobIDToTgz.put(jobId, IOUtil.tarAndGzip(new File(outputDir.getAbsolutePath() + File.separator + jobId)));
        }

        
        //write result HTML pages
        writeHtmlResultPages(performStatSigTests, numJobs,
				jobIDToAggregateEvaluations, overlapCsv,
				summaryCsv, jobIDToCSV, friedmanOverlapTablePNG,
				friedmanOverlapTable, friedmanWeightedOverlapTablePNG,
				friedmanWeightedOverlapTable, jobIDToTgz);
        
        return jobIDToAggregateEvaluations;
    }

	private Map<String, NemaData> aggregateFoldEvaluations(int numJobs,
			Map<String, List<NemaData>> jobIDTofoldEvaluations) {
		String jobId;
		getLogger().info("Producing aggregate evaluations over all folds");
        Map<String,NemaData> jobIDToAggregateEvaluations = new HashMap<String,NemaData>(numJobs); 
        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it.hasNext();){
        	jobId = it.next();
        	List<NemaData> evalList = jobIDTofoldEvaluations.get(jobId);
        	NemaData aggregateEval = new NemaData(jobId);
        	
        	double avgOverlap = 0.0;
        	double avgWeightedOverlap = 0.0;
        	
        	for (Iterator<NemaData> it2 = evalList.iterator(); it2.hasNext();) {
				NemaData nemaData = (NemaData) it2.next();
				avgOverlap += nemaData.getDoubleMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO);
	            avgWeightedOverlap += nemaData.getDoubleMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
			}
        	avgOverlap /= evalList.size();
        	avgWeightedOverlap /= evalList.size();
        	
        	aggregateEval.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, avgOverlap);
        	aggregateEval.setMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO, avgWeightedOverlap);
        }
		return jobIDToAggregateEvaluations;
	}

	private void writeHtmlResultPages(boolean performStatSigTests, int numJobs,
			Map<String, NemaData> jobIDToAggregateEvaluations, File overlapCsv,
			File summaryCsv,
			Map<String, File> jobIDToCSV, File friedmanOverlapTablePNG,
			File friedmanOverlapTable, File friedmanWeightedOverlapTablePNG,
			File friedmanWeightedOverlapTable, Map<String, File> jobIDToTgz) {
		
		String jobId;
		List<List<NemaData>> sysResults;
		getLogger().info("Creating result HTML files...");

        List<Page> resultPages = new ArrayList<Page>();
        List<PageItem> items;
        Page aPage;

        //do intro page to describe task
        {
        	items = new ArrayList<PageItem>();
	        Table descriptionTable = WriteChordResultFiles.prepTaskTable(task,dataset);
	        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
	        aPage = new Page("intro", "Introduction", items, false);
	        resultPages.add(aPage);
        }
        
        //do summary page
        {
	        items = new ArrayList<PageItem>();
	        Table summaryTable = WriteChordResultFiles.prepSummaryTable(jobIDToAggregateEvaluations,jobIDToName);
	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
	        aPage = new Page("summary", "Summary", items, false);
	        resultPages.add(aPage);
        }

        //do per metric pages
        {
            items = new ArrayList<PageItem>();
            Table overlapTable = WriteChordResultFiles.prepTableDataOverTracksAndSystems(jobIDToFoldResults, jobIDToName, NemaDataConstants.CHORD_OVERLAP_RATIO);
            items.add(new TableItem("chord_overlap", "Chord Overlap", overlapTable.getColHeaders(), overlapTable.getRows()));
            
            Table weightedOverlapTable = WriteChordResultFiles.prepTableDataOverTracksAndSystems(jobIDToFoldResults, jobIDToName, NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
            items.add(new TableItem("chord_weighted overlap", "Chord Weighted Overlap", weightedOverlapTable.getColHeaders(), weightedOverlapTable.getRows()));
            
            
            aPage = new Page("all_system_metrics", "Detailed Evaluation Metrics", items, true);
            resultPages.add(aPage);
        }

        //do per system pages
        {
        	for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
    			jobId = it.next();
    			items = new ArrayList<PageItem>();
    			sysResults = jobIDToFoldResults.get(jobId);
    			
    			Table systemTable = WriteChordResultFiles.prepTableDataOverTracks(sysResults,new String[]{NemaDataConstants.CHORD_OVERLAP_RATIO, NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO});
    			items.add(new TableItem(jobId, jobIDToName.get(jobId), systemTable.getColHeaders(), systemTable.getRows()));
                
    			aPage = new Page(jobId, jobIDToName.get(jobId), items, false);
                resultPages.add(aPage);
        	}            
        }
        
        //do significance tests
        if (getPerformMatlabStatSigTests() && performStatSigTests){
            items = new ArrayList<PageItem>();
            items.add(new ImageItem("friedmanOverlapTablePNG", "Chord Overlap: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanOverlapTablePNG, outputDir)));
            items.add(new ImageItem("friedmanWeightedOverlapTablePNG", "Chord Weighted Overlap: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanWeightedOverlapTablePNG, outputDir)));
            
            aPage = new Page("sig_tests", "Significance Tests", items, true);
            resultPages.add(aPage);
        }

        //do files page
        {
            items = new ArrayList<PageItem>();

          //Overall CSVs
            List<String> overallCsvs = new ArrayList<String>(3);
            
            overallCsvs.add(IOUtil.makeRelative(summaryCsv,outputDir));
            overallCsvs.add(IOUtil.makeRelative(overlapCsv,outputDir));
//            overallCsvs.add(IOUtil.makeRelative(weightOverlapCsv,outputDir));
            
            items.add(new FileListItem("overallCSVs", "Overall CSV result files", overallCsvs));

          //Per system CSVs
            List<String> perSystemCsvs = new ArrayList<String>(numJobs);
            for (Iterator<String> it = jobIDToCSV.keySet().iterator(); it.hasNext();) {
    			jobId = it.next();
    			File file = jobIDToCSV.get(jobId);
    			perSystemCsvs.add(IOUtil.makeRelative(file,outputDir));
            }
            items.add(new FileListItem("perSystemCSVs", "Per-system CSV result files", perSystemCsvs));

            //Friedman's tables and plots
            if (getPerformMatlabStatSigTests() && performStatSigTests){
                //Friedmans tables
                List<String> sigCSVPaths = new ArrayList<String>(2);
                sigCSVPaths.add(IOUtil.makeRelative(friedmanOverlapTable, outputDir));
                sigCSVPaths.add(IOUtil.makeRelative(friedmanWeightedOverlapTable, outputDir));
                
                items.add(new FileListItem("sigCSVs", "Significance test CSVs", sigCSVPaths));

                //Friedmans plots
                List<String> sigPNGPaths = new ArrayList<String>(2);
                sigPNGPaths.add(IOUtil.makeRelative(friedmanOverlapTablePNG, outputDir));
                sigPNGPaths.add(IOUtil.makeRelative(friedmanWeightedOverlapTablePNG, outputDir));
                
                items.add(new FileListItem("sigPNGs", "Significance test plots", sigPNGPaths));
            }

            //System Tarballs
            List<String> tarballPaths = new ArrayList<String>(numJobs);
            for (Iterator<String> it = jobIDToCSV.keySet().iterator(); it.hasNext();) {
    			jobId = it.next();
                tarballPaths.add(IOUtil.makeRelative(jobIDToTgz.get(jobId),outputDir));
            }
            items.add(new FileListItem("tarballs", "Per algorithm evaluation tarball", tarballPaths));
            aPage = new Page("files", "Raw data files", items, true);
            resultPages.add(aPage);
        }

        Page.writeResultPages(task.getName(), outputDir, resultPages);
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
        		throw new IllegalArgumentException("Length of SYS is 0!");
        	}
        	

        	int[][] gridSys = new int[lnSys][];	
//        	System.out.println("ln Sys " + lnSys + " last offset " + systemChords.get(systemChords.size()-1).getOffset());
//        	System.out.println("System chords length " + systemChords.size());
        	for (int i = 0; i < systemChords.size(); i++) {
        		NemaChord currentChord = systemChords.get(i);
        		int onset_index = (int)(currentChord.getOnset()*res);
        		int offset_index = (int)(currentChord.getOffset()*res);
        	//	System.out.println("Chord no " + i + " onset="+onset_index + "offset=" + offset_index);
        		for (int j= onset_index; j<offset_index; j++){
        			gridSys[j]=currentChord.getNotes();
        		}
			}
        	
        	
        	int lnOverlap = Math.min(lnGT, lnSys);
        	int[] overlaps = new int[lnOverlap]; 
        	int  overlap_total =0;
        	//Calculate the overlap score 
        	for (int i = 0; i <lnOverlap-1; i++ ){
        		int[] gtFrame = gridGT[i]; 
        		if(gtFrame == null)
        			System.out.println("GT Null at " +i + "ith frame");
        		
        		
        		int[] sysFrame = gridSys[i];
        		
        		if(sysFrame == null)
        			System.out.println("System Null at " +i + "ith frame");
        		
        		
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

	public File getMatlabPath() {
        return matlabPath;
    }

    public void setMatlabPath(File matlabPath) {
        this.matlabPath = matlabPath;
    }
}
