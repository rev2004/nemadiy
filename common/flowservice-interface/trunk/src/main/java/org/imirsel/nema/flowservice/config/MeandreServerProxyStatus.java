package org.imirsel.nema.flowservice.config;

/**
 * 
 * @author amitku
 * @since 0.5.0
 */
public class MeandreServerProxyStatus {
	private int numRunning;
	private int numAborting;
	/**
	 * @return the numRunning
	 */
	public int getNumRunning() {
		return numRunning;
	}
	/**
	 * @param numRunning the numRunning to set
	 */
	public void setNumRunning(int numRunning) {
		this.numRunning = numRunning;
	}
	/**
	 * @return the numAborting
	 */
	public int getNumAborting() {
		return numAborting;
	}
	/**
	 * @param numAborting the numAborting to set
	 */
	public void setNumAborting(int numAborting) {
		this.numAborting = numAborting;
	}
}
