package org.imirsel.nema.flowservice.config;

public interface MeandreServerProxyConfig {

	public abstract String getHost();

	public abstract int getPort();

	public abstract int getMaxConcurrentJobs();

	public abstract String getUsername();
	
	public abstract String getPassword();

}