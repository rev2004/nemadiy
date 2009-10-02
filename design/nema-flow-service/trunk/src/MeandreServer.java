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
	
	public boolean isBusy() {
	    return maxConcurrentJobs == numJobsRunning;
	}
	
	public void executeJob(Job job) {
		System.out.println("Server " + host + ":" + port + " executing job.");
		numJobsRunning++;
		// check that num running is < max
		// submit job to server
		// increment counter
	}
	public void abortJob(Job job) {
		// submit abort request to server via client
		// then executor thread needs to run to check database for status change (which is made by the  probe) and decrement the runnign counter.
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abortPending == null) ? 0 : abortPending.hashCode());
		result = prime * result + (headNode ? 1231 : 1237);
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + maxConcurrentJobs;
		result = prime * result + numJobsRunning;
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MeandreServer other = (MeandreServer) obj;
		if (abortPending == null) {
			if (other.abortPending != null)
				return false;
		} else if (!abortPending.equals(other.abortPending))
			return false;
		if (headNode != other.headNode)
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (maxConcurrentJobs != other.maxConcurrentJobs)
			return false;
		if (numJobsRunning != other.numJobsRunning)
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
	
}
