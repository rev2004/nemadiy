//TODO: fix this class
//package org.imirsel.nema.analytics.evaluation.classification;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.imirsel.nema.analytics.evaluation.FriedmansAnovaTkHsd;
//import org.imirsel.nema.analytics.evaluation.ResultRendererImpl;
//import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
//import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
//import org.imirsel.nema.analytics.evaluation.resultpages.ImageItem;
//import org.imirsel.nema.analytics.evaluation.resultpages.Page;
//import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
//import org.imirsel.nema.analytics.evaluation.resultpages.Table;
//import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
//import org.imirsel.nema.analytics.evaluation.vis.ConfusionMatrixPlot;
//import org.imirsel.nema.analytics.util.io.IOUtil;
//import org.imirsel.nema.model.NemaData;
//import org.imirsel.nema.model.NemaDataConstants;
//import org.imirsel.nema.model.NemaEvaluationResultSet;
//import org.imirsel.nema.model.NemaTrackList;
//
//public class ClassificaitonResultRenderer extends ResultRendererImpl {
//
//	private static final String CONF_MAT_PLOT_EXTENSION = ".conf.png";
//    private static final int CONF_MAT_HEIGHT = 850;
//    private static final int CONF_MAT_WIDTH = 900;
//    
//    public boolean usingAHierarchy(NemaEvaluationResultSet results){
//    	return results.getOverallEvalMetricsKeys().contains(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
//    }
//    
//    public void renderResults(NemaEvaluationResultSet results) throws IOException {
//
//    	boolean usingAHierarchy = usingAHierarchy(results);
//    	
//		String jobId;
//		int numJobs = results.getJobIds().size();
//		
//		/* Make per system result directories */
//		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
//		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
//			jobId = it.next();
//			
//			/* Make a sub-directory for the systems results */
//			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobId);
//			sysDir.mkdirs();
//			jobIDToResultDir.put(jobId, sysDir);
//		}
//		
//		//plot confusion matrices for each fold
//		getLogger().info("Plotting confusion matrices for each fold for each job");
//		Map<String,File[]> jobIDToFoldConfFileList = new HashMap<String,File[]>(numJobs);
//		for(Iterator<String> it = results.getJobIds().iterator(); it.hasNext();){
//			jobId = it.next();
//			Map<NemaTrackList,NemaData> evalList = results.getPerFoldEvaluation(jobId);
//			File[] foldConfFiles = plotConfusionMatricesForAllFolds(results,jobId, evalList);
//			jobIDToFoldConfFileList.put(jobId,foldConfFiles);
//		}
//		
//		//plot aggregate confusion for each job
//		getLogger().info("Plotting overall confusion matrices for each job");
//		Map<String,File> jobIDToOverallConfFile = new HashMap<String,File>(numJobs);
//		for(Iterator<String> it = results.getJobIds().iterator(); it.hasNext();){
//			jobId = it.next();
//			NemaData aggregateEval = results.getOverallEvaluation(jobId);
//			File overallConfFile = plotAggregatedConfusionForJob(results,jobId, aggregateEval);
//		    jobIDToOverallConfFile.put(jobId, overallConfFile);
//		}
//		
//		//retrieve class names from eval data
//		
//		
//		
//		//write out CSV results files
//		getLogger().info("Writing out CSV result files over whole task...");
//		File perClassCSV = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassResults.csv");
//		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT),perClassCSV);
//		
//		File perFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "PerFoldResults.csv");
//		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(), results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_ACCURACY),perFoldCSV);
//		
//		//write out discounted results summary CSVs
//		File discountedPerClassCSV = null;
//		File discountedPerFoldCSV = null;
//		if (results.getOverallEvalMetricsKeys().contains(NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY)){
//		    discountedPerClassCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerClassResults.csv");
//		    WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT),discountedPerClassCSV);
//		    discountedPerFoldCSV = new File(outputDir.getAbsolutePath() + File.separator + "DiscountedPerFoldResults.csv");
//		    WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY),discountedPerFoldCSV);
//		}
//		
//		//write out results summary CSV
//		File summaryCSV = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
//		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),results.getOverallEvalMetricsKeys()),summaryCSV);
//		
//		
//		//perform statistical tests
//		File friedmanClassTablePNG = null;
//		File friedmanClassTable = null;
//		File friedmanFoldTablePNG = null;
//		File friedmanFoldTable = null;
//		File friedmanDiscountClassTablePNG = null;
//		File friedmanDiscountClassTable = null;
//		File friedmanDiscountFoldTablePNG = null;
//		File friedmanDiscountFoldTable = null;
//		if (getPerformMatlabStatSigTests() && results.getJobIds().size() > 1){
//		    getLogger().info("Performing Friedman's tests...");
//		
//		    File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perClassCSV, 0, 1, 1, numJobs, getMatlabPath());
//		    friedmanClassTablePNG = tmp[0];
//		    friedmanClassTable = tmp[1];
//		
//		    tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, perFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
//		    friedmanFoldTablePNG = tmp[0];
//		    friedmanFoldTable = tmp[1];
//		
//		    if (usingAHierarchy){
//		        tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerClassCSV, 0, 1, 1, numJobs, getMatlabPath());
//		        friedmanDiscountClassTablePNG = tmp[0];
//		        friedmanDiscountClassTable = tmp[1];
//		        
//		        tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, discountedPerFoldCSV, 0, 1, 1, numJobs, getMatlabPath());
//		        friedmanDiscountFoldTablePNG = tmp[0];
//		        friedmanDiscountFoldTable = tmp[1];
//		    }
//		}
//		
//		
//		//write text reports
//		getLogger().info("Writing text evaluation reports...");
//		Map<String,File> jobIDToReportFile = new HashMap<String,File>(numJobs);
//		for (Iterator<String> it = results.getJobIdToJobName().keySet().iterator();it.hasNext();) {
//			jobId = it.next();
//			File reportFile = new File(outputDir.getAbsolutePath() + File.separator + jobId + File.separator + "report.txt");
//			writeSystemTextReport(results.getOverallEvaluation(jobId), results.getTestSetTrackLists(), results.getPerFoldEvaluation(jobId), classNames, jobId, results.getJobIdToJobName().get(jobId), hierarchyFile!=null, reportFile);
//			jobIDToReportFile.put(jobId, reportFile);
//		}
//		
//		
//		//create tarballs of individual result dirs
//		getLogger().info("Preparing evaluation data tarballs...");
//		Map<String,File> jobIDToTgz = new HashMap<String,File>(results.getJobIdToJobName().size());
//		for (Iterator<String> it = results.getJobIdToJobName().keySet().iterator();it.hasNext();) {
//			jobId = it.next();
//			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(new File(outputDir.getAbsolutePath() + File.separator + jobId)));
//		}
//		
//		
//		//write result HTML pages
//		getLogger().info("Creating result HTML files...");
//		writeHtmlResultPages(usingAHierarchy, results, classNames,
//				jobIDToOverallConfFile, perClassCSV, perFoldCSV,
//				discountedPerClassCSV, discountedPerFoldCSV,
//				friedmanClassTablePNG, friedmanClassTable,
//				friedmanFoldTablePNG, friedmanFoldTable,
//				friedmanDiscountClassTablePNG, friedmanDiscountClassTable,
//				friedmanDiscountFoldTablePNG, friedmanDiscountFoldTable,
//				jobIDToTgz, outputDir);
//    }
//
//	private void writeHtmlResultPages(boolean usingAHierarchy,
//			NemaEvaluationResultSet results, List<String> classNames,
//			Map<String, File> jobIDToOverallConfFile, File perClassCSV,
//			File perFoldCSV, File discountedPerClassCSV,
//			File discountedPerFoldCSV, File friedmanClassTablePNG,
//			File friedmanClassTable, File friedmanFoldTablePNG,
//			File friedmanFoldTable, File friedmanDiscountClassTablePNG,
//			File friedmanDiscountClassTable, File friedmanDiscountFoldTablePNG,
//			File friedmanDiscountFoldTable, Map<String, File> jobIDToTgz,
//			File outputDir) {
//		
//		int numJobs = results.getJobIds().size();
//		boolean performStatSigTests = (numJobs > 1) && this.getPerformMatlabStatSigTests();
//		
//		List<Page> resultPages = new ArrayList<Page>();
//        List<PageItem> items;
//        Page aPage;
//
//        //do intro page to describe task
//        {
//        	items = new ArrayList<PageItem>();
//	        Table descriptionTable = WriteCsvResultFiles.prepTaskTable(results.getTask(),results.getDataset());
//	        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
//	        aPage = new Page("intro", "Introduction", items, false);
//	        resultPages.add(aPage);
//        }
//        
//        //do summary page
//        {
//	        items = new ArrayList<PageItem>();
//	        Table summaryTable = WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),results.getOverallEvalMetricsKeys());
//	        items.add(new TableItem("summary_results", "Summary Results", summaryTable.getColHeaders(), summaryTable.getRows()));
//	        aPage = new Page("summary", "Summary", items, false);
//	        resultPages.add(aPage);
//        }
//
//        //do per class page
//        {
//            items = new ArrayList<PageItem>();
//            Table perClassTable = WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
//            items.add(new TableItem("acc_class", "Accuracy per Class", perClassTable.getColHeaders(), perClassTable.getRows()));
//            if (usingAHierarchy){
//                Table perDiscClassTable = WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.CLASSIFICATION_DISCOUNT_CONFUSION_VECTOR_PERCENT);
//                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Class", perDiscClassTable.getColHeaders(), perDiscClassTable.getRows()));
//            }
//            aPage = new Page("acc_per_class", "Accuracy per Class", items, false);
//            resultPages.add(aPage);
//        }
//
//        //do per fold page
//        {
//            items = new ArrayList<PageItem>();
//            Table perFoldTable = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_ACCURACY);
//            items.add(new TableItem("acc_class", "Accuracy per Fold", perFoldTable.getColHeaders(), perFoldTable.getRows()));
//            if (usingAHierarchy){
//                Table perDiscFoldTable = WriteCsvResultFiles.prepTableDataOverFoldsAndSystems(results.getTestSetTrackLists(), results.getJobIdToPerFoldEvaluation(),results.getJobIdToJobName(),NemaDataConstants.CLASSIFICATION_DISCOUNTED_ACCURACY);
//                items.add(new TableItem("disc_acc_class", "Discounted Accuracy per Fold", perDiscFoldTable.getColHeaders(), perDiscFoldTable.getRows()));
//            }
//            aPage = new Page("acc_per_fold", "Accuracy per Fold", items, false);
//            resultPages.add(aPage);
//        }
//        
//        //do significance tests
//        if (performStatSigTests){
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
//        List<String> sortedJobIDs = new ArrayList<String>(results.getJobIds());
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
//            if (usingAHierarchy){
//                CSVPaths.add(IOUtil.makeRelative(discountedPerClassCSV,outputDir));
//                CSVPaths.add(IOUtil.makeRelative(discountedPerFoldCSV,outputDir));
//            }
//            items.add(new FileListItem("dataCSVs", "CSV result files", CSVPaths));
//
//            //Friedman's tables and plots
//            if (performStatSigTests){
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
//        Page.writeResultPages(results.getTask().getName(), outputDir, resultPages);
//	}
//
//	private File plotAggregatedConfusionForJob(NemaEvaluationResultSet results, String jobID, NemaData aggregateEval) {
//		return plotConfusionMatrix(results, jobID, aggregateEval, "overall", " - overall");
//	}
//
//	private File[] plotConfusionMatricesForAllFolds(NemaEvaluationResultSet results, String jobID, Map<NemaTrackList, NemaData> evals) {
//		int numFolds = results.getTestSetTrackLists().size();
//		File[] foldConfFiles = new File[numFolds];
//		new File(outputDir.getAbsolutePath() + File.separator + jobID).mkdirs();
//		int count = 0;
//		for(Iterator<NemaTrackList> foldIt = results.getTestSetTrackLists().iterator();foldIt.hasNext();){
//			NemaTrackList testSet = foldIt.next();
//			NemaData eval = evals.get(testSet);
//			File plotFile = plotConfusionMatrix(results, jobID, eval, (""+testSet.getFoldNumber()), " - fold " + testSet.getFoldNumber());
//		    foldConfFiles[count++] = plotFile;
//		}
//		return foldConfFiles;
//	}
//
//	private File plotConfusionMatrix(NemaEvaluationResultSet results, String jobID, NemaData eval, String fileNameComp, String titleComp) {
//		double[][] confusion = eval.get2dDoubleArrayMetadata(NemaDataConstants.CLASSIFICATION_CONFUSION_MATRIX_PERCENT);
//		File plotFile = new File(outputDir.getAbsolutePath() + File.separator + jobID + File.separator + fileNameComp + CONF_MAT_PLOT_EXTENSION);
//		ConfusionMatrixPlot plot = new ConfusionMatrixPlot(results.getTask().getName() + " - " + results.getJobName(jobID) + titleComp, (String[])classNames.toArray(new String[classNames.size()]), confusion);
//		plot.writeChartToFile(plotFile, CONF_MAT_WIDTH, CONF_MAT_HEIGHT);
//		return plotFile;
//	}
//	
//}
