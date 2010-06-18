package org.imirsel.nema.contentrepository.client;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	
	
	@Test
	public void write0Bytes(){
		File tempFile = new File(System.getProperty("java.io.tmpDir"),"test");
		FileOutputStream fos = null;
		byte[] fileContents = null;
		try {
			fos = new FileOutputStream(tempFile);
			if(fileContents!=null){
			fos.write(fileContents);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos!=null){
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		System.out.println(tempFile.getAbsolutePath());
	}

}
