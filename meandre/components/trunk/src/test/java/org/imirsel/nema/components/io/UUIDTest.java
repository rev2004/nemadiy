package org.imirsel.nema.components.io;


import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UUIDTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	public void testUUID() throws UnsupportedEncodingException{
		String flowExecutionInstanceID="http__www.imirsel.org_dresslermelody05_dresslermelody051276747823451_0AFFFFF62DC2_1276747827642_1209694379_1276747822822-token_/";
		String jobId = UUID.nameUUIDFromBytes(flowExecutionInstanceID.getBytes("UTF-8")).toString();
		String jobId2 = UUID.nameUUIDFromBytes(flowExecutionInstanceID.getBytes("UTF-8")).toString();
			
		System.out.println(jobId);
		System.out.println(jobId2);
		
	}
	
	@Test
	public void testToken(){
		String flowId="http://www.imirsel.org/chord/chordmirex2010/80AE9A4A2DC2/1278447729478/626816347/admin_1278447726631/";
		
		String[] splits = flowId.split("/");
		
		for(String s: splits){
			System.out.println(s);
		}
		String token = splits[splits.length-1];
		
		System.out.println("Token: " + token);
		
		
	}
	
}
