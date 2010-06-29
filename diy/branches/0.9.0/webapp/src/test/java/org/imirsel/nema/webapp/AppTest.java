package org.imirsel.nema.webapp;

import junit.framework.TestCase;

public class AppTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", App.getHello());
    	org.springframework.security.providers.encoding.ShaPasswordEncoder spe;
    	spe = new 	org.springframework.security.providers.encoding.ShaPasswordEncoder();
    	spe.setEncodeHashAsBase64(false);
    	System.out.println(spe.encodePassword("user", null));
    	
    }
}
