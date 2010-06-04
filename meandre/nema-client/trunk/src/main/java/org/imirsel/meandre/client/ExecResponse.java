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
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
