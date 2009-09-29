
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MeandreJobScheduler implements JobScheduler {
	
	/*#MeandreJobSchedulerConfig lnkMeandreJobSchedulerConfig*/
private MeandreServer head;
	
	private Queue jobQueue;
	
	private List<MeandreServer> workers;

	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();

	private final ScheduledFuture<?> runJobsFuture;
	
	public MeandreJobScheduler() {
		runJobsFuture = executor.scheduleAtFixedRate(
				new RunQueuedJobs(), 10, 5, TimeUnit.SECONDS);
	}
	
	
	public void abortJob(Job job) {
		
	}
	
	public void scheduleJob(Job job){
		// queue the job
	}
	
	private class RunQueuedJobs implements Runnable {
		public void run() {
			runJobs();
		}
	}
	
	private void runJobs() {
		// lock access to queue
		// check to see if any servers are available
		// if there are:
		// pop job off queue
		// update job bean
		// persist job bean
		// submit job
		// unlock
	}


	public List<MeandreServer> getWorkers() {
		return workers;
	}


	public void addWorker(MeandreServer server) {
		
	}
	
	public void removeWorker(MeandreServer server) {
		
	}
	
}
