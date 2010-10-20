package org.imirsel.meandre.client;


/**Server Response with flow execution parameters
 * 
 * @author kumaramit01
 *
 */
public class ExecResponse {
	private String uri;
	private int port;
	private String hostname;
	private String token;
	
	/**
	 * 
	 * @return the uri where the flow is being executed
	 */
	public String getUri() {
		return uri;
	}
	/**Set the flow execution uri
	 * 
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * 
	 * @return the port where the flow is running
	 */
	public int getPort() {
		return port;
	}
	/**Set the port 
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**Return hostname
	 * 
	 * @return hostname as string
	 */
	public String getHostname() {
		return hostname;
	}
	/**Set the hostname
	 * 
	 * @param hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	/**Return token that identifies a unique flow execution
	 * 
	 * @return token as string
	 */
	public String getToken() {
		return token;
	}
	/**Set the token
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
