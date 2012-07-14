package org.imirsel.nema.melodyevaluator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.evaluation.Evaluator;
import org.imirsel.nema.analytics.evaluation.ResultRenderer;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaEvaluationResultSet;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.fileTypes.SingleTrackEvalFileType;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;

public abstract class AbstractEvaluatorMainSingleTrack {

	protected File taskPropsFile;
	protected TaskAndDatasetProperties taskProps;
	
	protected NemaTask task;
	protected NemaDataset dataset;
	protected List<NemaTrackList> testSets;
	
	protected File gtFile;
	protected List<NemaData> gtData;
	
	protected File outputDirectory;
	protected File submissionPropsDir;
	protected Logger _logger = null;
	
	protected String matlabPath = null;
	
	protected Evaluator eval = null;
	protected ResultRenderer renderer = null;
	
	boolean useSubmissionDb = true;
	
	public AbstractEvaluatorMainSingleTrack(String[] args, Logger _logger){
		this._logger = _logger;
		getLogger().setLevel(Level.FINEST);
		try{
			taskPropsFile = new File(args[0]);
			System.out.println("Reading task and dataset properties from: " + taskPropsFile.getAbsolutePath());
			taskProps = new TaskAndDatasetProperties(taskPropsFile);
			
			task = taskProps.getTask();
			dataset = taskProps.getDataset();
			
			System.out.println("Task name: " + task.getName() + "\n" +
					"Task Description: " + task.getDescription() + "\n\n" +
					"Task metadata: " + task.getSubjectTrackMetadataName() + "\n\n"+ 
					"Dataset name: " + dataset.getName() + "\n" +
					"Dataset description: " + dataset.getDescription() + "\n\n"); 
			
			
			//testSets = taskProps.getTestTrackLists();
	
		        
			gtFile = taskProps.getGtFile();
			gtData = readGTFile(gtFile); 
	        ArrayList<NemaTrack> trackList = new ArrayList<NemaTrack>(gtData.size());
	        for (Iterator<NemaData> iterator = gtData.iterator(); iterator.hasNext();) {
	        	trackList.add(new NemaTrack(iterator.next().getId()));
			}			
			testSets = new ArrayList<NemaTrackList>(1);
		    int id = 0;
		    testSets.add(new NemaTrackList(id, task.getDatasetId(), 3, "test", id, trackList));
		    id++;
			
		    submissionPropsDir = new File(args[1]); 
			
			outputDirectory = new File(args[2]);
			System.out.println("Output directory: " + outputDirectory.getAbsolutePath());
			
			
			Map<String,File> jobIdToResultDir = taskProps.getResultDataPaths();
			
			System.out.println("Got result paths for " + jobIdToResultDir.size() + " submission IDs");
			
			
			
			if(args.length > 3){
				matlabPath = args[3];
			}
			
			System.out.println("Setting up evaluator and renderer...\n");
			eval = getEvaluator();
			renderer = getRenderer(matlabPath);

			System.out.println("Reading result data...");
			
			for(String jobId:jobIdToResultDir.keySet()){
				File dir = jobIdToResultDir.get(jobId);
				System.out.println("\tJob Id: " + jobId + ", path: " + dir.getAbsolutePath());
				
				//List<NemaData> resultData = readResultDirectory(dir);
				SingleTrackEvalFileType reader =  readResultDirectory(dir);
			//	List<NemaData> resultData = reader.readDirectory(dir, null);
				int count = 0;
			//	for (Iterator<List<NemaData>> iterator = resultData.iterator(); iterator.hasNext();) {	
					List<NemaData> oneFoldResults = reader.readDirectory(dir, null);
					NemaTrackList set = testSets.get(0);
					System.out.println("\t\tgot results for "+ oneFoldResults.size() + " tracks for set " + set.getId() + " fold " + set.getFoldNumber());
					eval.addResults(jobId, jobId, set, oneFoldResults);
			//	}
			}
			
			
			
		}catch(Exception e){
			getLogger().log(Level.SEVERE,
					"An exception occured!",e);
		}
	}
	
	public void resolveSubmissionDetails(NemaEvaluationResultSet results) throws SQLException{

			List<String> subs = new ArrayList<String>(results.getJobIds());
			Map<String,NemaSubmission> out = new HashMap<String, NemaSubmission>();
			
//			RepositoryClientInterface client = null;
			
			try{
//				client = new RepositoryClientImpl();
				for (Iterator<String> iterator = subs.iterator(); iterator.hasNext();) {
					String subCode = iterator.next();
					System.out.println("Retrieving submission details for '" + subCode + "'");
					String subPropFileName = subCode + ".properties";
					File subPropsFile = new File(submissionPropsDir, subPropFileName);
					NemaSubmission sub = new NemaSubmission(subPropsFile);
					if (sub == null){
						System.out.println("\n\nERROR:  Failed to retrieve submission details for '" + subCode + "'"); 
					}
					out.put(subCode,sub);
				}
			}finally{

			}
		results.setJobIdToSubmissionDetails(out);
	}
	
	public NemaEvaluationResultSet evaluate() throws IllegalArgumentException, IOException{
		getLogger().info("\n\nEVALUATING\n\n");
		return eval.evaluate();
	}
	
	public void render(NemaEvaluationResultSet results) throws IOException{
		getLogger().info("\n\nRENDERING\n\n");
		renderer.renderResults(results);
	}
	
	protected abstract Evaluator getEvaluator();
	
	protected abstract ResultRenderer getRenderer(String matlabPath) throws FileNotFoundException;
	
	protected abstract List<NemaData> readGTFile(File path) throws IOException;
	
	protected abstract SingleTrackEvalFileType  readResultDirectory(File path) throws IOException;
	
	protected Logger getLogger(){
		return _logger;
	}

	public void setUseSubmissionDb(boolean useSubmissionDB) {
		this.useSubmissionDb = useSubmissionDB;
	}

	public boolean getUseSubmissionDb() {
		return useSubmissionDb;
	}
	
	
}
