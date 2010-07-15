package org.imirsel.nema.components.process;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.components.NemaComponent;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.model.fileTypes.NemaFileType;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;

/**
 * 
 * @author kris.west@gmail.com
 * @since 0.3.0
 */
@Component(creator = "Kris West", description = "Receives a datastructure that " +
		"defines a list of expected output file locations and then receives " +
		"a stream of ProcessArtifacts that represent entries in that datastructure " +
		"(files) being fulfilled by a process execution and retrieval of the " +
		"result to the desired local path. As the files are received, they are " +
		"recorded in teh repository database.",
		name = "OmenOutputPublisher", tags = "publish results repository", 
		firingPolicy = Component.FiringPolicy.any)
public class OmenOutputPublisher extends NemaComponent {

	@ComponentInput(description = "Map of NemaTrackList to List of NemaFile Objects defining expected output files that will be received as ProcessArtifacts.", name = "expectedOutput")
	private static final String DATA_IN_EXPECTED_OUTPUTS ="expectedOutput";

	@ComponentInput(description = "Process artifacts representing files that have been produced.", name = "processArtifacts")
	private static final String DATA_IN_PROCESS_ARTIFACTS ="processArtifacts";

	@ComponentInput(description = "NemaTask Object defining the task.", name = "NemaTask")
	private static final String DATA_IN_NEMATASK ="NemaTask";

	@ComponentInput(description = "Class representing the output file type that is to be recorded.", name = "FileType")
	private static final String DATA_IN_OUTPUT_TYPE ="FileType";

	@ComponentInput(description = "Submission code for the results to be stored.", name = "submissionCode")
	private static final String DATA_IN_SUBMISSION_CODE ="submissionCode";

	@ComponentInput(description = "Submission name for the results to be stored.", name = "submissionName")
	private static final String DATA_IN_SUBMISSION_NAME ="submissionName";

	NemaTask task = null;
	Class<NemaFileType> fileType = null;
	Map<NemaTrackList,List<File>> expectedPaths = null;
	Map<File,NemaTrackList> fileToTrackList = null;
	String subCode = null;
	String subName = null;
	
	@Override
	public void initialize(ComponentContextProperties ccp)
	throws ComponentExecutionException, ComponentContextException {
		super.initialize(ccp);
	}

	@Override
	public void dispose(ComponentContextProperties ccp)
	throws ComponentContextException {
		super.dispose(ccp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
		cc.getOutputConsole().println("In output reader -execute");
		if(task == null && cc.isInputAvailable(DATA_IN_NEMATASK)){
			task = (NemaTask)cc.getDataComponentFromInput(DATA_IN_NEMATASK);
			getLogger().fine("got task");
			if(subCode != null){
				RepositoryClientInterface client = null;
				try {
					client = RepositoryClientConnectionPool.getInstance().getFromPool();
					//clear out existing results for this task-submission code pair
					getLogger().info("deleting existing published results for submission code " + subCode + " on task " + task.getId() + " (" + task.getName() + ")");
					client.deletePublishedResultsForTaskAndSubmission(task.getId(),subCode);
				} catch (Exception e) {
					throw new ComponentExecutionException("Exception in "
							+ this.getClass().getName(), e);
				}finally{
					if(client!=null){
						RepositoryClientConnectionPool.getInstance().returnToPool(client);
					}
				}
			}
		}
		if(fileType == null && cc.isInputAvailable(DATA_IN_OUTPUT_TYPE)){
			fileType = (Class<NemaFileType>)cc.getDataComponentFromInput(DATA_IN_OUTPUT_TYPE);
			getLogger().fine("got output type");
		}
		if(subCode == null && cc.isInputAvailable(DATA_IN_SUBMISSION_CODE)){
			subCode = (String)cc.getDataComponentFromInput(DATA_IN_SUBMISSION_CODE);
			getLogger().fine("got submission code: " + subCode);
			if(task != null){
				RepositoryClientInterface client = null;
				try {
					client = RepositoryClientConnectionPool.getInstance().getFromPool();
					//clear out existing results for this task-submission code pair
					getLogger().info("deleting existing published results for submission code " + subCode + " on task " + task.getId() + " (" + task.getName() + ")");
					client.deletePublishedResultsForTaskAndSubmission(task.getId(),subCode);
				} catch (Exception e) {
					throw new ComponentExecutionException("Exception in "
							+ this.getClass().getName(), e);
				}finally{
					if(client!=null){
						RepositoryClientConnectionPool.getInstance().returnToPool(client);
					}
				}
			}
		}
		if(subName == null && cc.isInputAvailable(DATA_IN_SUBMISSION_NAME)){
			subCode = (String)cc.getDataComponentFromInput(DATA_IN_SUBMISSION_NAME);
			getLogger().fine("got submission name: " + subName);
		}
		if(expectedPaths == null && cc.isInputAvailable(DATA_IN_EXPECTED_OUTPUTS)){
			expectedPaths = (Map<NemaTrackList,List<File>>)cc.getDataComponentFromInput(DATA_IN_EXPECTED_OUTPUTS);
			fileToTrackList = new HashMap<File, NemaTrackList>();
			for (Iterator<NemaTrackList> it = expectedPaths.keySet().iterator(); it.hasNext();) {
				NemaTrackList trackList = it.next();
				List<File> files = expectedPaths.get(trackList);
				for (Iterator<File> it2 = files.iterator(); it2.hasNext();) {
					File file = it2.next();
					fileToTrackList.put(file, trackList);
				}
			}
			getLogger().fine("The output expected size: " + fileToTrackList.size());
		}
		//when we are configured start receiving process artifacts
		if(task != null && fileType != null && expectedPaths != null && subCode != null && subName != null){
			
			RepositoryClientInterface client = null;
			
			getLogger().fine("Receiving a process artifact");
			if(cc.isInputAvailable(DATA_IN_PROCESS_ARTIFACTS)){
				List<ProcessArtifact> in = (List<ProcessArtifact>)cc.getDataComponentFromInput(DATA_IN_PROCESS_ARTIFACTS);
				cc.getOutputConsole().println("The process artifact: " + in.size() + "  " + in.get(0).getResourcePath());
				try {
					client = RepositoryClientConnectionPool.getInstance().getFromPool();
						
					for(Iterator<ProcessArtifact> it = in.iterator();it.hasNext();){
						File path = new File(it.next().getResourcePath());
						NemaTrackList trackList = fileToTrackList.remove(path);
						if(trackList != null){
							getLogger().fine("Received file: " + path.getAbsolutePath() + ", expecting " + fileToTrackList.size() + " further files.");
							//save to repository DB here
							client.publishResultForTask(task.getId(), trackList.getId(), subCode, subName, path.getAbsolutePath(), fileType);
						}else{
							String msg = "Received unexpected file: " + path.getAbsolutePath() + ", waiting on " + fileToTrackList.size() + " files. Paths expected but not yet received: ";
							for (Iterator<File> iterator = fileToTrackList.keySet().iterator(); iterator.hasNext();) {
								msg += "\t" + iterator.next().getAbsolutePath() + "\n";
							}
							getLogger().warning(msg);
						}
						if (fileToTrackList.isEmpty()){
							//we have all the files expected so reset
							task = null;
							fileType = null;
							expectedPaths = null;
							fileToTrackList = null;
						}
					}
					
				} catch (Exception e) {
					throw new ComponentExecutionException("Exception in "
							+ this.getClass().getName(), e);
				}finally{
					if(client!=null){
						RepositoryClientConnectionPool.getInstance().returnToPool(client);
					}
				}
				
			}else{
				getLogger().info("Process Artifact input is not available yet.");
			}
			
			
		}else{
			getLogger().info("Waiting on getting the process artifact list to receive -rest of the parameters are all good.");
		}
		
		
		
		
	}

}
