package org.imirsel.nema.contentrepository.auth.spi;

public interface PasswordEncoder {
	public String encodePassword(String rawpassword);
}
