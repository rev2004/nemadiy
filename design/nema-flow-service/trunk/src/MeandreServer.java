import java.util.Set;

import org.meandre.client.MeandreClient;

public class MeandreServer {

	private String host;
	private int port;
	private int maxConcurrentJobs;
	private int numJobsRunning = 0;
	private boolean headNode;
	private Set<Job> abortPending;
	
	private MeandreClient meandreClient;
	
	public MeandreServer(String host, int port, int maxConcurrentJobs, boolean headNode) {
		this.host = host;
		this.port = port;
		this.maxConcurrentJobs = maxConcurrentJobs;
		this.headNode = headNode;
		// create meandre client
	}

	public MeandreClient getMeandreClient() {
		return null;
	}

	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getMaxConcurrentJobs() {
		return 0;
	}
	
	public int getNumJobsRunning() {
		return 0;
	}

	public boolean isHeadNode() {
		return headNode;
	}

	public void setHeadNode(boolean headNode) {
		this.headNode = headNode;
	}
	
	public void executeJob(Job job) {
		// check that num running is < max
		// submit job to server
		// increment counter
	}
	public void abortJob(Job job) {
		// submit abort request to server via client
		// then executor thread needs to run to check database for status change (which is made by the  probe) and decrement the runnign counter.
	}
}
