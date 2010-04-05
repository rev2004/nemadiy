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
import org.imirsel.nema.model.Job.JobStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.imirsel.nema.test.BaseManagerTestCase;

/**
 * @author singh14
 * 
 */
public class JobTest extends BaseManagerTestCase {

   private Job job;
   private Flow flow;
   private JobResult jobresult;
   int statusCode;

   /**
    * @throws java.lang.Exception
    */

   @Before
   public void init() throws Exception {
      job = new Job();
      flow = new Flow();
      jobresult = new JobResult();

      Long id = Long.valueOf(1);
      DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
      Date start_date = (Date) formatter.parse("2010-March-20");
      Date end_date = (Date) formatter.parse("2010-March-25");
      Date submit_date = (Date) formatter.parse("2010-March-24");

      job.setId(id);
      job.setName("testName");
      job.setDescription("test Description");
      job.setHost("test Host");
      job.setExecPort(1361);
      job.setPort(8080);
      job.setNumTries(3);
      // Setting Job.statusCode will set Job.updateTimestamp automatically.
      job.setStatusCode(1);
      job.setStartTimestamp(new Timestamp(start_date.getTime()));
      job.setEndTimestamp(new Timestamp(end_date.getTime()));
      job.setSubmitTimestamp(new Timestamp(submit_date.getTime()));
      job.setExecutionInstanceId("execution instance id");
      job.setOwnerEmail("singh14@illinois.edu");
      job.setOwnerId(id);

      Date created_date = (Date) formatter.parse("2010-March-25");

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
   public void destroy() throws Exception {
      job = null;

   }

   @Test
   public void testJobStatusOnNewJobInstanceIsUnknown() {
      Job newJob = new Job();
      assertTrue(newJob.getJobStatus() == JobStatus.UNKNOWN);
   }

   @Test
   public void testUpdateTimestampIsSetWhenStatusCodeIsSet() {
      Job job = new Job();
      assertNull(job.getUpdateTimestamp());
      assertTrue(job.getJobStatus() == JobStatus.UNKNOWN);
      job.setJobStatus(JobStatus.STARTED);
      Date updateTimestamp = job.getUpdateTimestamp();
      assertTrue(updateTimestamp != null);
      try {
         // sleep here for 1 millis to ensure updateTimestamp will be later
         Thread.sleep(1);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      job.setStatusCode(JobStatus.FINISHED.getCode());
      getLogger().info("Update timestamp: " + updateTimestamp);
      assertFalse(updateTimestamp.equals(job.getUpdateTimestamp()));
   }

   @Test
   public void testClone() {
      Job jobclone = (Job) job.clone();

      assertEquals(jobclone.getId(), job.getId());
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
      assertEquals(jobclone.getExecutionInstanceId(), job
            .getExecutionInstanceId());
      assertEquals(jobclone.getResults(), job.getResults());
      assertEquals(jobclone.getFlow(), job.getFlow());
      assertEquals(jobclone.getToken(), job.getToken());
   }
}
