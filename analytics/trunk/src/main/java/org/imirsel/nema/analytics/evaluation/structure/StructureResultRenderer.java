package org.imirsel.nema.analytics.evaluation.structure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.ResultRendererImpl;
import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
import org.imirsel.nema.analytics.evaluation.chord.NemaChord;
import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisFunctionTimestepPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisSegmentationPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

public class StructureResultRenderer extends ResultRendererImpl {

	public static final String STRUCTURE_PLOT_EXT = ".structure.png";
	public static final double TARGET_PLOT_RESOLUTION = 0.05;
	
	public StructureResultRenderer() {
		super();
	}

	public StructureResultRenderer(File workingDir, File outputDir) {
		super(workingDir, outputDir);
	}
	
	@Override
	public void renderResults(NemaEvaluationResultSet results) throws IOException {
		getLogger().info("Creating system result directories...");
		Map<String, File> jobIDToResultDir = makeSystemResultDirs(results);

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
		writeResultHtmlPages(results, summaryCsv, jobIDToPerTrackCSV,
				jobIDToTgz, outputDir);
		
		getLogger().info("Done.");
		
	}


	/**
	 * Writes the result HTML pages for the evaluation of multiple jobs/algorithms
	 * 
	 * @param results                   The NemaEvaluationResultSet to write results pages for.
	 * @param jobIDToResultPlotFileList map of a jobId to the results plots for that job.
	 * @param summaryCsv 				the summary csv file that summarizes all jobs.
	 * @param jobIDToPerTrackCSV 		map of jobId to individual per-track results csv files for that job.
	 * @param jobIDToTgz 				map of jobId to the tar-balls of individual job results.
	 * @param outputDir                 directory to write the HTML pages to.
	 */
	private void writeResultHtmlPages(NemaEvaluationResultSet results, File summaryCsv,
			Map<String, File> jobIDToPerTrackCSV, Map<String, File> jobIDToTgz, File outputDir) {
		String jobId;
		Map<NemaTrackList,List<NemaData>> sysResults;
		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;
		int numJobs = results.getJobIds().size();

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
			for (Iterator<String> it = results.getJobIds().iterator(); it
					.hasNext();) {
				jobId = it.next();
				items = new ArrayList<PageItem>();
				sysResults = results.getPerTrackEvaluationAndResults(jobId);
				
				/* Add per track table */
				Table perTrackTable = WriteCsvResultFiles.prepTableDataOverTracks(
						results.getTestSetTrackLists(), sysResults, 
						results.getTrackEvalMetricsAndResultsKeys()
					);
				
				items.add(new TableItem(results.getJobName(jobId) + "_results", results.getJobName(jobId)
						+ " Per Track Results", perTrackTable.getColHeaders(),
						perTrackTable.getRows()));

				/* Plot structure segmentation against GT for each track result for each system */
				PageItem[] plots = plotTranscriptionForJob(jobId, results);
				for (int i = 0; i < plots.length; i++) {
					items.add(plots[i]);
				}

				aPage = new Page(results.getJobName(jobId) + "_results", results.getJobName(jobId),
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
	 * Plots the structural segmentations for each job, for each file
	 * 
	 * @param jobId    the jobId we wish to plot results for.
	 * @param results  The results Object containing the data to plot.
	 * @return         an array of page items that will produce the plots.
	 */
	private PageItem[] plotTranscriptionForJob(String jobId,
			NemaEvaluationResultSet results) {
		NemaData result, groundtruth;

		/* Plot each result */
		Map<NemaTrackList, List<NemaData>> job_results = results.getPerTrackEvaluationAndResults(jobId);
		List<PageItem> plotItems = new ArrayList<PageItem>();
		
		for (Iterator<NemaTrackList> foldIt = job_results.keySet().iterator(); foldIt.hasNext();){
			NemaTrackList testSet = foldIt.next();
			for (Iterator<NemaData> iterator = job_results.get(testSet).iterator(); iterator
					.hasNext();) {
				
				
				result = iterator.next();
				getLogger().info("\t\tplotting track " + result.getId() +"...");
				groundtruth = results.getTrackIDToGT().get(result.getId());
				
				if(groundtruth == null){
					getLogger().warning("No ground-truth found for '" + result.getId() + "' to be used in plotting");
				}

				List<NemaSegment> rawGtData = (List<NemaSegment>)groundtruth.getMetadata(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA);
				List<NemaSegment> rawData = (List<NemaSegment>)result.getMetadata(NemaDataConstants.STRUCTURE_SEGMENTATION_DATA);
				
				//setup time line for for X-axis
				double startTimeSecs = 0.0;
				//end at last offset from GT or predictions
				double endTimeSecs = Math.max(rawGtData.get(rawGtData.size()-1).getOffset(), rawData.get(rawData.size()-1).getOffset());
				
				//setup data-series to plot
				Map<String,List<NemaSegment>> series = new HashMap<String, List<NemaSegment>>(2);
				series.put("Prediction", rawData);
				series.put("Ground-truth", rawGtData);
				
				ProtovisSegmentationPlotItem plot = new ProtovisSegmentationPlotItem(
						//plotname
						results.getJobName(jobId) + "_segments_" + result.getId(), 
						//plot caption
						results.getJobName(jobId) + ": Structural segmentation for track " + result.getId(), 
						//start time for x axis
						startTimeSecs, 
						//end time for x axis
						endTimeSecs, 
						//series to plot
						series);
				plotItems.add(plot);
			}
		}
		return plotItems.toArray(new PageItem[plotItems.size()]);
	}

}
