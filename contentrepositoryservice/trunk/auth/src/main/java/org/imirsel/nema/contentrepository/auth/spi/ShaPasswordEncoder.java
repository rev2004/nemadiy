package org.imirsel.nema.contentrepository.auth.spi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**Sha Password encoder
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class ShaPasswordEncoder implements PasswordEncoder {
	
	private static MessageDigest digest;
	

	public String encodePassword(String rawpassword, boolean hex) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            }catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Unable to initialize the SHA algorithm!", e);
            }
        }
        synchronized(digest) {
            digest.reset();
            digest.update(rawpassword.getBytes());
            if(!hex)
            	return new String(Base64.encodeBase64(digest.digest()));
            else
             	return new String(Hex.encodeHex(digest.digest()));
             
        }
	}
	
	public static void main(String args[]){
		ShaPasswordEncoder spe = new ShaPasswordEncoder();
		System.out.println(spe.encodePassword("admin"));
	}

	public String encodePassword(String rawpassword) {
		return encodePassword(rawpassword,true);
	}

}
