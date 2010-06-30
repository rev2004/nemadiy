package org.imirsel.nema.analytics.evaluation.tempo;

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
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisBarChartPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.util.IOUtil;

public class TempoResultRenderer extends ResultRendererImpl {

	@Override
	public void renderResults(NemaEvaluationResultSet results) throws IOException {
		getLogger().info("Creating system result directories...");
		Map<String, File> jobIDToResultDir = makeSystemResultDirs(results);

//		/* Plot summary bar plot for each system */
//		getLogger().info("Plotting result summaries...");
//		Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
//		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
//			String jobId = it.next();
//			File sysDir = jobIDToResultDir.get(jobId);
//
//			/* Get summary results to plot */	
//			File[] summaryPlot = new File[1];
//			summaryPlot[0] = plotSummaryForJob(jobId, results.getJobIdToOverallEvaluation(),sysDir);
//
//			jobIDToResultPlotFileList.put(jobId, summaryPlot);
//		}

		/* Write out summary CSV */
		getLogger().info("Writing out CSV result files over whole task...");
		File summaryCsv = writeOverallResultsCSV(results);
		
		/* Write out per track CSV for each system */
		getLogger().info("Writing out per-system result files...");
		Map<String, File> jobIDToPerTrackCSV = writePerTrackSystemResultCSVs(
				results, jobIDToResultDir);

		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = compressResultDirectories(jobIDToResultDir);

		/* Write result HTML pages */
		getLogger().info("Creating result HTML files...");
		writeResultHtmlPages(results, summaryCsv, 
				jobIDToPerTrackCSV, jobIDToTgz, outputDir);
	}

	/**
	 * Writes the result HTML pages for the evaluation of multiple jobs/algorithms
	 * 
	 * @param results					Results to be written to HTML files
	 * @param summaryCsv 				the summary csv file that summarizes all jobs
	 * @param jobIDToPerTrackCSV 		map of jobId to individual per-track results csv files for that job
	 * @param jobIDToTgz 				map of jobId to the tar-balls of individual job results
	 */
	private void writeResultHtmlPages(NemaEvaluationResultSet results, 
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
				items.add(new TableItem(results.getJobIdToJobName().get(jobId) + "_results", results.getJobName(jobId)
						+ " Per Track Results", perTrackTable.getColHeaders(),
						perTrackTable.getRows()));

//				/* Add list of plots */
//				List<String> plotPathList = new ArrayList<String>(numJobs);
//				File[] plotPaths = jobIDToResultPlotFileList.get(jobId);
//				for (int i = 0; i < plotPaths.length; i++) {
//					plotPathList.add(IOUtil.makeRelative(plotPaths[i],
//							outputDir));
//				}
//				items.add(new FileListItem("plots", "System summary plot",
//						plotPathList));

				/* Plot summary result bar chart for each system */
				PageItem plot = plotSummaryForJob(jobId, results);
				items.add(plot);
				
//				
				
				aPage = new Page(results.getJobIdToJobName().get(jobId) + "_results", results.getJobName(jobId),
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
	 * Plots bar chart of the performance scores for a job.
	 * 
	 * @param jobId    the jobId we wish to plot results for.
	 * @param results  The results Object containing the data to plot.
	 * @return         a PageItem that will produce the plot.
	 */
	private static PageItem plotSummaryForJob(String jobId,
			NemaEvaluationResultSet results) {

		NemaData resultSummary = results.getJobIdToOverallEvaluation().get(jobId);

		Map<String,Double> values = new HashMap<String, Double>();
		values.put(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE, resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE));
		values.put(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT, resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT));
		values.put(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT, resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT));
		

		List<String> seriesNames = new ArrayList<String>();
		List<Double> seriesVals = new ArrayList<Double>();
		
		seriesNames.add(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE);
		seriesVals.add(resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_P_SCORE));
		
		seriesNames.add(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT);
		seriesVals.add(resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_ONE_CORRECT));
		
		seriesNames.add(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT);
		seriesVals.add(resultSummary.getDoubleMetadata(NemaDataConstants.TEMPO_EXTRACTION_TWO_CORRECT));
		
		
		
		String name = results.getJobName(jobId) + "_perf_summary";
		String caption = results.getJobName(jobId) + ": Performance summary";
		ProtovisBarChartPlotItem chart = new ProtovisBarChartPlotItem(name, caption, seriesNames, seriesVals);
		
		return chart;
	}
}
