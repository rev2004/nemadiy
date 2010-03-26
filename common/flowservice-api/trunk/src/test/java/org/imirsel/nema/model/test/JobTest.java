/**
 * 
 */
package org.imirsel.nema.model.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;

import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author singh14
 *
 */
public class JobTest {
	
	private Job job;
	private Flow flow; 
	private JobResult jobresult; 
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		job = new Job();
		flow = new Flow();
		jobresult = new JobResult();
		
		Long id = Long.valueOf(1);
		job.setId(id);
		job.setName("testName");
		job.setDescription("test Description");
		job.setHost("test Host");
		job.setEndTimestamp(new Date(1095379501L));
		job.setExecPort(1361);
		job.setPort(8080);
		job.setNumTries(3);
		job.setStatusCode(1);
		job.setStartTimestamp(new Date("3/20/2010"));
		job.setSubmitTimestamp(new Date());
		job.setEndTimestamp(new Date());
		job.setExecutionInstanceId("execution instance id");
		job.setOwnerEmail("singh14@illinois.edu");
		job.setOwnerId(id);
		
		flow.setCreatorId(Long.valueOf(20001));
		flow.setDateCreated(new Date());
		flow.setDescription("Here is flow description");
		flow.setId(Long.valueOf(201));
		job.setFlow(flow);
		
		jobresult.setId(Long.valueOf(10001));
		jobresult.setResultType("dir");
		jobresult.setUrl("http://www.uiuc.edu");
		jobresult.setJob(job);
		HashSet<JobResult> jobset = new HashSet<JobResult>();
        jobset.add(jobresult);
	    job.setResults(jobset);
	    
		job.setToken("1-token");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.imirsel.nema.model.Job#getUpdateTimestamp()}.
	 */
	
	@Test
	public void testGetStatusCode() {
	int i = job.getStatusCode();
	assertTrue(i!=0);
	System.out.println("test status code  "+ i);
	}
	
	@Test
	public void testSetStatusCode() {
		
		int i=1;
		job.setStatusCode(i);
		
		int j = job.getStatusCode();
		assertTrue(j!=0);
		System.out.println("test status  "+ j);
		
		Date dt = new Date();
		
		dt = job.getUpdateTimestamp();
		
		assertTrue(dt!=null);
	
		System.out.println("update time stamp : " + dt);
	}

	
	@Test
	public void testClone()
	{  
		job = job.clone();
	   assertNotNull(job.getId());
	   assertNotNull(job.getName());
	   assertNotNull(job.getHost());
	   assertNotNull(job.getDescription());
	   assertNotNull(job.getEndTimestamp());
	   assertNotNull(job.getExecPort());
	   assertNotNull(job.getNumTries());
	   assertNotNull(job.getStatusCode());
	   assertNotNull(job.getUpdateTimestamp());
	   assertNotNull(job.getStartTimestamp());
	   assertNotNull(job.getSubmitTimestamp());
	   assertNotNull(job.getOwnerEmail());
	   assertNotNull(job.getOwnerId());
	   assertNotNull(job.getExecutionInstanceId());
	   assertNotNull(job.getResults());
	   assertNotNull(job.getFlow());
	   assertNotNull(job.getToken());
	   
	   System.out.println(job.getId());
	   System.out.println(job.getName());
	   System.out.println(job.getHost());
	   System.out.println(job.getDescription());
	   System.out.println(job.getExecPort());
	   System.out.println(job.getNumTries());
	   System.out.println(job.getStatusCode());
	   System.out.println("update time "+job.getUpdateTimestamp());
	   System.out.println("start time " +job.getStartTimestamp());
	   System.out.println("submit time  " + job.getSubmitTimestamp());
	   System.out.println("end time " + job.getEndTimestamp());
	   System.out.println(job.getOwnerEmail());
	   System.out.println(job.getOwnerId());
	   System.out.println(job.getExecutionInstanceId());
	   
	   for(JobResult jb:job.getResults())
	   {
		   System.out.println(jb.getResultType());
		   System.out.println(jb.getId());
		   System.out.println(jb.getUrl());
		   System.out.println(jb.getJob().getHost());
	   }
	   
	   System.out.println(job.getFlow().getId());
	   System.out.println(job.getFlow().getDescription());
	   System.out.println(job.getFlow().getDateCreated());
	   System.out.println(job.getFlow().getCreatorId());
		  
	   System.out.println(job.getToken());
	}
}
