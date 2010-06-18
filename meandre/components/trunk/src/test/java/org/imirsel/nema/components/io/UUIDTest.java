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

	@Test
	public void testUUID() throws UnsupportedEncodingException{
		String flowExecutionInstanceID="http__www.imirsel.org_dresslermelody05_dresslermelody051276747823451_0AFFFFF62DC2_1276747827642_1209694379_1276747822822-token_/";
		String jobId = UUID.nameUUIDFromBytes(flowExecutionInstanceID.getBytes("UTF-8")).toString();
		String jobId2 = UUID.nameUUIDFromBytes(flowExecutionInstanceID.getBytes("UTF-8")).toString();
			
		System.out.println(jobId);
		System.out.println(jobId2);
		
	}
	
}
