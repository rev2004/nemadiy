package org.imirsel.nema.flowservice.config;

public interface MeandreServerProxyConfig {

	public String getHost();

	public int getPort();

	public int getMaxConcurrentJobs();

	public String getUsername();
	
	public String getPassword();

}