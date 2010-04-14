package org.imirsel.nema.analytics.evaluation.melody;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.imirsel.nema.analytics.evaluation.util.resultpages.*;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaTrackList;

/**
 * Melody (single F0) evaluation and result rendering.
 * 
 * @author afe405@gmail.com
 * @author kris.west@gmail.com
 * @since 0.1.0
 *
 */
public class MelodyEvaluator extends EvaluatorImpl {

	public static final String MELODY_PLOT_EXT = ".png";
	private static final int LOWER_BOUND = 220;
	private static final int UPPER_BOUND = 440;
	private static final double TOLERANCE = 0.5;

	/**
	 * Constructor (no arg - task, dataset, output and working dirs, training
	 * and test sets must be set manually).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public MelodyEvaluator() {
		super();
	}
	
	protected void setupEvalMetrics() {
		this.trackEvalMetrics.clear();
		this.trackEvalMetrics.add(NemaDataConstants.MELODY_OVERALL_ACCURACY);
		this.trackEvalMetrics.add(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY);
		this.trackEvalMetrics.add(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY);
		this.trackEvalMetrics.add(NemaDataConstants.MELODY_VOICING_RECALL);
		this.trackEvalMetrics.add(NemaDataConstants.MELODY_VOICING_FALSE_ALARM);
		
		this.overallEvalMetrics.clear();
		this.overallEvalMetrics.add(NemaDataConstants.MELODY_OVERALL_ACCURACY);
		this.overallEvalMetrics.add(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY);
		this.overallEvalMetrics.add(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY);
		this.overallEvalMetrics.add(NemaDataConstants.MELODY_VOICING_RECALL);
		this.overallEvalMetrics.add(NemaDataConstants.MELODY_VOICING_FALSE_ALARM);
		
		//same as overall metrics - single fold experiment format
		this.foldEvalMetrics = this.overallEvalMetrics;
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public NemaEvaluationResultSet evaluate() throws IllegalArgumentException,
			IOException {
		String jobId;
		String jobName;
		int numJobs = jobIDToFoldResults.size();
		
		/* Check all systems have just one result set */
		Map<NemaTrackList,List<NemaData>> sysResults;
		
		/* 
		 * Make sure we only have one set of results per jobId (i.e. system), 
		 * as this is not a cross-fold validated experiment */
		checkFolds();
		
		/* prepare NemaEvaluationResultSet*/
		NemaEvaluationResultSet results = getEmptyEvaluationResultSet();
		
		{
			/* Perform the evaluations on all jobIds (systems) */
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
		renderResults(results, outputDir);

		return results;
	}

	public void renderResults(NemaEvaluationResultSet results, File outputDir) throws IOException {
		String jobId;
		int numJobs = results.getJobIds().size();
		Map<NemaTrackList, List<NemaData>> sysResults;
		/* Make per system result directories */
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			/* Make a sub-directory for the systems results */
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator + jobId);
			sysDir.mkdirs();
			jobIDToResultDir.put(jobId, sysDir);
		}

		/* Plot melody transcription against GT for each track result for each system */
		Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			File sysDir = jobIDToResultDir.get(jobId);
		
			/* Get results to plot */
			sysResults = results.getPerTrackEvaluationAndResults(jobId);
			File[] plotFiles = plotTranscriptionForJob(jobId, sysResults, sysDir);
			
			jobIDToResultPlotFileList.put(jobId, plotFiles);
		}

		/* Write out summary CSV */
		getLogger().info("Writing out CSV result files over whole task...");
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
					WriteCsvResultFiles.prepTableDataOverTracks(testSets, sysResults, this.getTrackEvalMetricKeys())
					,trackCSV);
			jobIDToPerTrackCSV.put(jobId, trackCSV);
		}


		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = new HashMap<String, File>(numJobs);
		for (Iterator<String> it = results.getJobIds().iterator(); it.hasNext();) {
			jobId = it.next();
			jobIDToTgz.put(jobId, IOUtil.tarAndGzip(jobIDToResultDir.get(jobId)));
		}

		/* Write result HTML pages */
		getLogger().info("Creating result HTML files...");

		writeResultHtmlPages(results,
				jobIDToResultPlotFileList, summaryCsv, jobIDToPerTrackCSV,
				jobIDToTgz, outputDir);
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
			Map<String, File[]> jobIDToResultPlotFileList, File summaryCsv,
			Map<String, File> jobIDToPerTrackCSV, Map<String, File> jobIDToTgz, File outputDir) {
		String jobId;
		Map<NemaTrackList,List<NemaData>> sysResults;
		List<NemaData> resultList;
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
					results.getJobIdToOverallEvaluation(), results.getJobIdToJobName(), this.getOverallEvalMetricsKeys());
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
						this.getTrackEvalMetricKeys()
					);
				
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
				items.add(new FileListItem("plots", "Per track result plots",
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
	 * @param jobId    the jobId we wish to plot results for
	 * @param results  a map of fold to the transcriptions to plot
	 * @param sysDir   directory to store plots in
	 * @return         a file array containing all the plots
	 */
	private File[] plotTranscriptionForJob(String jobId,
			Map<NemaTrackList, List<NemaData>> results, File sysDir) {
		NemaData result;

		/* Plot each result */
		List<File> plotFiles = new ArrayList<File>();
		
		int idx = 0;
		for (Iterator<NemaTrackList> foldIt = results.keySet().iterator(); foldIt.hasNext();){
			NemaTrackList testSet = foldIt.next();
			for (Iterator<NemaData> iterator = results.get(testSet).iterator(); iterator
					.hasNext();) {
				result = iterator.next();
				plotFiles.add(new File(sysDir.getAbsolutePath()
						+ File.separator + jobId + File.separator
						+ "fold_" + testSet.getFoldNumber() + MELODY_PLOT_EXT));
	
				// TODO actually plot the result
			}
		}
		return plotFiles.toArray(new File[plotFiles.size()]);
	}

	public NemaData evaluateResultFold(String jobID, NemaTrackList testSet, List<NemaData> theData) {
		//count the number of examples returned and search for any missing tracks in the results returned for the fold
    	int numExamples = checkFoldResultsAreComplete(jobID, testSet, theData);
		
		NemaData outObj = new NemaData(jobID);

		NemaData data;
		NemaData gtData;
		double[][] rawData;
		double[][] rawGtData;
		
		double vxRecallOvarall = 0.0;
		double vxFalseAlarmOverall = 0.0;
		double rawPitchOverall = 0.0;
		double rawChromaOverall = 0.0;
		double accuracyOverall = 0.0;

		for (int x = 0; x < theData.size(); x++) {
			
			/* Pull the algorithm and ground-truth raw data */
			data = theData.get(x);
			gtData = trackIDToGT.get(data.getId());
			rawData = data
					.get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);
			rawGtData = gtData
					.get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);

			/* Initialize frame-by-frame counters for the evaluation measures */
			int correct = 0;
			int nomelcorrect = 0;
			int incorrect = 0;
			int falsePositives = 0;
			int falseNegatives = 0;
			int falseNegCorF0 = 0;
			int octaveCorrect = 0;
			int octaveNoMelCorrect = 0;
			int octaveIncorrect = 0;
			int octaveFalsePositives = 0;
			int octaveFalseNegatives = 0;
			int octaveFalseNegCorF0 = 0;
			
			/* Perform evaluation */
			int tot = rawGtData.length;
			for (int t = 0; t < tot; t++) {
				double gtF0 = rawGtData[t][1];
				double detF0;
				if (t >= rawData.length) {
					detF0 = 0.0;
				} else {
					detF0 = rawData[t][1];
				}

				if ((gtF0 == 0) && (detF0 > 0)) {
					falsePositives++;
				} else if ((detF0 <= 0) && (gtF0 != 0)) {
					falseNegatives++;
					if ((-detF0 > (gtF0 / Math.pow(Math.pow(2.0, TOLERANCE),
							(1.0 / 12.0))))
							&& (-detF0 < (gtF0 * Math.pow(Math.pow(2.0,
									TOLERANCE), (1.0 / 12.0))))) {
						falseNegCorF0++;
					}
				} else if ((detF0 <= 0) && (gtF0 == 0)) {
					nomelcorrect++;
				} else if ((detF0 > (gtF0 / Math.pow(Math.pow(2.0, TOLERANCE),
						(1.0 / 12.0))))
						&& (detF0 < (gtF0 * Math.pow(Math.pow(2.0, TOLERANCE),
								(1.0 / 12.0))))) {
					correct++;
				} else {
					incorrect++;
				}
			}

			/* Do one octave evaluation */
			for (int t = 0; t < tot; t++) {
				double gtF0 = rawGtData[t][1];
				double detF0;
				if (t >= rawData.length) {
					detF0 = 0.0;
				} else {
					detF0 = rawData[t][1];
				}

				/* Map to one octave */
				if (gtF0 != 0) {
					while (!((gtF0 >= LOWER_BOUND) && (gtF0 < UPPER_BOUND))) {
						gtF0 = (gtF0 >= UPPER_BOUND) ? (gtF0 / 2) : (gtF0 * 2);
					}
				}
				if (detF0 != 0) {
					if (detF0 > 0) {
						while (!((detF0 >= LOWER_BOUND) && (detF0 < UPPER_BOUND))) {
							detF0 = (detF0 >= UPPER_BOUND) ? (detF0 / 2)
									: (detF0 * 2);
						}
					} else {
						while (!((-detF0 >= LOWER_BOUND) && (-detF0 < UPPER_BOUND))) {
							detF0 = (-detF0 >= UPPER_BOUND) ? (detF0 / 2)
									: (detF0 * 2);
						}
					}
				}

				if ((gtF0 == 0) && (detF0 > 0)) {
					octaveFalsePositives++;
				} else if ((detF0 <= 0) && (gtF0 == 0)) {
					octaveNoMelCorrect++;
				} else if ((detF0 <= 0) && (gtF0 != 0)) {
					octaveFalseNegatives++;
					if ((-detF0 > (gtF0 / Math.pow(Math.pow(2.0, TOLERANCE),
							(1.0 / 12.0))))
							&& (-detF0 < (gtF0 * Math.pow(Math.pow(2.0,
									TOLERANCE), (1.0 / 12.0))))) {
						octaveFalseNegCorF0++;
					}
					/*
					 * The following two else if's test a certain pathological
					 * case in octave mapping. For example in mapping to the
					 * range of [220, 440), a ground truth of 438 will stay 438
					 * while a prediction of 442 will be mapped to 221.
					 * Therefore we have to check if either double the
					 * prediction or double the ground truth is within
					 * TOLERANCE. This case will only arise in cases around the
					 * pitch 'A' for these bounds.
					 */
					else if ((-2.0 * detF0 > (gtF0 / Math.pow(Math.pow(2.0,
							TOLERANCE), (1.0 / 12.0))))
							&& (-2.0 * detF0 < (gtF0 * Math.pow(Math.pow(2.0,
									TOLERANCE), (1.0 / 12.0))))) {
						octaveFalseNegCorF0++;
					} else if ((-detF0 > (2.0 * gtF0 / Math.pow(Math.pow(2.0,
							TOLERANCE), (1.0 / 12.0))))
							&& (-detF0 < (2.0 * gtF0 * Math.pow(Math.pow(2.0,
									TOLERANCE), (1.0 / 12.0))))) {
						octaveFalseNegCorF0++;
					}
				} else if ((detF0 > (gtF0 / Math.pow(Math.pow(2.0, TOLERANCE),
						(1.0 / 12.0))))
						&& (detF0 < (gtF0 * Math.pow(Math.pow(2.0, TOLERANCE),
								(1.0 / 12.0))))) {
					octaveCorrect++;
				}
				/*
				 * The following two else if's test a certain pathological case
				 * in octave mapping. For example in mapping to the range of
				 * [220, 440), a ground truth of 438 will stay 438 while a
				 * prediction of 442 will be mapped to 221. Therefore we have to
				 * check if either double the prediction or double the ground
				 * truth is within TOLERANCE. This case will only arise in cases
				 * around the pitch 'A' for these bounds.
				 */
				else if ((2.0 * detF0 > (gtF0 / Math.pow(Math.pow(2.0,
						TOLERANCE), (1.0 / 12.0))))
						&& (2.0 * detF0 < (gtF0 * Math.pow(Math.pow(2.0,
								TOLERANCE), (1.0 / 12.0))))) {
					octaveCorrect++;
				} else if ((detF0 > (2.0 * gtF0 / Math.pow(Math.pow(2.0,
						TOLERANCE), (1.0 / 12.0))))
						&& (detF0 < (2.0 * gtF0 * Math.pow(Math.pow(2.0,
								TOLERANCE), (1.0 / 12.0))))) {
					octaveCorrect++;
				} else {
					octaveIncorrect++;
				}
			} 
			
			/* Calculate the evaluation measures for this track*/
			int gv = correct + incorrect + falseNegatives;
			int gu = nomelcorrect + falsePositives;
			double vxRecall = ((double) correct + (double) incorrect)
					/ ((double) gv);
			double vxFalseAlarm = (Math.max(0.001, (double) falsePositives))
					/ (Math.max(0.001, (double) gu));
			double rawPitch = ((double) correct + (double) falseNegCorF0) 
					/ ((double) gv); 
			double rawChroma = ((double) octaveCorrect + (double) octaveFalseNegCorF0)
					/ ((double) gv);
			double accuracy = ((double) correct + (double) nomelcorrect)
					/ ((double) tot);
			
			vxRecallOvarall += vxRecall;
			vxFalseAlarmOverall += vxFalseAlarm;
			rawPitchOverall += rawPitch;
			rawChromaOverall += rawChroma;
			accuracyOverall += accuracy;	
			
			data.setMetadata(NemaDataConstants.MELODY_OVERALL_ACCURACY, accuracy);
			data.setMetadata(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY, rawPitch);
			data.setMetadata(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY, rawChroma);
			data.setMetadata(NemaDataConstants.MELODY_VOICING_RECALL, vxRecall);
			data.setMetadata(NemaDataConstants.MELODY_VOICING_FALSE_ALARM, vxFalseAlarm);
					

		}
		
		/* 
		 * Calculate summary/overall evaluation results. Populate a summary NemaData object with 
		 * the evaluations, and return it */
		
		vxRecallOvarall = vxRecallOvarall / ((double) numExamples);
		vxFalseAlarmOverall = vxFalseAlarmOverall / ((double) numExamples);
		rawPitchOverall = rawPitchOverall / ((double) numExamples);
		rawChromaOverall = rawChromaOverall / ((double) numExamples);
		accuracyOverall = accuracyOverall / ((double) numExamples);
		
		outObj.setMetadata(NemaDataConstants.MELODY_OVERALL_ACCURACY, accuracyOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY, rawPitchOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY, rawChromaOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_VOICING_RECALL, vxRecallOvarall);
		outObj.setMetadata(NemaDataConstants.MELODY_VOICING_FALSE_ALARM, vxFalseAlarmOverall);

		return outObj;
	}
}
