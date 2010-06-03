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
import org.imirsel.nema.analytics.evaluation.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.resultpages.TableItem;
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
		String jobId;
		Map<NemaTrackList, List<NemaData>> sysResults;

		// Make per system result dirs
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		Map<String, List<File>> jobIDToFoldResultDirs = new HashMap<String, List<File>>();

		int numFolds = results.getTestSetTrackLists().size();
		int numJobs = results.getJobIds().size();

		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			// make a sub-dir for the systems results
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator
					+ jobId);
			sysDir.mkdirs();

			// make a sub-dir for each fold
			List<File> foldDirs = new ArrayList<File>(numFolds);
			for (int i = 0; i < numFolds; i++) {
				File foldDir = new File(sysDir.getAbsolutePath()
						+ File.separator + "fold_" + i);
				foldDir.mkdirs();
				foldDirs.add(foldDir);
			}

			jobIDToResultDir.put(jobId, sysDir);
			jobIDToFoldResultDirs.put(jobId, foldDirs);
		}

		// plot chords for each track in each fold
		Map<String, List<File[]>> jobIDToResultPlotFileList = new HashMap<String, List<File[]>>();
		// iterate over systems
		for (Iterator<String> it_systems = results.getJobIds().iterator(); it_systems
				.hasNext();) {
			jobId = it_systems.next();
			getLogger().info("Plotting Chord transcriptions for: " + jobId);
			sysResults = results.getPerTrackEvaluationAndResults(jobId);

			// iterate over folds
			List<File> foldDirs = jobIDToFoldResultDirs.get(jobId);
			List<File[]> plotFolds = new ArrayList<File[]>();
			Iterator<File> it_foldResDir = foldDirs.iterator();

			for (Iterator<NemaTrackList> it_folds = sysResults.keySet()
					.iterator(); it_folds.hasNext();) {
				NemaTrackList testSet = it_folds.next();
				List<NemaData> list = sysResults.get(testSet);

				File[] plots = new File[list.size()];
				File foldDir = it_foldResDir.next();

				// iterate over tracks
				int plotCount = 0;
				for (Iterator<NemaData> it_tracks = list.iterator(); it_tracks
						.hasNext();) {
					NemaData nemaData = it_tracks.next();

					File plotFile = new File(foldDir.getAbsolutePath()
							+ File.separator + jobId + "-" + "fold"
							+ testSet.getFoldNumber() + PLOT_EXT);
					plots[plotCount++] = plotFile;

					// TODO: actually plot the chords

				}
				plotFolds.add(plots);
			}
			jobIDToResultPlotFileList.put(jobId, plotFolds);
		}

		// write out per metric CSV results files
		getLogger().info("Writing out CSV result files over whole task...");
		File overlapCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "overlap.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
				.prepTableDataOverTracksAndSystems(results
						.getTestSetTrackLists(), results
						.getJobIdToPerTrackEvaluationAndResults(), results
						.getJobIdToJobName(),
						NemaDataConstants.CHORD_OVERLAP_RATIO), overlapCsv);

		// write out results summary CSV
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "summaryResults.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
				.prepSummaryTable(results.getJobIdToOverallEvaluation(),
						results.getJobIdToJobName(), results
								.getOverallEvalMetricsKeys()), summaryCsv);

		// write out per system CSVs - per track
		getLogger().info("Writing out CSV result files for each system...");
		Map<String, List<File>> jobIDToCSVs = new HashMap<String, List<File>>(
				numJobs);
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			sysResults = results.getPerTrackEvaluationAndResults(jobId);

			File sysDir = jobIDToResultDir.get(jobId);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator
					+ "per_track_results.csv");
			WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
					.prepTableDataOverTracks(results.getTestSetTrackLists(),
							sysResults, results
									.getTrackEvalMetricsAndResultsKeys()),
					trackCSV);
			ArrayList<File> list = new ArrayList<File>(2);
			list.add(trackCSV);
			jobIDToCSVs.put(jobId, list);
		}

		// write out per system CSVs - per fold
		getLogger().info("Writing out CSV result files for each system...");

		for (Iterator<String> it = results.getJobIdToJobName().keySet()
				.iterator(); it.hasNext();) {
			jobId = it.next();
			Map<NemaTrackList, NemaData> sysFoldResults = results
					.getPerFoldEvaluation(jobId);

			File sysDir = jobIDToResultDir.get(jobId);
			File foldCSV = new File(sysDir.getAbsolutePath() + File.separator
					+ "per_fold_results.csv");
			WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
					.prepTableDataOverFolds(results.getTestSetTrackLists(),
							sysFoldResults, results.getFoldEvalMetricsKeys()),
					foldCSV);
			jobIDToCSVs.get(jobId).add(foldCSV);
		}

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

		// create tarballs of individual result dirs
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = new HashMap<String, File>(results
				.getJobIdToJobName().size());
		for (Iterator<String> it = results.getJobIdToJobName().keySet()
				.iterator(); it.hasNext();) {
			jobId = it.next();
			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(new File(outputDir
					.getAbsolutePath()
					+ File.separator + jobId)));
		}

		// write result HTML pages
		writeHtmlResultPages(performStatSigTests, results, overlapCsv,
				summaryCsv, jobIDToCSVs, friedmanOverlapTablePNG,
				friedmanOverlapTable, friedmanWeightedOverlapTablePNG,
				friedmanWeightedOverlapTable, jobIDToTgz);

	}

	private void writeHtmlResultPages(boolean performStatSigTests,
			NemaEvaluationResultSet results, File overlapCsv, File summaryCsv,
			Map<String, List<File>> jobIDToCSV, File friedmanOverlapTablePNG,
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
				items.add(new TableItem(jobId + "_per_fold", results
						.getJobIdToJobName().get(jobId)
						+ " per fold results", systemFoldTable.getColHeaders(),
						systemFoldTable.getRows()));

				Table systemTrackTable = WriteCsvResultFiles
						.prepTableDataOverTracks(
								results.getTestSetTrackLists(), sysResults,
								results.getTrackEvalMetricsAndResultsKeys());
				items.add(new TableItem(jobId + "_per_track", results
						.getJobIdToJobName().get(jobId)
						+ " per track results", systemTrackTable
						.getColHeaders(), systemTrackTable.getRows()));

				aPage = new Page(jobId, results.getJobIdToJobName().get(jobId),
						items, false);
				resultPages.add(aPage);
			}
		}

		// do significance tests
		if (getPerformMatlabStatSigTests() && performStatSigTests) {
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
			for (Iterator<String> it = jobIDToCSV.keySet().iterator(); it
					.hasNext();) {
				jobId = it.next();
				List<File> files = jobIDToCSV.get(jobId);
				for (Iterator<File> iterator = files.iterator(); iterator
						.hasNext();) {
					perSystemCsvs.add(IOUtil.makeRelative(iterator.next(),
							outputDir));
				}
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
			for (Iterator<String> it = jobIDToCSV.keySet().iterator(); it
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

		Page.writeResultPages(results.getTask().getName(), outputDir,
				resultPages);
	}

}
