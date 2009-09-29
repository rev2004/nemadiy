import org.meandre.client.MeandreClient;

public class MeandreServer {

	private String host;
	private int port;
	private int maxConcurrentJobs;
	private int numJobsRunning;
	private boolean isHeadNode;
	
	private MeandreClient meandreClient;
	
	public MeandreServer(String host, int port, boolean isHeadNode) {
		
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
		return isHeadNode;
	}

	public void setHeadNode(boolean isHeadNode) {
		this.isHeadNode = isHeadNode;
	}
	
	
}
