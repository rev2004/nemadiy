package org.imirsel.nema.flowservice.config;

import java.io.Serializable;

/**
 * Stores {@link MeandreServerProxy} Status
 * @author kumaramit01
 * @since 0.5.0
 */
public class MeandreServerProxyStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2032279664228170577L;
	private boolean isHead;
	// number of flows that are running
	private int numRunning;
	// number of flows that have been asked to abort
	private int numAborting;
	// number of flows that can run concurrently
	private int maxConcurrentJobs;
	public MeandreServerProxyStatus(int numJobsRunning, int numJobsAborting, int numCapacity, boolean isHead) {
		this.numRunning = numJobsRunning;
		this.numAborting = numJobsAborting;
		this.maxConcurrentJobs = numCapacity;
		this.isHead= isHead;
	}
	// default constructor
	public MeandreServerProxyStatus() {
	}
	/**
	 * @return the numRunning
	 * @since 0.5.0
	 */
	public int getNumRunning() {
		return numRunning;
	}
	/**
	 * @param numRunning the numRunning to set
	 * @since 0.5.0
	 */
	public void setNumRunning(int numRunning) {
		this.numRunning = numRunning;
	}
	/**
	 * @return the numAborting
	 * @since 0.5.0
	 */
	public int getNumAborting() {
		return numAborting;
	}
	/**
	 * @param numAborting the numAborting to set
	 * @since 0.5.0
	 */
	public void setNumAborting(int numAborting) {
		this.numAborting = numAborting;
	}
	public int getMaxConcurrentJobs() {
		return maxConcurrentJobs;
	}
	public void setMaxConcurrentJobs(int maxConcurrentJobs) {
		this.maxConcurrentJobs = maxConcurrentJobs;
	}
	public void setHead(boolean isHead) {
		this.isHead = isHead;
	}
	public boolean isHead() {
		return isHead;
	}

}
