package org.imirsel.nema.webapp.webflow;

import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.service.UserManager;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class TasksServiceImplTest {
	Mockery context=new Mockery();
	final FlowService flowService=context.mock(FlowService.class);
	private UserManager userManager=context.mock(UserManager.class);
	private String uploadDirectory="upload";
	private ArtifactService artifactService=context.mock(ArtifactService.class);
	static private Log logger=LogFactory.getLog(TasksServiceImplTest.class);
	TasksServiceImpl tasksService=new TasksServiceImpl();
	@Before
	public void setUp() throws Exception {
		tasksService.setArtifactService(artifactService);
		tasksService.setFlowService(flowService);
		tasksService.setUserManager(userManager);
	}

	@Test
	public final void testAddExecutable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClearBundles() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetFlowTemplates() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFindBundle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFillDefaultParameter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetDatatypeMaps() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetComponentList() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetJobResult() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetUploadDirectory() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHideExecutableProperties() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testShownMap() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIsRemoteServiceComponent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testUpdateDataMap() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSaveParameter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRun() {
		fail("Not yet implemented"); // TODO
	}

}
