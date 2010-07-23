package org.imirsel.nema.analytics.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisBarChartPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.model.NemaContributor;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.logging.AnalyticsLogFormatter;
import org.imirsel.nema.model.util.IOUtil;

/**
 * Abstract implementation of ResultRenderer.
 * 
 * @author kris.west@gmail.com
 * @since 0.2.0
 *
 */
public abstract class ResultRendererImpl implements ResultRenderer {

	protected Logger _logger;
	protected File workingDir;
	protected File outputDir;
	
	//temporary variables for matlab until we have java implementation of stats tests
	protected boolean performMatlabStatSigTests = true;
	protected File matlabPath = new File("matlab");
	
	public ResultRendererImpl() {
		this._logger = Logger.getLogger(this.getClass().getName());
		this.workingDir = null;
		this.outputDir = null;
	}
	
	public ResultRendererImpl(File workingDir, File outputDir) {
		this._logger = Logger.getLogger(this.getClass().getName());
		this.workingDir = workingDir;
		this.outputDir = outputDir;
	}
	

	/** Create a directory for each system's individual evaluation result files.
	 * 
	 * @param results The results Object to create a directory for.
	 * @return Map of jobID to result directory created.
	 */
	protected Map<String, File> makeSystemResultDirs(
			NemaEvaluationResultSet results) {
		String jobId;
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			/* Make a sub-directory for the systems results */
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + results.getJobName(jobId));
			sysDir.mkdirs();
			jobIDToResultDir.put(jobId, sysDir);
		}
		return jobIDToResultDir;
	}
	
	/** Create a directories for each system's per-fold evaluation result files.
	 * 
	 * @param jobIDToResultDir Map of jobID to each job's result directory.
	 * @param numFolds The number of folds to create directories for.
	 * @return A Map of Job ID to a List of the per-fold results directories.
	 */
	protected Map<String, List<File>> makePerFoldSystemResultDirs(
			Map<String, File> jobIDToResultDir, int numFolds) {
		Map<String, List<File>> jobIDToFoldResultDirs = new HashMap<String, List<File>>();
		for(Iterator<String> it = jobIDToResultDir.keySet().iterator();it.hasNext();){
			String jobId = it.next();
			File dir = jobIDToResultDir.get(jobId);
			
			// make a sub-dir for each fold
			List<File> foldDirs = new ArrayList<File>(numFolds);
			for (int i = 0; i < numFolds; i++) {
				File foldDir = new File(dir.getAbsolutePath()
						+ File.separator + "fold_" + i);
				foldDir.mkdirs();
				foldDirs.add(foldDir);
			}

			jobIDToFoldResultDirs.put(jobId, foldDirs);
		}
		return jobIDToFoldResultDirs;
	}
	
	/**
	 * Default method of writing result CSV files per track, for each system. 
	 * Uses the declared per-track metrics and results keys to produce a 
	 * per-track result table for each system.
	 * 
	 * @param numJobs The number of jobs.
	 * @param jobIDToResultDir Map of job ID to result directory to write to.
	 * @return A map of job ID to the CSV file created for it.
	 * @throws IOException
	 */
	protected Map<String, File> writePerTrackSystemResultCSVs(
			NemaEvaluationResultSet results,
			Map<String, File> jobIDToResultDir) throws IOException {
		String jobId;
		Map<NemaTrackList, List<NemaData>> sysResults;
		Map<String, File> jobIDToPerTrackCSV = new HashMap<String, File>(jobIDToResultDir.size());
		for (Iterator<String> it = results.getJobIds().iterator(); it
				.hasNext();) {
			jobId = it.next();
			sysResults = results.getPerTrackEvaluationAndResults(jobId);
			
			File sysDir = jobIDToResultDir.get(jobId);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator + "per_track_results.csv");
			WriteCsvResultFiles.writeTableToCsv(
					WriteCsvResultFiles.prepTableDataOverTracks(results.getTestSetTrackLists(), sysResults, results.getTrackEvalMetricsAndResultsKeys())
					,trackCSV);
			jobIDToPerTrackCSV.put(jobId, trackCSV);
		}
		return jobIDToPerTrackCSV;
	}
	
	/**
	 * Default method of writing result CSV files per fold, for each system. 
	 * Uses the declared per-fold metrics and results keys to produce a 
	 * per-fold result table for each system.
	 * 
	 * @param numJobs The number of jobs.
	 * @param jobIDToResultDir Map of job ID to result directory to write to.
	 * @return A map of job ID to the CSV file created for it.
	 * @throws IOException
	 */
	protected Map<String, File> writePerFoldSystemResultCSVs(
			NemaEvaluationResultSet results,
			Map<String, File> jobIDToResultDir) throws IOException {
		String jobId;
		Map<NemaTrackList, NemaData>  sysFoldResults;
		Map<String, File> jobIDToPerFoldCSV = new HashMap<String, File>(jobIDToResultDir.size());
		for (Iterator<String> it = results.getJobIds().iterator(); it
				.hasNext();) {
			jobId = it.next();
			sysFoldResults = results
			.getPerFoldEvaluation(jobId);
			
			File sysDir = jobIDToResultDir.get(jobId);
			File foldCSV = new File(sysDir.getAbsolutePath() + File.separator + "per_fold_results.csv");
			WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
					.prepTableDataOverFolds(results.getTestSetTrackLists(),
							sysFoldResults, results.getFoldEvalMetricsKeys()),
					foldCSV);
			jobIDToPerFoldCSV.put(jobId, foldCSV);
		}
		return jobIDToPerFoldCSV;
	}

	/**
	 * Default method of writing overall result summary CSV file. Uses the 
	 * declared overall metric keys to produce a summary result table.
	 * 
	 * @param results Result set to get per-track, per-system result data from.
	 * @return File Object representing the CSV created.
	 * @throws IOException
	 */
	protected File writeOverallResultsCSV(NemaEvaluationResultSet results)
			throws IOException {
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
		WriteCsvResultFiles.writeTableToCsv(
				WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), results.getOverallEvalMetricsKeys()),
				summaryCsv
			);
		return summaryCsv;
	}
	
    /**
     * Compresses result directories into gzipped tarballs (tar.gz).
     * 
     * @param jobIDToResultDir
     * @return Map of job ID to a File Object representing the path to the 
     * gzipped tarball of its per-system results directory.
     */
	protected Map<String, File> compressResultDirectories(
			Map<String, File> jobIDToResultDir) {
		String jobId;
		Map<String, File> jobIDToTgz = new HashMap<String, File>(jobIDToResultDir.size());
		for (Iterator<String> it = jobIDToResultDir.keySet().iterator(); it.hasNext();) {
			jobId = it.next();
			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(jobIDToResultDir.get(jobId)));
		}
		return jobIDToTgz;
	}
	
	/**
	 * Plots bar chart of a performance score over all jobs.
	 * 
	 * @param metric   The metric to summarize as a bar chart.
	 * @param results  The results Object containing the data to plot.
	 * @return         a PageItem that will produce the plot.
	 */
	protected static PageItem plotSummaryOverMetric(
			NemaEvaluationResultSet results, String metric) {

		List<String> seriesNames = new ArrayList<String>();
		List<Double> seriesVals = new ArrayList<Double>();
		for(String jobId:results.getJobIdToOverallEvaluation().keySet()){
			NemaData eval = results.getJobIdToOverallEvaluation().get(jobId);
			seriesNames.add(results.getJobName(jobId));
			seriesVals.add(eval.getDoubleMetadata(metric));
		}
		
		String name = metric + "_summary";
		String caption = metric;
		ProtovisBarChartPlotItem chart = new ProtovisBarChartPlotItem(name, caption, seriesNames, seriesVals);
		
		return chart;
	}

	/**
     * Removes foreign characters and symbols from strings, maps to lowercase
     * and replaces whitespace with underscores. Used to create names for
     * items.
     * 
     * @param name String to be cleaned
     * @return Cleaned version of String.
     */
    public static String cleanName(String name){
        return name.toLowerCase().replaceAll("\\s+", "_").replaceAll("[^a-z0-9]", "");
    }
    
	public abstract void renderResults(NemaEvaluationResultSet results) throws IOException;
	
	public abstract void renderAnalysis(NemaEvaluationResultSet results) throws IOException;

	public void setPerformMatlabStatSigTests(boolean performMatlabStatSigTests) {
		this.performMatlabStatSigTests = performMatlabStatSigTests;
	}

	public boolean getPerformMatlabStatSigTests() {
		return performMatlabStatSigTests;
	}

	public void setMatlabPath(File matlabPath) {
		this.matlabPath = matlabPath;
	}

	public File getMatlabPath() {
		return matlabPath;
	}

	public Logger getLogger() {
		if (_logger == null){
			_logger = Logger.getLogger(this.getClass().getName());
		}
		return _logger;
	}

	public void addLogDestination(PrintStream stream) {
		Handler handler = new StreamHandler(stream, new AnalyticsLogFormatter());
		getLogger().addHandler(handler);
	}

	public void setOutputDir(File outputDir_) throws FileNotFoundException {
		outputDir = outputDir_;
		outputDir.mkdirs();
		if (!outputDir.exists()){
			throw new FileNotFoundException("Output directory " + outputDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}

	public void setWorkingDir(File workingDir_) throws FileNotFoundException {
		workingDir = workingDir_;
		workingDir.mkdirs();
		if (!workingDir.exists()){
			throw new FileNotFoundException("Working directory " + workingDir.getAbsolutePath() + " was not found and could not be created!");
		}
	}
	
	protected Page createIntroHtmlPage(NemaEvaluationResultSet results){
    	List<PageItem> items = new ArrayList<PageItem>();
        Table descriptionTable = WriteCsvResultFiles.prepTaskTable(results.getTask(),results.getDataset());
        items.add(new TableItem("task_description", "Task Description", descriptionTable.getColHeaders(), descriptionTable.getRows()));
        
        
        Map<String,NemaSubmission> subDetails = results.getJobIdToSubmissionDetails();
        List<String[]> rows = new ArrayList<String[]>();
        String[] colNames;
        if (subDetails == null){
        	colNames = new String[2];
    	    colNames[0] = "Job ID";
    	    colNames[1] = "Job name";
    	    List<String> jobIDs = new ArrayList<String>(results.getJobIds());
    	    Collections.sort(jobIDs);
    	    for(String job:jobIDs){
    	    	rows.add(new String[]{job,results.getJobName(job)});
    	    }
        }else{
        	colNames = new String[4];
    	    colNames[0] = "Submission code";
    	    colNames[1] = "Submission name";
    	    colNames[2] = "Abstract PDF";
    	    colNames[3] = "Contributors";
    	
    	    System.out.println("\n\nCreating legend table\n\n");
    	    
        	List<String> jobIDs = new ArrayList<String>(results.getJobIds());
    	    Collections.sort(jobIDs);
    	    for(String job:jobIDs){
    	    	NemaSubmission sub = subDetails.get(job);
    	    	if(sub == null){
    	    		rows.add(new String[]{job,results.getJobName(job),"",""});
    	    	}else{
    	    		String contrib = "";
    	    		for(Iterator<NemaContributor> personIt = sub.getContributors().iterator(); personIt.hasNext();){
    	    			NemaContributor person = personIt.next();
    	    			contrib += "<a href=\\\"" + person.getAffiliationUrl() + "\\\">" + 
    	    			person.getFirstName() + " " + person.getLastName() + "</a>";
    	    			if(personIt.hasNext()){
    	    				contrib += ", ";
    	    			}
    	    		}
    	    		
    	    		rows.add(new String[]{job,sub.getSubmissionName(),"<a href=\"" + sub.getAbstractUrl() + "\">PDF</a>",contrib});
    	    	}
    	    }
    	    
        }
        items.add(new TableItem("legend", "Legend", colNames, rows));
        
	    
        return new Page("intro", "Introduction", items, false);
    }
	
	/**
	 * Plots bar chart of an overall metric for all jobs.
	 * 
	 * @param jobId    the jobId we wish to plot results for.
	 * @param results  The results Object containing the data to plot.
	 * @return         a PageItem that will produce the plot.
	 */
	protected static PageItem plotOverallMetricBarChart(String metric,
			NemaEvaluationResultSet results, String name, String caption) {

		List<String> seriesNames = new ArrayList<String>();
		List<Double> seriesVals = new ArrayList<Double>();
		
		for(String jobId:results.getJobIds()){
			seriesNames.add(results.getJobName(jobId));
			seriesVals.add(results.getJobIdToOverallEvaluation().get(jobId).getDoubleMetadata(metric));
		}
		
		return new ProtovisBarChartPlotItem(name, caption, seriesNames, seriesVals);
	}
}
