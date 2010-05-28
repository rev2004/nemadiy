package org.imirsel.nema.contentrepository.client;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author kumaramit01
 * @since 0.1.0
 * 
 */
public class SerializePropertiesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testSerialize() throws IOException{
		Properties properties = new Properties();
		properties.setProperty("executableName",  "name");
		properties.setProperty("typeName", "typeName");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		properties.storeToXML(baos, "serialized properties for name" );
		System.out.println(baos.toString());
	}

}
