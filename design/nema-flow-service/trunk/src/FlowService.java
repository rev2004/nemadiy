import java.util.List;
import java.util.Set;

public interface FlowService {

	public void abortJob(String jobId) throws IllegalStateException;

	public void deleteJob(String jobId) throws IllegalStateException;

	public void executeJob(String jobId, String flowInstanceId, Long userId,
			String userEmail);

	public Set<Flow> getFlowTemplates();

	public Job getJob(String jobId);

	public List<Job> getUserJobs(Long userId);

	public List<Notification> getUserNotifications(Long userId);

	public Long storeFlowInstance(Flow instance);

}
