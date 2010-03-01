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
import org.imirsel.nema.analytics.evaluation.util.resultpages.*;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;

public class MelodyEvaluator extends EvaluatorImpl {

	public static final String MELODY_PLOT_EXT = ".png";
	private static final int LOWER_BOUND = 220;
	private static final int UPPER_BOUND = 440;
	private static final double TOLERANCE = 0.5;

	public MelodyEvaluator(NemaTask task_, NemaDataset dataset_,
			File outputDir_, File workingDir_) throws FileNotFoundException,
			IOException {
		super(workingDir_, outputDir_, task_, dataset_);

	}

	@Override
	public Map<String, NemaData> evaluate() throws IllegalArgumentException,
			IOException {
		String jobID;
		int numJobs = jobIDToFoldResults.size();
		List<List<NemaData>> sysResults;
		
		// Make sure we only have one set of results per jobId (i.e. system), 
		// as this is not a cross-fold validated experiment
		checkFolds();
		
		// Perform the evaluations on all jobIds (systems)
		Map<String, NemaData> jobIdToEvaluation = new HashMap<String, NemaData>(
				numJobs);
		for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			_logger.info("Evaluating experiment for jobID: " + jobID);
			sysResults = jobIDToFoldResults.get(jobID);
			NemaData result = evaluateResult(jobID, sysResults.get(0));
			jobIdToEvaluation.put(jobID, result);
		}

		// Make per system result dirs
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = jobIdToEvaluation.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			// make a sub-dir for the systems results
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator
					+ jobID);
			sysDir.mkdirs();
			jobIDToResultDir.put(jobID, sysDir);
		}

		// plot melody transcription against GT for each track result for each
		// system
		List<NemaData> resultList;
		Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
		for (Iterator<String> it = jobIdToEvaluation.keySet().iterator(); it.hasNext();) {
			jobID = it.next();
			File sysDir = jobIDToResultDir.get(jobID);
		
			// get results to plot
			sysResults = jobIDToFoldResults.get(jobID);
			resultList = sysResults.get(0);
		
			File[] plotFiles = plotTranscriptionForJob(jobID, resultList,
					sysDir);
			
			jobIDToResultPlotFileList.put(jobID, plotFiles);
		}

		// write out summary CSV
		_logger.info("Writing out CSV result files over whole task...");
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "allResults.csv");
		WriteMelodyResultFiles.prepSummaryCsv(jobIdToEvaluation, jobIDToName,
				summaryCsv);

		// write out per track CSV for each system
		Map<String, File> jobIDToPerTrackCSV = writePerTrackCsvFiles(numJobs,
				jobIDToResultDir);

		// create tarballs of individual result dirs
		_logger.info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = new HashMap<String, File>(jobIDToName
				.size());
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			jobIDToTgz.put(jobID, IOUtil
					.tarAndGzip(jobIDToResultDir.get(jobID)));
		}

		// write result HTML pages
		_logger.info("Creating result HTML files...");

		writeResultHtmlPages(numJobs, jobIdToEvaluation,
				jobIDToResultPlotFileList, summaryCsv, jobIDToPerTrackCSV,
				jobIDToTgz);

		return jobIdToEvaluation;
	}

	private void writeResultHtmlPages(int numJobs,
			Map<String, NemaData> jobIdToEvaluation,
			Map<String, File[]> jobIDToResultPlotFileList, File summaryCsv,
			Map<String, File> jobIDToPerTrackCSV, Map<String, File> jobIDToTgz) {
		String jobID;
		List<List<NemaData>> sysResults;
		List<NemaData> resultList;
		List<Page> resultPages = new ArrayList<Page>();
		List<PageItem> items;
		Page aPage;

		// do intro page to describe task
		{
			items = new ArrayList<PageItem>();
			Table descriptionTable = WriteMelodyResultFiles.prepTaskTable(task,
					dataset);
			items.add(new TableItem("task_description", "Task Description",
					descriptionTable.getColHeaders(), descriptionTable
							.getRows()));
			aPage = new Page("intro", "Introduction", items, false);
			resultPages.add(aPage);
		}

		// do summary page
		{
			items = new ArrayList<PageItem>();
			Table summaryTable = WriteMelodyResultFiles.prepSummaryTableData(
					jobIdToEvaluation, jobIDToName);
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}

		// do per system pages
		{
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it
					.hasNext();) {
				jobID = it.next();
				items = new ArrayList<PageItem>();
				sysResults = jobIDToFoldResults.get(jobID);
				resultList = sysResults.get(0);
				// add per track table
				Table perTrackTable = WriteMelodyResultFiles
						.prepPerTrackTableData(resultList,
								jobIDToName.get(jobID));
				items.add(new TableItem(jobID + "_results", jobIDToName
						.get(jobID)
						+ " Per Track Results", perTrackTable.getColHeaders(),
						perTrackTable.getRows()));

				// add list of plots
				List<String> plotPathList = new ArrayList<String>(numJobs);
				File[] plotPaths = jobIDToResultPlotFileList.get(jobID);
				for (int i = 0; i < plotPaths.length; i++) {
					plotPathList.add(IOUtil.makeRelative(plotPaths[i],
							outputDir));
				}
				items.add(new FileListItem("plots", "Per track result plots",
						plotPathList));

				aPage = new Page(jobID + "_results", jobIDToName.get(jobID),
						items, true);
				resultPages.add(aPage);
			}
		}

		// do files page
		{
			items = new ArrayList<PageItem>();

			// CSVs
			List<String> CSVPaths = new ArrayList<String>(4);
			CSVPaths.add(IOUtil.makeRelative(summaryCsv, outputDir));
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it
					.hasNext();) {
				jobID = it.next();
				CSVPaths.add(IOUtil.makeRelative(jobIDToPerTrackCSV.get(jobID),
						outputDir));
			}

			items.add(new FileListItem("dataCSVs", "CSV result files",
							CSVPaths));

			// System Tarballs
			List<String> tarballPaths = new ArrayList<String>(numJobs);
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it
					.hasNext();) {
				jobID = it.next();
				tarballPaths.add(IOUtil.makeRelative(jobIDToTgz.get(jobID),
						outputDir));
			}
			items.add(new FileListItem("tarballs",
					"Per algorithm evaluation tarball", tarballPaths));
			aPage = new Page("files", "Raw data files", items, true);
			resultPages.add(aPage);
		}

		Page.writeResultPages(task.getName(), outputDir, resultPages);
	}

	private void checkFolds() {
		String jobID;
		List<List<NemaData>> sysResults;
		for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			sysResults = jobIDToFoldResults.get(jobID);
			if (sysResults.size() != 1) {
				throw new IllegalArgumentException(
						"The number of folds ("
								+ sysResults.size()
								+ ") detected for system ID: "
								+ jobID
								+ ", name: "
								+ jobIDToName.get(jobID)
								+ " is not 1 (Melody Evaluation is not cross-validated)!");
			}
		}
	}

	/**
	 * 
	 * @param numJobs
	 * @param jobIDToResultDir
	 * @return
	 * @throws IOException
	 */
	private Map<String, File> writePerTrackCsvFiles(int numJobs,
			Map<String, File> jobIDToResultDir) throws IOException {
		String jobID;
		List<List<NemaData>> sysResults;
		List<NemaData> resultList;
		Map<String, File> jobIDToPerTrackCSV = new HashMap<String, File>(
				numJobs);
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			sysResults = jobIDToFoldResults.get(jobID);
			resultList = sysResults.get(0);
			File sysDir = jobIDToResultDir.get(jobID);
			File trackCsv = new File(sysDir.getAbsolutePath() + File.separator
					+ jobID + File.separator + "perTrack.csv");
			WriteMelodyResultFiles.prepPerTrackCsv(
					resultList, jobIDToName.get(jobID),
					trackCsv);
			jobIDToPerTrackCSV.put(jobID, trackCsv);
		}
		return jobIDToPerTrackCSV;
	}


	private File[] plotTranscriptionForJob(String jobID,
			List<NemaData> resultList, File sysDir) {
		NemaData result;
		// plot each result
		File[] plotFiles = new File[resultList.size()];
		
		int idx = 0;
		for (Iterator<NemaData> iterator = resultList.iterator(); iterator
				.hasNext();) {
			result = iterator.next();
			plotFiles[idx++] = new File(sysDir.getAbsolutePath()
					+ File.separator + jobID + File.separator
					+ result.getId() + MELODY_PLOT_EXT);

			// TODO actually plot the result
		}
		return plotFiles;
	}

	private NemaData evaluateResult(String jobID, List<NemaData> theData) {

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

		int numTracks = theData.size();
		for (int x = 0; x < numTracks; x++) {
			// pull the algorithm and ground-truth raw data
			data = theData.get(x);
			gtData = trackIDToGT.get(data.getId());
			rawData = data
					.get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);
			rawGtData = gtData
					.get2dDoubleArrayMetadata(NemaDataConstants.MELODY_EXTRACTION_DATA);

			// initialize frame-by-frame counters for the evaluation measures
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
			
			// perform evaluation
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

			// Do one octave evaluation
			for (int t = 0; t < tot; t++) {
				double gtF0 = rawGtData[t][1];
				double detF0;
				if (t >= rawData.length) {
					detF0 = 0.0;
				} else {
					detF0 = rawData[t][1];
				}

				// map to one octave
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
			} // end counts for a single trackID evaluation

			
			
			// Calculate the evaluation measures for this track
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
		
		
		vxRecallOvarall = vxRecallOvarall / ((double) numTracks);
		vxFalseAlarmOverall = vxFalseAlarmOverall / ((double) numTracks);
		rawPitchOverall = rawPitchOverall / ((double) numTracks);
		rawChromaOverall = rawChromaOverall / ((double) numTracks);
		accuracyOverall = accuracyOverall / ((double) numTracks);
		
		outObj.setMetadata(NemaDataConstants.MELODY_OVERALL_ACCURACY, accuracyOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_RAW_PITCH_ACCURACY, rawPitchOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_RAW_CHROMA_ACCURACY, rawChromaOverall);
		outObj.setMetadata(NemaDataConstants.MELODY_VOICING_RECALL, vxRecallOvarall);
		outObj.setMetadata(NemaDataConstants.MELODY_VOICING_FALSE_ALARM, vxFalseAlarmOverall);

		return outObj;
	}
}
