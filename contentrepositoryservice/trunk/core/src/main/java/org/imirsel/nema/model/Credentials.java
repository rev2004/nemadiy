package org.imirsel.nema.model;

public class Credentials {
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
