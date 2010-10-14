package org.imirsel.nema.webapp;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import junit.framework.TestCase;

public class AppTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", App.getHello());
    	ShaPasswordEncoder spe;
    	spe = new 	ShaPasswordEncoder();
    	spe.setEncodeHashAsBase64(false);
    	System.out.println(spe.encodePassword("user", null));
    	
    }
}
