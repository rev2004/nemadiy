package org.imirsel.nema.flowservice.config;

/**
 * Stores {@link MeandreServerProxy} Status
 * @author kumaramit01
 * @since 0.5.0
 */
public class MeandreServerProxyStatus {
	private int numRunning;
	private int numAborting;
	public MeandreServerProxyStatus(int numJobsRunning, int numJobsAborting) {
		this.numRunning = numJobsRunning;
		this.numAborting = numJobsAborting;
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
}
