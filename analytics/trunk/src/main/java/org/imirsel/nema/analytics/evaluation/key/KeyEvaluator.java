package org.imirsel.nema.analytics.evaluation.key;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.EvaluatorImpl;
import org.imirsel.nema.analytics.evaluation.melody.WriteMelodyResultFiles;
import org.imirsel.nema.analytics.evaluation.util.resultpages.FileListItem;
import org.imirsel.nema.analytics.evaluation.util.resultpages.Page;
import org.imirsel.nema.analytics.evaluation.util.resultpages.PageItem;
import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;
import org.imirsel.nema.analytics.evaluation.util.resultpages.TableItem;
import org.imirsel.nema.analytics.util.io.IOUtil;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataConstants;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;

public class KeyEvaluator extends EvaluatorImpl {
	
	public static final String MELODY_PLOT_EXT = ".png";
	private static final int LOWER_BOUND = 220;
	private static final int UPPER_BOUND = 440;
	private static final double TOLERANCE = 0.5;
	
	/**
     * Constant definition for Major circle of fifths.
     */
    public static final String[] MAJOR_CIRCLE = {"c","g","d","a","e","b","f#","db","ab","eb","bb","f"};
    /**
     * Constant definition for Minor circle of fifths.
     */
    public static final String[] MINOR_CIRCLE = {"a","e","b","f#","c#","g#","eb","bb","f","c","g","d"};
    /**
     * Constant definition for note equivalences.
     */
    public static final String[][] EQUIVALENCES = {{"eb","d#"},{"f#","gb"},{"g#","ab"},{"bb", "a#"},{"c#","db"}};
    /**
     * Constant definition for score received for a correct detection.
     */
    public static final double CORRECT_SCORE = 1.0;
    /**
     * Constant definition for score received for a fifth error.
     */
    public static final double FIFTH_SCORE = 0.5;
    /**
     * Constant definition for score received for a relative major/minor error.
     */
    public static final double RELATIVE_SCORE = 0.3;
    /**
     * Constant definition for score received for a parallel major/minor error.
     */
    public static final double PARALLEL_SCORE = 0.2;
    /**
     * Constant definition for score received for a total error.
     */
    public static final double MISSED_SCORE = 0.0;

	
	/**
	 * Constructor
	 * 
	 * @param task_ 		the evaluation task e.g. melody, onset, etc
	 * @param dataset_ 		the data-set being worked on
	 * @param outputDir_ 	the output directory to write evaluation results to
	 * @param workingDir_ 	the working directory for writing temporary files, etc
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public KeyEvaluator(NemaTask task_, NemaDataset dataset_,
			File outputDir_, File workingDir_) throws FileNotFoundException,
			IOException {
		super(workingDir_, outputDir_, task_, dataset_);

	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public Map<String, NemaData> evaluate() throws IllegalArgumentException,
			IOException {
		String jobID;
		int numJobs = jobIDToFoldResults.size();
		
		/* Check all systems have just one result set */
		List<List<NemaData>> sysResults;
		
		/* 
		 * Make sure we only have one set of results per jobId (i.e. system), 
		 * as this is not a cross-fold validated experiment */
		checkFolds();
		
		/* Perform the evaluations on all jobIds (systems) */
		Map<String, NemaData> jobIdToEvaluation = new HashMap<String, NemaData>(
				numJobs);
		for (Iterator<String> it = jobIDToFoldResults.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			getLogger().info("Evaluating experiment for jobID: " + jobID);
			sysResults = jobIDToFoldResults.get(jobID);
			NemaData result = evaluateResult(jobID, sysResults.get(0));
			jobIdToEvaluation.put(jobID, result);
		}

		/* Make per system result directories */
		Map<String, File> jobIDToResultDir = new HashMap<String, File>();
		for (Iterator<String> it = jobIdToEvaluation.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			/* Make a sub-directory for the systems results */
			File sysDir = new File(outputDir.getAbsolutePath() + File.separator
					+ jobID);
			sysDir.mkdirs();
			jobIDToResultDir.put(jobID, sysDir);
		}

		/* Plot melody transcription against GT for each track result for each system */
		List<NemaData> resultList;
		Map<String, File[]> jobIDToResultPlotFileList = new HashMap<String, File[]>();
		for (Iterator<String> it = jobIdToEvaluation.keySet().iterator(); it.hasNext();) {
			jobID = it.next();
			File sysDir = jobIDToResultDir.get(jobID);
		
			/* Get results to plot */
			sysResults = jobIDToFoldResults.get(jobID);
			resultList = sysResults.get(0);
		
			File[] plotFiles = plotTranscriptionForJob(jobID, resultList,
					sysDir);
			
			jobIDToResultPlotFileList.put(jobID, plotFiles);
		}

		/* Write out summary CSV */
		getLogger().info("Writing out CSV result files over whole task...");
		File summaryCsv = new File(outputDir.getAbsolutePath() + File.separator
				+ "allResults.csv");
		WriteMelodyResultFiles.writeTableToCsv(
				WriteMelodyResultFiles.prepSummaryTableData(jobIdToEvaluation, jobIDToName),
				summaryCsv
			);

		/* Write out per track CSV for each system */
		Map<String, File> jobIDToPerTrackCSV = new HashMap<String, File>(
				numJobs);
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			sysResults = jobIDToFoldResults.get(jobID);
			resultList = sysResults.get(0);
			File sysDir = jobIDToResultDir.get(jobID);
			File trackCSV = new File(sysDir.getAbsolutePath() + File.separator + "perTrack.csv");
			WriteMelodyResultFiles.writeTableToCsv(
					WriteMelodyResultFiles.prepPerTrackTableData(resultList, jobIDToName.get(jobID)),
					trackCSV
				);
			jobIDToPerTrackCSV.put(jobID, trackCSV);
		}


		/* Create tar-balls of individual result directories */
		getLogger().info("Preparing evaluation data tarballs...");
		Map<String, File> jobIDToTgz = new HashMap<String, File>(jobIDToName
				.size());
		for (Iterator<String> it = jobIDToName.keySet().iterator(); it
				.hasNext();) {
			jobID = it.next();
			jobIDToTgz.put(jobID, IOUtil
					.tarAndGzip(jobIDToResultDir.get(jobID)));
		}

		/* Write result HTML pages */
		getLogger().info("Creating result HTML files...");

		writeResultHtmlPages(numJobs, jobIdToEvaluation,
				jobIDToResultPlotFileList, summaryCsv, jobIDToPerTrackCSV,
				jobIDToTgz);

		return jobIdToEvaluation;
	}

	/**
	 * Writes the result HTML pages for the evaluation of multiple jobs/algorithms
	 * 
	 * @param numJobs 					the number of jobs/algorithms to evaluation
	 * @param jobIdToEvaluation 		map of a jobId to evaluation results for that job
	 * @param jobIDToResultPlotFileList map of a jobId to the results plots for that job
	 * @param summaryCsv 				the summary csv file that summarizes all jobs
	 * @param jobIDToPerTrackCSV 		map of jobId to individual per-track results csv files for that job
	 * @param jobIDToTgz 				map of jobId to the tar-balls of individual job results
	 */
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

		/* Do intro page to describe task */
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

		/* Do summary page */
		{
			items = new ArrayList<PageItem>();
			Table summaryTable = WriteMelodyResultFiles.prepSummaryTableData(
					jobIdToEvaluation, jobIDToName);
			items.add(new TableItem("summary_results", "Summary Results",
					summaryTable.getColHeaders(), summaryTable.getRows()));
			aPage = new Page("summary", "Summary", items, false);
			resultPages.add(aPage);
		}

		/* Do per system pages */
		{
			for (Iterator<String> it = jobIDToName.keySet().iterator(); it
					.hasNext();) {
				jobID = it.next();
				items = new ArrayList<PageItem>();
				sysResults = jobIDToFoldResults.get(jobID);
				resultList = sysResults.get(0);
				
				/* Add per track table */
				Table perTrackTable = WriteMelodyResultFiles
						.prepPerTrackTableData(resultList,
								jobIDToName.get(jobID));
				items.add(new TableItem(jobID + "_results", jobIDToName
						.get(jobID)
						+ " Per Track Results", perTrackTable.getColHeaders(),
						perTrackTable.getRows()));

				/* Add list of plots */
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

		/* Do files page */
		{
			items = new ArrayList<PageItem>();

			/* CSVs */
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

			/* System tar-balls */
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

	/**
	 * Checks that the algorithm results contain only a single fold of results. This evaluation 
	 * task is not cross-fold validated
	 */
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
	 * Plots the melody transcriptions for each job, for each file
	 * 
	 * @param jobId			the jobId we wish to plot results for
	 * @param resultList	a list of the transcriptions to plot
	 * @param sysDir		directory to store plots in
	 * @return				a file array containing all the plots
	 */
	private File[] plotTranscriptionForJob(String jobId,
			List<NemaData> resultList, File sysDir) {
		NemaData result;

		/* Plot each result */
		File[] plotFiles = new File[resultList.size()];
		
		int idx = 0;
		for (Iterator<NemaData> iterator = resultList.iterator(); iterator
				.hasNext();) {
			result = iterator.next();
			plotFiles[idx++] = new File(sysDir.getAbsolutePath()
					+ File.separator + jobId + File.separator
					+ result.getId() + MELODY_PLOT_EXT);

			// TODO actually plot the result
		}
		return plotFiles;
	}

	/**
	 * The core evaluation method. Evaluates each file against its ground-truth for a given jobId
	 * @param jobID		the jobId to evaluate
	 * @param theData	the results to evaluate for the jobId. Individual results for each file are added back to this List
	 * @return 			a single NemaData object that contains the average/summary/overall evaluation	
	 */
	private NemaData evaluateResult(String jobID, List<NemaData> theData) {

		NemaData outObj = new NemaData(jobID);

		NemaData data;
		NemaData gtData;
		String[] rawData;
		String[] rawGtData;
		
		/* Initialize the overall/summary counters */
		int overallCorrect = 0;
        int overallPerfectFifths = 0;
        int overallRelative = 0;
        int overallParallel = 0;
        int overallErrors = 0;
        double overallPerf = 0.0;

        /* Begin track by track evaluation */
		int numTracks = theData.size();
		for (int x = 0; x < numTracks; x++) {
			
			data = theData.get(x);
			gtData = trackIDToGT.get(data.getId());
			rawData = data
					.getStringArrayMetadata(NemaDataConstants.KEY_DETECTION_DATA);
			rawGtData = gtData
					.getStringArrayMetadata(NemaDataConstants.KEY_DETECTION_DATA);
			
			/* Initialize the individual track scores */
			int correct = 0;
			int perfectFifth = 0;
			int relative = 0;
			int parallel = 0;
			int error = 0;
			double perf = 0.0;
			
			/* Check if the tonics match between result and ground-truth. Map equivalent tonics to the 'same' thing */
			if (mapToPrimaryEquivalents(rawData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(rawGtData[0].toLowerCase())) == 0) {
 
				/* Check if modes also match. If yes, correct key. If not, parallel major/minor error */ 
				if (rawData[1].compareTo(rawGtData[1]) == 0) {
                    correct++;
                    overallCorrect++;
                    perf = CORRECT_SCORE;
                } else {
                    parallel++;
                    overallParallel++;
                    perf = PARALLEL_SCORE;
                }
            } 
			
			/* If tonics do not match, check for perfect-fifth or relative major/minor errors */
			else {
				
				/* Check if both result and ground-truth are same mode */
                if ((rawData[1].toLowerCase()).compareTo(rawGtData[1].toLowerCase()) == 0) {
                    
                	/* Check for perfect-fifth errors using the circle-of-fifths */ 
                    int detIdx = -1;
                    int gtIdx = -1;
                    if ((rawData[1] == "minor")) {
                        
                    	/* find indices on the minor circle-of-fifths */
                        for (int m=0;m<MINOR_CIRCLE.length;m++) {
                            if(mapToPrimaryEquivalents(rawData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MINOR_CIRCLE[m])) == 0) {
                                detIdx = m;
                            }
                            if(mapToPrimaryEquivalents(rawGtData[0].toLowerCase()).compareTo(this.mapToPrimaryEquivalents(MINOR_CIRCLE[m])) == 0) {
                                gtIdx = m;
                            }
                        }
                    } 
                    else
                    {
                    	/* find indices on the major circle-of-fifths */
                        for (int m=0;m<MAJOR_CIRCLE.length;m++) {
                            if(mapToPrimaryEquivalents(rawData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MAJOR_CIRCLE[m])) == 0) {
                                detIdx = m;
                            }
                            if(mapToPrimaryEquivalents(rawGtData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MAJOR_CIRCLE[m])) == 0) {
                                gtIdx = m;
                            }
                        }
                    }
                    
                    /* 
                     * For a fifth error, the result must be at an index 1 higher than the ground-truth. 
                     * Since the circle-of-fifths is circular, we must also check the wrap around from index 
                     * 0 to index 11 in the circle */
                    if( (detIdx - gtIdx == 1) || (detIdx - gtIdx) == -11) {
                        perfectFifth++;
                        overallPerfectFifths++;
                        perf = FIFTH_SCORE;
                    } else {
                        error++;
                        overallErrors++;
                    }  
                } 
                
                /* If mode AND tonic are different, we check for relative major/minor error */
                else {
                	
                    /* Search for relative major/minor errors */
                    int detIdx = -1;
                    int gtIdx = -1;
                    
                    /* If result is minor, find indices of result in the minor circle, 
                     * and ground-truth in the major circle
                     */
                    if (((rawData[1].toLowerCase()).compareTo("minor")) == 0) {

                    	/*
                    	 * find index into the minor circle-of-fifths for the result tonic, and the index
                    	 * into the major circle-of-fifths for the ground-truth tonic 
                    	 */
                        for (int m=0;m<MAJOR_CIRCLE.length;m++) {
                            if(mapToPrimaryEquivalents(rawData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MINOR_CIRCLE[m])) == 0) {
                                detIdx = m;
                            }
                            if(mapToPrimaryEquivalents(rawGtData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MAJOR_CIRCLE[m])) == 0) {
                                gtIdx = m;
                            }
                        }
                    } 
                    else{
                    	
                    	/*
                    	 * find index into the major circle-of-fifths for the result tonic, and the index
                    	 * into the minor circle-of-fifths for the ground-truth tonic 
                    	 */
                        for (int m=0;m<MAJOR_CIRCLE.length;m++) {
                            if(mapToPrimaryEquivalents(rawData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MAJOR_CIRCLE[m])) == 0) {
                                detIdx = m;
                            }
                            if(mapToPrimaryEquivalents(rawGtData[0].toLowerCase()).compareTo(mapToPrimaryEquivalents(MINOR_CIRCLE[m])) == 0) {
                                gtIdx = m;
                            }
                        }
                    }
                    
                    /* If the indices in the corresponding circles are the same, we have a relative error */
                    if ((gtIdx == detIdx)&&(gtIdx != -1)) {
                        relative++;
                        overallRelative++;
                        perf = RELATIVE_SCORE;
                    } else {
                        error++;
                        overallErrors++;
                    }
                }
                
			} 	
			
			/* 
			 * Populate each track's NemaData object with the measures. Most of these are binary {0,1} 
			 * except for perf. Store them as doubles though for consistency. */
			data.setMetadata(NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE, perf);
			data.setMetadata(NemaDataConstants.KEY_DETECTION_CORRECT, (double)correct);
			data.setMetadata(NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR, (double)perfectFifth);
			data.setMetadata(NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR, (double)relative);
			data.setMetadata(NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR, (double)parallel);
			data.setMetadata(NemaDataConstants.KEY_DETECTION_ERROR, (double)error);		

		}
		
		/* 
		 * Calculate summary/overall evaluation results. Populate a summary NemaData object with 
		 * the evaluations, and return it */		
		overallPerf = CORRECT_SCORE*(double)overallCorrect 
					+ FIFTH_SCORE*(double)overallPerfectFifths 
					+ RELATIVE_SCORE*(double)overallRelative 
					+ PARALLEL_SCORE*(double)overallParallel 
					+ MISSED_SCORE*(double)overallErrors;

		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_WEIGHTED_SCORE, overallPerf);
		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_CORRECT, (double)overallCorrect/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_PERFECT_FIFTH_ERROR, (double)overallPerfectFifths/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_RELATIVE_ERROR, (double)overallRelative/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_PARALLEL_ERROR, (double)overallParallel/(double)numTracks);
		outObj.setMetadata(NemaDataConstants.KEY_DETECTION_ERROR, (double)overallErrors/(double)numTracks);

		return outObj;
	}
	
	/**
     * Some tonics in music are 'equivalent'. This method looks for tonics that are
     * equivalent to others and maps them onto the primary version of the tonic.
     * 
     * @param key 	The key (a tonic) to map to its primary equivalent
     * @return 		The re-mapped key (a tonic)
     */
    public String mapToPrimaryEquivalents(String key) {
        String output;
        output = key;
        for (int i=0;i<EQUIVALENCES.length;i++) {
            if(output.compareTo(EQUIVALENCES[i][1]) == 0) {
                output = EQUIVALENCES[i][0];
            }
        }
        return output;
    }
}
