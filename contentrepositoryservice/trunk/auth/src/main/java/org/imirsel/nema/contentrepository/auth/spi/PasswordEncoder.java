package org.imirsel.nema.contentrepository.auth.spi;

/**abstracts the password encoding function.
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public interface PasswordEncoder {
	public String encodePassword(String rawpassword);
}
