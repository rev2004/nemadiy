package org.imirsel.nema.contentrepository.client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompressionUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompress() {
		CompressionUtils cu=CompressionUtils.getInstanceOf();
		
		String fileLocation = "/tmp/testme/javaProcess.properties";
		String relLocation = "/tmp";
		try {
			cu.compress(fileLocation,relLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
