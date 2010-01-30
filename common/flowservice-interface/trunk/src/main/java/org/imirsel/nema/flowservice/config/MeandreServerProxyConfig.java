package org.imirsel.nema.flowservice.config;


/**
 * Configuration instructions for a {@link MeandreServerProxy}
 * 
 * @author amitku
 * @since 0.5.0
 */
public class MeandreServerProxyConfig {
	private String host;
	private int port;
	private int maxConcurrentJobs;
	private String username;
	private String password;

	public MeandreServerProxyConfig(String username,String password, String host, int port,
			 int maxConcurrentJobs) {
		this.host = host;
		this.maxConcurrentJobs = maxConcurrentJobs;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxConcurrentJobs() {
		return maxConcurrentJobs;
	}

	public void setMaxConcurrentJobs(int maxConcurrentJobs) {
		this.maxConcurrentJobs = maxConcurrentJobs;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}