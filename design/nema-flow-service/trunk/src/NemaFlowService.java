import java.util.List;
import java.util.Set;

import org.meandre.client.MeandreClient;

public class NemaFlowService implements FlowService {

	private JobScheduler jobScheduler;
	
	/**
	 * Kill a running {@link Job}.
	 * 
	 * @param jobId Unique ID of the {@link Job} to kill.
	 * @throws IllegalStateException if the {@link Job} is not running.
	 */
	@Override
	public void abortJob(String jobId) throws IllegalStateException {
		// call jobscheduler abortjob
	}

	/**
	 * Delete a {@link Job} and the results it produced.
	 * 
	 * @param jobId Unique ID of the {@link Job} to delete.
	 * @throws IllegalStateException if the {@link Job} is not finished.
	 */
	@Override
	public void deleteJob(String jobId) throws IllegalStateException {
		// validate that the job has finished executing
		// throw illegal state exception if still running
		// delete the job from the database
		// delete the results from disk?
		// delete the flow instance used for the job
	}

	/**
	 * Execute a {@link Job} using the specified {@link Flow} instance on behalf of the specified user.
	 * 
	 * @param jobId Unique ID of the {@link Job}.
	 * @param flowInstanceId Unique ID of the {@link Flow} instance to execute.
	 * @param userId Unique ID of the user on whose behalf the {@link Job} will be executed.
	 * @param userEmail Email address for the user where notifications about {@link Job} status changes should be sent.
	 */
	@Override
	public void executeJob(String jobId, String flowInstanceId, Long userId,
			String userEmail) {
		// create job object
		// persist job object
		// submit job to cluster for execution
	}

	/**
	 * Return all {@link Flow}s that are templates.
	 * 
	 * @return All {@link Flow}s that are templates.
	 */
	@Override
	public Set<Flow> getFlowTemplates() {
		// return all flows where isTemplate = true
		return null;
	}

	/**
	 * Return the details of a job with the specified ID.
	 * 
	 * @param jobId The ID of the job to get details for.
	 */
	@Override
	public Job getJob(String jobId) {
		// return job with the specified id
		return null;
	}

	/**
	 * Return all {@link Job}s owned by the specified user.
	 * 
	 * @param userId Unique ID of the user whose {@link Job}s will be returned.
	 */
	@Override
	public List<Job> getUserJobs(Long userId) {
		// return all jobs for the specified user
		return null;
	}

	/**
	 * Return {@link Notification}s that have been sent to the specified user.
	 * 
	 * @param userId Unique ID of the user for whom {@link Notification}s should be returned.
	 */
	@Override
	public List<Notification> getUserNotifications(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Persist the specified {@link Flow} instance.
	 * 
	 * @param instance The {@link Flow} instance that should be stored.
	 * @return The flow instance ID.
	 */
	@Override
	public Long storeFlowInstance(Flow instance) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return the {@link JobScheduler} instance currently being used.
	 * 
	 * @return Current {@link JobScheduler} instance.
	 */
	public JobScheduler getJobScheduler() {
		return jobScheduler;
	}

	/**
	 * Set the {@link JobScheduler} instance to use.
	 * 
	 * @param jobScheduler The {@link JobScheduler} to use.
	 */
	public void setJobScheduler(JobScheduler jobScheduler) {
		this.jobScheduler = jobScheduler;
	}
	
	
}
