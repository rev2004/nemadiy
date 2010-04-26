package org.imirsel.nema.model;

/**This class stores the username and password hash which
 * is provided by the DIY webapplication and used for authentication
 * by the content repository service
 * 
 * @author kumaramit01
 * @since 0.0.1 -content repository service
 * @since 0.5.1 -DIY webapp
 * @since 0.6.1 -Flow Service
 */
public class NemaCredentials{
	private String username;
	private String passwordHash;
	public String getUsername() {
		return username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
