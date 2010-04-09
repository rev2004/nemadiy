/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.analytics.evaluation.classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.imirsel.nema.analytics.evaluation.*;
import org.imirsel.nema.analytics.evaluation.util.resultpages.*;
import org.imirsel.nema.analytics.evaluation.util.*;
import org.imirsel.nema.analytics.evaluation.vis.ConfusionMatrixPlot;
import org.imirsel.nema.analytics.util.io.*;
import org.imirsel.nema.model.*;

/**
 * Classification evaluation and result rendering.
 * 
 * @author kris.west@gmail.com
 * @since 0.1.0
 */
public class ClassificationEvaluator extends EvaluatorImpl{

	/** Command line harness usage statement. */
    public static final String USAGE = "args: taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription subjectMetadata /path/to/GT/file /path/to/output/dir [-h /path/to/hierarchy/file] /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    
    private File hierarchyFile = null;
    private List<String[]> hierarchies = null;
    private List<String> hierachiesKey = null;
    private List<String> classNames = null;
    
    private static final String BIG_DIVIDER =    "================================================================================\n";
    private static final String SMALL_DIVIDER = "--------------------------------------------------------------------------------\n";
    private static final int COL_WIDTH = 7;
    private static final DecimalFormat dec = new DecimalFormat("0.00");
    private static final String CONF_MAT_PLOT_EXTENSION = ".conf.png";
    private static final int CONF_MAT_HEIGHT = 850;
    private static final int CONF_MAT_WIDTH = 900;
    
    /**
	 * Constructor (no arg - task, dataset, output and working dirs, training
	 * and test sets must be set manually).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ClassificationEvaluator() {
		super();
	}
  
    protected void setupEvalMetrics() {
		this.trackEvalMetrics.clear();
		this.trackEvalMetrics.add(NemaDataConstants.CLASSIFICATION_ACCURACY);
		if(this.hierarchyFile != null){
			this.trackEvalMetrics.add(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
		}
		
		this.overallEvalMetrics.clear();
		this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_ACCURACY);
		this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY);
		//TODO think again what to do about matrix based eval metrics...
//		this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW);
//		this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
		if(this.hierarchyFile != null){
			this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
			this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY);
//			this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW);
//			this.overallEvalMetrics.add(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT);
		}
		
		//same as overall metrics - single fold experiment format
		this.foldEvalMetrics = this.overallEvalMetrics;
	}
    
    /**
     * Initializes the class names list from the ground-truth.
     */
    private void initClassNames() throws IllegalArgumentException{
    	String type = this.getTask().getSubjectTrackMetadataName();
    	String aClass;
    	NemaData data;
    	Set<String> classes = new HashSet<String>();
    	
    	for (Iterator<NemaData> it = this.getGroundTruth().iterator(); it.hasNext();){
    		data = it.next();
    		aClass = data.getStringMetadata(type);
    		if(aClass == null){
    			throw new IllegalArgumentException("Ground-truth example " + data.getId() + " had no metadata of type '" + type + "'");
    		}else{
    			classes.add(aClass);
    		}
    	}
    	
    	classNames = new ArrayList<String>(classes);
    	Collections.sort(classNames);
    	String classesMsg = "Classes of type '" + type + "' found in ground-truth:";
    	for(Iterator<String> it = classNames.iterator();it.hasNext();){
    		classesMsg += "\n\t" + it.next();
    	}
    	getLogger().info(classesMsg);
    }
    

    /**
     * Initialises the class hierarchy data-structures if a hierarchy file is in use.
     */
    private void initHierachy() throws FileNotFoundException, IOException{
        //Initialise Hierarchy scoring stuff
    	String msg = "reading hierarchy file: " + hierarchyFile.getAbsolutePath() + "\n";
        this.hierarchies = new ArrayList<String[]>();
        this.hierachiesKey = new ArrayList<String>();
        BufferedReader textBuffer = null;
        String[] dataLine = {"init1", "init2"};
        msg += "Hierarchy data:\n";
        try {
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(hierarchyFile) );
            String line = null; //not declared within while loop
            while (( line = textBuffer.readLine()) != null) {
                line = line.trim();
                if(!line.equals("")){
                    dataLine = line.split("[\t]+");
//                    for (int i = 0; i < dataLine.length; i++){
//                        dataLine[i] = TagClassificationGroundTruthFileReader.cleanTag(dataLine[i]);
//                    }
                    this.hierarchies.add(dataLine);
                    this.hierachiesKey.add(dataLine[0]);

                    msg += "\t" + dataLine[0];
                    for (int i = 1; i < dataLine.length; i++){
                        msg += " -> " + dataLine[i];
                    }
                    msg += "\n";
                }
            }
            getLogger().info(msg);
        } finally {
            try {
                if (textBuffer!= null) {
                    //flush and close both "input" and its underlying FileReader
                    textBuffer.close();
                }
            } catch (IOException ex) {
            }
        }
    }
    
//    /** Parse command line arguments for the main method harness.
//     * 
//     * @param args Full command line arguments received by the JVM.
//     * @return An instantiated ClassificationEvaluator, based on the arguments, that is ready to run.
//     * @throws IllegalArgumentException Thrown if a results or ground-truth file is not in the expected format.
//     * @throws FileNotFoundException Thrown if a non-null hierarchy file is passed, but cannot be 
//     * found.
//     * @throws IOException Thrown if there is a problem reading a results or ground-truth file, unrelated to 
//     * format.
//     */
//    public static ClassificationEvaluator parseCommandLineArgs(String[] args) throws IllegalArgumentException, FileNotFoundException, IOException{
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
//        ClassificationEvaluator eval = new ClassificationEvaluator(task, dataset, workingDir, workingDir, true, new File("matlab"), hierarchyFile);
//        eval.getLogger().info(msg);
//        
//        //reading ground-truth data
//        ClassificationTextFile reader = new ClassificationTextFile(task.getSubjectTrackMetadataName());
//        List<NemaData> gt = reader.readFile(gtFile);
//        eval.setGroundTruth(gt);
//        
//        msg = "Results to evaluate:\n";
//        for (int i = startIdx; i < args.length; i+=2) {
//            String systemName = args[i+1];
//            File resultsPath = new File(args[i]);
//            
//            List<List<NemaData>> results = reader.readDirectory(resultsPath,null);
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
//        ClassificationEvaluator eval = null;
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
//				Logger.getLogger(ClassificationEvaluator.class.getName()).log(Level.SEVERE, "Exception occured while parsing command line arguments!",e);
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
    public NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException{
    	int numJobs = jobIDToFoldResults.size();
        String jobId, jobName;
        
        //check that all systems have the same number of results
        checkFolds();
        
		/* prepare NemaEvaluationResultSet*/
		NemaEvaluationResultSet results = getEmptyEvaluationResultSet();

		{
			/* Perform the evaluations on all jobIds (systems) */
			Map<NemaTrackList,List<NemaData>> sysResults;
			Map<String, Map<NemaTrackList,NemaData>> jobIdToFoldEvaluation = new HashMap<String, Map<NemaTrackList,NemaData>>(numJobs);
			for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();) {
				jobId = it.next();
				getLogger().info("Evaluating experiment for jobID: " + jobId);
				sysResults = jobIDToFoldResults.get(jobId);
				Map<NemaTrackList,NemaData> foldEvals = new HashMap<NemaTrackList,NemaData>(testSets.size());
				for (Iterator<NemaTrackList> trackIt = sysResults.keySet().iterator(); trackIt.hasNext();) {
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
				// note we are using an overridden version of averageFoldMetrics as the confusion matrices have to be averaged for classification
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
		int numJobs = results.getJobIds().size();
		
		/* Make per system result directories */
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			
			/* Make a sub-directory for the systems results */
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobId);
			sysDir.mkdirs();
			jobIDToResultDir.put(jobId, sysDir);
		}
		
		//plot confusion matrices for each fold
		getLogger().info("Plotting confusion matrices for each fold for each job");
		Map<String,File[]> jobIDToFoldConfFileList = new HashMap<String,File[]>(numJobs);
		for(Iterator<String> it = results.getJobIds().iterator(); it.hasNext();){
			jobId = it.next();
			Map<NemaTrackList,NemaData> evalList = results.getPerFoldEvaluation(jobId);
			File[] foldConfFiles = plotConfusionMatricesForAllFolds(jobId, evalList);
			jobIDToFoldConfFileList.put(jobId,foldConfFiles);
		}
		
		//plot aggregate confusion for each job
		getLogger().info("Plotting overall confusion matrices for each job");
		Map<String,File> jobIDToOverallConfFile = new HashMap<String,File>(numJobs);
		for(Iterator<String> it = results.getJobIds().iterator(); it.hasNext();){
			jobId = it.next();
			NemaData aggregateEval = results.getOverallEvaluation(jobId);
			File overallConfFile = plotAggregatedConfusionForJob(jobId, aggregateEval);
		    jobIDToOverallConfFile.put(jobId, overallConfFile);
		}
		
		
		//write out CSV results files
		getLogger().info("Writing out CSV result files over whole task...");
		File perClassCSV = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassResults.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT),perClassCSV);
		
		File perFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "PerFoldResults.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(), jobIDToName,NemaDataConstants.CLASSIFICATION_ACCURACY),perFoldCSV);
		
		//write out discounted results summary CSVs
		File discountedPerClassCSV = null;
		File discountedPerFoldCSV = null;
		if (hierarchyFile != null){
		    discountedPerClassCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerClassResults.csv");
		    WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),jobIDToName,classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT),discountedPerClassCSV);
		    discountedPerFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerFoldResults.csv");
		    WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),jobIDToName,NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY),discountedPerFoldCSV);
		}
		
		//write out results summary CSV
		File summaryCSV = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),jobIDToName,this.getOverallEvalMetricsKeys()),summaryCSV);
		
		
		//perform statistical tests
		File friedmanClassTablePNG = null;
		File friedmanClassTable = null;
		File friedmanFoldTablePNG = null;
		File friedmanFoldTable = null;
		File friedmanDiscountClassTablePNG = null;
		File friedmanDiscountClassTable = null;
		File friedmanDiscountFoldTablePNG = null;
		File friedmanDiscountFoldTable = null;
		if (getPerformMatlabStatSigTests() && results.getJobIds().size() > 1){
		    getLogger().info("Performing Friedman's tests...");
		
		    File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perClassCSV, 0, 1, 1, numJobs, getMatlabPath());
		    friedmanClassTablePNG = tmp[0];
		    friedmanClassTable = tmp[1];
		
		    tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
		    friedmanFoldTablePNG = tmp[0];
		    friedmanFoldTable = tmp[1];
		
		    if (hierarchyFile != null){
		        tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerClassCSV, 0, 1, 1, numJobs, getMatlabPath());
		        friedmanDiscountClassTablePNG = tmp[0];
		        friedmanDiscountClassTable = tmp[1];
		        
		        tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
		        friedmanDiscountFoldTablePNG = tmp[0];
		        friedmanDiscountFoldTable = tmp[1];
		    }
		}
		
		
		//write text reports
		getLogger().info("Writing text evaluation reports...");
		Map<String,File> jobIDToReportFile = new HashMap<String,File>(numJobs);
		for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
			jobId = it.next();
			File reportFile = new File(outputDir.getAbsolutePath() + File.separator + jobId + File.separator + "report.txt");
			writeSystemTextReport(results.getOverallEvaluation(jobId), results.getTestSetTrackLists(), results.getPerFoldEvaluation(jobId), classNames, jobId, jobIDToName.get(jobId), hierarchyFile!=null, reportFile);
			jobIDToReportFile.put(jobId, reportFile);
		}
		
		
		//create tarballs of individual result dirs
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String,File> jobIDToTgz = new HashMap<String,File>(jobIDToName.size());
		for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
			jobId = it.next();
			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(new File(outputDir.getAbsolutePath() + File.separator + jobId)));
		}
		
		
		//write result HTML pages
		getLogger().info("Creating result HTML files...");
		writeHtmlResultPages(results, classNames,
				jobIDToOverallConfFile, perClassCSV, perFoldCSV,
				discountedPerClassCSV, discountedPerFoldCSV,
				friedmanClassTablePNG, friedmanClassTable,
				friedmanFoldTablePNG, friedmanFoldTable,
				friedmanDiscountClassTablePNG, friedmanDiscountClassTable,
				friedmanDiscountFoldTablePNG, friedmanDiscountFoldTable,
				jobIDToTgz, outputDir);
    }

	private void writeHtmlResultPages(
			NemaEvaluationResultSet results, List<String> classNames,
			Map<String, File> jobIDToOverallConfFile, File perClassCSV,
			File perFoldCSV, File discountedPerClassCSV,
			File discountedPerFoldCSV, File friedmanClassTablePNG,
			File friedmanClassTable, File friedmanFoldTablePNG,
			File friedmanFoldTable, File friedmanDiscountClassTablePNG,
			File friedmanDiscountClassTable, File friedmanDiscountFoldTablePNG,
			File friedmanDiscountFoldTable, Map<String, File> jobIDToTgz,
			File outputDir) {
		
		int numJobs = results.getJobIds().size();
		boolean performStatSigTests = (numJobs > 1) && this.getPerformMatlabStatSigTests();
		boolean usingAHierarchy = this.hierarchyFile != null;
		
		List<Page> resultPages = new ArrayList<Page>();
        List<PageItem> items;
        Page aPage;

        //do intro page to describe task
        {
        	items = new ArrayList<PageItem>();
	        Table descriptionTable = WriteCsvResultFiles.prepTaskTable(results.getTask(),results.getDataset());
	        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
	        aPage = new Page("intro", "Introduction", items, false);
	        resultPages.add(aPage);
        }
        
        //do summary page
        {
	        items = new ArrayList<PageItem>();
	        Table summaryTable = WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),this.getOverallEvalMetricsKeys());
	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
	        aPage = new Page("summary", "Summary", items, false);
	        resultPages.add(aPage);
        }

        //do per class page
        {
            items = new ArrayList<PageItem>();
            Table perClassTable = WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
            items.add(new TableItem("acc_class", "Accuracy per Class", perClassTable.getColHeaders(), perClassTable.getRows()));
            if (usingAHierarchy){
                Table perDiscClassTable = WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT);
                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Class", perDiscClassTable.getColHeaders(), perDiscClassTable.getRows()));
            }
            aPage = new Page("acc_per_class", "Accuracy per Class", items, false);
            resultPages.add(aPage);
        }

        //do per fold page
        {
            items = new ArrayList<PageItem>();
            Table perFoldTable = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_ACCURACY);
            items.add(new TableItem("acc_class", "Accuracy per Fold", perFoldTable.getColHeaders(), perFoldTable.getRows()));
            if (usingAHierarchy){
                Table perDiscFoldTable = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Fold", perDiscFoldTable.getColHeaders(), perDiscFoldTable.getRows()));
            }
            aPage = new Page("acc_per_fold", "Accuracy per Fold", items, false);
            resultPages.add(aPage);
        }
        
        //do significance tests
        if (performStatSigTests){
            items = new ArrayList<PageItem>();
            items.add(new ImageItem("friedmanClassTablePNG", "Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanClassTablePNG, outputDir)));
            items.add(new ImageItem("friedmanFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanFoldTablePNG, outputDir)));
            if(friedmanDiscountClassTable != null){
                items.add(new ImageItem("friedmanDiscountClassTablePNG", "Discounted Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountClassTablePNG, outputDir)));
            }
            if(friedmanDiscountFoldTable != null){
                items.add(new ImageItem("friedmanDiscountFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountFoldTablePNG, outputDir)));
            }
            aPage = new Page("sig_tests", "Significance Tests", items, true);
            resultPages.add(aPage);
        }

        //do confusion matrices
        List<String> sortedJobIDs = new ArrayList<String>(results.getJobIds());
        Collections.sort(sortedJobIDs);
        {
            items = new ArrayList<PageItem>();
            
            for (int i = 0; i < numJobs; i++){
                items.add(new ImageItem("confusion_" + i, sortedJobIDs.get(i), IOUtil.makeRelative(jobIDToOverallConfFile.get(sortedJobIDs.get(i)), outputDir)));
            }
            aPage = new Page("confusion", "Confusion Matrices", items, true);
            resultPages.add(aPage);
        }

        //do files page
        {
            items = new ArrayList<PageItem>();

            //CSVs
            List<String> CSVPaths = new ArrayList<String>(4);
            CSVPaths.add(IOUtil.makeRelative(perClassCSV,outputDir));
            CSVPaths.add(IOUtil.makeRelative(perFoldCSV,outputDir));
            if (usingAHierarchy){
                CSVPaths.add(IOUtil.makeRelative(discountedPerClassCSV,outputDir));
                CSVPaths.add(IOUtil.makeRelative(discountedPerFoldCSV,outputDir));
            }
            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));

            //Friedman's tables and plots
            if (performStatSigTests){
                //Friedmans tables
                List<String> sigCSVPaths = new ArrayList<String>(4);
                sigCSVPaths.add(IOUtil.makeRelative(friedmanClassTable, outputDir));
                sigCSVPaths.add(IOUtil.makeRelative(friedmanFoldTable, outputDir));
                if(friedmanDiscountClassTable != null){
                    sigCSVPaths.add(IOUtil.makeRelative(friedmanDiscountClassTable, outputDir));
                }
                if(friedmanDiscountFoldTable != null){
                    sigCSVPaths.add(IOUtil.makeRelative(friedmanDiscountFoldTable, outputDir));
                }
                items.add(new FileListItem("sigCSVs", "Significance test CSVs", sigCSVPaths));

                //Friedmans plots
                List<String> sigPNGPaths = new ArrayList<String>(4);
                sigPNGPaths.add(IOUtil.makeRelative(friedmanClassTablePNG, outputDir));
                sigPNGPaths.add(IOUtil.makeRelative(friedmanFoldTablePNG, outputDir));
                if(friedmanDiscountClassTable != null){
                    sigPNGPaths.add(IOUtil.makeRelative(friedmanDiscountClassTablePNG, outputDir));
                }
                if(friedmanDiscountFoldTable != null){
                    sigPNGPaths.add(IOUtil.makeRelative(friedmanDiscountFoldTablePNG, outputDir));
                }
                items.add(new FileListItem("sigPNGs", "Significance test plots", sigPNGPaths));
            }

            //System Tarballs
            List<String> tarballPaths = new ArrayList<String>(numJobs);
            for (int i = 0; i < numJobs; i++){
                tarballPaths.add(IOUtil.makeRelative(jobIDToTgz.get(sortedJobIDs.get(i)),outputDir));
            }
            items.add(new FileListItem("tarballs", "Per algorithm evaluation tarball", tarballPaths));
            aPage = new Page("files", "Raw data files", items, true);
            resultPages.add(aPage);
        }

        Page.writeResultPages(results.getTask().getName(), outputDir, resultPages);
	}

	@Override
	protected NemaData averageFoldMetrics(String jobId, Collection<NemaData> perFoldEvaluations) {
		int numClasses = this.classNames.size();
		int numFolds = this.testSets.size();
		boolean usingAHierarchy = this.hierarchyFile != null;
		NemaData aggregateEval = new NemaData(jobId);
		int[][][] confFolds = new int[numFolds][][];
		int f = 0;
		
		if(perFoldEvaluations.size() != numFolds){
			throw new IllegalArgumentException("Job ID " + jobId + 
					" returned " + perFoldEvaluations.size() + " folds, expected " + numFolds);
		}
		
		for(Iterator<NemaData> foldIt = perFoldEvaluations.iterator(); foldIt.hasNext();){
			confFolds[f] = foldIt.next().get2dIntArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW);
			if(confFolds[f].length != numClasses){
				throw new IllegalArgumentException("Fold " + f + " for job ID " + jobId + 
						" returned a confusion matrix of dimension " + confFolds[f].length + ", expected " + numClasses);
			}
			for (int i = 0; i < confFolds[f].length; i++) {
				if(confFolds[f][i].length != numClasses){
					throw new IllegalArgumentException("Fold " + f + " for job ID " + jobId + 
							" returned a (non-square) confusion matrix of dimension " + confFolds[f][i].length + ", expected " + numClasses);
				}
			}
			f++;
		}
		int[][] confusionRaw = new int[numClasses][numClasses];
		double[][] confusionPercent = new double[numClasses][numClasses];
		
		int[] resultsPerClass = new int[numClasses];
		for(int i=0;i<numClasses;i++){
			resultsPerClass[i] = 0;
			for(int j=0;j<numClasses;j++){
				for(int f2=0;f2<numFolds;f2++){
					confusionRaw[j][i] += confFolds[f2][j][i];
				}
				resultsPerClass[i] += confusionRaw[j][i];
			}
			if(resultsPerClass[i] > 0){
				for(int j=0;j<numClasses;j++){
					confusionPercent[j][i] = (double)confusionRaw[j][i] / resultsPerClass[i];
				}
			}
		}
		
		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW, confusionRaw);
		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT, confusionPercent);
		
		//Calculate final accuracy as diagonal sum of confusion matrix divided by total number of examples
      
		double finalAccuracy = 0.0;
		double finalDiscountedAccuracy = 0.0;
		int finalSum = 0;
		for (int i=0;i<numClasses; i++) {
		    finalSum += resultsPerClass[i];
			finalAccuracy += confusionRaw[i][i];
		}
		finalAccuracy /= (double)finalSum;
		
		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY, finalAccuracy);
		
		//Calculate Normalized accuracy as mean of percentage confusion matrix diagonal
		double finalNormalisedAccuracy = 0.0;
		for (int i=0;i<numClasses; i++) {
		    finalNormalisedAccuracy += confusionPercent[i][i];
		}
		finalNormalisedAccuracy /= (double)numClasses;
		aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY, finalNormalisedAccuracy);
		
		//repeat for discounted stuff
		if(usingAHierarchy){
			double[][] discountFoldAccs = new double[numFolds][];
			f = 0;
			for(Iterator<NemaData> foldIt = perFoldEvaluations.iterator(); foldIt.hasNext();){
				discountFoldAccs[f++] = foldIt.next().getDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW);
			}
			double[] discountConfusionRaw = new double[numClasses];
			double[] discountConfusionPercent = new double[numClasses];
			for(int i=0;i<numClasses;i++){
				for(int f2=0;f2<numFolds;f2++){
					discountConfusionRaw[i] += discountFoldAccs[f2][i];
				}
				if(resultsPerClass[i] > 0){
					discountConfusionPercent[i] = discountConfusionRaw[i] / resultsPerClass[i];
				}
			}
			
			aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW, discountConfusionRaw);
			aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, discountConfusionPercent);
			
			for (int i=0;i<numClasses; i++) {
		        finalDiscountedAccuracy += discountConfusionRaw[i];
		    }
		    finalDiscountedAccuracy /= finalSum;
		    aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY, finalDiscountedAccuracy);
		    
		    //Calculate Normalized accuracy as mean of percentage discounted confusion matrix diagonal
		    double finalNormalisedDiscountedAccuracy = 0.0;
		    for (int i=0;i<numClasses; i++) {
		        finalNormalisedDiscountedAccuracy += discountConfusionPercent[i];
		    }
		    finalNormalisedDiscountedAccuracy /= (double)numClasses;
		    aggregateEval.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY, finalNormalisedDiscountedAccuracy);    
		}
		return aggregateEval;
	}

	private File plotAggregatedConfusionForJob(String jobID, NemaData aggregateEval) {
		return plotConfusionMatrix(jobID, aggregateEval, "overall", " - overall");
	}

	private File[] plotConfusionMatricesForAllFolds(String jobID, Map<NemaTrackList, NemaData> evals) {
		int numFolds = this.testSets.size();
		File[] foldConfFiles = new File[numFolds];
		new File(outputDir.getAbsolutePath() + File.separator + jobID).mkdirs();
		int count = 0;
		for(Iterator<NemaTrackList> foldIt = testSets.iterator();foldIt.hasNext();){
			NemaTrackList testSet = foldIt.next();
			NemaData eval = evals.get(testSet);
			File plotFile = plotConfusionMatrix(jobID, eval, (""+testSet.getFoldNumber()), " - fold " + testSet.getFoldNumber());
		    foldConfFiles[count++] = plotFile;
		}
		return foldConfFiles;
	}

	private File plotConfusionMatrix(String jobID, NemaData eval, String fileNameComp, String titleComp) {
		double[][] confusion = eval.get2dDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
		File plotFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + fileNameComp + CONF_MAT_PLOT_EXTENSION);
		ConfusionMatrixPlot plot = new ConfusionMatrixPlot(getTask().getName() + " - " + jobIDToName.get(jobID) + titleComp, (String[])classNames.toArray(new String[classNames.size()]), confusion);
		plot.writeChartToFile(plotFile, CONF_MAT_WIDTH, CONF_MAT_HEIGHT);
		return plotFile;
	}
    
    public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> theData) {
    	if(classNames == null){
    		initClassNames();
    	}        
    	
    	//count the number of examples returned and search for any missing tracks in the results returned for the fold
    	int numExamples = checkFoldResultsAreComplete(jobID, testSet, theData);
    	
//    	//count the number of examples returned and search for any missing tracks in the results returned for the fold
//    	List<NemaTrack> tracks = testSet.getTracks();
//    	int numExamples = -1;
//    	if (tracks == null){
//    		numExamples = theData.size();
//    		getLogger().warning("The list of tracks in the test set was not " +
//    				"provided, hence, it cannot be confirmed that job ID " 
//    				+ jobID + " returned results for the entire set.");
//    		
//    		for (Iterator<NemaData> iterator = theData.iterator(); iterator.hasNext();) {
//				numExamplesPerClass[classNames.indexOf(trackIDToGT.get(iterator.next().getId()))]++;
//			}
//    	}else{
//    		numExamples = tracks.size();
//    		for (Iterator<NemaTrack> iterator = tracks.iterator(); iterator.hasNext();) {
//				numExamplesPerClass[classNames.indexOf(trackIDToGT.get(iterator.next().getId()))]++;
//			}
//    		
//    		if (numExamples != theData.size()){
//    			getLogger().warning("job ID " + jobID + " returned results for " +
//    					theData.size() + " tracks, when the test contains " + 
//    					numExamples + ". Missing tracks will still be counted in" +
//    							" the final accuracy score.");
//    			
//    			//find missing results and report
//    			Set<String> returnedIds = new HashSet<String>(numExamples);
//    			for (Iterator<NemaData> iterator = theData.iterator(); iterator
//						.hasNext();) {
//					returnedIds.add(iterator.next().getId());
//				}
//    			List<String> missing = new ArrayList<String>();
//    			for (Iterator<NemaTrack> iterator = tracks.iterator(); iterator
//						.hasNext();) {
//					String id = iterator.next().getId();
//					if (!returnedIds.contains(id)){
//						missing.add(id);
//					}
//				}
//    			if (!missing.isEmpty()){
//    				String msg = "No predictions were returned for the following track ID(s): ";
//    				for (Iterator<String> iterator = missing.iterator(); iterator
//							.hasNext();) {
//    					String id = iterator.next();
//						msg += id;
//						if (iterator.hasNext()){
//							msg += ", ";
//						}
//						
//					}
//    				getLogger().warning(msg);
//    			}
//    		}
//    	}
    	
    	int[] numExamplesPerClass = new int[classNames.size()];
    	List<NemaTrack> tracks = testSet.getTracks();
    	if (tracks == null){
    		for (Iterator<NemaData> iterator = theData.iterator(); iterator.hasNext();) {
    			numExamplesPerClass[classNames.indexOf(trackIDToGT.get(iterator.next().getId()).getStringMetadata(this.getTask().getSubjectTrackMetadataName()))]++;
			}
    	}else{
    		for (Iterator<NemaTrack> iterator = tracks.iterator(); iterator.hasNext();) {
				numExamplesPerClass[classNames.indexOf(trackIDToGT.get(iterator.next().getId()).getStringMetadata(this.getTask().getSubjectTrackMetadataName()))]++;
			}
    	}
    	
    	
    	boolean usingAHierarchy = hierarchyFile != null;
        String type = getTask().getSubjectTrackMetadataName();
        
        int errors = 0;
        int[][] confusion = new int[classNames.size()][classNames.size()];
        double[] discountedConfusion = null;
        if(usingAHierarchy) {
            discountedConfusion = new double[classNames.size()];
        }
        
        NemaData outObj = new NemaData(jobID);
        
        NemaData data;
        NemaData gtData;
        String classString;
        int classification;
        String truthString;
        int truth;
        
        for(int x=0; x < theData.size(); x++) {
            //Do simple evaluation
        	data = theData.get(x);
        	classString = data.getStringMetadata(type);
            classification = classNames.indexOf(classString);
            gtData = trackIDToGT.get(data.getId());
            truthString = gtData.getStringMetadata(type);
            truth = classNames.indexOf(truthString);
            
            confusion[classification][truth]++;
            if(usingAHierarchy&&(truthString.equalsIgnoreCase(classString)))
            {
                discountedConfusion[truth] += 1.0;
            }
            if (!truthString.equals(classString)) {
                errors++;
                //set individual accuracy (1 or 0 - no hierarchy)
                data.setMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY, 0.0);
                
                // do hierarchical discounting of confusions if necessary
                if(usingAHierarchy) {
                    ArrayList<String[]> trueHierachies = new ArrayList<String[]>(this.hierarchies);
                    ArrayList<String> trueKeys = new ArrayList<String>(this.hierachiesKey);
                    
                    double highestDiscountScore = 0.0;
                    
                    int trueIndex = trueKeys.indexOf(truthString);
                    while(trueIndex != -1)
                    {
                        double discountScore = 0.0;
                        ArrayList<String[]> classifiedHierachies = new ArrayList<String[]>(this.hierarchies);
                        ArrayList<String> classifiedKeys = new ArrayList<String>(this.hierachiesKey);
                        int classifiedIndex = classifiedKeys.indexOf(classString);
                    
                        trueKeys.remove(trueIndex);
                        String[] tempTrue = (String[])trueHierachies.remove(trueIndex);
                        ArrayList<String> truePath = new ArrayList<String>();
                        for(int i=0;i<tempTrue.length;i++) {
                            truePath.add(tempTrue[i]);
                        }
                        while(classifiedIndex != -1)
                        {
                            classifiedKeys.remove(classifiedIndex);
                            String[] tempClassification = (String[])classifiedHierachies.remove(classifiedIndex);
                            ArrayList<String> classifiedPath = new ArrayList<String>();
                            for(int i=0;i<tempClassification.length;i++) {
                                classifiedPath.add(tempClassification[i]);
                            }
                            for (int i=0;i<classifiedPath.size();i++) {
                                if (truePath.indexOf(classifiedPath.get(i)) != - 1) {
                                    discountScore += 1.0 / ((double)truePath.size());
                                }
                            }
                            
                            if (discountScore > highestDiscountScore){
                                highestDiscountScore = discountScore;
                            }
                            classifiedIndex = classifiedKeys.indexOf(classString);                    
                        }
                        trueIndex = trueKeys.indexOf(truthString);
                    }
                
                    discountedConfusion[truth] += highestDiscountScore;
                    //set individual accuracy (1 or 0)
                    data.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY, highestDiscountScore);
                }
            }else{//correct classification
            	//set individual accuracy (1 or 0)
            	data.setMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY, 1.0);
            	if(usingAHierarchy){
            		data.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY, 1.0);
            	}
            }
        }
        
        //Store class names
        outObj.setMetadata(NemaDataConstants.CLASSIFICATION_EXPERIMENT_CLASSNAMES, classNames);
        
        //store raw confusion matrices
        outObj.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_RAW, confusion);
        //If necessary, store discounted confusion matrices
        if(usingAHierarchy) {
        	outObj.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW, discountedConfusion);
        }
        
        //calculate percentage confusion matrix and, if necessary, discounted confusion matrix for this iteration
        double[][] percentConfusion = new double[classNames.size()][classNames.size()];
        double[] percentDiscountedConfusion = null;
        if (usingAHierarchy) {
            percentDiscountedConfusion = new double[classNames.size()];
        }
        for(int y=0; y<classNames.size(); y++) {
            if(numExamplesPerClass[y] > 0){
	            if(usingAHierarchy) {
	                percentDiscountedConfusion[y] = discountedConfusion[y] / (double)numExamplesPerClass[y];
	            }
	            for(int x=0; x<classNames.size(); x++) {
	                percentConfusion[x][y] = (double)confusion[x][y] / (double)numExamplesPerClass[y];
	            }
            }
        }
        
        //store percentage confusion matrices
        outObj.setMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT, percentConfusion);
        //If necessary, store discounted confusion matrices
        if(usingAHierarchy) {
        	outObj.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, percentDiscountedConfusion);
        }
        
        //Calculate accuracy as diagonal sum of confusion matrix divided by total number of examples
        double Accuracy = 0.0;//(double)(theSignalsLength - errors) / (double)(theSignalsLength);
        double DiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            Accuracy += confusion[i][i];
        }
        Accuracy /= (double)numExamples;
        outObj.setMetadata(NemaDataConstants.CLASSIFICATION_ACCURACY, Accuracy);
        if (usingAHierarchy) {
            //Calculate accuracy as diagonal sum of discounted confusion matrix divided by total number of examples
            for (int i=0;i<classNames.size(); i++) {
                DiscountedAccuracy += discountedConfusion[i];
            }
            DiscountedAccuracy /= (double)numExamples;
            outObj.setMetadata(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY, DiscountedAccuracy);
        }
        
        //Calculate Normalized accuracy as mean of percentage confusion matrix diagonal
        double NormalisedAccuracy = 0.0;
        double NormalisedDiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            NormalisedAccuracy += percentConfusion[i][i];
        }
        NormalisedAccuracy /= classNames.size();
        outObj.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_ACCURACY, NormalisedAccuracy);
        
        if (usingAHierarchy) {
            //Calculate Normalized accuracy as mean of percentage discounted confusion matrix diagonal
            for (int i=0;i<classNames.size(); i++) {
                NormalisedDiscountedAccuracy += percentDiscountedConfusion[i];
            }
            NormalisedDiscountedAccuracy /= (double)classNames.size();
            outObj.setMetadata(NemaDataConstants.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY, NormalisedDiscountedAccuracy);
        }

        return outObj;
    }
    
    /**
     * Writes a textual evaluation report on the results of one system to an UTF-8 text file. Includes 
     * the confusion matrices, accuracy, discounted accuracy and normalised versions of each for each 
     * iteration of the experiment and overall.
     * 
     * @param aggregateEval An Object representing the combined evaluation of all iterations.
     * @param testSets A list of the NemaTrackList Objects representing the test sets.
     * @param foldEvals A map of objects representing the evaluation of each fold/iteration of the 
     * experiment.
     * @param classNames An ordered list of the class names used in the experiment.
     * @param jobID The jobID of the system being evaluated.
     * @param jobName The name of the job being evaluated.
     * @param usingAHierarchy Flag indicating whether the evaluation used a hierarchy to discount confusions
     * (meaning we need to retrieve and report on the extra discounted results).
     * @param outputFile The File to write the report to.
     * @throws IOException Thrown if there is a problem writing to the report file.
     * @throws FileNotFoundException Thrown if the report file cannot be created.
     */
    public void writeSystemTextReport(NemaData aggregateEval, List<NemaTrackList> testSets, Map<NemaTrackList,NemaData> foldEvals, List<String> classNames, String jobID, String jobName, boolean usingAHierarchy, File outputFile) throws IOException, FileNotFoundException{
        
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
    	
	    for(Iterator<NemaTrackList> foldIt = testSets.iterator();foldIt.hasNext();){
	    	NemaTrackList fold = foldIt.next();
	    	NemaData foldData = foldEvals.get(fold);
	    	
	    	bufferString += "Fold " + fold.getFoldNumber() + " (" + fold.getId() + ")\n";
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
		    if(foldIt.hasNext()){
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
	    
	    FileUtils.writeStringToFile(outputFile, bufferString, "UTF-8");
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


	public void setHierarchyFile(File hierarchyFile) {
		this.hierarchyFile = hierarchyFile;
	}

	public File getHierarchyFile() {
		return hierarchyFile;
	}
	
}
