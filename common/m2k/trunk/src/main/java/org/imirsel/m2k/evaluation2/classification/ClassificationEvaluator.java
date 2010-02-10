/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.m2k.evaluation2.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.m2k.evaluation2.DataObj;
import org.imirsel.m2k.evaluation2.EvaluatorImpl;
import org.imirsel.m2k.evaluation2.TaskDescription;
import org.imirsel.m2k.evaluation2.resultPages.FileListItem;
import org.imirsel.m2k.evaluation2.resultPages.ImageItem;
import org.imirsel.m2k.evaluation2.resultPages.Page;
import org.imirsel.m2k.evaluation2.resultPages.PageItem;
import org.imirsel.m2k.evaluation2.resultPages.TableItem;
import org.imirsel.m2k.evaluation2.tagsClassification.TagClassificationAffinityEvaluator;
import org.imirsel.m2k.evaluation2.tagsClassification.TagClassificationBinaryEvaluator;
import org.imirsel.m2k.evaluation2.tagsClassification.TagClassificationGroundTruthFileReader;
import org.imirsel.m2k.io.file.CopyFileFromClassPathToDisk;
import org.imirsel.m2k.io.file.IOUtil;
import org.imirsel.m2k.util.MatlabCommandlineIntegrationClass;
import org.imirsel.m2k.vis.ConfusionMatrixPlot;

/**
 *
 * @author kris.west@gmail.com
 */
public class ClassificationEvaluator extends EvaluatorImpl{

    public static final String EVAL_DATA_EXT = ".evalData.ser";
    public static final String EVAL_REPORT_EXT = ".eval.txt";
    public static final String USAGE = "args: taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription subjectMetadata /path/to/GT/file /path/to/output/dir [-h /path/to/hierarchy/file] /path/to/system1/results/dir system1Name ... /path/to/systemN/results/dir systemNName";
    
    private boolean performMatlabStatSigTests = true;
    private String matlabPath = "matlab";
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
     * The task must at least contain the metadata class to be predicted. The default
     * class is 'genre'.
     * @param task_
     * @param outputDir_
     * @param workingDir_
     * @param performMatlabStatSigTests_
     * @param matlabPath_
     * @param hierarchyFile_
     * @throws FileNotFoundException
     */
    public ClassificationEvaluator(
    		TaskDescription task_,
            File outputDir_,
            File workingDir_,
            boolean performMatlabStatSigTests_,
            String matlabPath_,
            File hierarchyFile_) 
    		throws FileNotFoundException{
        super(ClassificationEvaluator.class, workingDir_, outputDir_, task_);
        performMatlabStatSigTests = performMatlabStatSigTests_;
        matlabPath = matlabPath_;
        hierarchyFile = hierarchyFile_;
        if(hierarchyFile != null) {
            initHierachy();
        }
    }
    
    /**
     * The task must at least contain the metadata class to be predicted. The default
     * class is 'genre'.
     * @param task_
     * @param outputDir_
     * @param workingDir_
     * @param performMatlabStatSigTests_
     * @param matlabPath_
     * @param hierarchyFile_
     * @param logger
     * @throws FileNotFoundException
     */
    public ClassificationEvaluator(
    		TaskDescription task_,
            File outputDir_,
            File workingDir_,
            boolean performMatlabStatSigTests_,
            String matlabPath_,
            File hierarchyFile_,
            Logger logger) 
    		throws FileNotFoundException{
        super(logger, workingDir_, outputDir_, task_);
        performMatlabStatSigTests = performMatlabStatSigTests_;
        matlabPath = matlabPath_;
        hierarchyFile = hierarchyFile_;
        if(hierarchyFile != null) {
            initHierachy();
        }
    }
    
    /**
     * Initialises the class names list from the ground-truth.
     */
    private void initClassNames() throws IllegalArgumentException{
    	String type = task.getMetadataPredicted();
    	String aClass;
    	DataObj data;
    	Set<String> classes = new HashSet<String>();
    	for (Iterator<DataObj> it = trackIDToGT.values().iterator(); it.hasNext();){
    		data = it.next();
    		aClass = data.getStringMetadata(type);
    		if(aClass == null){
    			throw new IllegalArgumentException("Ground-truth example " + data.getStringMetadata(DataObj.PROP_FILE_LOCATION) + " had no metadata of type '" + type + "'");
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
    	_logger.info(classesMsg);
    }
    

    /**
     * Initialises the class hierarchy data-structures if a hierarchy file is in use.
     */
    private void initHierachy() {
        //Initialise Hierarchy scoring stuff
        System.out.println("reading hierarchy file: " + hierarchyFile);
        this.hierarchies = new ArrayList<String[]>();
        this.hierachiesKey = new ArrayList<String>();
        BufferedReader textBuffer = null;
        String[] dataLine = {"init1", "init2"};
        try {
            //use buffering
            //this implementation reads one line at a time
            textBuffer = new BufferedReader( new FileReader(hierarchyFile) );
            String line = null; //not declared within while loop
            while (( line = textBuffer.readLine()) != null) {
                line = line.trim();
                if(!line.equals("")){
                    dataLine = line.split("\t");
                    for (int i = 0; i < dataLine.length; i++){
                        dataLine[i] = TagClassificationGroundTruthFileReader.cleanTag(dataLine[i]);
                    }
                    this.hierarchies.add(dataLine);
                    this.hierachiesKey.add(dataLine[0]);
                    System.out.print("Adding");
                    for (int i = 0; i < dataLine.length; i++){
                        System.out.print("\t" + dataLine[i]);
                    }
                    System.out.println("");
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (textBuffer!= null) {
                    //flush and close both "input" and its underlying FileReader
                    textBuffer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static ClassificationEvaluator parseCommandLineArgs(String[] args) throws IllegalArgumentException, FileNotFoundException, IOException{
        if (args.length < 9){
            System.err.println("ERROR: Insufficient arguments!\n" + USAGE);
            System.exit(1);
        }
        
        //taskID(int) taskName taskDescription datasetID(int) datasetName datasetDescription
        TaskDescription task = new TaskDescription();
        task.setTaskID(Integer.parseInt(args[0]));
        task.setTaskName(args[1]);
        task.setTaskDescription(args[2]);
        task.setDatasetId(Integer.parseInt(args[3]));
        task.setDatasetName(args[4]);
        task.setDatasetDescription(args[5]);
        task.setMetadataPredicted(args[6]);
        File gtFile = new File(args[7]);
        File workingDir = new File(".");
        File hierarchyFile = null;
        String msg = 
        		"Task description:  " + task.toString() + 
    			"Ground-truth file: " + gtFile.getAbsolutePath() + "\n" + 
    			"Working directory: " + workingDir.getAbsolutePath() + "\n" + 
    			"OutputDirectory:   " + workingDir.getAbsolutePath() + "\n";

        if (args.length % 2 != 0){
            System.err.println("WARNING: an odd number of arguments was specified, one may have been ignored!\n" + USAGE);
        }
        
        int startIdx = -1;
        if (args[8].equalsIgnoreCase("-h")){
            hierarchyFile = new File(args[9]);
            msg += "Hierarchy file:    " + hierarchyFile.getAbsolutePath() + "\n";
            startIdx = 9;
        }else{startIdx = 7;
            msg += "Hierarchy file:    null\n";
        }
        
        ClassificationEvaluator eval = new ClassificationEvaluator(task, workingDir, workingDir, true, "matlab", hierarchyFile);
        eval.getLogger().info(msg);
        
        //reading ground-truth data
        MIREXClassificationTextFile reader = new MIREXClassificationTextFile(task.getMetadataPredicted());
        List<DataObj> gt = reader.readFile(gtFile);
        eval.setGroundTruth(gt);
        
        msg = "Results to evaluate:\n";
        for (int i = startIdx; i < args.length; i+=2) {
            String systemName = args[i+1];
            File resultsPath = new File(args[i]);
            
            List<List<DataObj>> results = reader.readDirectory(resultsPath,null);
            for(Iterator<List<DataObj>> it = results.iterator();it.hasNext();){
            	eval.addResults(systemName, systemName, it.next());
            }
            msg += systemName + ": " + resultsPath.getAbsolutePath() + ", read " + results.size() + "result files\n";
        }
        eval.getLogger().info(msg);
        
        return eval;
    }
    
    public static void main(String[] args) {
        
        System.err.println("MIREX 2008 Classification evaluator\n" +
                "\t\tby Kris West (kris.west@gmail.com");
        System.err.println("");
        
        ClassificationEvaluator eval;
		try {
			eval = parseCommandLineArgs(args);
			eval.evaluate();
		} catch (Exception e) {
			Logger.getLogger(ClassificationEvaluator.class.getName()).log(Level.SEVERE, "Exception occured while parsing command line arguments!",e);
		}
        
        System.err.println("---exit---");
    }
    
    public Map<String,DataObj> evaluate() throws IllegalArgumentException, IOException{

    	if(classNames == null){
    		initClassNames();
    	}
    	
        boolean performStatSigTests = true;
        int numJobs = jobIDToFoldResults.size();
        if(numJobs < 2){
            performStatSigTests = false;
        }
        int numClasses = classNames.size();
        int numFolds = -1;
        String jobID;
        boolean usingAHierarchy = hierarchyFile != null;
        
        List<List<DataObj>> sysResults;
        //check that all systems have the same number of results
        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	sysResults = jobIDToFoldResults.get(jobID);
            if (numFolds == -1) {
                numFolds = sysResults.size();
            } else if (numFolds != sysResults.size()) {
                throw new IllegalArgumentException("The number of folds (" + sysResults.size() + ") detected for system ID: " + jobID + 
                		", name: " + jobIDToName.get(jobID) + "is not equal to the number detected " + 
                		"for the preceeding systems (" + numFolds + ")!");
            }
        }

        //evaluate each fold for each system
        
        Map<String,List<DataObj>> jobIDTofoldEvaluations = new HashMap<String,List<DataObj>>(numJobs); 
        for(Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	_logger.info("Evaluating experiment folds for jobID: " + jobID);
        	sysResults = jobIDToFoldResults.get(jobID);
        	List<DataObj> foldResultList = new ArrayList<DataObj>(numFolds);
        	for(Iterator<List<DataObj>> it2 = sysResults.iterator();it2.hasNext();){
        		foldResultList.add(evaluateResultFold(jobID, it2.next()));
        	}
        	jobIDTofoldEvaluations.put(jobID, foldResultList);
        }
        
        //plot confusion matrices for each fold
        _logger.info("Plotting confusion matrices for each fold");
        List<DataObj> evalList;
        DataObj eval;
        double[][] confusion;
        Map<String,File[]> jobIDToFoldConfFileList = new HashMap<String,File[]>(numJobs);
        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	evalList = jobIDTofoldEvaluations.get(jobID);
        	File[] foldConfFiles = new File[numFolds];
        	jobIDToFoldConfFileList.put(jobID,foldConfFiles);
        	new File(outputDir.getAbsolutePath() + File.separator + jobID).mkdirs();
    		for(int i=0;i<evalList.size();i++){
        		eval = evalList.get(i);
        		confusion = eval.get2dDoubleArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
        		foldConfFiles[i] = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + i + CONF_MAT_PLOT_EXTENSION);
        		
        		ConfusionMatrixPlot plot = new ConfusionMatrixPlot(getTask().getTaskName() + " - " + jobIDToName.get(jobID) + " - fold " + i, (String[])classNames.toArray(new String[numClasses]), confusion);
                plot.writeChartToFile(foldConfFiles[i], CONF_MAT_WIDTH, CONF_MAT_HEIGHT);
        	}
        }
        
        //aggregate results to produce overall evaluation
        _logger.info("Producing aggregate evaluations over all folds");
        Map<String,DataObj> jobIDToAggregateEvaluations = new HashMap<String,DataObj>(numJobs); 
        Map<String,File> jobIDToOverallConfFile = new HashMap<String,File>(numJobs);
        for(Iterator<String> it = jobIDTofoldEvaluations.keySet().iterator(); it.hasNext();){
        	jobID = it.next();
        	evalList = jobIDTofoldEvaluations.get(jobID);
        	DataObj aggregateEval = new DataObj(jobID);
        	int[][][] confFolds = new int[numFolds][][];
        	for(int f=0;f<numFolds;f++){
        		confFolds[f] = evalList.get(f).get2dIntArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_RAW);
        	}
        	int[][] confusionRaw = new int[numClasses][numClasses];
        	double[][] confusionPercent = new double[numClasses][numClasses];
        	
        	int[] sums = new int[numClasses];
    		for(int i=0;i<numClasses;i++){
    			sums[i] = 0;
    			for(int j=0;j<numClasses;j++){
    				for(int f=0;f<numFolds;f++){
        				confusionRaw[j][i] += confFolds[f][j][i];
    				}
    				sums[i] += confusionRaw[j][i];
    			}
    			if(sums[i] > 0){
	    			for(int j=0;j<numClasses;j++){
	    				confusionPercent[j][i] = (double)confusionRaw[j][i] / sums[i];
	    			}
    			}
    		}
        	
    		aggregateEval.setMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_RAW, confusionRaw);
    		aggregateEval.setMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT, confusionPercent);
        	
    		//plot aggregate confusion
    		File overallConfFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + "overall" + CONF_MAT_PLOT_EXTENSION);
    		jobIDToOverallConfFile.put(jobID, overallConfFile);
    		ConfusionMatrixPlot plot = new ConfusionMatrixPlot(getTask().getTaskName() + " - " + jobIDToName.get(jobID) + " - overall", (String[])classNames.toArray(new String[numClasses]), confusionPercent);
            plot.writeChartToFile(overallConfFile, CONF_MAT_WIDTH, CONF_MAT_HEIGHT);
    		
    		//Calculate final accuracy as diagonal sum of confusion matrix divided by total number of examples
	        double finalAccuracy = 0.0;
	        double finalDiscountedAccuracy = 0.0;
	        int finalSum = 0;
	        for (int i=0;i<numClasses; i++) {
	            finalSum += sums[i];
	        	finalAccuracy += confusionRaw[i][i];
	        }
	        finalAccuracy /= (double)finalSum;
	        
	        aggregateEval.setMetadata(DataObj.CLASSIFICATION_ACCURACY, finalAccuracy);
	        
        	//Calculate Normalised accuracy as mean of percentage confusion matrix diagonal
	        double finalNormalisedAccuracy = 0.0;
	        for (int i=0;i<numClasses; i++) {
	            finalNormalisedAccuracy += confusionPercent[i][i];
	        }
	        finalNormalisedAccuracy /= (double)numClasses;
	        aggregateEval.setMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY, finalNormalisedAccuracy);
	        
	        //repeat for discounted stuff
        	if(usingAHierarchy){
        		double[][] discountFoldAccs = new double[numFolds][];
        		for(int f=0;f<numFolds;f++){
        			discountFoldAccs[f] = evalList.get(f).getDoubleArrayMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW);
        		}
	        	int[] discountConfusionRaw = new int[numClasses];
	        	double[] discountConfusionPercent = new double[numClasses];
	        	for(int i=0;i<numClasses;i++){
	        		for(int f=0;f<numFolds;f++){
	        			discountConfusionRaw[i] += discountFoldAccs[f][i];
	        		}
	        		if(sums[i] > 0){
	        			discountConfusionPercent[i] = (double)discountConfusionRaw[i] / sums[i];
	        		}
	        	}
	        	
	        	aggregateEval.setMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW, discountConfusionRaw);
	    		aggregateEval.setMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, discountConfusionPercent);
	    		
	    		for (int i=0;i<numClasses; i++) {
	                finalDiscountedAccuracy += discountConfusionRaw[i];
	            }
	            finalDiscountedAccuracy /= finalSum;
	            aggregateEval.setMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY, finalDiscountedAccuracy);
	            
	            //Calculate Normalised accuracy as mean of percentage discounted confusion matrix diagonal
	            double finalNormalisedDiscountedAccuracy = 0.0;
	            for (int i=0;i<numClasses; i++) {
	                finalNormalisedDiscountedAccuracy += discountConfusionPercent[i];
	            }
	            finalNormalisedDiscountedAccuracy /= (double)numClasses;
	            aggregateEval.setMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY, finalNormalisedDiscountedAccuracy);    
        	}
        	
        	jobIDToAggregateEvaluations.put(jobID, aggregateEval);
        }
        
        //write out CSV results files
        _logger.info("Writing out CSV result files over whole task...");
        File perClassCSV = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassResults.csv");
        WriteResultFilesClass.prepFriedmanTestDataCSVOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT,perClassCSV);
        
        File perFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "PerFoldResults.csv");
        WriteResultFilesClass.prepFriedmanTestDataCSVOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_ACCURACY,perFoldCSV);
        
        //write out results summary CSV
        File summaryCSV = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
        WriteResultFilesClass.prepSummaryResultDataCSV(jobIDToAggregateEvaluations,jobIDToName,classNames,summaryCSV,usingAHierarchy);
        
        //write out discounted results summary CSVs
        File discountedPerClassCSV = null;
        File discountedPerFoldCSV = null;
        if (hierarchyFile != null){
            discountedPerClassCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerClassResults.csv");
            WriteResultFilesClass.prepFriedmanTestDataCSVOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, discountedPerClassCSV);
            discountedPerFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerFoldResults.csv");
            WriteResultFilesClass.prepFriedmanTestDataCSVOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY,perFoldCSV);
        }
        
        //perform statistical tests
        
        File friedmanClassTablePNG = null;
        File friedmanClassTable = null;
        File friedmanFoldTablePNG = null;
        File friedmanFoldTable = null;
        File friedmanDiscountClassTablePNG = null;
        File friedmanDiscountClassTable = null;
        File friedmanDiscountFoldTablePNG = null;
        File friedmanDiscountFoldTable = null;
        if (getPerformMatlabStatSigTests() && performStatSigTests){
            _logger.info("Performing Friedman's tests in Matlab...");
            String[] systemNamesArr = jobIDToName.values().toArray(new String[jobIDToName.size()]);
            
            File[] tmp = performFriedmanTestWithClassAccuracy(outputDir, perClassCSV, systemNamesArr);
            friedmanClassTablePNG = tmp[0];
            friedmanClassTable = tmp[1];

            tmp = performFriedmanTestWithFoldAccuracy(outputDir, perFoldCSV, systemNamesArr);
            friedmanFoldTablePNG = tmp[0];
            friedmanFoldTable = tmp[1];

            if (hierarchyFile != null){
                tmp = performFriedmanTestWithClassAccuracy(outputDir, discountedPerClassCSV, systemNamesArr);
                friedmanDiscountClassTablePNG = tmp[0];
                friedmanDiscountClassTable = tmp[1];
                
                tmp = performFriedmanTestWithFoldAccuracy(outputDir, discountedPerFoldCSV, systemNamesArr);
                friedmanDiscountFoldTablePNG = tmp[0];
                friedmanDiscountFoldTable = tmp[1];
            }
        }
        
        
        //write text reports?
        _logger.info("Writing text evaluation reports...");
        Map<String,File> jobIDToReportFile = new HashMap<String,File>(numJobs);
        for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
        	jobID = it.next();
        	File reportFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + "report.txt");
        	writeSystemTextReport(jobIDToAggregateEvaluations.get(jobID), jobIDTofoldEvaluations.get(jobID), classNames, jobID, jobIDToName.get(jobID), usingAHierarchy, reportFile);
        	jobIDToReportFile.put(jobID, reportFile);
        }
        
        
        //create tarballs of individual result dirs
        _logger.info("Preparing evaluation data tarballs...");
        Map<String,File> jobIDToTgz = new HashMap<String,File>(jobIDToName.size());
        for (Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();) {
        	jobID = it.next();
        	jobIDToTgz.put(jobID, IOUtil.tarAndGzip(new File(outputDir.getAbsolutePath() + File.separator + jobID)));
        }
        
        
        //write result HTML pages
        _logger.info("Creating result HTML files...");

        List<Page> resultPages = new ArrayList<Page>();
        List<PageItem> items;
        Page aPage;

        //do summary page
        {
	        items = new ArrayList<PageItem>();
	        WriteResultFilesClass.Table summaryTable = WriteResultFilesClass.prepSummaryTable(jobIDToAggregateEvaluations,jobIDToName,classNames,usingAHierarchy);
	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
	        aPage = new Page(task, "summary", "Summary", items, false);
	        resultPages.add(aPage);
        }

        //do per class page
        {
            items = new ArrayList<PageItem>();
            WriteResultFilesClass.Table perClassTable = WriteResultFilesClass.prepTableDataOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
            items.add(new TableItem("acc_class", "Accuracy per Class", perClassTable.getColHeaders(), perClassTable.getRows()));
            if (hierarchyFile != null){
                WriteResultFilesClass.Table perDiscClassTable = WriteResultFilesClass.prepTableDataOverClasses(jobIDToAggregateEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT);
                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Class", perDiscClassTable.getColHeaders(), perDiscClassTable.getRows()));
            }
            aPage = new Page(task, "acc_per_class", "Accuracy per Class", items, false);
            resultPages.add(aPage);
        }

        //do per fold page
        {
            items = new ArrayList<PageItem>();
            WriteResultFilesClass.Table perFoldTable = WriteResultFilesClass.prepTableDataOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_ACCURACY);
            items.add(new TableItem("acc_class", "Accuracy per Fold", perFoldTable.getColHeaders(), perFoldTable.getRows()));
            if (hierarchyFile != null){
                WriteResultFilesClass.Table perDiscFoldTable = WriteResultFilesClass.prepTableDataOverFolds(jobIDTofoldEvaluations,jobIDToName,classNames,DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY);
                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Fold", perDiscFoldTable.getColHeaders(), perDiscFoldTable.getRows()));
            }
            aPage = new Page(task, "acc_per_fold", "Accuracy per Fold", items, false);
            resultPages.add(aPage);
        }
        
        //do significance tests
        if (getPerformMatlabStatSigTests() && performStatSigTests){
            items = new ArrayList<PageItem>();
            items.add(new ImageItem("friedmanClassTablePNG", "Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanClassTablePNG, outputDir)));
            items.add(new ImageItem("friedmanFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanFoldTablePNG, outputDir)));
            if(friedmanDiscountClassTable != null){
                items.add(new ImageItem("friedmanDiscountClassTablePNG", "Discounted Accuracy Per Class: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountClassTablePNG, outputDir)));
            }
            if(friedmanDiscountFoldTable != null){
                items.add(new ImageItem("friedmanDiscountFoldTablePNG", "Accuracy Per Fold: Friedman's ANOVA w/ Tukey Kramer HSD", IOUtil.makeRelative(friedmanDiscountFoldTablePNG, outputDir)));
            }
            aPage = new Page(task, "sig_tests", "Significance Tests", items, true);
            resultPages.add(aPage);
        }

        //do confusion matrices
        List<String> sortedJobIDs = new ArrayList<String>(jobIDToName.keySet());
        Collections.sort(sortedJobIDs);
        {
            items = new ArrayList<PageItem>();
            
            for (int i = 0; i < numJobs; i++){
                items.add(new ImageItem("confusion_" + i, sortedJobIDs.get(i), IOUtil.makeRelative(jobIDToOverallConfFile.get(sortedJobIDs.get(i)), outputDir)));
            }
            aPage = new Page(task, "confusion", "Confusion Matrices", items, true);
            resultPages.add(aPage);
        }

        //do files page
        {
            items = new ArrayList<PageItem>();

            //CSVs
            List<String> CSVPaths = new ArrayList<String>(4);
            CSVPaths.add(IOUtil.makeRelative(perClassCSV,outputDir));
            CSVPaths.add(IOUtil.makeRelative(perFoldCSV,outputDir));
            if (hierarchyFile != null){
                CSVPaths.add(IOUtil.makeRelative(discountedPerClassCSV,outputDir));
                CSVPaths.add(IOUtil.makeRelative(discountedPerFoldCSV,outputDir));
            }
            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));

            //Friedman's tables and plots
            if (getPerformMatlabStatSigTests() && performStatSigTests){
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
            aPage = new Page(task, "files", "Raw data files", items, true);
            resultPages.add(aPage);
        }

        Page.writeResultPages(task.getTaskName(), outputDir, resultPages);
        
        return jobIDToAggregateEvaluations;
    }


    
    private File[] performFriedmanTestWithClassAccuracy(File outputDir, File CSVResultFile, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForClassAccuracy";
        String name = CSVResultFile.getName().replaceAll(".csv", "");
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + name + ".friedman.tukeyKramerHSD.png";
        String matlabPlotRelPath = "." + File.separator + name + ".friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + name + ".friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));
            
            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(Acc_Scores));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores(:,fliplr(sort_idx)),1,'on'); close(gcf)");
            textOut.newLine();
            textOut.write("[c,m,h,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05,'display','off');");
            textOut.newLine();
            textOut.write("fig = figure;");
            textOut.newLine();
            textOut.write("width = (-c(1,3)+c(1,5))/4;");
            textOut.newLine();
            textOut.write("set(fig,'paperunit','points')");
            textOut.newLine();
            textOut.write("set(fig,'paperposition',[1 500 1200 500])");
            textOut.newLine();
            textOut.write("set(fig,'papersize',[1200 500])");
            textOut.newLine();
            textOut.write("set(fig,'position',[1 500 1200 500])");
            textOut.newLine();
            textOut.write("plot(friedmanStats.meanranks,'ro'); hold on");
            textOut.newLine();
            textOut.write("for i=1:" + systemNames.length + ",");
            textOut.newLine();
            textOut.write("    plot([i i],[-width width]+friedmanStats.meanranks(i));");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[-width -width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[+width +width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("set(gca,'xtick',1:" + systemNames.length + ",'xlim',[0.5 " + systemNames.length + "+0.5])");
            textOut.newLine();
            textOut.write("sortedAlgNames = algNames(fliplr(sort_idx));");
            textOut.newLine();
            textOut.write("set(gca,'xticklabel',sortedAlgNames)");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFile.getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("outerpos = get(gca,'outerposition');");
            textOut.newLine();
            textOut.write("tightinset = get(gca,'tightinset');");
            textOut.newLine();
            textOut.write("newpos = [tightinset(1) tightinset(2) outerpos(3)-(tightinset(1) + tightinset(3)) outerpos(4)-(tightinset(2) + tightinset(4))];");
            textOut.newLine();
            textOut.write("set(gca,'position',newpos);");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotRelPath + "');");
            textOut.newLine();
            textOut.write("fidFriedman=fopen('" + friedmanTablePath + "','w+');");
            textOut.newLine();
            textOut.write("fprintf(fidFriedman,'%s,%s,%s,%s,%s,%s\\n','*TeamID','TeamID','Lowerbound','Mean','Upperbound','Significance');");
            textOut.newLine();
            textOut.write("for i=1:size(c,1)");
            textOut.newLine();
            textOut.write("        if sign(c(i,3))*sign(c(i,5)) > 0");
            textOut.newLine();
            textOut.write("            tf='TRUE';");
            textOut.newLine();
            textOut.write("        else");
            textOut.newLine();
            textOut.write("            tf='FALSE';");
            textOut.newLine();
            textOut.write("        end");
            textOut.newLine();
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',sortedAlgNames{c(i,1)},sortedAlgNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("fclose(fidFriedman);");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();


            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(getMatlabPath());
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();
            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new File[]{new File(matlabPlotPath),new File(friedmanTablePath)};
    }
    
    private File[] performFriedmanTestWithFoldAccuracy(File outputDir, File CSVResultFile, String[] systemNames) {
        //make sure readtext is in the working directory for matlab
        File readtextMFile = new File(outputDir.getAbsolutePath() + File.separator + "readtext.m");
        CopyFileFromClassPathToDisk.copy("/org/imirsel/m2k/evaluation2/tagsClassification/resources/readtext.m", readtextMFile);
        
        //create an m-file to run the test
        String evalCommand = "performFriedmanForFoldAccuracy";
        String name = CSVResultFile.getName().replaceAll(".csv", "");
        File tempMFile = new File(outputDir.getAbsolutePath() + File.separator + evalCommand + ".m");
        String matlabPlotPath = outputDir.getAbsolutePath() + File.separator + name + ".friedman.tukeyKramerHSD.png";
        String matlabPlotRelPath = "." + File.separator + name + ".friedman.tukeyKramerHSD.png";
        String friedmanTablePath = outputDir.getAbsolutePath() + File.separator + name + ".friedman.tukeyKramerHSD.csv";
        try {
            BufferedWriter textOut = new BufferedWriter(new FileWriter(tempMFile));

            textOut.write("[data, result] = readtext('" + CSVResultFile.getAbsolutePath() + "', ',');");
            textOut.newLine();
            textOut.write("algNames = data(1,2:" + (systemNames.length + 1) + ")';");
            textOut.newLine();
            textOut.write("[length,width] = size(data);");
            textOut.newLine();
            textOut.write("Acc_Scores = cell2mat(data(2:length,2:" + (systemNames.length + 1) + "));");
            textOut.newLine();
            textOut.write("[val sort_idx] = sort(mean(Acc_Scores));");
            textOut.newLine();
            textOut.write("[P,friedmanTable,friedmanStats] = friedman(Acc_Scores(:,fliplr(sort_idx)),1,'on'); close(gcf)");
            textOut.newLine();
            textOut.write("[c,m,h,gnames] = multcompare(friedmanStats, 'ctype', 'tukey-kramer','estimate', 'friedman', 'alpha', 0.05,'display','off');");
            textOut.newLine();
            textOut.write("fig = figure;");
            textOut.newLine();
            textOut.write("width = (-c(1,3)+c(1,5))/4;");
            textOut.newLine();
            textOut.write("set(fig,'paperunit','points')");
            textOut.newLine();
            textOut.write("set(fig,'paperposition',[1 500 1200 500])");
            textOut.newLine();
            textOut.write("set(fig,'papersize',[1200 500])");
            textOut.newLine();
            textOut.write("set(fig,'position',[1 500 1200 500])");
            textOut.newLine();
            textOut.write("plot(friedmanStats.meanranks,'ro'); hold on");
            textOut.newLine();
            textOut.write("for i=1:" + systemNames.length + ",");
            textOut.newLine();
            textOut.write("    plot([i i],[-width width]+friedmanStats.meanranks(i));");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[-width -width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("    plot([-0.1 .1]+i,[+width +width]+friedmanStats.meanranks(i))");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("set(gca,'xtick',1:" + systemNames.length + ",'xlim',[0.5 " + systemNames.length + "+0.5])");
            textOut.newLine();
            textOut.write("sortedAlgNames = algNames(fliplr(sort_idx));");
            textOut.newLine();
            textOut.write("set(gca,'xticklabel',sortedAlgNames)");
            textOut.newLine();
            textOut.write("ylabel('Mean Column Ranks')");
            textOut.newLine();
            textOut.write("h = title('" + CSVResultFile.getAbsolutePath() + "')");
            textOut.newLine();
            textOut.write("set(h,'interpreter','none')");
            textOut.newLine();
            textOut.write("outerpos = get(gca,'outerposition');");
            textOut.newLine();
            textOut.write("tightinset = get(gca,'tightinset');");
            textOut.newLine();
            textOut.write("newpos = [tightinset(1) tightinset(2) outerpos(3)-(tightinset(1) + tightinset(3)) outerpos(4)-(tightinset(2) + tightinset(4))];");
            textOut.newLine();
            textOut.write("set(gca,'position',newpos);");
            textOut.newLine();
            textOut.write("saveas(fig,'" + matlabPlotRelPath + "');");
            textOut.newLine();
            textOut.write("fidFriedman=fopen('" + friedmanTablePath + "','w+');");
            textOut.newLine();
            textOut.write("fprintf(fidFriedman,'%s,%s,%s,%s,%s,%s\\n','*TeamID','TeamID','Lowerbound','Mean','Upperbound','Significance');");
            textOut.newLine();
            textOut.write("for i=1:size(c,1)");
            textOut.newLine();
            textOut.write("        if sign(c(i,3))*sign(c(i,5)) > 0");
            textOut.newLine();
            textOut.write("            tf='TRUE';");
            textOut.newLine();
            textOut.write("        else");
            textOut.newLine();
            textOut.write("            tf='FALSE';");
            textOut.newLine();
            textOut.write("        end");
            textOut.newLine();
            textOut.write("         fprintf(fidFriedman,'%s,%s,%6.4f,%6.4f,%6.4f,%s\\n',sortedAlgNames{c(i,1)},sortedAlgNames{c(i,2)},c(i,3),c(i,4),c(i,5),tf);");
            textOut.newLine();
            textOut.write("end");
            textOut.newLine();
            textOut.write("fclose(fidFriedman);");
            textOut.newLine();
            textOut.write("exit;");
            textOut.newLine();


            
            textOut.close();
        } catch (IOException ex) {
            Logger.getLogger(TagClassificationBinaryEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        MatlabCommandlineIntegrationClass matlabIntegrator = new MatlabCommandlineIntegrationClass();
        matlabIntegrator.setMatlabBin(getMatlabPath());
        matlabIntegrator.setCommandFormattingStr("");
        matlabIntegrator.setMainCommand(evalCommand);
        matlabIntegrator.setWorkingDir(outputDir.getAbsolutePath());
        matlabIntegrator.start();
        try {
            matlabIntegrator.join();
            //  call matlab and execute Beta-Binomial test
        } catch (InterruptedException ex) {
            Logger.getLogger(TagClassificationAffinityEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new File[]{new File(matlabPlotPath),new File(friedmanTablePath)};
    }
    

    private DataObj evaluateResultFold(String jobID, List<DataObj> theData) throws IllegalArgumentException, IllegalArgumentException, IOException {
                
        int numExamples = theData.size();
        boolean usingAHierarchy = hierarchyFile != null;
        String type = getTask().getMetadataPredicted();
        
        int errors = 0;
        int[][] confusion = new int[classNames.size()][classNames.size()];
        double[] discountedConfusion = null;
        if(usingAHierarchy) {
            discountedConfusion = new double[classNames.size()];
        }
        
        DataObj outObj = new DataObj(jobID);
        
        DataObj data;
        DataObj gtData;
        String classString;
        int classification;
        String truthString;
        int truth;
        
        for(int x=0; x<theData.size(); x++) {
            //Do simple evaluation
        	data = theData.get(x);
        	classString = data.getStringMetadata(type);
            classification = classNames.indexOf(classString);
            gtData = trackIDToGT.get(data.getStringMetadata(DataObj.PROP_FILE_LOCATION));
            truthString = gtData.getStringMetadata(type);
            truth = classNames.indexOf(truthString);
            
            confusion[classification][truth]++;
            if(usingAHierarchy&&(truthString.equalsIgnoreCase(classString)))
            {
                discountedConfusion[truth] += 1.0;
            }
            if (!truthString.equals(classString)) {
                errors++;
                
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
                }
            }
        }
        
        //Store class names
        outObj.setMetadata(DataObj.CLASSIFICATION_EXPERIMENT_CLASSNAMES, classNames);
        
        //store raw confusion matrices
        outObj.setMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_RAW, confusion);
        //If necessary, store discounted confusion matrices
        if(usingAHierarchy) {
        	outObj.setMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW, discountedConfusion);
        }
        
        //calculate percentage confusion matrix and, if necessary, discounted confusion matrix for this iteration
        double[][] percentConfusion = new double[classNames.size()][classNames.size()];
        double[] percentDiscountedConfusion = null;
        if (usingAHierarchy) {
            percentDiscountedConfusion = new double[classNames.size()];
        }
        for(int y=0; y<classNames.size(); y++) {
            int tot = 0;
            for(int x=0; x<classNames.size(); x++) {
                tot += confusion[x][y];
            }
            
            if(tot > 0){
	            if(usingAHierarchy) {
	                percentDiscountedConfusion[y] = discountedConfusion[y] / (double)tot;
	            }
	            for(int x=0; x<classNames.size(); x++) {
	                percentConfusion[x][y] = (double)confusion[x][y] / (double)tot;
	            }
            }
        }
        
        //store percentage confusion matrices
        outObj.setMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT, percentConfusion);
        //If necessary, store discounted confusion matrices
        if(usingAHierarchy) {
        	outObj.setMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT, percentDiscountedConfusion);
        }
        
        //Calculate accuracy as diagonal sum of confusion matrix divided by total number of examples
        double Accuracy = 0.0;//(double)(theSignalsLength - errors) / (double)(theSignalsLength);
        double DiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            Accuracy += confusion[i][i];
        }
        Accuracy /= (double)numExamples;
        outObj.setMetadata(DataObj.CLASSIFICATION_ACCURACY, Accuracy);
        if (usingAHierarchy) {
            //Calculate accuracy as diagonal sum of discounted confusion matrix divided by total number of examples
            for (int i=0;i<classNames.size(); i++) {
                DiscountedAccuracy += discountedConfusion[i];
            }
            DiscountedAccuracy /= (double)numExamples;
            outObj.setMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY, DiscountedAccuracy);
        }
        
        //Calculate Normalised accuracy as mean of percentage confusion matrix diagonal
        double NormalisedAccuracy = 0.0;
        double NormalisedDiscountedAccuracy = 0.0;
        for (int i=0;i<classNames.size(); i++) {
            NormalisedAccuracy += percentConfusion[i][i];
        }
        NormalisedAccuracy /= classNames.size();
        outObj.setMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY, NormalisedAccuracy);
        
        if (usingAHierarchy) {
            //Calculate Normalised accuracy as mean of percentage discounted confusion matrix diagonal
            for (int i=0;i<classNames.size(); i++) {
                NormalisedDiscountedAccuracy += percentDiscountedConfusion[i];
            }
            NormalisedDiscountedAccuracy /= (double)classNames.size();
            outObj.setMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY, NormalisedDiscountedAccuracy);
        }

        return outObj;
    }
    
    
    public void writeSystemTextReport(DataObj aggregateEval, List<DataObj> foldEvals, List<String> classNames, String jobID, String jobName, boolean usingAHierarchy, File outputFile) throws IOException, FileNotFoundException{
        
    	//Write output for each fold
    	String bufferString = BIG_DIVIDER + "Classification Evaluation Report\n";
    	bufferString += "Job ID:               " + jobID + "\n";
    	bufferString += "Job Name:             " + jobName + "\n";
    	bufferString += "Number of iterations: " + foldEvals.size() + "\n";
    	bufferString += "Task ID:              " + task.getTaskID() + "\n";
    	bufferString += "Task Name:            " + task.getTaskName() + "\n";
    	bufferString += "Task Description:     " + task.getTaskDescription() + "\n";
    	bufferString += "Metadata predicted:   " + task.getMetadataPredicted() + "\n";
    	bufferString += "Dataset ID:           " + task.getDatasetId() + "\n";
    	bufferString += "Dataset Name:         " + task.getDatasetName() + "\n";
    	bufferString += "Dataset Description:  " + task.getDatasetDescription() + "\n\n";
    	bufferString += SMALL_DIVIDER;
    	
	    for(int i=0;i<foldEvals.size();i++){
	    	DataObj foldData = foldEvals.get(i);
	    	
	    	bufferString += "Iteration " + i + "\n";
		    bufferString += "Accuracy: " + dec.format(foldData.getDoubleMetadata(DataObj.CLASSIFICATION_ACCURACY) * 100) + "%\n";
		    bufferString += "Accuracy (normalised for class sizes): " + dec.format(foldData.getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY) * 100) + "%\n";
	    	
		    if(usingAHierarchy) {
		        bufferString += "Hierachically Discounted Accuracy: " + dec.format(foldData.getDoubleMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100) + "%\n";
		        bufferString += "Hierachically Discounted Accuracy (normalised for class sizes): " + dec.format(foldData.getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100) + "%\n";
		    }
		    
		    bufferString += "Raw Confusion Matrix:\n";
		    bufferString += writeIntConfusionMatrix(foldData.get2dIntArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_RAW), classNames);
		    bufferString += "\nConfusion Matrix percentage:\n";
		    bufferString += writePercentageConfusionMatrix(foldData.get2dDoubleArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT), classNames);
		    bufferString += writeMatrixKey(classNames);
		    
		    if (usingAHierarchy)
		    {
		        bufferString += "\nHierachically Discounted Confusion Vector:\n";
		        bufferString += writeDoubleConfusionVector(foldData.getDoubleArrayMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW), classNames);
		        bufferString += "\nHierachically Discounted Confusion Matrix percentage:\n";
		        bufferString += writePercentageConfusionVector(foldData.getDoubleArrayMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT), classNames);
		    }
		    if(i<foldEvals.size()-1){
		    	bufferString += SMALL_DIVIDER;
		    }
	    }
	    
	    bufferString += "\n" + BIG_DIVIDER;
	    bufferString += "Overall Evaluation\n";
	    bufferString += "Accuracy: " + dec.format(aggregateEval.getDoubleMetadata(DataObj.CLASSIFICATION_ACCURACY) * 100) + "%\n";
	    bufferString += "Accuracy (normalised for class sizes): " + dec.format(aggregateEval.getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_ACCURACY) * 100) + "%\n";
    	
	    if(usingAHierarchy) {
	        bufferString += "Hierachically Discounted Accuracy: " + dec.format(aggregateEval.getDoubleMetadata(DataObj.CLASSIFICATION_DISCOUNTED_ACCURACY) * 100) + "%\n";
	        bufferString += "Hierachically Discounted Accuracy (normalised for class sizes): " + dec.format(aggregateEval.getDoubleMetadata(DataObj.CLASSIFICATION_NORMALISED_DISCOUNTED_ACCURACY) * 100) + "%\n";
	    }
	    
	    bufferString += "Raw Confusion Matrix:\n";
	    bufferString += writeIntConfusionMatrix(aggregateEval.get2dIntArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_RAW), classNames);
	    bufferString += "\nConfusion Matrix percentage:\n";
	    bufferString += writePercentageConfusionMatrix(aggregateEval.get2dDoubleArrayMetadata(DataObj.CLASSIFICATION_CONFUSION_MATRIX_PERCENT), classNames);
	    bufferString += writeMatrixKey(this.classNames);
	        
	    if (usingAHierarchy)
	    {
	        bufferString += "\nHierachically Discounted Confusion Vector:\n";
	        bufferString += writeDoubleConfusionVector(aggregateEval.getDoubleArrayMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_RAW), classNames);
	        bufferString += "\nHierachically Discounted Confusion Matrix percentage:\n";
	        bufferString += writePercentageConfusionVector(aggregateEval.getDoubleArrayMetadata(DataObj.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT), classNames);
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
            bufferString += getKey(x) + "\t\t";
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
            bufferString += getKey(x) + "\t\t";
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

    public boolean getPerformMatlabStatSigTests() {
        return performMatlabStatSigTests;
    }

    public void setPerformMatlabStatSigTests(boolean performMatlabStatSigTests) {
        this.performMatlabStatSigTests = performMatlabStatSigTests;
    }

    public String getMatlabPath() {
        return matlabPath;
    }

    public void setMatlabPath(String matlabPath) {
        this.matlabPath = matlabPath;
    }

	public void setHierarchyFile(File hierarchyFile) {
		this.hierarchyFile = hierarchyFile;
	}

	public File getHierarchyFile() {
		return hierarchyFile;
	}

	
}
