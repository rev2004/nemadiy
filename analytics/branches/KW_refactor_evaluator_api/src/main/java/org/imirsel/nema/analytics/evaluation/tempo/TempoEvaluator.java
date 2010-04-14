package org.imirsel.nema.analytics.evaluation.tempo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.EvaluatorImpl;
import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

public class TempoEvaluator extends EvaluatorImpl {

	/**
	 * Constant definition for tolerance of how much a tempo can be off (ratio).
	 */
	public static final double TOLERANCE = 0.08;

	/**
	 * Constructor
	 */
	public TempoEvaluator(){
		super();
	}

	@Override
	protected void setupEvalMetrics() {
		this.trackEvalMetrics.clear();
		this.trackEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE);
		this.trackEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT);
		this.trackEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT);

		this.overallEvalMetrics.clear();
		this.overallEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE);
		this.overallEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT);
		this.overallEvalMetrics.add(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT);

		//same as overall metrics - single fold experiment format
		this.foldEvalMetrics = this.overallEvalMetrics;
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException {
		String jobId, jobName;
		int numJobs = jobIDToFoldResults.size();

		/* 
		 * Make sure we have same number of sets of results per jobId (i.e. system), 
		 * as defined in the experiment */
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
		renderResults(results,outputDir);

		return results;
	}

	@Override
	public void renderResults(NemaEvaluationResultSet results, File outputDir)
	throws IOException {
		int numJobs = results.getJobIds().size();
		String jobId;
		Map<NemaTrackList, List<NemaData>> sysResults;

		/* Make per system result directories */
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = results.getJobIds().iterator(); it
		.hasNext();) {
			jobId = it.next();

			/* Make a sub-directory for the systems results */
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobId);
			sysDir.mkdirs();
			jobIDToResultDir.put(jobId, sysDir);
		}

		/* Plot summary bar plot for each system */
		List<NemaData> resultList;
		Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			File sysDir = jobIDToResultDir.get(jobId);

			/* Get summary results to plot */	
			File[] summaryPlot = new File[1];
			summaryPlot[0] = plotSummaryForJob(jobId, results.getJobIdToOverallEvaluation(),sysDir);

			jobIDToResultPlotFileList.put(jobId, summaryPlot);
		}

		/* Write out summary CSV */
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator + "allResults.csv");
		WriteCsvResultFiles.writeTableToCsv(
				WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), this.getOverallEvalMetricsKeys()),
				summaryCsv
		);

		/* Write out per track CSV for each system */
		Map<String, File> jobIDToPerTrackCSV = new HashMap<String, File>(numJobs);
		for (Iterator<String> it = results.getJobIds().iterator(); it
		.hasNext();) {
			jobId = it.next();
			sysResults = results.getPerTrackEvaluationAndResults(jobId);

			File sysDir = jobIDToResultDir.get(jobId);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator + "perTrack.csv");
			WriteCsvResultFiles.writeTableToCsv(
					WriteCsvResultFiles.prepTableDataOverTracks(testSets, sysResults, this.getTrackEvalMetricKeys()),
					trackCSV
			);
			jobIDToPerTrackCSV.put(jobId, trackCSV);
		}

		/* Create tar-balls of individual result directories */
		Map<String, File> jobIDToTgz = new HashMap<String, File>(results.getJobIdToJobName()
				.size());
		for (Iterator<String> it = results.getJobIds().iterator(); it
		.hasNext();) {
			jobId = it.next();
			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(jobIDToResultDir.get(jobId)));
		}

		/* Write result HTML pages */
		writeResultHtmlPages(results, jobIDToResultPlotFileList, summaryCsv, 
				jobIDToPerTrackCSV, jobIDToTgz, outputDir);
	}

	/**
	 * Writes the result HTML pages for the evaluation of multiple jobs/algorithms
	 * 
	 * @param results					Results to be written to HTML files
	 * @param jobIDToResultPlotFileList map of a jobId to the results plots for that job
	 * @param summaryCsv 				the summary csv file that summarizes all jobs
	 * @param jobIDToPerTrackCSV 		map of jobId to individual per-track results csv files for that job
	 * @param jobIDToTgz 				map of jobId to the tar-balls of individual job results
	 */
	private void writeResultHtmlPages(NemaEvaluationResultSet results, Map<String,File[]> jobIDToResultPlotFileList, 
			File summaryCsv, Map<String, File> jobIDToPerTrackCSV, Map<String, File> jobIDToTgz, File outputDir) {
		String jobId;
		int numJobs = results.getJobIds().size();

		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;

		Map<NemaTrackList,List<NemaData>> sysResults;
		/* Do intro page to describe task */
		{
			items = new ArrayList<PageItem>();
			Table descriptionTable = WriteCsvResultFiles.prepTaskTable(results.getTask(),
					results.getDataset());
			items.add(new TableItem("task_description", "Task Description",
					descriptionTable.getColHeaders(), descriptionTable
					.getRows()));
			aPage = new Page("intro", "Introduction", items, false);
			resultPages.add(aPage);
		}

		/* Do summary page */
		{
			items = new ArrayList<PageItem>();
			Table summaryTable = WriteCsvResultFiles.prepSummaryTable(
					results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), this.getOverallEvalMetricsKeys());
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}

		/* Do per system pages */
		{
			for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
				jobId = it.next();
				items = new ArrayList<PageItem>();
				sysResults = results.getPerTrackEvaluationAndResults(jobId);

				/* Add per track table */
				Table perTrackTable = WriteCsvResultFiles.prepTableDataOverTracks(testSets, sysResults, this.getTrackEvalMetricKeys());
				items.add(new TableItem(jobId + "_results", results.getJobName(jobId)
						+ " Per Track Results", perTrackTable.getColHeaders(),
						perTrackTable.getRows()));

				/* Add list of plots */
				List<String> plotPathList = new ArrayList<String>(numJobs);
				File[] plotPaths = jobIDToResultPlotFileList.get(jobId);
				for (int i = 0; i < plotPaths.length; i++) {
					plotPathList.add(IOUtil.makeRelative(plotPaths[i],
							outputDir));
				}
				items.add(new FileListItem("plots", "System summary plot",
						plotPathList));

				aPage = new Page(jobId + "_results", results.getJobName(jobId),
						items, true);
				resultPages.add(aPage);
			}
		}

		/* Do files page */
		{
			items = new ArrayList<PageItem>();

			/* CSVs */
			List<String> CSVPaths = new ArrayList<String>(4);
			CSVPaths.add(IOUtil.makeRelative(summaryCsv, outputDir));
			for (Iterator<String> it = results.getJobIds().iterator(); it
			.hasNext();) {
				jobId = it.next();
				CSVPaths.add(IOUtil.makeRelative(jobIDToPerTrackCSV.get(jobId),
						outputDir));
			}

			items.add(new FileListItem("dataCSVs", "CSV result files",
					CSVPaths));

			/* System tar-balls */
			List<String> tarballPaths = new ArrayList<String>(numJobs);
			for (Iterator<String> it = results.getJobIds().iterator(); it
			.hasNext();) {
				jobId = it.next();
				tarballPaths.add(IOUtil.makeRelative(jobIDToTgz.get(jobId),
						outputDir));
			}
			items.add(new FileListItem("tarballs",
					"Per algorithm evaluation tarball", tarballPaths));
			aPage = new Page("files", "Raw data files", items, true);
			resultPages.add(aPage);
		}

		Page.writeResultPages(results.getTask().getName(), outputDir, resultPages);
	}

	/**
	 * Plots the melody transcriptions for each job, for each file
	 * 
	 * @param jobId			the jobId we wish to plot results for
	 * @param resultList	a list of the transcriptions to plot
	 * @param sysDir		directory to store plots in
	 * @return				a file array containing all the plots
	 */
	private static File plotSummaryForJob(String jobId,
			Map<String, NemaData> jobIdToEvaluation, File sysDir) {

		NemaData resultSummary = jobIdToEvaluation.get(jobId);

		/* Plot each result */
		File plotFile = null;

		// TODO actually plot the result

		return plotFile;
	}


	/**
	 * The core evaluation method. Evaluates each file against its ground-truth for a given jobId
	 * @param jobID		the jobId to evaluate
	 * @param theData	the results to evaluate for the jobId. Individual results for each file are added back to this List
	 * @return 			a single NemaData object that contains the average/summary/overall evaluation	
	 */
	@Override
	public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> theData) {
		//count the number of examples returned and search for any missing tracks in the results returned for the fold
		int numExamples = checkFoldResultsAreComplete(jobID, testSet, theData);


		NemaData outObj = new NemaData(jobID);

		NemaData data;
		NemaData gtData;
		double[] rawData;
		double[] rawGtData;

		/* Initialize the overall/summary counters */
		int overallOneTempoCorrect = 0;
		int overallTwoTempoCorrect = 0;
		double overallP = 0.0;

		/* Begin track by track evaluation */
		int numTracks = theData.size();
		for (int x = 0; x < numTracks; x++) {

			data = theData.get(x);
			gtData = trackIDToGT.get(data.getId());
			rawData = data
			.getDoubleArrayMetadata(NemaDataConstants.TEMPO_EXTRACTION_DATA);
			rawGtData = gtData
			.getDoubleArrayMetadata(NemaDataConstants.TEMPO_EXTRACTION_DATA);
			
			int TT1 = 0;
            int TT2 = 0;
			int oneTempoCorrect = 0;
			int twoTempoCorrect = 0;
			double pScore = 0.0;

			//First tempo correct
            if (Math.abs(rawGtData[0] - rawData[0]) < (rawGtData[0] * TOLERANCE)) {
                TT1++;
            }
            //check for case of switched TT1 and TT2
            else if (Math.abs(rawGtData[0] - rawData[1]) < (rawGtData[0] * TOLERANCE)) {
                TT1++;
            }
            
            // Second tempo correct, if TT1 and TT2 were switched this won't
            // be true anyway
            if (Math.abs(rawGtData[1] - rawData[1]) < (rawGtData[1] * TOLERANCE)) {                   
                TT2++;
            }
            // check for switched TT2 and TT1
            else if(Math.abs(rawGtData[1] - rawData[0]) < (rawGtData[1] * TOLERANCE)) { 
                TT2++;
            }
			
            if ((TT1 + TT2) == 1 || (TT1 + TT2) == 2) {
            	oneTempoCorrect = 1;
            	overallOneTempoCorrect++;
            }
            if ((TT1 + TT2) == 2) {
            	twoTempoCorrect = 1;
            	overallTwoTempoCorrect++;
            }
            
            pScore = rawGtData[2] * TT1 + (1 - rawGtData[2]) * TT2;
            overallP += pScore;
            
            

			/* 
			 * Populate each track's NemaData object with the measures. Most of these are binary {0,1} 
			 * except for p. Store them as doubles though for consistency. */
			data.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT, (double)oneTempoCorrect);
			data.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT, (double)twoTempoCorrect);
			data.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE, pScore);


		}

		/* 
		 * Calculate summary/overall evaluation results. Populate a summary NemaData object with 
		 * the evaluations, and return it */		


		outObj.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT, (double)overallOneTempoCorrect/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT, (double)overallTwoTempoCorrect/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE, overallP/numTracks);

		return outObj;
	}
}
