package org.imirsel.nema.webapp.mock;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;


/**
 * @author gzhu1
 *
 */
public class MockMeandreServerProxyConfig implements MeandreServerProxyConfig {

	
	private String host;
	private int maxConcurrentJobs;
	private String password;
	private int port;
	private String username;
	
	public String getHost() {
		return host;
	}

	public int getMaxConcurrentJobs() {
		return maxConcurrentJobs;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setMaxConcurrentJobs(int maxConcurrentJobs) {
		this.maxConcurrentJobs = maxConcurrentJobs;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}
