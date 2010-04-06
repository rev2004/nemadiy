package org.imirsel.nema.webapp.controller;

import static org.junit.Assert.*;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Job;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JobControllerTest {

	Mockery context=new Mockery();
	JobController jobController=new JobController();
	final FlowService flowService=context.mock(FlowService.class);
	
	Job job1=new Job();
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
		        oneOf(flowService).getJob(100L); will(returnValue(job1));
		        oneOf(flowService).getConsole(job1); will(returnValue(consoleText));
		 }});      
		 MockHttpServletRequest request=new MockHttpServletRequest();
		 MockHttpServletResponse response=new MockHttpServletResponse();
		 request.addParameter("jobId",String.valueOf(100L));
		 try{
			 ModelAndView mav=jobController.getConsole(request, response);
			 assertTrue(consoleText.equals(response.getOutputStream()));
		 }catch(MeandreServerException e){
			 
		 }
		 
	}

}
