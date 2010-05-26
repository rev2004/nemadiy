package org.imirsel.nema.webapp.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.ModelAndViewAssert.*;
import java.util.List;

import javax.annotation.Resource;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-bean.xml")
public class JobControllerTest {

	
	Mockery context=new Mockery();
	JobController jobController=new JobController();
	final FlowService flowService=context.mock(FlowService.class);
	static private Log logger=LogFactory.getLog(JobControllerTest.class);
	
	@Resource
	Job job1;
	@Resource
	Job job2;
	@Resource
	Job job3;
	
	@Resource
	JobResult result1;
	@Resource
	JobResult result2;
	
	@Resource 
	User user;
	
	@Resource
	List<Job> jobList;
	
	@Before
	public void setUp() throws Exception {
		jobController.setFlowService(flowService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetConsole() {
		final String consoleText="console 1";
		 context.checking(new Expectations() {{
		        oneOf(flowService).getJob(job1.getId()); will(returnValue(job1));
		        oneOf(flowService).getConsole(job1); will(returnValue(consoleText));
		        
		 }});      
		 MockHttpServletRequest request=new MockHttpServletRequest();
		 MockHttpServletResponse response=new MockHttpServletResponse();
		 request.addParameter("jobId",String.valueOf(job1.getId()));
		 try{
			 ModelAndView mav=jobController.getConsole(request, response);
			 //assertTrue(consoleText.equals(response.getOutputStream().));
		 }catch(MeandreServerException e){
			 logger.error(e,e);
		 }
		 
	}
	
	@Test
	public void testGetUserJobs() {
		final UserManager userManager=context.mock(UserManager.class);
		 context.checking(new Expectations() {{
			    oneOf(userManager).getCurrentUser(); will(returnValue(user));
		        oneOf(flowService).getUserJobs(user.getId()); will(returnValue(jobList));	
			    oneOf(userManager).getCurrentUser(); will(returnValue(user));
		        oneOf(flowService).getUserJobs(user.getId()); will(returnValue(jobList));	
		        
		 }});  
		 MockHttpServletRequest request=new MockHttpServletRequest();
		 request.setRequestURI("http://test.com/get/getUserJobs");
		 jobController.setUserManager(userManager);
		 ModelAndView mav=jobController.getUserJobs(request, null);
		 assertModelAttributeValue(mav,Constants.JOBLIST,jobList);	
		 assertViewName(mav,"job/jobList");
		 
		 
		 request.setRequestURI("http://test.com/get/getUserJobs.json");
		 mav=jobController.getUserJobs(request, null);
		 assertModelAttributeValue(mav,Constants.JOBLIST,jobList);	
		 assertViewName(mav,"jsonView");

	}
	
	
    @Test 
    public void testDoJobAction(){
		 context.checking(new Expectations() {{
			    oneOf(flowService).getJob(job1.getId()); will(returnValue(job1));
		        oneOf(flowService).abortJob(job1.getId());	
			    oneOf(flowService).getJob(job1.getId()); will(returnValue(job1));
			    oneOf(flowService).deleteJob(job1.getId()); 
			    oneOf(flowService).removeFlow(job1.getFlow().getUri());
		        
		 }});
		 MockHttpServletRequest request=new MockHttpServletRequest();
		 request.addParameter("id",String.valueOf(job1.getId()));
		 request.addParameter("submit", "Abort This Job");
		 ModelAndView mav;
		try {
			mav = jobController.doJobAction(request, null);
			assertTrue(mav.getView() instanceof RedirectView);
		} catch (MeandreServerException e) {
			logger.error(e,e);
			
		}
		request=new MockHttpServletRequest();
		 request.addParameter("id",String.valueOf(job1.getId()));
		 request.addParameter("submit", "Delete This Job");
	
		try {
			mav = jobController.doJobAction(request, null);
			assertTrue(mav.getView() instanceof RedirectView);
		} catch (MeandreServerException e) {
			logger.error(e,e);
			
		}
	
				

    }

}
