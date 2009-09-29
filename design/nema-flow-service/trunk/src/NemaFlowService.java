import java.util.List;
import java.util.Set;

import org.meandre.client.MeandreClient;

public class NemaFlowService implements FlowService {

	private JobScheduler jobScheduler;
	
	@Override
	public void abortJob(String jobId) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteJob(String jobId) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeJob(String jobId, String flowInstanceId, Long userId,
			String userEmail) {
		// craete job object
		// persist job object
		// submit job to cluster for execution
	}

	@Override
	public Set<Flow> getFlowTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Job getJob(String jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Job> getUserJobs(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notification> getUserNotifications(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long storeFlowInstance(Flow instance) {
		// TODO Auto-generated method stub
		return null;
	}

	public JobScheduler getJobScheduler() {
		return jobScheduler;
	}

	public void setJobScheduler(JobScheduler jobScheduler) {
		this.jobScheduler = jobScheduler;
	}
	
	
}
