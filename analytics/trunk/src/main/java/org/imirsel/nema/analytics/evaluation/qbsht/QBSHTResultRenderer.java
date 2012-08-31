package org.imirsel.nema.analytics.evaluation.qbsht;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.util.IOUtil;

/**
 * 
 */
public class QBSHTResultRenderer extends ResultRendererImpl {

	public QBSHTResultRenderer() {
		super();
	}

	public QBSHTResultRenderer(File workingDir, File outputDir) {
		super(workingDir, outputDir);
	}

	@Override
	public void renderResults(NemaEvaluationResultSet results)
			throws IOException 
	{
		int numJobs = results.getJobIds().size();
		
		getLogger().info("Creating system result directories...");
		Map<String, File> jobIDToResultDir = makeSystemResultDirs(results);

		/* Write out leaderboard CSV file */
		getLogger().info("Writing out leaderboard CSV...");
		File leaderboardCSV = this.writeLeaderBoardCSVFile(NemaDataConstants.QBSHT_QUERY_HITMISS, results, false);
		
		/* Write out summary CSV */
		getLogger().info("Writing out CSV result files over whole task...");
		File summaryCsv = writeOverallResultsCSV(results);
		
		/* Write out per track CSV for each system */
		getLogger().info("Writing out per-system result files...");
		Map<String, File> jobIDToPerTrackCSV = writePerTrackSystemResultCSVs(
				results, jobIDToResultDir);
		
		getLogger().info("Writing out query group CSV...");
		File groupMrrCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "group_mrr.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
				.prepTableDataOverFoldsAndSystems(
						results.getTestSetTrackLists(), 
						results.getJobIdToPerFoldEvaluation(), 
						results.getJobIdToJobName(),
						NemaDataConstants.QBSHT_QUERY_MRR), groupMrrCsv);

		File groupHitMissCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "group_hm.csv");
		WriteCsvResultFiles.writeTableToCsv(WriteCsvResultFiles
				.prepTableDataOverFoldsAndSystems(
						results.getTestSetTrackLists(), 
						results.getJobIdToPerFoldEvaluation(), 
						results.getJobIdToJobName(),
						NemaDataConstants.QBSHT_QUERY_HITMISS), groupHitMissCsv);
		
		File friedmanTableMrrPNG = null;
		File friedmanTableMrr = null;
		File friedmanTableHitMissPNG = null;
		File friedmanTableHitMiss = null;
		/* Do we need to stats tests? */
		if (numJobs > 2 && getPerformMatlabStatSigTests())
		{
			getLogger().info("Performing Friedman's tests in Matlab...");

			File[] tmp = FriedmansAnovaTkHsd.performFriedman(outputDir,
					groupMrrCsv, 0, 1, 1, numJobs, getMatlabPath());
			friedmanTableMrrPNG = tmp[0];
			friedmanTableMrr = tmp[1]; 
			
			tmp = FriedmansAnovaTkHsd.performFriedman(outputDir,
					groupHitMissCsv, 0, 1, 1, numJobs, getMatlabPath());
			friedmanTableHitMissPNG = tmp[0];
			friedmanTableHitMiss = tmp[1]; 
		}
			

		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = compressResultDirectories(jobIDToResultDir);

		/* Write result HTML pages */
		getLogger().info("Creating result HTML files...");
		writeResultHtmlPages(results, summaryCsv, jobIDToPerTrackCSV,
				jobIDToTgz, friedmanTableMrr, friedmanTableMrrPNG, friedmanTableHitMiss, 
				friedmanTableHitMissPNG, outputDir);
		
		getLogger().info("Done.");
		
	}

	@Override
	public void renderAnalysis(NemaEvaluationResultSet results)
			throws IOException 
	{
		/* Write analysis HTML pages */
		getLogger().info("Creating result HTML files...");
		writeHtmlAnalysisPages(results, outputDir);
		
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
	private void writeResultHtmlPages(NemaEvaluationResultSet results,
			File summaryCsv, Map<String, File> jobIDToPerTrackCSV, 
			Map<String, File> jobIDToTgz, File friedmanTableMrrFile, File friedmanTableMrrPNG, 
			File friedmanTableHitMissFile, File friedmanTableHitMissPNG, 
			File outputDir) 
	{
		String jobId;
		Map<NemaTrackList,List<NemaData>> sysResults;
		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;
		int numJobs = results.getJobIds().size();

		TableItem legendTable = createLegendTable(results);
		
		//do intro page to describe task
        resultPages.add(createIntroHtmlPage(results,legendTable));

		/* Do summary page */
		{
			items = new ArrayList<PageItem>();
			items.add(legendTable);
			Table summaryTable = WriteCsvResultFiles.prepSummaryTable(
					results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), results.getOverallEvalMetricsKeys());
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}

		/* Do per system pages */
		for (Iterator<String> it = results.getJobIds().iterator(); it
				.hasNext();) {
			jobId = it.next();
			items = new ArrayList<PageItem>();
			
			TableItem filtLegend = filterLegendTable(legendTable, jobId);
			if(filtLegend != null){
				items.add(filtLegend);
			}
			sysResults = results.getPerTrackEvaluationAndResults(jobId);
			
			/* Add per track table */
			Table perTrackTable = WriteCsvResultFiles.prepTableDataOverTracks(
					results.getTestSetTrackLists(), sysResults, 
					results.getTrackEvalMetricsAndResultsKeys()
				);
			
			items.add(new TableItem(results.getJobName(jobId) + "_results", results.getJobName(jobId)
					+ " Summary Results by Query", perTrackTable.getColHeaders(),
					perTrackTable.getRows()));
			
			aPage = new Page(results.getJobName(jobId) + "_results", results.getJobName(jobId),
					items, true);
			resultPages.add(aPage);
		}
		
		// do per metric page		
		{
			getLogger().info("Creating per-metric page...");
			items = new ArrayList<PageItem>();
			items.add(legendTable);
	
			Table weightedOverlapTablePerFold = WriteCsvResultFiles
					.prepTableDataOverFoldsAndSystems(
							results.getTestSetTrackLists(),
							results.getJobIdToPerFoldEvaluation(),
							results.getJobIdToJobName(),
							NemaDataConstants.QBSHT_QUERY_HITMISS);
			items.add(new TableItem("qbsh_hit_miss_per_group",
					"Simple Count Per Group",
					weightedOverlapTablePerFold.getColHeaders(),
					weightedOverlapTablePerFold.getRows()));
	
			Table overlapTablePerFold = WriteCsvResultFiles
					.prepTableDataOverFoldsAndSystems(results
							.getTestSetTrackLists(), results
							.getJobIdToPerFoldEvaluation(), results
							.getJobIdToJobName(),
							NemaDataConstants.QBSHT_QUERY_MRR);
			items.add(new TableItem("qbsh_mrr_per_group",
					"MRR Per Group", overlapTablePerFold
							.getColHeaders(), overlapTablePerFold.getRows()));
			aPage = new Page("all_system_metrics",
					"Detailed Evaluation Metrics", items, true);
			resultPages.add(aPage);
		}

		/* Do files page */
		{
			items = new ArrayList<PageItem>();

			/* CSVs */
			List<String> CSVPaths = new ArrayList<String>(numJobs+4);
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
		
		// do significance tests
		if (friedmanTableMrrFile != null && friedmanTableMrrPNG != null)
		{
			getLogger().info("Creating significance tests page...");
			items = new ArrayList<PageItem>();
			items.add(legendTable);
			
			items.add(new ImageItem("friedmanTablePNG",
					"Simple Count: Friedman's ANOVA w/ Tukey Kramer HSD (Figure)",
					IOUtil.makeRelative(friedmanTableHitMissPNG, outputDir)));
			
			Table friedmansHitMissTable = createTableFromCsv(friedmanTableHitMissFile);
			items.add(new TableItem("hitmiss_tabl", "Simple Count: Friedman's ANOVA w/ Tukey Kramer HSD (Table)",
					friedmansHitMissTable.getColHeaders(), friedmansHitMissTable.getRows()));
			
			items.add(new ImageItem("friedmanTablePNG",
					"MRR: Friedman's ANOVA w/ Tukey Kramer HSD (Figure)",
					IOUtil.makeRelative(friedmanTableMrrPNG, outputDir)));

			Table friedmansMrrTable = createTableFromCsv(friedmanTableMrrFile);
			items.add(new TableItem("mrr_table", "MRR: Friedman's ANOVA w/ Tukey Kramer HSD (Table)",
					friedmansMrrTable.getColHeaders(), friedmansMrrTable.getRows()));
			
			aPage = new Page("sig_tests", "Significance Tests", items, true);
			resultPages.add(aPage);
		}
		
		Page.writeResultPages(results.getTask().getName(), outputDir, resultPages);
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
	private void writeHtmlAnalysisPages(NemaEvaluationResultSet results, File outputDir) {
		String jobId;
		Map<NemaTrackList,List<NemaData>> sysResults;
		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;;

		TableItem legendTable = createLegendTable(results);
		
		//do intro page to describe task
        resultPages.add(createIntroHtmlPage(results,legendTable));

		/* Do per system pages */
		{
			for (Iterator<String> it = results.getJobIds().iterator(); it
					.hasNext();) {
				jobId = it.next();
				items = new ArrayList<PageItem>();
				TableItem filtLegend = filterLegendTable(legendTable, jobId);
				if(filtLegend != null){
					items.add(filtLegend);
				}
				sysResults = results.getPerTrackEvaluationAndResults(jobId);
				
				aPage = new Page(results.getJobName(jobId) + "_results", results.getJobName(jobId),
						items, true);
				resultPages.add(aPage);
			}
		}
		
		if(results.getJobIds().size() > 1)
		{
			items = new ArrayList<PageItem>();
			items.add(legendTable);
			
			aPage = new Page("comparisonPlots", "Comparative Plots", items, true);
			resultPages.add(aPage);
		}		

		Page.writeResultPages(results.getTask().getName(), outputDir, resultPages);
	}
	
	private Table createTableFromCsv(File csvFile)
	{
		List<String> colHeaders = new ArrayList<String>();
		List<String[]> rows = new ArrayList<String[]>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			String line;
			int row = 0;

			while ((line = br.readLine()) != null)
			{
				String[] fields = line.split(",");
				
				if (row == 0) 
				{	
					for (int i=0; i < fields.length; i++)
						colHeaders.add(fields[i]);
				}
				else
					rows.add(fields);
				row++;
			}
		} catch (IOException e) {
			getLogger().severe("Error reading Friedman's test CSV files");
		}
	    return new Table(colHeaders.toArray(new String[0]), rows);
	}
}
