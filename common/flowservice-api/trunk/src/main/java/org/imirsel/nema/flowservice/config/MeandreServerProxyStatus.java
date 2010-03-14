package org.imirsel.nema.flowservice.config;

import java.io.Serializable;

/**
 * Stores {@link MeandreServerProxy} Status
 * 
 * @author kumaramit01
 * @since 0.5.0
 */
public class MeandreServerProxyStatus implements Serializable {

   /** Version of this class */
	private static final long serialVersionUID = 2032279664228170577L;
	
	private int numRunning;
	private int numAborting;
	
	/**
	 * Create a new instance with the given initial state.
	 * 
	 * @param numJobsRunning Number of jobs running.
	 * @param numJobsAborting Number of jobs pending abort.
	 */
	public MeandreServerProxyStatus(int numJobsRunning, int numJobsAborting) {
		this.numRunning = numJobsRunning;
		this.numAborting = numJobsAborting;
	}
	
	/**
	 * Create a new instance.
	 */
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
