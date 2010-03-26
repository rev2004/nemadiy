/**
 * 
 */
package org.imirsel.nema.model.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
		DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Date start_date = (Date)formatter.parse("2010-March-20"); 
        Date end_date = (Date)formatter.parse("2010-March-25");
        Date submit_date = (Date)formatter.parse("2010-March-24");
        
		job.setId(id);
		job.setName("testName");
		job.setDescription("test Description");
		job.setHost("test Host");
		job.setExecPort(1361);
		job.setPort(8080);
		job.setNumTries(3);
		job.setStatusCode(1);
		job.setStartTimestamp(new Timestamp(start_date.getTime()));
		job.setEndTimestamp(new Timestamp(end_date.getTime()));
		job.setSubmitTimestamp(new Timestamp(submit_date.getTime()));
		job.setExecutionInstanceId("execution instance id");
		job.setOwnerEmail("singh14@illinois.edu");
		job.setOwnerId(id);
		
		Date created_date = (Date)formatter.parse("2010-March-25");
		
		flow.setCreatorId(Long.valueOf(20001));
		flow.setDateCreated(new Timestamp(created_date.getTime()));
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
	
//	@Test
//	public void testGetStatusCode() {
//	int i = job.getStatusCode();
//	assertTrue(i!=0);
//	System.out.println("test status code  "+ i);
//	}
//	
//	@Test
//	public void testSetStatusCode() {
//		
//		int i=1;
//		job.setStatusCode(i);
//		
//		int j = job.getStatusCode();
//		assertTrue(j!=0);
//		System.out.println("test status  "+ j);
//		
//		Date dt = new Date();
//		
//		dt = job.getUpdateTimestamp();
//		
//		assertTrue(dt!=null);
//	
//		System.out.println("update time stamp : " + dt);
//	}

	
	@Test
	public void testClone()
	{  
		Job jobclone = (Job)job.clone();
		
		assertEquals(jobclone.getId(), job.getId());
		System.out.println("clone id = " + jobclone.getId() + "job id = " +job.getId());
		
	   assertEquals(jobclone.getName(), job.getName());
	   assertEquals(jobclone.getHost(), job.getHost());
	   assertEquals(jobclone.getDescription(), job.getDescription());
	   assertEquals(jobclone.getEndTimestamp(), job.getEndTimestamp());
	   assertEquals(jobclone.getExecPort(), job.getExecPort());
	   assertEquals(jobclone.getNumTries(), job.getNumTries());
	   assertEquals(jobclone.getStatusCode(), job.getStatusCode());
	   assertEquals(jobclone.getUpdateTimestamp(), job.getUpdateTimestamp());
	   assertEquals(jobclone.getStartTimestamp(), job.getStartTimestamp());
	   assertEquals(jobclone.getSubmitTimestamp(), job.getSubmitTimestamp());
	   assertEquals(jobclone.getOwnerEmail(), job.getOwnerEmail());
	   assertEquals(jobclone.getOwnerId(), job.getOwnerId());
	   assertEquals(jobclone.getExecutionInstanceId(), job.getExecutionInstanceId());
	   assertEquals(jobclone.getResults(), job.getResults());
	   assertEquals(jobclone.getFlow(), job.getFlow());
	   assertEquals(jobclone.getToken(), job.getToken());
	   
	   System.out.println("clone = " + jobclone.getName() + "job = " +job.getName());
	   System.out.println("clone = " + jobclone.getHost() + "job = " +job.getHost());
	   System.out.println("clone = " + jobclone.getDescription() + "job = " +job.getDescription());
	   System.out.println("clone = " + jobclone.getExecPort() + "job = " +job.getExecPort());
	   System.out.println("clone = " + jobclone.getNumTries() + "job = " +job.getNumTries());
	   System.out.println("clone = " + jobclone.getStatusCode() + "job = " +job.getStatusCode());
	   System.out.println("clone = " + jobclone.getUpdateTimestamp() + "job update time "+job.getUpdateTimestamp());
	   System.out.println("clone = " + jobclone.getStartTimestamp() + "job start time " +job.getStartTimestamp());
	   System.out.println("clone = " + jobclone.getSubmitTimestamp() + "job submit time  " + job.getSubmitTimestamp());
	   System.out.println("clone = " + jobclone.getEndTimestamp() + "job end time " + job.getEndTimestamp());
	   System.out.println("clone = " + jobclone.getOwnerEmail() + "job = " +job.getOwnerEmail());
	   System.out.println("clone = " + jobclone.getOwnerId() + "job = " +job.getOwnerId());
	   System.out.println("clone = " + jobclone.getExecutionInstanceId() + "job = " +job.getExecutionInstanceId());
	   
	   for(JobResult jb:jobclone.getResults())
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
		  
	   System.out.println("clone token = " + jobclone.getToken() + "job token = "+job.getToken());
	}
}
