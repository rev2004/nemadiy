/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.chord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ImageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.*;
import org.imirsel.nema.model.*;

/**
 * Chord estimation evaluation and results rendering.
 * 
 * @author mert.bay@gmail.com
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class ChordEvaluator extends EvaluatorImpl{

//	/** Command line harness usage statement. */
//    public static final String USAGE = "args: taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription subjectMetadata /path/to/GT/file /path/to/output/dir [-h /path/to/hierarchy/file] /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    
//    private static final String BIG_DIVIDER =    "================================================================================\n";
//    private static final String SMALL_DIVIDER = "--------------------------------------------------------------------------------\n";
//    private static final int COL_WIDTH = 7;
//    private static final DecimalFormat dec = new DecimalFormat("0.00");
	
    private static final String PLOT_EXT = ".chords.png";
    private static final int GRID_RESOLUTION = 1000; //The grid resolution. 
    
    /**
	 * Constructor (no arg - task, dataset, output and working dirs, training
	 * and test sets must be set manually).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ChordEvaluator() {
		super();
	}

    protected void setupEvalMetrics() {
		this.trackEvalMetrics.clear();
		this.trackEvalMetrics.add(NemaDataConstants.CHORD_OVERLAP_RATIO);
		
		this.overallEvalMetrics.clear();
		this.overallEvalMetrics.add(NemaDataConstants.CHORD_OVERLAP_RATIO);
		this.overallEvalMetrics.add(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
		
		//same as overall metrics
		this.foldEvalMetrics = this.overallEvalMetrics;
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
    
    public NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException{
    	
		/* prepare NemaEvaluationResultSet*/
		NemaEvaluationResultSet results = getEmptyEvaluationResultSet();
		{
			/* Make sure we have same number of sets of results per jobId (i.e. system), 
			 * as defined in the experiment */
			checkFolds();
			
			int numJobs = jobIDToFoldResults.size();
	        
	        String jobId, jobName;
	        Map<NemaTrackList,List<NemaData>> sysResults;
			
	        //evaluate each fold for each system
			Map<String, Map<NemaTrackList,NemaData>> jobIdToFoldEvaluation = new HashMap<String, Map<NemaTrackList,NemaData>>(numJobs);
			for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
	        	jobId = it.next();
	        	getLogger().info("Evaluating experiment folds for jobID: " + jobId);
	        	sysResults = jobIDToFoldResults.get(jobId);
	        	Map<NemaTrackList,NemaData> foldEvals = new HashMap<NemaTrackList,NemaData>(testSets.size());
				for (Iterator<NemaTrackList> trackIt = sysResults.keySet().iterator(); trackIt.hasNext();) {
					//make sure we use the evaluators copy of the track list
					NemaTrackList trackList = testSets.get(testSets.indexOf(trackIt.next()));
					NemaData result = evaluateResultFold(jobId, trackList, sysResults.get(trackList));
					foldEvals.put(trackList, result);
				}
	
	        	jobIdToFoldEvaluation.put(jobId, foldEvals);
	        }
			
			/* Aggregated evaluation to produce overall results */
			Map<String, NemaData> jobIdToOverallEvaluation = new HashMap<String, NemaData>(numJobs);
			for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				getLogger().info("Aggregating results for jobID: " + jobId);
				Map<NemaTrackList,NemaData> foldEvals = jobIdToFoldEvaluation.get(jobId);
				NemaData overall = averageFoldMetrics(jobId, foldEvals.values());
				jobIdToOverallEvaluation.put(jobId, overall);
			}
			
			/* Populate NemaEvaluationResultSet */
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				jobName = jobIDToName.get(jobId);
				results.addCompleteResultSet(jobId, jobName, jobIdToOverallEvaluation.get(jobId), jobIdToFoldEvaluation.get(jobId), jobIDToFoldResults.get(jobId));
			}
	    }
        
		getLogger().info("Rendering results");
		renderResults(results, outputDir);
		
        return results;
    }
    
    public void renderResults(NemaEvaluationResultSet results, File outputDir) throws IOException {
    	String jobId;
        Map<NemaTrackList,List<NemaData>> sysResults;
        
		// Make per system result dirs
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		Map<String, List<File>> jobIDToFoldResultDirs = new HashMap<String, List<File>>();
		
		int numFolds = results.getTestSetTrackLists().size();
		int numJobs = results.getJobIds().size();
		
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
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
        for(Iterator<String> it_systems = results.getJobIds().iterator(); it_systems.hasNext();){
        	jobId = it_systems.next();
        	getLogger().info("Plotting Chord transcriptions for: " + jobId);
        	sysResults = results.getPerTrackEvaluationAndResults(jobId);
        	
        	//iterate over folds
        	List<File> foldDirs = jobIDToFoldResultDirs.get(jobId);
        	List<File[]> plotFolds = new ArrayList<File[]>();
        	Iterator<File> it_foldResDir = foldDirs.iterator();
        	
        	for (Iterator<NemaTrackList> it_folds = sysResults.keySet().iterator(); it_folds.hasNext();) {
        		NemaTrackList testSet = it_folds.next();
				List<NemaData> list = sysResults.get(testSet);
				
				File[] plots = new File[list.size()];
				File foldDir = it_foldResDir.next();
				
				//iterate over tracks
				int plotCount = 0;
				for (Iterator<NemaData> it_tracks = list.iterator(); it_tracks.hasNext();) {
					NemaData nemaData = it_tracks.next();
					
					File plotFile = new File(foldDir.getAbsolutePath() + File.separator + jobId + "-" + "fold" + testSet.getFoldNumber() + PLOT_EXT);
					plots[plotCount++] = plotFile;
					
					//TODO: actually plot the chords
					
					
					
					
				}
				plotFolds.add(plots);
			}
        	jobIDToResultPlotFileList.put(jobId, plotFolds);
        }

        //write out per metric CSV results files
        getLogger().info("Writing out CSV result files over whole task...");
        File overlapCsv = new File(outputDir.getAbsolutePath()+ File.separator + "overlap.csv");
        WriteCsvResultFiles.writeTableToCsv(
        		WriteCsvResultFiles.prepTableDataOverTracksAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerTrackEvaluationAndResults(),results.getJobIdToJobName(),NemaDataConstants.CHORD_OVERLAP_RATIO),
        		overlapCsv
    		);
        
        //write out results summary CSV
        File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
        WriteCsvResultFiles.writeTableToCsv(
        		WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),this.getOverallEvalMetricsKeys()),
        		summaryCsv
        	);
        
        //write out per system CSVs - per track
        getLogger().info("Writing out CSV result files for each system...");
        Map<String, List<File>> jobIDToCSVs = new HashMap<String, List<File>>(numJobs);
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			sysResults = results.getPerTrackEvaluationAndResults(jobId);
			
			File sysDir = jobIDToResultDir.get(jobId);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator + "per_track_results.csv");
			WriteCsvResultFiles.writeTableToCsv(
					WriteCsvResultFiles.prepTableDataOverTracks(results.getTestSetTrackLists(), sysResults, this.getTrackEvalMetricKeys()),
					trackCSV
				);
			ArrayList<File> list = new ArrayList<File>(2);
			list.add(trackCSV);
			jobIDToCSVs.put(jobId, list);
		}
        
        //write out per system CSVs - per fold
		getLogger().info("Writing out CSV result files for each system...");
		
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it.hasNext();) {
			jobId = it.next();
			Map<NemaTrackList, NemaData> sysFoldResults = results.getPerFoldEvaluation(jobId);
			
			File sysDir = jobIDToResultDir.get(jobId);
			File foldCSV = new File(sysDir.getAbsolutePath() + File.separator + "per_fold_results.csv");
			WriteCsvResultFiles.writeTableToCsv(
					WriteCsvResultFiles.prepTableDataOverFolds(results.getTestSetTrackLists(), sysFoldResults, this.getFoldEvalMetricsKeys()),
					foldCSV
				);
			jobIDToCSVs.get(jobId).add(foldCSV);
		}
        
        //perform statistical tests
		/* Do we need to stats tests? */
		boolean performStatSigTests = true;
        if(numJobs < 2){
            performStatSigTests = false;
        }
        
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
        writeHtmlResultPages(performStatSigTests, results, overlapCsv,
				summaryCsv, jobIDToCSVs, friedmanOverlapTablePNG,
				friedmanOverlapTable, friedmanWeightedOverlapTablePNG,
				friedmanWeightedOverlapTable, jobIDToTgz);

}

//	private Map<String, NemaData> aggregateFoldEvaluations(int numJobs,
//			Map<String, List<NemaData>> jobIDTofoldEvaluations) {
//		String jobId;
//		getLogger().info("Producing aggregate evaluations over all folds");
//        Map<String,NemaData> jobIDToAggregateEvaluations = new HashMap<String,NemaData>(numJobs); 
//        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it.hasNext();){
//        	jobId = it.next();
//        	List<NemaData> evalList = jobIDTofoldEvaluations.get(jobId);
//        	NemaData aggregateEval = new NemaData(jobId);
//        	
//        	double avgOverlap = 0.0;
//        	double avgWeightedOverlap = 0.0;
//        	
//        	for (Iterator<NemaData> it2 = evalList.iterator(); it2.hasNext();) {
//				NemaData nemaData = (NemaData) it2.next();
//				avgOverlap += nemaData.getDoubleMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO);
//	            avgWeightedOverlap += nemaData.getDoubleMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
//			}
//        	avgOverlap /= evalList.size();
//        	avgWeightedOverlap /= evalList.size();
//        	
//        	aggregateEval.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, avgOverlap);
//        	aggregateEval.setMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO, avgWeightedOverlap);
//        	jobIDToAggregateEvaluations.put(jobId, aggregateEval);
//        }
//		return jobIDToAggregateEvaluations;
//	}

	private void writeHtmlResultPages(boolean performStatSigTests, 
			NemaEvaluationResultSet results,
			File overlapCsv, 
			File summaryCsv, 
			Map<String, List<File>> jobIDToCSV, File friedmanOverlapTablePNG,
			File friedmanOverlapTable, File friedmanWeightedOverlapTablePNG,
			File friedmanWeightedOverlapTable, Map<String, File> jobIDToTgz) {
		
		int numJobs = results.getJobIds().size();
        
		String jobId;
		Map<NemaTrackList,List<NemaData>> sysResults;
		Map<NemaTrackList,NemaData> systemFoldResults;
		getLogger().info("Creating result HTML files...");

        List<Page> resultPages = new ArrayList<Page>();
        List<PageItem> items;
        Page aPage;

        //do intro page to describe task
        {
        	items = new ArrayList<PageItem>();
	        Table descriptionTable = WriteCsvResultFiles.prepTaskTable(task,dataset);
	        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
	        aPage = new Page("intro", "Introduction", items, false);
	        resultPages.add(aPage);
        }
        
        //do summary page
        {
	        items = new ArrayList<PageItem>();
	        Table summaryTable = WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),jobIDToName,this.getOverallEvalMetricsKeys());
	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
	        aPage = new Page("summary", "Summary", items, false);
	        resultPages.add(aPage);
        }

        //do per metric page
        {
            items = new ArrayList<PageItem>();
            
            Table weightedOverlapTablePerFold = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(), results.getJobIdToJobName(), NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
            items.add(new TableItem("chord_weighted_overlap_per_fold", "Chord Weighted Average Overlap Per Fold", weightedOverlapTablePerFold.getColHeaders(), weightedOverlapTablePerFold.getRows()));
            
            Table overlapTablePerFold = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(), results.getJobIdToJobName(), NemaDataConstants.CHORD_OVERLAP_RATIO);
            items.add(new TableItem("chord_overlap_per_fold", "Chord Average Overlap Per Fold", overlapTablePerFold.getColHeaders(), overlapTablePerFold.getRows()));
            
            Table overlapTable = WriteCsvResultFiles.prepTableDataOverTracksAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerTrackEvaluationAndResults(), results.getJobIdToJobName(), NemaDataConstants.CHORD_OVERLAP_RATIO);
            items.add(new TableItem("chord_overlap_per_track", "Chord Average Overlap Per Track", overlapTable.getColHeaders(), overlapTable.getRows()));
            
            aPage = new Page("all_system_metrics", "Detailed Evaluation Metrics", items, true);
            resultPages.add(aPage);
        }

        //do per system pages
        {
        	for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
    			jobId = it.next();
    			items = new ArrayList<PageItem>();
    			sysResults = results.getPerTrackEvaluationAndResults(jobId);
    			systemFoldResults = results.getPerFoldEvaluation(jobId);
    			
    			Table systemFoldTable = WriteCsvResultFiles.prepTableDataOverFolds(results.getTestSetTrackLists(), systemFoldResults, this.getFoldEvalMetricsKeys());
    			items.add(new TableItem(jobId+"_per_fold", jobIDToName.get(jobId) + " per fold results", systemFoldTable.getColHeaders(), systemFoldTable.getRows()));
                
    			Table systemTrackTable = WriteCsvResultFiles.prepTableDataOverTracks(results.getTestSetTrackLists(), sysResults, this.getTrackEvalMetricKeys());
    			items.add(new TableItem(jobId+"_per_track", jobIDToName.get(jobId) + " per track results", systemTrackTable.getColHeaders(), systemTrackTable.getRows()));
                
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
            List<String> perSystemCsvs = new ArrayList<String>(numJobs*2);
            for (Iterator<String> it = jobIDToCSV.keySet().iterator(); it.hasNext();) {
    			jobId = it.next();
    			List<File> files = jobIDToCSV.get(jobId);
    			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
    				perSystemCsvs.add(IOUtil.makeRelative((File)iterator.next(),outputDir));
				}
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
//
//	private int checkFolds() {
//		int numFolds = -1;
//        String jobID;
//        
//        List<List<NemaData>> sysResults;
//        List<Integer> tracksPerFold = null;
//        //check that all systems have the same number of folds
//        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
//        	jobID = it.next();
//        	sysResults = jobIDToFoldResults.get(jobID);
//            if (numFolds == -1) {
//                numFolds = sysResults.size();
//                tracksPerFold = new ArrayList<Integer>(numFolds);
//                for (Iterator<List<NemaData>> it2 = sysResults.iterator(); it2.hasNext();) {
//					tracksPerFold.add(it2.next().size());
//				}
//            } else if (numFolds != sysResults.size()) {
//                throw new IllegalArgumentException("The number of folds (" + sysResults.size() + ") detected for system ID: " + jobID + 
//                		", name: " + jobIDToName.get(jobID) + " is not equal to the number detected " + 
//                		"for the preceeding systems (" + numFolds + ")!");
//            } else{
//            	Iterator<Integer> it3 = tracksPerFold.iterator();
//            	int foldCount = 0;
//            	for (Iterator<List<NemaData>> it2 = sysResults.iterator(); it2.hasNext();) {
//            		int num = it2.next().size();
//            		int expected = it3.next().intValue();
//					if(num != expected){
//						throw new IllegalArgumentException("The number of tracks (" + num + ") for fold " + foldCount + " detected for system ID: " + jobID + 
//		                		", name: " + jobIDToName.get(jobID) + " is not equal to the number detected " + 
//		                		"for the preceeding systems (" + expected + ")!");
//					}
//					foldCount++;
//				}
//            }
//        }
//		return numFolds;
//	}
    
    public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> theData) {
    	//count the number of examples returned and search for any missing tracks in the results returned for the fold
    	int numExamples = checkFoldResultsAreComplete(jobID, testSet, theData);
    	
        NemaData outObj = new NemaData(jobID);
        NemaData data;
        NemaData gtData;
        
        List<NemaChord> systemChords;
        List<NemaChord> gtChords;
        
        double overlapAccum = 0.0;
        double weightedAverageOverlapAccum = 0.0;
        double lengthAccum = 0.0;
        
        
        //iterate through GT tracks and compute true length of GT
        HashMap<String,Integer> trackIdToLnGT = new HashMap<String, Integer>();
        List<NemaTrack> tracks = testSet.getTracks();
    	if (tracks == null){
    		getLogger().warning("The list of tracks in the test set was not " +
    				"provided, hence, it cannot be confirmed that job ID " 
    				+ jobID + " returned results for the entire set.");
    		for (Iterator<NemaData> iterator = theData.iterator(); iterator.hasNext();) {
    			String id = iterator.next().getId();
	    		gtData = trackIDToGT.get(id);
	    		gtChords = (List<NemaChord>)gtData.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
	    		int lnGT = (int)(GRID_RESOLUTION*gtChords.get(gtChords.size()-1).getOffset()) ;
	    		lengthAccum += lnGT; 
	    		trackIdToLnGT.put(id, lnGT);
			}
    	}else{
    		for (Iterator<NemaTrack> iterator = tracks.iterator(); iterator.hasNext();) {
    			String id = iterator.next().getId();
	    		gtData = trackIDToGT.get(id);
	    		gtChords = (List<NemaChord>)gtData.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
	    		int lnGT = (int)(GRID_RESOLUTION*gtChords.get(gtChords.size()-1).getOffset()) ;
	    		lengthAccum += lnGT; 
	    		trackIdToLnGT.put(id, lnGT);
			}
    	}
        
        //iterate through tracks
        for(int x=0; x < theData.size(); x++) {
            //Do simple evaluation
        	data = theData.get(x);
        	gtData = trackIDToGT.get(data.getId());

        	systemChords = (List<NemaChord>)data.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
        	gtChords = (List<NemaChord>)gtData.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
        	
        	//evaluate here
        	
        	//Create grid for the ground-truth
        	int lnGT = trackIdToLnGT.get(data.getId()) ;
        	if (lnGT == 0 ){
        		throw new IllegalArgumentException("Length of GT is 0!");
        	}        		
        	int[][] gridGT = new int[lnGT][];
        	for (int i = 0; i < gtChords.size(); i++) {
        		NemaChord currentChord = gtChords.get(i);
        		int onset_index = (int)(currentChord.getOnset()*GRID_RESOLUTION);
        		int offset_index = (int)(currentChord.getOffset()*GRID_RESOLUTION);
        		for (int j = onset_index; j<offset_index; j++){
        			gridGT[j]=currentChord.getNotes();
        		}
			}
        
        	// Create grid for the system
        	int lnSys = (int)(GRID_RESOLUTION*systemChords.get(systemChords.size()-1).getOffset());
        	double overlap_score;
        	if (lnSys == 0 ){
        		//they get nothing for this file!
        		getLogger().warning("Length of system results is " +
        				"0 for track: " + data.getId() + ", number of system " +
        						"chords: " + systemChords.size() + ", last chord: "
        						+ systemChords.get(systemChords.size()-1));
        		overlap_score = 0;
        	}else{
        	

	        		int[][] gridSys = new int[lnSys][];	
	//        	System.out.println("ln Sys " + lnSys + " last offset " + systemChords.get(systemChords.size()-1).getOffset());
	//        	System.out.println("System chords length " + systemChords.size());
	        	for (int i = 0; i < systemChords.size(); i++) {
	        		NemaChord currentChord = systemChords.get(i);
	        		int onset_index = (int)(currentChord.getOnset()*GRID_RESOLUTION);
	        		int offset_index = (int)(currentChord.getOffset()*GRID_RESOLUTION);
	        	//	System.out.println("Chord no " + i + " onset="+onset_index + "offset=" + offset_index);
	        		for (int j = onset_index; j < offset_index; j++){
	        			gridSys[j]=currentChord.getNotes();
	        			if (gridSys[j] == null){
	        				getLogger().warning("Returned null notes for track: " + data.getId() + ", chord " + i + ", onset index: " + onset_index + ", offset index: " + offset_index);
	        			}
	        				
	        		}
				}
	        	
	        	
	        	int lnOverlap = Math.min(lnGT, lnSys);
	        	//int[] overlaps = new int[lnOverlap]; 
	        	int  overlap_total = 0;
	        	//Calculate the overlap score 
	        	for (int i = 0; i < lnOverlap; i++ ){
	        		int[] gtFrame = gridGT[i]; 
	//        		if(gtFrame == null){
	//        			getLogger().warning("GT chord Null at " +i + "-ith frame");
	//        		}
	        		
	        		int[] sysFrame = gridSys[i];
	        		//disabled check as some systems do not mark data until first chord 
	//        		if(sysFrame == null){
	//        			getLogger().warning("System chord Null at " +i + "-ith frame");
	//        		}
	        		overlap_total +=  calcOverlap(gtFrame,sysFrame);        	
	        		
	        	}
	        	
	        	//set eval metrics on input obj for track
	        	overlap_score = (double)overlap_total / (double)lnGT;
        	}
        	weightedAverageOverlapAccum += overlap_score*lnGT;	
        	overlapAccum += overlap_score;
        	getLogger().info("jobID: " + jobID + ", track: " + data.getId() + ", overlap score: " + overlap_score);
        	data.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, overlap_score);
        }
        
        //produce avg chord ratio
        double avg = (double)overlapAccum / (double)numExamples;
        //produce weighted average chord ratio
        double weightedAverageOverlap = weightedAverageOverlapAccum / lengthAccum;
        
        getLogger().info("jobID: " + jobID + ", average overlap score: " + avg);
        getLogger().info("jobID: " + jobID + ", weighted average overlap score: " + weightedAverageOverlap);
        
        //set eval metrics on eval object for fold
        outObj.setMetadata(NemaDataConstants.CHORD_OVERLAP_RATIO, avg);
        outObj.setMetadata(NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO, weightedAverageOverlap);
        
        return outObj;
    }
    
    private int calcOverlap(int[] gt, int[] sys) {
    	if (gt == null || sys == null || gt.length!=sys.length){
    		return 0;
    	}
    		
    	else {
    		for (int i = 0; i < gt.length; i++) {
				if(gt[i] != sys[i]){
					return 0;
				}
			}
    		return 1;
    	}
    }    
}
