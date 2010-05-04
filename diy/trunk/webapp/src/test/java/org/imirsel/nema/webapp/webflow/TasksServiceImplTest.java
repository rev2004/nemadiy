/**
 * 
 */
package org.imirsel.nema.webapp.webflow;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.webapp.controller.JobController;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author gzhu1
 *
 */
public class TasksServiceImplTest {
	Mockery context=new Mockery();
	final FlowService flowService=context.mock(FlowService.class);
	static private Log logger=LogFactory.getLog(TasksServiceImplTest.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#getRoles()}.
	 */
	@Test
	public void testGetRoles() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#fillDefaultParameter(org.imirsel.nema.model.Flow)}.
	 */
	@Test
	public void testFillDefaultParameter() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#saveParameter(org.springframework.webflow.execution.RequestContext)}.
	 */
	@Test
	public void testSaveParameter() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#getUploadDirectory()}.
	 */
	@Test
	public void testGetUploadDirectory() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#setUploadDirectory(java.lang.String)}.
	 */
	@Test
	public void testSetUploadDirectory() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#testRun(org.imirsel.nema.model.Flow, java.util.Map, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testTestRun() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#run(org.imirsel.nema.model.Flow, java.util.Map, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRun() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.imirsel.nema.webapp.webflow.TasksServiceImpl#getJobResult(org.imirsel.nema.model.Job)}.
	 */
	@Test
	public void testGetJobResult() {
		fail("Not yet implemented"); // TODO
	}

}
