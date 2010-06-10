package org.imirsel.nema.analytics.evaluation.chord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.FriedmansAnovaTkHsd;
import org.imirsel.nema.analytics.evaluation.ResultRendererImpl;
import org.imirsel.nema.analytics.evaluation.WriteCsvResultFiles;
import org.imirsel.nema.analytics.evaluation.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ImageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisFunctionTimestepPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.ProtovisSegmentationPlotItem;
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
import org.imirsel.nema.analytics.evaluation.structure.NemaSegment;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

/**
 * Chord estimation results rendering.
 * 
 * @author kris.west@gmail.com
 * @since 0.2.0
 */
public class ChordResultRenderer extends ResultRendererImpl {

	private static final String PLOT_EXT = ".chords.png";

	public ChordResultRenderer() {
		super();
	}

	public ChordResultRenderer(File workingDir, File outputDir) {
		super(workingDir, outputDir);
	}

	@Override
	public void renderResults(NemaEvaluationResultSet results)
			throws IOException {
		
		int numFolds = results.getTestSetTrackLists().size();
		int numJobs = results.getJobIds().size();
		
		
		getLogger().info("Creating system result directories...");
		Map<String, File> jobIDToResultDir = makeSystemResultDirs(results);
		
//		getLogger().info("Creating per-fold result directories...");
//		Map<String, List<File>> jobIDToFoldResultDirs = makePerFoldSystemResultDirs(
//				jobIDToResultDir, numFolds);
//		
//
//		// plot chords for each track in each fold
//		Map<String, List<File[]>> jobIDToResultPlotFileList = new HashMap<String, List<File[]>>();
//		// iterate over systems
//		for (Iterator<String> it_systems = results.getJobIds().iterator(); it_systems
//				.hasNext();) {
//			String jobId = it_systems.next();
//			getLogger().info("Plotting Chord transcriptions for: " + jobId);
//			Map<NemaTrackList,List<NemaData>> sysResults = results.getPerTrackEvaluationAndResults(jobId);
//
//			// iterate over folds
//			List<File> foldDirs = jobIDToFoldResultDirs.get(jobId);
//			List<File[]> plotFolds = new ArrayList<File[]>();
//			Iterator<File> it_foldResDir = foldDirs.iterator();
//
//			for (Iterator<NemaTrackList> it_folds = sysResults.keySet()
//					.iterator(); it_folds.hasNext();) {
//				NemaTrackList testSet = it_folds.next();
//				List<NemaData> list = sysResults.get(testSet);
//
//				File[] plots = new File[list.size()];
//				File foldDir = it_foldResDir.next();
//
//				// iterate over tracks
//				int plotCount = 0;
//				for (Iterator<NemaData> it_tracks = list.iterator(); it_tracks
//						.hasNext();) {
//					NemaData nemaData = it_tracks.next();
//
//					File plotFile = new File(foldDir.getAbsolutePath()
//							+ File.separator + jobId + "-" + "fold"
//							+ testSet.getFoldNumber() + PLOT_EXT);
//					plots[plotCount++] = plotFile;
//
//					// TODO: actually plot the chords
//
//				}
//				plotFolds.add(plots);
//			}
//			jobIDToResultPlotFileList.put(jobId, plotFolds);
//		}

		/* Write out summary CSV */
		getLogger().info("Writing out CSV result files over whole task...");
		File overlapCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "overlap.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
				.prepTableDataOverTracksAndSystems(results
						.getTestSetTrackLists(), results
						.getJobIdToPerTrackEvaluationAndResults(), results
						.getJobIdToJobName(),
						NemaDataConstants.CHORD_OVERLAP_RATIO), overlapCsv);

		/* Write out per track CSV for each system */
		getLogger().info("Writing out per-system result files...");
		File summaryCsv = writeOverallResultsCSV(results);

		// write out per system CSVs - per track
		getLogger().info("Writing out CSV result files for each system...");
		Map<String, File> jobIDToPerTrackCSV = writePerTrackSystemResultCSVs(
				results, jobIDToResultDir);
		Map<String, File> jobIDToPerFoldCSV = writePerFoldSystemResultCSVs(
				results, jobIDToResultDir);
		
		

		// perform statistical tests
		/* Do we need to stats tests? */
		boolean performStatSigTests = true;
		if (numJobs < 2) {
			performStatSigTests = false;
		}

		File friedmanOverlapTablePNG = null;
		File friedmanOverlapTable = null;
		File friedmanWeightedOverlapTablePNG = null;
		File friedmanWeightedOverlapTable = null;

		if (getPerformMatlabStatSigTests() && performStatSigTests) {
			getLogger().info("Performing Friedman's tests in Matlab...");

			File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir,
					overlapCsv, 0, 2, 1, numJobs, getMatlabPath());
			friedmanOverlapTablePNG = tmp[0];
			friedmanOverlapTable = tmp[1];

			// tmp = performFriedmanTestWithFoldAccuracy(outputDir, perFoldCSV,
			// systemNamesArr);
			tmp = FriedmansAnovaTkHsd.performFriedman(outputDir, overlapCsv, 0,
					2, 1, numJobs, getMatlabPath());
			friedmanWeightedOverlapTablePNG = tmp[0];
			friedmanWeightedOverlapTable = tmp[1];
		}

		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = compressResultDirectories(jobIDToResultDir);


		// write result HTML pages
		writeHtmlResultPages(performStatSigTests, results, overlapCsv,
				summaryCsv, jobIDToPerTrackCSV, jobIDToPerFoldCSV, friedmanOverlapTablePNG,
				friedmanOverlapTable, friedmanWeightedOverlapTablePNG,
				friedmanWeightedOverlapTable, jobIDToTgz);

	}

	

	private void writeHtmlResultPages(boolean performStatSigTests,
			NemaEvaluationResultSet results, File overlapCsv, File summaryCsv,
			Map<String, File> jobIDToPerTrackCSV, 
			Map<String, File> jobIDToPerFoldCSV, File friedmanOverlapTablePNG,
			File friedmanOverlapTable, File friedmanWeightedOverlapTablePNG,
			File friedmanWeightedOverlapTable, Map<String, File> jobIDToTgz) {

		int numJobs = results.getJobIds().size();

		String jobId;
		Map<NemaTrackList, List<NemaData>> sysResults;
		Map<NemaTrackList, NemaData> systemFoldResults;
		getLogger().info("Creating result HTML files...");

		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;

		// do intro page to describe task
		{
			getLogger().info("Creating intro page...");
			items = new ArrayList<PageItem>();
			Table descriptionTable = WriteCsvResultFiles.prepTaskTable(results
					.getTask(), results.getDataset());
			items.add(new TableItem("task_description", "Task Description",
					descriptionTable.getColHeaders(), descriptionTable
							.getRows()));
			aPage = new Page("intro", "Introduction", items, false);
			resultPages.add(aPage);
		}

		// do summary page
		{
			getLogger().info("Creating summary page...");
			items = new ArrayList<PageItem>();
			Table summaryTable = WriteCsvResultFiles.prepSummaryTable(results
					.getJobIdToOverallEvaluation(),
					results.getJobIdToJobName(), results
							.getOverallEvalMetricsKeys());
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}

		// do per metric page
		{
			getLogger().info("Creating per-metric page...");
			items = new ArrayList<PageItem>();

			Table weightedOverlapTablePerFold = WriteCsvResultFiles
					.prepTableDataOverFoldsAndSystems(
							results.getTestSetTrackLists(),
							results.getJobIdToPerFoldEvaluation(),
							results.getJobIdToJobName(),
							NemaDataConstants.CHORD_WEIGHTED_AVERAGE_OVERLAP_RATIO);
			items.add(new TableItem("chord_weighted_overlap_per_fold",
					"Chord Weighted Average Overlap Per Fold",
					weightedOverlapTablePerFold.getColHeaders(),
					weightedOverlapTablePerFold.getRows()));

			Table overlapTablePerFold = WriteCsvResultFiles
					.prepTableDataOverFoldsAndSystems(results
							.getTestSetTrackLists(), results
							.getJobIdToPerFoldEvaluation(), results
							.getJobIdToJobName(),
							NemaDataConstants.CHORD_OVERLAP_RATIO);
			items.add(new TableItem("chord_overlap_per_fold",
					"Chord Average Overlap Per Fold", overlapTablePerFold
							.getColHeaders(), overlapTablePerFold.getRows()));

			Table overlapTable = WriteCsvResultFiles
					.prepTableDataOverTracksAndSystems(results
							.getTestSetTrackLists(), results
							.getJobIdToPerTrackEvaluationAndResults(), results
							.getJobIdToJobName(),
							NemaDataConstants.CHORD_OVERLAP_RATIO);
			items.add(new TableItem("chord_overlap_per_track",
					"Chord Average Overlap Per Track", overlapTable
							.getColHeaders(), overlapTable.getRows()));

			aPage = new Page("all_system_metrics",
					"Detailed Evaluation Metrics", items, true);
			resultPages.add(aPage);
		}

		// do per system pages
		{
			getLogger().info("Creating per-system page...");
			for (Iterator<String> it = results.getJobIds().iterator(); it
					.hasNext();) {
				jobId = it.next();
				items = new ArrayList<PageItem>();
				sysResults = results.getPerTrackEvaluationAndResults(jobId);
				systemFoldResults = results.getPerFoldEvaluation(jobId);

				Table systemFoldTable = WriteCsvResultFiles
						.prepTableDataOverFolds(results.getTestSetTrackLists(),
								systemFoldResults, results
										.getFoldEvalMetricsKeys());
				items.add(new TableItem(results.getJobIdToJobName().get(jobId) + "_per_fold", results
						.getJobIdToJobName().get(jobId)
						+ " per fold results", systemFoldTable.getColHeaders(),
						systemFoldTable.getRows()));

				Table systemTrackTable = WriteCsvResultFiles
						.prepTableDataOverTracks(
								results.getTestSetTrackLists(), sysResults,
								results.getTrackEvalMetricsAndResultsKeys());
				items.add(new TableItem(results.getJobIdToJobName().get(jobId) + "_per_track", results
						.getJobIdToJobName().get(jobId)
						+ " per track results", systemTrackTable
						.getColHeaders(), systemTrackTable.getRows()));

				/* Plot chord transcription against GT for each track result for each system */
				getLogger().info("\tplotting chords for " + results.getJobIdToJobName().get(jobId) +"...");
				PageItem[] plots = plotTranscriptionForJob(jobId, results);
				for (int i = 0; i < plots.length; i++) {
					items.add(plots[i]);
				}
				getLogger().info("\tdone.");
				
				
				aPage = new Page(results.getJobIdToJobName().get(jobId), results.getJobIdToJobName().get(jobId),
						items, true);
				resultPages.add(aPage);
			}
		}

		// do significance tests
		if (getPerformMatlabStatSigTests() && performStatSigTests) {
			getLogger().info("Performing significance tests...");
			items = new ArrayList<PageItem>();
			items.add(new ImageItem("friedmanOverlapTablePNG",
					"Chord Overlap: Friedman's ANOVA w/ Tukey Kramer HSD",
					IOUtil.makeRelative(friedmanOverlapTablePNG, outputDir)));
			items
					.add(new ImageItem(
							"friedmanWeightedOverlapTablePNG",
							"Chord Weighted Overlap: Friedman's ANOVA w/ Tukey Kramer HSD",
							IOUtil.makeRelative(
									friedmanWeightedOverlapTablePNG, outputDir)));

			aPage = new Page("sig_tests", "Significance Tests", items, true);
			resultPages.add(aPage);
		}

		// do files page
		{
			getLogger().info("Creating files page...");
			items = new ArrayList<PageItem>();

			// Overall CSVs
			List<String> overallCsvs = new ArrayList<String>(3);

			overallCsvs.add(IOUtil.makeRelative(summaryCsv, outputDir));
			overallCsvs.add(IOUtil.makeRelative(overlapCsv, outputDir));
			// overallCsvs.add(IOUtil.makeRelative(weightOverlapCsv,outputDir));

			items.add(new FileListItem("overallCSVs",
					"Overall CSV result files", overallCsvs));

			// Per system CSVs
			List<String> perSystemCsvs = new ArrayList<String>(numJobs * 2);
			for (Iterator<String> it = jobIDToPerTrackCSV.keySet().iterator(); it
					.hasNext();) {
				jobId = it.next();
				File pertrack = jobIDToPerTrackCSV.get(jobId);
				perSystemCsvs.add(IOUtil.makeRelative(pertrack, outputDir));
				File perfold = jobIDToPerFoldCSV.get(jobId);
				perSystemCsvs.add(IOUtil.makeRelative(perfold, outputDir));
				
			}
			items.add(new FileListItem("perSystemCSVs",
					"Per-system CSV result files", perSystemCsvs));

			// Friedman's tables and plots
			if (getPerformMatlabStatSigTests() && performStatSigTests) {
				// Friedmans tables
				List<String> sigCSVPaths = new ArrayList<String>(2);
				sigCSVPaths.add(IOUtil.makeRelative(friedmanOverlapTable,
						outputDir));
				sigCSVPaths.add(IOUtil.makeRelative(
						friedmanWeightedOverlapTable, outputDir));

				items.add(new FileListItem("sigCSVs", "Significance test CSVs",
						sigCSVPaths));

				// Friedmans plots
				List<String> sigPNGPaths = new ArrayList<String>(2);
				sigPNGPaths.add(IOUtil.makeRelative(friedmanOverlapTablePNG,
						outputDir));
				sigPNGPaths.add(IOUtil.makeRelative(
						friedmanWeightedOverlapTablePNG, outputDir));

				items.add(new FileListItem("sigPNGs",
						"Significance test plots", sigPNGPaths));
			}

			// System Tarballs
			List<String> tarballPaths = new ArrayList<String>(numJobs);
			for (Iterator<String> it = jobIDToTgz.keySet().iterator(); it
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

		getLogger().info("Writing out pages...");
		Page.writeResultPages(results.getTask().getName(), outputDir,
				resultPages);
		getLogger().info("Done...");
		
	}

	/**
	 * Plots the chord transcriptions for each job, for each file
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
				
//				File plotFile = new File(sysDir.getAbsolutePath()
//						+ File.separator + "track_" + result.getId() + MELODY_PLOT_EXT);
//				plotItems.add(plotFile);
	
				List<NemaChord> rawGtData = (List<NemaChord>)groundtruth.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
				List<NemaChord> rawData = (List<NemaChord>)result.getMetadata(NemaDataConstants.CHORD_LABEL_SEQUENCE);
				
				//setup time line for for X-axis
				double startTimeSecs = 0.0;
				//end at last offset from GT or predictions
				double endTimeSecs = Math.max(rawGtData.get(rawGtData.size()-1).getOffset(), rawData.get(rawData.size()-1).getOffset());
				
				//setup data-series to plot
				Map<String,List<NemaSegment>> series = new HashMap<String, List<NemaSegment>>(2);
				series.put("Prediction", convertChordsToSegments(rawData));
				series.put("Ground-truth", convertChordsToSegments(rawGtData));
				
				ProtovisSegmentationPlotItem plot = new ProtovisSegmentationPlotItem(
						//plotname
						results.getJobName(jobId) + "_chords_" + result.getId(), 
						//plot caption
						results.getJobName(jobId) + ": Chord transcription for track " + result.getId(), 
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
	
	private static List<NemaSegment> convertChordsToSegments(List<NemaChord> chords){
		List<NemaSegment> segs = new ArrayList<NemaSegment>(chords.size());
		ChordConversionUtil util = ChordConversionUtil.getInstance();
		NemaChord chord;
		for (Iterator<NemaChord> iterator = chords.iterator(); iterator.hasNext();) {
			chord = iterator.next();
			segs.add(new NemaSegment(chord.onset,chord.offset,util.convertNoteNumbersToShorthand(chord.notes)));
		}
		return segs;
	}
	
}
