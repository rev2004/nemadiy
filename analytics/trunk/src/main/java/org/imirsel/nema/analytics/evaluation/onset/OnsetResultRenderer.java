package org.imirsel.nema.analytics.evaluation.onset;

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
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisFunctionTimestepPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisOnsetPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

public class OnsetResultRenderer extends ResultRendererImpl {
	
	public static final String ONSET_PLOT_EXT = ".onset.png";
	public static final double TARGET_PLOT_RESOLUTION = 0.05;
	
	public OnsetResultRenderer() {
		super();
	}

	public OnsetResultRenderer(File workingDir, File outputDir) {
		super(workingDir, outputDir);
	}
	
	@Override
	public void renderResults(NemaEvaluationResultSet results)
			throws IOException {
		
		getLogger().info("Creating system result directories...");
		Map<String, File> jobIDToResultDir = makeSystemResultDirs(results);
		String jobId;
		
		
		/* Write out summary CSV and per-class CSV */
		getLogger().info("Writing out CSV result files over whole task...");
		List<String> metrics = new ArrayList<String>();
		metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_FMEASURE);
		metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_PRECISION);
		metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_RECALL);
		
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator + "summaryResults.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), metrics), summaryCsv);
		
		jobId = results.getJobIds().iterator().next();
		NemaData aggregateEval = results.getOverallEvaluation(jobId);
		List<String> classNames = (List<String>)aggregateEval.getMetadata(NemaDataConstants.ONSET_DETECTION_CLASSES);
		
		File perClassFMeasureCsv = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassFMeasure.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.ONSET_DETECTION_AVG_FMEASURE_BY_CLASS),perClassFMeasureCsv);
		
		File perClassPrecisionCsv = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassPrecision.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.ONSET_DETECTION_AVG_PRECISION_BY_CLASS),perClassPrecisionCsv);
		
		File perClassRecallCsv = new File(outputDir.getAbsolutePath()+ File.separator + "PerClassRecall.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles.prepTableDataOverClasses(results.getJobIdToOverallEvaluation(),results.getJobIdToJobName(),classNames,NemaDataConstants.ONSET_DETECTION_AVG_RECALL_BY_CLASS),perClassRecallCsv);
		
		/* Write out per track CSV for each system */
		getLogger().info("Writing out per-system result files...");
		Map<String, File> jobIDToPerTrackCSV = writePerTrackSystemResultCSVs(
				results, jobIDToResultDir);

		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = compressResultDirectories(jobIDToResultDir);

		/* Write result HTML pages */
		getLogger().info("Creating result HTML files...");
		writeResultHtmlPages(results, classNames,
				/*jobIDToResultPlotFileList, */summaryCsv, perClassFMeasureCsv, 
				perClassPrecisionCsv, perClassRecallCsv, jobIDToPerTrackCSV,
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
	private void writeResultHtmlPages(NemaEvaluationResultSet results, List<String> classNames,
			/*Map<String, File[]> jobIDToResultPlotFileList, */File summaryCsv, File perClassFMeasureCsv, 
			File perClassPrecisionCsv, File perClassRecallCsv,
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
			
			List<String> metrics = new ArrayList<String>();
			metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_FMEASURE);
			metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_PRECISION);
			metrics.add(NemaDataConstants.ONSET_DETECTION_AVG_RECALL);
			
			Table summaryTable = WriteCsvResultFiles.prepSummaryTable(results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), metrics);
			
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}
		
		/* Do per class page */
		{
			items = new ArrayList<PageItem>();
				
			Table perClassFMeasureTable = WriteCsvResultFiles
					.prepTableDataOverClasses(
							results.getJobIdToOverallEvaluation(),
							results.getJobIdToJobName(),
							classNames,
							NemaDataConstants.ONSET_DETECTION_AVG_FMEASURE_BY_CLASS);

			Table perClassPrecisionTable = WriteCsvResultFiles
					.prepTableDataOverClasses(
							results.getJobIdToOverallEvaluation(),
							results.getJobIdToJobName(),
							classNames,
							NemaDataConstants.ONSET_DETECTION_AVG_PRECISION_BY_CLASS);

			Table perClassRecallTable = WriteCsvResultFiles
					.prepTableDataOverClasses(
							results.getJobIdToOverallEvaluation(),
							results.getJobIdToJobName(),
							classNames,
							NemaDataConstants.ONSET_DETECTION_AVG_RECALL_BY_CLASS);
			
			items.add(new TableItem("fmeasure_class", "F-Measure per Class",
					perClassFMeasureTable.getColHeaders(),
					perClassFMeasureTable.getRows()));
			
			items.add(new TableItem("precision_class", "Pecision per Class",
					perClassPrecisionTable.getColHeaders(),
					perClassPrecisionTable.getRows()));
			
			items.add(new TableItem("recall_class", "Recall per Class",
					perClassRecallTable.getColHeaders(),
					perClassRecallTable.getRows()));
			
			aPage = new Page("results_per_class", "Results per Class", items, false);
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

				/* Plot onset transcription against GT for each track result for each system */
				PageItem[] plots = plotTranscriptionForJob(jobId, results);
				for (int i = 0; i < plots.length; i++) {
					items.add(plots[i]);
				}
				
//				Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
//					jobIDToResultPlotFileList.put(jobId, plotFiles);
//				
//				List<String> plotPathList = new ArrayList<String>(numJobs);
//				File[] plotPaths = jobIDToResultPlotFileList.get(jobId);
//				for (int i = 0; i < plotPaths.length; i++) {
//					plotPathList.add(IOUtil.makeRelative(plotPaths[i],
//							outputDir));
//				}
//				items.add(new FileListItem("plots", "Per track result plots",
//						plotPathList));

				aPage = new Page(results.getJobName(jobId) + "_results", results.getJobName(jobId),
						items, true);
				resultPages.add(aPage);
			}
		}

		/* Do files page */
		{
			items = new ArrayList<PageItem>();

			/* CSVs */
			List<String> CSVPaths = new ArrayList<String>(numJobs+4);
			CSVPaths.add(IOUtil.makeRelative(summaryCsv, outputDir));
			CSVPaths.add(IOUtil.makeRelative(perClassFMeasureCsv, outputDir));
			CSVPaths.add(IOUtil.makeRelative(perClassPrecisionCsv, outputDir));
			CSVPaths.add(IOUtil.makeRelative(perClassRecallCsv, outputDir));
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
	 * Plots the onset transcriptions for each job, for each file
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
				groundtruth = results.getTrackIDToGT().get(result.getId());
				
				if(groundtruth == null){
					getLogger().warning("No ground-truth found for '" + result.getId() + "' to be used in plotting");
				}
				
//				File plotFile = new File(sysDir.getAbsolutePath()
//						+ File.separator + "track_" + result.getId() + MELODY_PLOT_EXT);
//				plotItems.add(plotFile);
	
				double[][] rawData2D = result.get2dDoubleArrayMetadata(NemaDataConstants.ONSET_DETECTION_DATA);
				double[][] rawGtData2D = groundtruth.get2dDoubleArrayMetadata(NemaDataConstants.ONSET_DETECTION_DATA);
				
				String[] annotators = null;
				if (groundtruth.hasMetadata(NemaDataConstants.ONSET_DETECTION_ANNOTATORS)) {
					annotators = groundtruth.getStringArrayMetadata(NemaDataConstants.ONSET_DETECTION_ANNOTATORS);
				}
				//setup time line for for X-axis
				double startTimeSecs = 0.0;
				double endTimeSecs = 0.0;
				
				// Load in the prediction data
				double[] rawData = new double[rawData2D.length];
				for (int i = 0; i < rawData.length; i++) {
					rawData[i] = rawData2D[i][0];
					if (rawData2D[i][0] > endTimeSecs) {
						endTimeSecs = rawData2D[i][0];
					}
				}
					
				//setup hash map for to plot
				Map<String,double[]> series = new HashMap<String, double[]>(rawGtData2D[0].length + 1);
				ArrayList<String> seriesNames = new ArrayList<String>(rawGtData2D[0].length + 1);
				series.put(results.getJobName(jobId), rawData);
				seriesNames.add(results.getJobName(jobId));
				for (int curGT = 0; curGT < rawGtData2D[0].length; curGT++) {
					ArrayList<Double> gtDataArr = new ArrayList<Double>();
					for (int t=0; t<rawGtData2D.length; t++) {
						double onTime = rawGtData2D[t][curGT];
						if (!Double.isNaN(onTime)) {
							gtDataArr.add(new Double(onTime));
							if (onTime > endTimeSecs) {
								endTimeSecs = onTime;
							}
						}
					}
					double[] rawGtData = new double[gtDataArr.size()];
					for(int t = 0; t < rawGtData.length; t++) {
						rawGtData[t] = gtDataArr.get(t).doubleValue();
					}
					if (groundtruth.hasMetadata(NemaDataConstants.ONSET_DETECTION_ANNOTATORS)) {
						String annotator = annotators[curGT];
						series.put(annotator, rawGtData);
						seriesNames.add(annotator);
					} else {
						series.put("Ground-truth " + curGT, rawGtData);
						seriesNames.add("Ground-truth " + curGT);
					}
						
					
				}
				
				
				ProtovisOnsetPlotItem plot = new ProtovisOnsetPlotItem(
						//plotname
						results.getJobName(jobId) + "_transcription_" + result.getId(), 
						//plot caption
						results.getJobName(jobId) + ": Onset transcription for track " + result.getId(), 
						//start time for x axis
						startTimeSecs, 
						//end time for x axis
						endTimeSecs, 
						//map of annotator/system names to the data
						series,
						//ordered list of annotator/system names
						seriesNames);
				plotItems.add(plot);
			}
		}
		return plotItems.toArray(new PageItem[plotItems.size()]);
	}
}
