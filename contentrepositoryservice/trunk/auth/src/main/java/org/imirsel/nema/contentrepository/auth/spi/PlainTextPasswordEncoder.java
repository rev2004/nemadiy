package org.imirsel.nema.contentrepository.auth.spi;

/** Returns raw password back.
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class PlainTextPasswordEncoder implements PasswordEncoder{

	/**
	 * 
	 */
	public String encodePassword(String rawpassword) {
		return rawpassword;
	}
	
}
