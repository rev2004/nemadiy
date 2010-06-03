package org.imirsel.nema.analytics.evaluation.key;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.ResultRendererImpl;
import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

public class KeyResultRenderer extends ResultRendererImpl {

	public KeyResultRenderer() {
		super();
	}

	public KeyResultRenderer(File workingDir, File outputDir) {
		super(workingDir, outputDir);
	}
	
	@Override
	public void renderResults(NemaEvaluationResultSet results)
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
				WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), results.getOverallEvalMetricsKeys()),
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
					WriteCsvResultFiles.prepTableDataOverTracks(results.getTestSetTrackLists(), sysResults, results.getTrackEvalMetricsAndResultsKeys()),
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
					results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), results.getOverallEvalMetricsKeys());
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
				Table perTrackTable = WriteCsvResultFiles.prepTableDataOverTracks(results.getTestSetTrackLists(), sysResults, results.getTrackEvalMetricsAndResultsKeys());
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

}
