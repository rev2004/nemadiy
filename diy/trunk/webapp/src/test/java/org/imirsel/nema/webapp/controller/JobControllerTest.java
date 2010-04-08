package org.imirsel.nema.webapp.controller;

import static org.junit.Assert.*;

import javax.annotation.Resource;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-bean.xml")
public class JobControllerTest {

	Mockery context=new Mockery();
	JobController jobController=new JobController();
	final FlowService flowService=context.mock(FlowService.class);
	
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
			 assertTrue(consoleText.equals(response.getOutputStream()));
		 }catch(MeandreServerException e){
			 
		 }
		 
	}
	
	@Test
	public void testUserJobs() {
		final UserManager userManager;
		 context.checking(new Expectations() {{
			    oneOf(userManager).getCurrentUser(); will(returnValue(user));
		        oneOf(flowService).getUserJobs(user.getId()); will(returnValue(new ArrayList(){job1));
		        
		 }});  
		 MockHttpServletRequest request=new MockHttpServletRequest();
		 request.setRequestURI("http://test.com/get/getUserJobs");
		 ModelAndView mav=jobController.getUserJobs(request, null);
		 
	}

}
